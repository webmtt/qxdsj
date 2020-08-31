package cma.cimiss2.dpc.indb.storm.dc_surf_aws_reg.ots;

import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.indb.core.bean.StatInsert;
import cma.cimiss2.dpc.indb.core.etl.otsETL.SurfREGEtl;
import cma.cimiss2.dpc.indb.core.ots.OTS;
import cma.cimiss2.dpc.indb.core.ots.OTSHelper;
import com.alicloud.openservices.tablestore.AsyncClient;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Aouthor: xzh
 * @create: 2018-04-16 11:01
 */
public class otsRepBolt extends BaseRichBolt {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(otsRepBolt.class);
    private OutputCollector collector;
    private Map map;
    private String repTable;
    private SurfREGEtl surfREGEtl;
    private OTSHelper repTableHelper;
    private int batchSize;
    private Long intervalTime;
    private String path;
    private String dataFlow;
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.map = stormConf;
        repTable = (String) map.get("repTable");//可以改变表名
        path = (String) map.get("congigPath");
        batchSize = Integer.valueOf(map.get("batchSize").toString());
        intervalTime = Long.valueOf(map.get("intervalTime").toString());
        dataFlow = (String) (map.get("dataFlow"));
        surfREGEtl = new SurfREGEtl();
        surfREGEtl.setIntervalTime(intervalTime);
        surfREGEtl.setPath(path);
        surfREGEtl.setBatchSize(batchSize);
        surfREGEtl.setDataFlow(dataFlow);
        AsyncClient client = OTS.getClient();
        repTableHelper = new OTSHelper(client, repTable);
    }


    @Override
    public void execute(Tuple input) {
        if (input.getSourceStreamId().equals("REG-REP-Bolt")) {
//            System.out.println("REG-REP-Bolt=============");
            List<Map<String, Object>> reports = (List<Map<String, Object>>) input.getValue(0);
            if (reports!=null) {
                StringBuffer sBuffer = (StringBuffer) input.getValue(1);
                String value = (String) input.getValue(2);
                StringBuffer buffer = new StringBuffer();
                buffer.append(sBuffer.toString());
                StatInsert loadRep = null;
                try {
                    long t1 = System.currentTimeMillis();
                    surfREGEtl.setEvent(value.split(":")[0]);
                    surfREGEtl.setFile( new File(value.split(":")[1]));
                    loadRep = surfREGEtl.load(reports, repTableHelper,true);
                    List<RestfulInfo> eiInfo = loadRep.getEiInfo();
                    if (eiInfo!=null&&eiInfo.size()>0){
                        for (int i = 0; i < eiInfo.size(); i++) {
                            EI fields = (EI)eiInfo.get(i).getFields();
                            String message = fields.getKIndex();
                            buffer.append(message+ "\n");
                        }
                    }
                    long t2 = System.currentTimeMillis();
                    buffer.append("decode : num= " + reports.size() +
                            " ，insert " + repTable +
                            " : correct = " + loadRep.getCorrectNum() +
                            " error = " + loadRep.getErrorNum() + "  insertTime : " + (t2 - t1) + "\n");
                    buffer.append("INFO : " + new Date() + "end" + "\n");
                } catch (Exception e) {
                    System.out.println(e);
                }

                logger.info(buffer.toString());
                reports = null;
                input = null;
                buffer = null;
                sBuffer = null;
            }
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("diInfo", "eiInfo"));
    }
}
