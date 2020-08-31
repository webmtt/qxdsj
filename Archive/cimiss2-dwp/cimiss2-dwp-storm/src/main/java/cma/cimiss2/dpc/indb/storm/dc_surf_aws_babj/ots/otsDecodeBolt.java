package cma.cimiss2.dpc.indb.storm.dc_surf_aws_babj.ots;

import cma.cimiss2.dpc.decoder.bean.*;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationDataNation;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import cma.cimiss2.dpc.indb.core.bean.StatTF;
import cma.cimiss2.dpc.indb.core.etl.otsETL.SurfBABJEtl;
import cma.cimiss2.dpc.indb.core.ots.OTS;
import cma.cimiss2.dpc.indb.core.ots.OTSHelper;
import cma.cimiss2.dpc.indb.core.tools.DIEISender;
import com.alicloud.openservices.tablestore.AsyncClient;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

/**
 * @Description:
 * @Aouthor: xzh
 * @create: 2018-04-16 10:59
 */
public class otsDecodeBolt extends BaseRichBolt {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(otsDecodeBolt.class);
    private static final long serialVersionUID = -7666702251017898967L;
    private OutputCollector collector;
    private Map map;
    private Action action;
    private String path;
    private String dataSource;
    private String horTable;
    private String minTable;
    private String repTable;
    private int batchSize;
    private Long intervalTime;
    private String dataFlow;
    private SurfBABJEtl surfBABJEtl;
    OTSHelper horTableHelper;
    OTSHelper minTableHelper;
    OTSHelper repTableHelper;

    @Override
    public void prepare(Map map, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.map = map;
        horTable = (String) map.get("horTable");
        minTable = (String) map.get("minTable");
        repTable = (String) map.get("repTable");
        path = (String) map.get("congigPath");
        batchSize = Integer.valueOf(map.get("batchSize").toString());
        intervalTime = Long.valueOf(map.get("intervalTime").toString());
        dataSource = (String) (map.get("dataBaseType"));
        dataFlow = (String)(map.get("dataFlow"));
        surfBABJEtl = new SurfBABJEtl();
        surfBABJEtl.setIntervalTime(intervalTime);
        surfBABJEtl.setPath(path);
        surfBABJEtl.setBatchSize(batchSize);
        surfBABJEtl.setDataFlow(dataFlow);
        AsyncClient client = OTS.getClient();
        minTableHelper = new OTSHelper(client, minTable);
        horTableHelper = new OTSHelper(client, horTable);
        repTableHelper = new OTSHelper(client, repTable);
    }

    @Override
    public void execute(Tuple tuple) {

        List<ReportError> errorList = null;
        List<RestfulInfo> diInfo = new ArrayList<>();
        List<RestfulInfo> eiInfo = new ArrayList<>();
        String message = "";
        StringBuffer buffer = new StringBuffer();
        List<Map<String, Object>> horList = null;
        List<Map<String, Object>> minList = null;
        List<Map<String, Object>> reports = null;//
        String event = null;
        String value = null;
        action = Action.ACCEPT;
        try {
            //event = ((String) tuple.getValue(1)).split(":")[0];
            //message = ((String) tuple.getValue(1)).split(":")[1];
        	value = (String) tuple.getValue(1);
            event = value.split(":")[0];
            message = value.split(":")[1];
            surfBABJEtl.setEvent(event);
            File file = new File(message);
            surfBABJEtl.setFile(file);

            buffer.append("INFO : " + new Date() + " begin : " + message + "\n");
            //解码
            try {
                ParseResult<SurfaceObservationDataNation> result = surfBABJEtl.extract(file);
                errorList = result.getError();
                //格式化数据
                StatTF statBABJ = surfBABJEtl.transform(result, file, horTableHelper, minTableHelper, repTableHelper);
                horList = statBABJ.getHorList();
                minList = statBABJ.getMinList();
                reports = statBABJ.getRepList();
                buffer.append(statBABJ.getBuffer().toString());
                if (result.isSuccess()) {//解码有成功
                    for (int i = 0; i < errorList.size(); i++) {
                        ReportError reportError = errorList.get(i);
                        reportError.getMessage();
                        buffer.append("解码失败:" + message + "\n" +
                                "错误原因:" + reportError.getMessage() + "\n" +
                                "行号:" + reportError.getPositionx() + "\n" +
                                "错误片段摘录:" + reportError.getSegment() + "\n");
                        HashMap<String, String> map = new HashMap<>();
                        map.put("message", reportError.getMessage());
                        map.put("path", path);
                        List<RestfulInfo> info = DIEISender.makeEI(map);
                        eiInfo.addAll(info);
                    }
                } else {
                    //解码全部失败
                    collector.emit("BABJ-REP-Bolt", new Values(reports, new StringBuffer(buffer),value));
                    collector.ack(tuple);
                    if (errorList != null && errorList.size() > 0) {
                        for (int i = 0; i < errorList.size(); i++) {
                            ReportError reportError = errorList.get(i);
                            reportError.getMessage();
                            buffer.append("解码失败:" + message + "\n" +
                                    "错误原因:" + reportError.getMessage() + "\n" +
                                    "行号:" + reportError.getPositionx() + "\n" +
                                    "错误片段摘录:" + reportError.getSegment() + "\n");
                            HashMap<String, String> map = new HashMap<>();
                            map.put("message", reportError.getMessage());
                            map.put("path", path);
                            List<RestfulInfo> info = DIEISender.makeEI(map);
                            eiInfo.addAll(info);
                        }
                        action = Action.REJECT;
                    } else {
                        int code = result.getParseInfo().getCode();
                        switch (code) {
                            case 1:
                                //非法格式
                                buffer.append("解码失败:" + message + "\n" +
                                        "错误原因:非法格式" + "\n");
                                HashMap<String, String> map = new HashMap<>();
                                map.put("message", "非法格式");
                                map.put("path", path);
                                List<RestfulInfo> info = DIEISender.makeEI(map);
                                eiInfo.addAll(info);
                                action = Action.REJECT;
                                break;
                            case 2:
                                //文件不存在，空文件
                                buffer.append("解码失败:" + message + "\n" +
                                        "错误原因:文件不存在或为空文件" + "\n");
//                                DIEISender.EI("文件不存在或为空文件", path);
                                HashMap<String, String> map1 = new HashMap<>();
                                map1.put("message", "文件不存在或为空文件");
                                map1.put("path", path);
                                List<RestfulInfo> info2 = DIEISender.makeEI(map1);
                                eiInfo.addAll(info2);
                                action = Action.REJECT;
                                break;
                        }
                    }
                    action = Action.REJECT;
                }
            } catch (Exception e) {
                action = Action.REJECT;
                e.printStackTrace();
                buffer.append("解码入库失败:" + message + "\n" +
                        "错误原因:" + e + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
            action = Action.REJECT;
            buffer.append("rabbitmq消息错误:" + message + "\n" +
                    "错误原因:消息格式错误");
        } finally {
            if (action == Action.ACCEPT) {
                collector.emit("BABJ-HOR-Bolt", tuple, new Values(horList, buffer,value));
                collector.emit("BABJ-MIN-Bolt", tuple, new Values(minList, buffer,value));
                collector.emit("BABJ-REP-Bolt", new Values(reports, buffer,value));
                collector.ack(tuple);
            } else {
                try {
//                    System.out.println("进入final====REJECT......");
                    buffer.append("文件错误 REJECT" + "\n");
                    buffer.append("INFO : " + new Date() + "end" + "\n");
                    logger.info(buffer.toString());
//                    System.out.println(buffer.toString());
                    collector.ack(tuple);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void declareOutputFields1(OutputFieldsDeclarer declarer) {
        declarer.declareStream("BABJ-HOR-Bolt", new Fields("data", "buffer","value"));
        declarer.declareStream("BABJ-MIN-Bolt", new Fields("data", "buffer","value"));
        declarer.declareStream("BABJ-REP-Bolt", new Fields("data", "buffer","value"));
    }

    @Override
    public void cleanup() {
    }

	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		// TODO Auto-generated method stub
		
	}
}