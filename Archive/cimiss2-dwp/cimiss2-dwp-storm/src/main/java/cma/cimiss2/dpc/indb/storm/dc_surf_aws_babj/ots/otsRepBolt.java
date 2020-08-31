package cma.cimiss2.dpc.indb.storm.dc_surf_aws_babj.ots;

import cma.cimiss2.dpc.indb.core.bean.StatInsert;
import cma.cimiss2.dpc.indb.core.etl.otsETL.SurfBABJEtl;
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
    private SurfBABJEtl surfBABJEtl;
    private OTSHelper repTableHelper;
    private int batchSize;
    private Long intervalTime;
    private String path;
    private String dataFlow;

    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.map = stormConf;
        repTable = (String) map.get("repTable");//可以改变表名
        path = (String) map.get("congigPath");
        batchSize = Integer.valueOf(map.get("batchSize").toString());
        intervalTime = Long.valueOf(map.get("intervalTime").toString());
        dataFlow = (String) (map.get("dataFlow"));
        surfBABJEtl = new SurfBABJEtl();
        surfBABJEtl.setIntervalTime(intervalTime);
        surfBABJEtl.setPath(path);
        surfBABJEtl.setBatchSize(batchSize);
        surfBABJEtl.setDataFlow(dataFlow);
        AsyncClient client = OTS.getClient();
        repTableHelper = new OTSHelper(client, repTable);
    }


    public void execute(Tuple input) {
        if (input.getSourceStreamId().equals("BABJ-REP-Bolt")) {
//            System.out.println("BABJ-REP-Bolt=============");
            List<Map<String, Object>> reports = (List<Map<String, Object>>) input.getValue(0);
            if (reports != null) {
                StringBuffer sBuffer = (StringBuffer) input.getValue(1);
                String value = (String) input.getValue(2);
                StringBuffer buffer = new StringBuffer();
                buffer.append(sBuffer.toString());
                StatInsert loadRep = null;
                try {
                    long t1 = System.currentTimeMillis();
                    surfBABJEtl.setEvent(value.split(":")[0]);
                    surfBABJEtl.setFile( new File(value.split(":")[1]));
                    loadRep = surfBABJEtl.load(reports, repTableHelper, true);
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
//                System.out.println(buffer.toString());
                reports = null;
                input = null;
                buffer = null;
                sBuffer = null;
            }
        }

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("diInfo", "eiInfo"));
    }
}
