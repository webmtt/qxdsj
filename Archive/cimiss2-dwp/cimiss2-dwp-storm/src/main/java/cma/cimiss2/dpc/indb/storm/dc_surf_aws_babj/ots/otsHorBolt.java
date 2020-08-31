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
import org.apache.storm.tuple.Values;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Aouthor: xzh
 * @create: 2018-04-16 11:00
 */
public class otsHorBolt extends BaseRichBolt {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(otsHorBolt.class);

    private OutputCollector collector;
    private Map map;
    private String horTable;
    private SurfBABJEtl surfBABJEtl;
    private int batchSize;
    private Long intervalTime;
    private String path;
    private OTSHelper horTableHelper;
    private String dataFlow;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.map = stormConf;
        path = (String) map.get("congigPath");
        batchSize = Integer.valueOf(map.get("batchSize").toString());
        intervalTime = Long.valueOf(map.get("intervalTime").toString());
        dataFlow = (String) (map.get("dataFlow"));
        surfBABJEtl = new SurfBABJEtl();
        surfBABJEtl.setIntervalTime(intervalTime);
        surfBABJEtl.setPath(path);
        surfBABJEtl.setBatchSize(batchSize);
        surfBABJEtl.setDataFlow(dataFlow);
        horTable = (String) map.get("horTable");
        AsyncClient client = OTS.getClient();
        horTableHelper = new OTSHelper(client, horTable);
    }

    @Override
    public void execute(Tuple input) {

        if (input.getSourceStreamId().equals("BABJ-HOR-Bolt")) {
//            System.out.println("BABJ-HOR-Bolt=============");
            List<Map<String, Object>> horList = (List<Map<String, Object>>) input.getValue(0);
            StringBuffer sBuffer = (StringBuffer) input.getValue(1);
            String value = (String) input.getValue(2);
            StringBuffer buffer = new StringBuffer();
            buffer.append(sBuffer.toString());
            StatInsert loadHor = null;
            try {
                long t1 = System.currentTimeMillis();
                surfBABJEtl.setEvent(value.split(":")[0]);
                surfBABJEtl.setFile(new File(value.split(":")[1]));
                if (horList != null && horList.size() > 0) {
                    loadHor = surfBABJEtl.load(horList, horTableHelper, false);
                }
                long t2 = System.currentTimeMillis();
                buffer.append(loadHor.getBuffer());
                buffer.append("decode : num = " + horList.size() +
                        " ，insert " + horTable +
                        " : correct = " + loadHor.getCorrectNum() +
                        " error = " + loadHor.getErrorNum() + "  insertTime : " + (t2 - t1) + "\n");
                buffer.append("INFO : " + new Date() + "end" + "\n");
            } catch (Exception e) {
                buffer.append("入库 " + horTable + " 失败:" + "surfBABJEtl.getFile().getPath()" + "\n" +
                        "错误原因:" + e.getMessage() + "\n");
            }
            if (horList != null && horList.size() > 0) {
                logger.info(buffer.toString());
                collector.emit(new Values(loadHor.getDiInfo(), loadHor.getEiInfo()));
                if (loadHor.getAction() == 0)
                    collector.ack(input);

            } else {
                collector.emit(new Values(null, null));

                collector.ack(input);
            }

            horList = null;
            input = null;
        }


    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("diInfo", "eiInfo"));
    }
}
