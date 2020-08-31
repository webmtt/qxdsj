package cma.cimiss2.dpc.indb.storm.dc_surf_aws_perf.mysql;

import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.indb.core.IocBuilder;
import cma.cimiss2.dpc.indb.core.bean.StatInsert;
import cma.cimiss2.dpc.indb.core.etl.mysqlETL.SurfPERFEtl_new;

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
public class MysqlRepBolt extends BaseRichBolt {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(MysqlRepBolt.class);
    private OutputCollector collector;
    private Map map;
    private SurfPERFEtl_new precipitationObservationDataRegEtl;
    private String repTable;
    private int batchSize;
    private Long intervalTime;
    private String path;
    private String dataSource;
    private String dataFlow;
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.map = stormConf;
        path = (String) map.get("configPath");
        dataSource = (String) (map.get("dataBaseType"));
        batchSize = Integer.valueOf(map.get("batchSize").toString());
        intervalTime = Long.valueOf(map.get("intervalTime").toString());
        dataFlow = (String) (map.get("dataFlow"));
        precipitationObservationDataRegEtl = IocBuilder.ioc(path).get(SurfPERFEtl_new.class);
        precipitationObservationDataRegEtl.setIntervalTime(intervalTime);
        precipitationObservationDataRegEtl.setPath(path);
        precipitationObservationDataRegEtl.setDataSource(dataSource);
        precipitationObservationDataRegEtl.setBatchSize(batchSize);
        precipitationObservationDataRegEtl.setDataFlow(dataFlow);
        repTable = (String) map.get("repTable");
    }


    @Override
    public void execute(Tuple input) {
        if (input.getSourceStreamId().equals("PERF-REP-Bolt")) {
//            System.out.println("PERF-REP-Bolt=============");
            List<Map<String, Object>> reports = (List<Map<String, Object>>) input.getValue(0);
            StringBuffer SBuffer = (StringBuffer) input.getValue(1);
            String value = (String) input.getValue(2);
            StringBuffer buffer = new StringBuffer();
            buffer.append(value.toString());
            StatInsert loadRep = null;
            try {
                long t1 = System.currentTimeMillis();
                precipitationObservationDataRegEtl.setEvent(value.split(":")[0]);
                precipitationObservationDataRegEtl.setFile( new File(value.split(":")[1]));
                loadRep = precipitationObservationDataRegEtl.load(reports, "cimiss");
                List<RestfulInfo> eiInfo = loadRep.getEiInfo();
                if (eiInfo!=null&&eiInfo.size()>0){
                    for (int i = 0; i < eiInfo.size(); i++) {
                        EI fields = (EI)eiInfo.get(i).getFields();
                        String message = fields.getKIndex();
                        buffer.append(message+ "\n");
                    }
                }
                long t2 = System.currentTimeMillis();
                buffer.append(loadRep.getBuffer());
                buffer.append("decode : num= " + reports.size() +
                        " , insert " + repTable +
                        " : correct = " + loadRep.getCorrectNum() +
                        " error = " + loadRep.getErrorNum() + "  insertTime : " + (t2 - t1) + "\n");
                buffer.append("INFO : " + new Date() + "end" + "\n");
            } catch (Exception e) {
                buffer.append("入库 " + repTable + " 失败:"  + "\n" +
                        "错误原因:" + e + "\n");
            }finally{
            	logger.info(buffer.toString());
            	collector.ack(input);
            }
            
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("diInfo", "eiInfo"));
    }
}
