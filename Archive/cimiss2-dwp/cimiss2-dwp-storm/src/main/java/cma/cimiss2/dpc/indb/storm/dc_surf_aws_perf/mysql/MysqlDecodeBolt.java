package cma.cimiss2.dpc.indb.storm.dc_surf_aws_perf.mysql;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.PrecipitationObservationDataReg;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import cma.cimiss2.dpc.indb.core.IocBuilder;
import cma.cimiss2.dpc.indb.core.bean.StatTF;
import cma.cimiss2.dpc.indb.core.etl.mysqlETL.SurfPERFEtl_new;
import cma.cimiss2.dpc.indb.core.tools.DIEISender;
import cma.cimiss2.dpc.indb.storm.tools.LoadPropertiesFile;

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
 * @Description:雨量站-mysql-解码bolt
 * @Aouthor: xzh
 * @create: 2018-04-16 10:59
 */
public class MysqlDecodeBolt extends BaseRichBolt {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(MysqlDecodeBolt.class);
    private org.slf4j.Logger messageLogger = LoggerFactory.getLogger("messageNote");
    private static final long serialVersionUID = -7666702251317848927L;
    private OutputCollector collector;
    private Map map;
    private Action action;
    private String path;
    private String dataSource;
    private String horTable;
    private String minTable;
    private String minPreTable;
    private String cumPreTable;
    private String repTable;
    private int batchSize;
    private Long intervalTime;
    private SurfPERFEtl_new precipitationObservationDataRegEtl;
    private String dataFlow;
    private Map<String,String> codeMap = new HashMap<String,String>();

    @Override
    public void prepare(Map map, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.map = map;
        horTable = (String) map.get("horTable");
        minTable = (String) map.get("minTable");
        minPreTable = (String) map.get("minPreTable");
        cumPreTable = (String) map.get("cumPreTable");
        repTable = (String) map.get("repTable");
        path = (String) map.get("configPath");
        dataSource = (String) (map.get("dataBaseType"));
        batchSize = Integer.valueOf(map.get("batchSize").toString());
        intervalTime = Long.valueOf(map.get("intervalTime").toString());
        dataSource = (String) (map.get("dataBaseType"));
        dataFlow = (String)(map.get("dataFlow"));
        codeMap.put("cts_code", (String)map.get("cts_code"));
        codeMap.put("hor_sod_code", (String)map.get("hor_sod_code"));
        codeMap.put("mul_sod_code", (String)map.get("mul_sod_code"));
        codeMap.put("pre_sod_code", (String)map.get("pre_sod_code"));
        codeMap.put("accu_sod_code", (String)map.get("accu_sod_code"));
        codeMap.put("report_sod_code", (String)map.get("report_sod_code"));
        precipitationObservationDataRegEtl = IocBuilder.ioc(path).get(SurfPERFEtl_new.class);
        precipitationObservationDataRegEtl.setIntervalTime(intervalTime);
        precipitationObservationDataRegEtl.setPath(path);
        precipitationObservationDataRegEtl.setBatchSize(batchSize);
        precipitationObservationDataRegEtl.setDataSource(dataSource);
        precipitationObservationDataRegEtl.setBatchSize(batchSize);
        precipitationObservationDataRegEtl.setDataFlow(dataFlow);
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
        List<Map<String, Object>> minPreList = null;
        List<Map<String, Object>> cumPreList = null;
        List<Map<String, Object>> reports = null;
        String event = null;
        String value = null;
        action = Action.ACCEPT;
        try {
            value = (String) tuple.getValue(1);
//            event = value.split(":")[0];
//            message = value.split(":")[1];
            int idx = value.indexOf(":");
            event = value.substring(0, idx);
            message = value.substring(idx + 1);
            
            precipitationObservationDataRegEtl.setEvent(event);
            File file = new File(message);
            precipitationObservationDataRegEtl.setFile(file);
            //messageLogger.info(value);
            buffer.append("INFO : " + new Date() + " begin : " + message + "\n");
            //解码
            try {
            	
                ParseResult<PrecipitationObservationDataReg> result = precipitationObservationDataRegEtl.extract(file);
                errorList = result.getError();
                //格式化数据
                StatTF statPERF = precipitationObservationDataRegEtl.transform(result, file, codeMap, horTable, minTable, minPreTable, cumPreTable, repTable);
                horList = statPERF.getHorList();
                minList = statPERF.getMinList();
                minPreList = statPERF.getMinPreList();
                cumPreList = statPERF.getCumPreList();
                reports = statPERF.getRepList();
                buffer.append(statPERF.getBuffer().toString());
                if (result.isSuccess()) {//解码you成功
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
                    //解码失败
                    collector.emit("PERF-REP-Bolt", new Values(reports, new StringBuffer(buffer),value));
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
            	e.printStackTrace();
                action = Action.REJECT;
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
                collector.emit("PERF-HOR-Bolt", tuple, new Values(horList, buffer, value));
                collector.emit("PERF-MIN-Bolt", tuple, new Values(minList, buffer, value));
                collector.emit("PERF-MIN-PRE-Bolt", tuple, new Values(minPreList, buffer, value));
                collector.emit("PERF-CUM-PRE-Bolt", tuple, new Values(cumPreList, buffer, value));
                collector.emit("PERF-REP-Bolt", new Values(reports, buffer, value));
                //collector.ack(tuple);
            } else {
                try {
                    buffer.append("消息错误 REJECT" + "\n");
                    buffer.append("INFO : " + new Date() + "end" + "\n");
                    logger.info(buffer.toString());
                    //collector.ack(tuple);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            collector.ack(tuple);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
//        declarer.declare(new Fields("diInfo", "eiInfo"));
        declarer.declareStream("PERF-HOR-Bolt", new Fields("data", "buffer", "value"));
        declarer.declareStream("PERF-MIN-Bolt", new Fields("data", "buffer", "value"));
        declarer.declareStream("PERF-MIN-PRE-Bolt", new Fields("data", "buffer", "value"));
        declarer.declareStream("PERF-CUM-PRE-Bolt", new Fields("data", "buffer", "value"));
        declarer.declareStream("PERF-REP-Bolt", new Fields("data", "buffer", "value"));
    }

    @Override
    public void cleanup() {
    }
}
