package cma.cimiss2.dpc.indb.storm.bufr.mysql;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
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

public class BinaryDecodeAndInsertBolt extends BaseRichBolt {

	private static final long serialVersionUID = 1L;
	public static final Logger Log = LoggerFactory.getLogger("loggerInfo");
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**   
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)   
	 */ 
	private BufrServiceImpl2 bufrServiceImpl;
	private OTSServiceImpl otsServiceImpl;
	public BinaryDecodeAndInsertBolt() {
		
	}
	SimpleDateFormat ymdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	@Override
	public void execute(Tuple tuple) {

//		StringUtil.setConfigPath(configPath);
//		BufrConfig.init();
//		
//		BufrServiceImpl bufrServiceImpl = new BufrServiceImpl();
		String msg = (String) tuple.getValue(1);//1:Messsge,2:fileName;3:SendTime;4:CCCC
		String fileName = (String)tuple.getValue(2);
		String sendtime = (String)tuple.getValue(3);
		int idx = sendtime.lastIndexOf(".");
		if(idx > -1){ // 精确到秒
			sendtime = sendtime.substring(0, idx);  
		}
			
		String CCCC = (String)tuple.getValue(4);
		Date recv_time = new Date();
		try{ // 时间的两种格式
			if(sendtime.length() == 14)
				recv_time = dateFormat.parse(sendtime);
			else if(sendtime.length() == 19)
				recv_time = dateFormat2.parse(sendtime);
			else {
				Log.error("文件"+ fileName + "的 SendTime 异常" + sendtime + "使用当前系统时间" + recv_time);
			}
		}catch (Exception e) {
			try {
				recv_time = dateFormat2.parse(sendtime);
			} catch (ParseException e1) {
				System.out.println("文件"+ fileName + "的 SendTime 异常" + sendtime + "使用当前系统时间" + recv_time);
				Log.error("文件"+ fileName + "的 SendTime 异常" + sendtime + "使用当前系统时间" + recv_time);
			}
		}
		
		try {
			int length = msg.indexOf(";");
			/*	if(length > -1){
				String msgHead = msg.substring(0, length);
				String[] hs = msgHead.split(",");
				for(String ev : hs) {
					String[] evs = ev.split("=");
					if("FileName".equalsIgnoreCase(evs[0]) && evs.length == 2) {
						fileName = evs[1];
						//break;
					}
					else if("CCCC".equalsIgnoreCase(evs[0]) && evs.length == 2){
						CCCC = evs[1];
					}
				}
			}
			else 
				length = -1;
			*/
			if(length <= -1)
				length = -1;
			if(section.toUpperCase().equals("BUFR_SURF_WEA_REG_HOR") 
					|| section.toUpperCase().equals("BUFR_SURF_WEA_REG_MIN"))
				length = -1;
			//2020-4-26 
			if(msg.indexOf("BUFR") == 43)
				length = -1;
			String msgbody = msg.substring(length + 1);
			List<String> tableList = BufrConfig.getTableMap().get(section);
			byte[] messageBytes = msgbody.getBytes("ISO-8859-1");
			try{
				if(writeFile && !fileName.isEmpty()) {
					String filePath = StringUtil.replaceFileNameExp(fileName, writePath) + fileName;
					Log.info("Write File: " + filePath);
					File file = new File(filePath);
					File pFile = file.getParentFile();
					if (!pFile.exists()) {
						pFile.mkdirs();
					}
					if(file.exists()){
						filePath = filePath + ".1";
					}
					if(messageBytes.length < 1024 * 4)
						FileUtils.writeByteArrayToFile(new File(filePath), messageBytes);
					else{
						Log.info("Considering performance, do not write this file!" + filePath);
					}
//					FileUtil.writeBytesToFile(messageBytes, filePath);
				}
			}catch (Exception e) {
				Log.info("Write File Error! ");
			}
			boolean decode = false;
			List<StatDi> DI = new ArrayList<>();
			List<RestfulInfo> EI = new ArrayList<>();
			if(databaseType == 1 || databaseType == 0)
				decode = bufrServiceImpl.decode(recv_time, fileName, CCCC, messageBytes, section, tableList, DI, EI,useBBB);
			else
				decode = otsServiceImpl.decode(recv_time, fileName, CCCC, messageBytes, section, tableList, DI, EI,useBBB);
			
			List<RestfulInfo> diInfos = new ArrayList<>();
			for(StatDi di : DI){
				RestfulInfo diInfo = new RestfulInfo();
				diInfo.setType("RT.DPC.STATION.DI");
				diInfo.setName("台站级资料处理详细信息");
				diInfo.setMessage("台站级资料处理详细信息");
				try {
					diInfo.setOccur_time(ymdhm.parse(di.getDATA_TIME()).getTime());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				di.setDATA_FLOW(dataFlow);
				di.setTRAN_TIME(ymdhm.format(recv_time));
				di.setFILE_SIZE(String.valueOf(messageBytes.length));
				diInfo.setFields(di);
				diInfos.add(diInfo);
			}
			if(decode) {
				collector.emit(new Values(diInfos, EI));
				collector.ack(tuple);
			}else {
//				collector.emit(new Values(null, null));
//				collector.fail(tuple);
				
				Log.info("decodeFalse!");
				collector.ack(tuple);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	OutputCollector collector;
	private String configPath;
	private String section;
	
	private boolean writeFile;
	private String writePath;
	private int databaseType;
	private int fileloop;
//	private int eiOption;
//	private int diOption;
	private String dataFlow;
	private int useBBB;
	@Override
	public void prepare(Map map, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		this.configPath = (String) map.get("configPath");
		this.section = (String) map.get("section");
		
		this.writeFile = (boolean) map.get("writeFile");
		this.writePath = (String) map.get("writePath");
//		this.databaseType = (int)map.get("dataBaseType");
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
		declarer.declare(new Fields("diInfo", "eiInfo"));
//		declarer.declareStream("BUFR-DEC-Bolt", new Fields("data", "buffer", "value"));
	}

}
