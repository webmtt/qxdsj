package cma.cimiss2.dpc.indb.storm.bufr.mysql;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hitec.bufr.util.StringUtil;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.storm.bufr.service.BufrServiceImpl2;
import cma.cimiss2.dpc.indb.storm.bufr.service.OTSServiceImpl;
import cma.cimiss2.dpc.indb.storm.tools.BufrConfig;

public class FileDecodeAndInsertBolt extends BaseRichBolt { 
	private static final long serialVersionUID = 1L;
	public static final Logger Log = LoggerFactory.getLogger("loggerInfo");
	private BufrServiceImpl2 bufrServiceImpl; 
	private OTSServiceImpl otsServiceImpl;
	SimpleDateFormat ymdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public FileDecodeAndInsertBolt() {
		
	}
	@Override
	public void execute(Tuple tuple) {
		String msg = (String) tuple.getValue(1);
		int idx = msg.indexOf(":");
		if(idx != 16){
			Log.error("Receive error message! \n" + msg);
			collector.ack(tuple); 
		}
		else{
			String filePath = msg.substring(idx + 1);
			Date recv_time = new Date();
			File file = new File(filePath);
			if (!file.exists()) {
				System.out.println("File not exist: " + filePath);
				collector.ack(tuple);
			}
			recv_time = new Date(file.lastModified());
			List<String> tableList = BufrConfig.getTableMap().get(section);
			List<StatDi> DI = new ArrayList<>();
			List<RestfulInfo> EI = new ArrayList<>();
			String CCCC = "";
			boolean decode = false;
			
			if(databaseType == 1 || databaseType == 0)
				decode = bufrServiceImpl.decode(recv_time, filePath, CCCC, section, tableList, DI, EI,useBBB);
			else
				decode = otsServiceImpl.decode(recv_time, filePath, CCCC, section, tableList, DI, EI,useBBB);
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			List<RestfulInfo> diInfos = new ArrayList<>();
			for(StatDi di : DI){
				RestfulInfo diInfo = new RestfulInfo();
				diInfo.setType("RT.DPC.STATION.DI");
				diInfo.setName("台站级资料处理详细信息");
				diInfo.setMessage("台站级资料处理详细信息");
				try {
					diInfo.setOccur_time(dateFormat.parse(di.getDATA_TIME()).getTime());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				di.setDATA_FLOW(dataFlow);
				di.setTRAN_TIME(ymdhm.format(recv_time));
				di.setFILE_SIZE(String.valueOf(file.length()));
				diInfo.setFields(di);
				diInfos.add(diInfo);
			}
			
			if(decode) {
				collector.emit(new Values(diInfos, EI));
				collector.ack(tuple);
			}else {
				Log.info("decodeFalse!");
				collector.ack(tuple);
//				collector.emit(new Values(null, null));
//				collector.fail(tuple);
			}
		}
	}

	OutputCollector collector;
	private String configPath;
	private String section;
	
//	private boolean writeFile;
//	private String writePath;
	private int databaseType;
	private int fileloop;
	private String dataFlow;
	private int useBBB;
	
	@Override
	public void prepare(Map map, TopologyContext context, OutputCollector collector) {
		
		this.collector = collector;
		this.configPath = (String) map.get("configPath");
		this.section = (String) map.get("section");
		
//		this.writeFile = (boolean) map.get("writeFile");
//		this.writePath = (String) map.get("writePath");
		this.databaseType = Integer.parseInt(map.get("dataBaseType").toString());
		this.fileloop = Integer.parseInt(map.get("fileloop").toString());
//		this.diOption = Integer.parseInt(map.get("di.option").toString());
//		this.eiOption = Integer.parseInt(map.get("ei.option").toString());
		this.dataFlow = map.get("dataFlow").toString();
		this.useBBB = Integer.parseInt(map.get("useBBB").toString());
		
		StringUtil.setConfigPath(this.configPath);
		BufrConfig.init();
		
		bufrServiceImpl = new BufrServiceImpl2();
		otsServiceImpl = new OTSServiceImpl();
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
//		declarer.declareStream("BUFR-DEC-Bolt", new Fields("data", "buffer", "value"));
		declarer.declare(new Fields("diInfo", "eiInfo"));
	}

}
