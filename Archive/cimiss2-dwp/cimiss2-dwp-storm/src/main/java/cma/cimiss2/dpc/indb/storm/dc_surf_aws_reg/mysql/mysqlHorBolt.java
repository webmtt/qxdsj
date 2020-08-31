package cma.cimiss2.dpc.indb.storm.dc_surf_aws_reg.mysql;

import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.indb.core.IocBuilder;
import cma.cimiss2.dpc.indb.core.bean.StatInsert;
import cma.cimiss2.dpc.indb.core.etl.mysqlETL.SurfREGEtl_new;

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
public class mysqlHorBolt extends BaseRichBolt {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(mysqlHorBolt.class);
    private OutputCollector collector;
    private Map map;
    private String horTable;
    private SurfREGEtl_new surfREGEtl;
    private int batchSize;
    private Long intervalTime;
    private String path;
    private String dataSource;
    private String dataFlow;
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.map = stormConf;
        String path = (String) map.get("configPath");
        surfREGEtl = IocBuilder.ioc(path).get(SurfREGEtl_new.class);

        batchSize = Integer.valueOf(map.get("batchSize").toString());
        dataSource = (String) (map.get("dataBaseType"));
        intervalTime = Long.valueOf(map.get("intervalTime").toString());
        dataFlow = (String) (map.get("dataFlow"));
        surfREGEtl.setIntervalTime(intervalTime);
        surfREGEtl.setPath(path);
        surfREGEtl.setBatchSize(batchSize);
        surfREGEtl.setDataSource(dataSource);
        surfREGEtl.setDataFlow(dataFlow);
        horTable = (String) map.get("horTable");
    }

    @Override
    public void execute(Tuple input) {
        if (input.getSourceStreamId().equals("REG-HOR-Bolt")) {
//            System.out.println("REG-HOR-Bolt=============");
//        	System.out.println("reg_hor_tuple_id=" + input.getMessageId());
            List<Map<String, Object>> horList = (List<Map<String, Object>>) input.getValue(0);
            StringBuffer sBuffer = (StringBuffer) input.getValue(1);
            String value = (String) input.getValue(2);
            StringBuffer buffer = new StringBuffer();
            buffer.append("reg_hor_tuple_id=" + input.getMessageId() + "\n");
            buffer.append(sBuffer.toString());
            StatInsert loadHor = null;
            try {
                long t1 = System.currentTimeMillis();
                surfREGEtl.setEvent(value.split(":")[0]);
                surfREGEtl.setFile( new File(value.split(":")[1]));
                loadHor = surfREGEtl.load(horList, "rdb");
                List<RestfulInfo> eiInfo = loadHor.getEiInfo();
                if (eiInfo!=null&&eiInfo.size()>0){
                    for (int i = 0; i < eiInfo.size(); i++) {
                        EI fields = (EI)eiInfo.get(i).getFields();
                        String message = fields.getKIndex();
                        buffer.append(message+ "\n");
                    }
                }
                long t2 = System.currentTimeMillis();
                buffer.append(loadHor.getBuffer());
                buffer.append("decode : num= " + horList.size() +
                        " , insert " + horTable +
                        " : correct = " + loadHor.getCorrectNum() +
                        " error = " + loadHor.getErrorNum() +"  insertTime : " + (t2 - t1) +  "\n");
                buffer.append("INFO : " + new Date() + "end" + "\n");
            } catch (Exception e) {
                buffer.append("入库 " + horTable + " 失败:" + surfREGEtl.getFile().getPath() + "\n" +
                        "错误原因:" + e.getMessage() + "\n");
            }finally {
            	 if (horList != null && horList.size() > 0 && loadHor != null) {
            		 logger.info(buffer.toString());
                     collector.emit(new Values(loadHor.getDiInfo(), loadHor.getEiInfo()));
                     //collector.ack(input);
                     
                 }else {
                     collector.emit(new Values(null, null));
                     //collector.ack(input);
                 }
                 collector.ack(input);
                 horList = null;
                 input = null;
            }
        }


    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("diInfo", "eiInfo"));
    }
}
