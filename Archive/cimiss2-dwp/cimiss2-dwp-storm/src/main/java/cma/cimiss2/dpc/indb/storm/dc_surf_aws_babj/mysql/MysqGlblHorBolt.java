package cma.cimiss2.dpc.indb.storm.dc_surf_aws_babj.mysql;

import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.indb.core.IocBuilder;
import cma.cimiss2.dpc.indb.core.bean.StatInsert;
import cma.cimiss2.dpc.indb.core.etl.mysqlETL.SurfBABJEtl_new;
import lombok.val;

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
 * @Description:国家站-mysql-小时数据入库bolt
 * @Aouthor: xzh
 * @create: 2018-04-16 11:00
 */
public class MysqGlblHorBolt extends BaseRichBolt {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(MysqGlblHorBolt.class);
    private OutputCollector collector;
    private Map map;
    private String glbHorTable;
    private SurfBABJEtl_new surfBABJEtl;
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
        surfBABJEtl = IocBuilder.ioc(path).get(SurfBABJEtl_new.class);

        batchSize = Integer.valueOf(map.get("batchSize").toString());
        dataSource = (String) (map.get("dataBaseType"));
        intervalTime = Long.valueOf(map.get("intervalTime").toString());
        dataFlow = (String) (map.get("dataFlow"));
        surfBABJEtl.setIntervalTime(intervalTime);
        surfBABJEtl.setPath(path);
        surfBABJEtl.setBatchSize(batchSize);
        surfBABJEtl.setDataSource(dataSource);
        surfBABJEtl.setDataFlow(dataFlow);
        glbHorTable = (String) map.get("glbHorTable");
        
    }

    @Override
    public void execute(Tuple input) {
        if (input.getSourceStreamId().equals("BABJ-GLB-HOR-Bolt")) {
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
                
                surfBABJEtl.setFile(new File(value.substring(value.indexOf(":") + 1)));
                
//                surfBABJEtl.setFile(new File(value.split(":")[1]));
                loadHor = surfBABJEtl.load(horList, "rdb");
                List<RestfulInfo> eiInfo = loadHor.getEiInfo();
                if (eiInfo != null && eiInfo.size() > 0) {
                    for (int i = 0; i < eiInfo.size(); i++) {
                        EI fields = (EI) eiInfo.get(i).getFields();
                        String message = fields.getKIndex();
                        buffer.append(message + "\n");
                    }
                }
                long t2 = System.currentTimeMillis();
                buffer.append(loadHor.getBuffer());
                buffer.append("decode : num= " + horList.size() +
                        " , insert " + glbHorTable +
                        " : correct = " + loadHor.getCorrectNum() +
                        " error = " + loadHor.getErrorNum() + "  insertTime : " + (t2 - t1) + "\n");
                buffer.append("INFO : " + new Date() + "end" + "\n");
            } catch (Exception e) {
                buffer.append("入库 " + glbHorTable + " 失败:" + surfBABJEtl.getFile().getPath() + "\n" +
                        "错误原因:" + e.getMessage() + "\n");
            }finally {
            	 if (horList != null && horList.size() > 0 && loadHor != null) {
                     logger.info(buffer.toString());
                     collector.emit(new Values(loadHor.getDiInfo(), loadHor.getEiInfo()));
                     //collector.ack(input);

                 } else {
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
