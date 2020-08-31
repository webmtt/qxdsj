package cma.cimiss2.dpc.indb.storm.dc_surf_aws_perf.ots;

import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.indb.core.bean.StatInsert;
import cma.cimiss2.dpc.indb.core.etl.otsETL.SurfPERFEtl;
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
 * @create: 2018-04-16 11:01
 */
public class otsMinBolt extends BaseRichBolt {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(otsMinBolt.class);
    private OutputCollector collector;
    private Map map;
    private String minTable;
    private SurfPERFEtl precipitationObservationDataRegEtl;
    private OTSHelper minTableHelper ;
    private int batchSize;
    private Long intervalTime;
    private String path;
    private String dataFlow;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.map = stormConf;
        path = (String) map.get("congigPath");
        batchSize = Integer.valueOf(map.get("batchSize").toString());
        intervalTime = Long.valueOf(map.get("intervalTime").toString());
        dataFlow = (String)(map.get("dataFlow"));
        precipitationObservationDataRegEtl = new SurfPERFEtl();
        precipitationObservationDataRegEtl.setIntervalTime(intervalTime);
        precipitationObservationDataRegEtl.setPath(path);
        precipitationObservationDataRegEtl.setBatchSize(batchSize);
        precipitationObservationDataRegEtl.setDataFlow(dataFlow);
        minTable = (String) map.get("minTable");
        AsyncClient client = OTS.getClient();
        minTableHelper = new OTSHelper(client, minTable);
    }

    @Override
    public void execute(Tuple input) {

        if (input.getSourceStreamId().equals("PERF-MIN-Bolt")) {
//            System.out.println("REG-MIN-Bolt=============");
            List<Map<String, Object>> minList = (List<Map<String, Object>>) input.getValue(0);
            if (minList != null) {
                StringBuffer sBuffer = (StringBuffer) input.getValue(1);
                String value = (String) input.getValue(2);
                StringBuffer buffer = new StringBuffer();
                buffer.append(sBuffer.toString());

                StatInsert loadMin = null;
                try {
                    long t1 = System.currentTimeMillis();
                    precipitationObservationDataRegEtl.setEvent(value.split(":")[0]);
                    precipitationObservationDataRegEtl.setFile(new File(value.split(":")[1]));
                    loadMin = precipitationObservationDataRegEtl.load(minList,minTableHelper,false);
                    List<RestfulInfo> eiInfo = loadMin.getEiInfo();
                    long t2 = System.currentTimeMillis();
                    buffer.append(loadMin.getBuffer());
                    buffer.append("decode : num= " + minList.size() +
                            " ，insert " + minTable +
                            " : correct = " + loadMin.getCorrectNum() +
                            " error = " + loadMin.getErrorNum() + "  insertTime : " + (t2 - t1) + "\n");
                    buffer.append("INFO : " + new Date() + "end" + "\n");
                } catch (Exception e) {
                    buffer.append("入库 " + minTable + " 失败:" + "precipitationObservationDataRegEtl.getFile().getPath()" + "\n" +
                            "错误原因:" + e.getMessage() + "\n");
                }

                logger.info(buffer.toString());
                if (loadMin != null) {
                    collector.emit(new Values(loadMin.getDiInfo(), loadMin.getEiInfo()));
                    if (loadMin.getAction() == 0)
                    collector.ack(input);
                }
                minList = null;
                input=null;

            }
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("diInfo", "eiInfo"));
    }
}
