package cma.cimiss2.dpc.indb.storm;

import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.indb.core.tools.DIEISender;
import cma.cimiss2.dpc.indb.core.tools.RestfulSendData;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.nutz.json.Json;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DIEI 发送bolt
 */
public class DIEIBolt extends BaseRichBolt {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DIEIBolt.class);
    private static final long serialVersionUID = -7666702296317848920L;
    private OutputCollector collector;
    private String path;
    private List<RestfulInfo> diInfo = new ArrayList<>();
    private List<RestfulInfo> eiInfo = new ArrayList<>();
    private long eiStartTime = System.currentTimeMillis();
    private long eiEndTime = System.currentTimeMillis();
    private long diStartTime = System.currentTimeMillis();
    private long diEndTime = System.currentTimeMillis();

    public void prepare(Map map, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        path = (String) map.get("configPath");
        DIEISender.LOCAL_DI_OPTION = (Boolean) map.get("diOption");
        DIEISender.LOCAL_EI_OPTION = (Boolean) map.get("eiOption");
    }

    @Override
    public void execute(Tuple tuple) {
    	// 从下面移到此处 2019-11-12
    	collector.ack(tuple);
    	
    	
        List<RestfulInfo> di = (List<RestfulInfo>) tuple.getValueByField("diInfo");
        List<RestfulInfo> ei = (List<RestfulInfo>) tuple.getValueByField("eiInfo");
        if(di != null){
        	diInfo.addAll(di);
        }
        if(ei != null){
        	eiInfo.addAll(ei);
        }
        diEndTime = System.currentTimeMillis();
        if (DIEISender.GLOBAL_DI_OPTION && DIEISender.LOCAL_DI_OPTION) {
        	if(diInfo.size() >= 200 || (diEndTime - diStartTime)/1000 >= 3){
        		int d = RestfulSendData.getInstance(path).SendData(diInfo, 0);
        		diStartTime = System.currentTimeMillis();
//        		logger.info(Json.toJson(diInfo));
        		if(d == 0){
        			logger.info(" di发送成功：num="+diInfo.size());
        		}else if(d == -1){
        			logger.info("di发送失败：num="+diInfo.size());
        		}
        		diInfo.clear();
        	}
        }
        eiEndTime = System.currentTimeMillis();
        if (DIEISender.GLOBAL_EI_OPTION && DIEISender.LOCAL_EI_OPTION) {
        	if(eiInfo.size() >= 200 || (eiEndTime - eiStartTime)/1000 >= 3){
        		int e = RestfulSendData.getInstance(path).SendData(eiInfo, 1);
        		 eiStartTime = System.currentTimeMillis(); 
        		 if(e == 0){
         			logger.info("ei发送成功：num="+eiInfo.size());
         		}else if(e == -1){
         			logger.info("ei发送失败：num="+eiInfo.size());
         		}
        		 eiInfo.clear();
        	}
        }
        /* if (DIEISender.GLOBAL_DI_OPTION && DIEISender.LOCAL_DI_OPTION) {

            int d = RestfulSendData.SendData(di, path, 0);
        }
        if (DIEISender.GLOBAL_EI_OPTION && DIEISender.LOCAL_EI_OPTION) {
            int e = RestfulSendData.SendData(ei, path, 1);
        }*/
//        System.err.println("======diei数量=========" + diei.size());
        
        // chy 2019-11-12
//        collector.ack(tuple);
        di=null;
        ei=null;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("diInfo", "eiInfo"));
    }

    @Override
    public void cleanup() {
        System.out.println("===================cleanup================");
    }
}
