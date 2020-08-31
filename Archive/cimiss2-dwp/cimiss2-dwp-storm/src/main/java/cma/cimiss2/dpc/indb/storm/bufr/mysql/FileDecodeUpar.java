package cma.cimiss2.dpc.indb.storm.bufr.mysql;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hitec.bufr.util.StringUtil;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.storm.bufr.service.BufrServiceImpl1;
import cma.cimiss2.dpc.indb.storm.bufr.service.OTSServiceImpl;
import cma.cimiss2.dpc.indb.storm.bufr.service.UparBufrServiceImpl;
import cma.cimiss2.dpc.indb.storm.tools.BufrConfig;

public class FileDecodeUpar  {//extends BaseRichBolt
	/*
	public static final Logger Log = LoggerFactory.getLogger("loggerInfo");
	private BufrServiceImpl1 bufrServiceImpl;
	private OTSServiceImpl otsServiceImpl;
	
	public FileDecodeUpar() {
		
	}
	@Override
	public void execute(Tuple tuple) {
//		if (tuple.getSourceStreamId().equals("BUFR-DEC-Bolt")) {
			StringUtil.setConfigPath(configPath);
			BufrConfig.init();
	
			UparBufrServiceImpl bufrServiceImpl = new UparBufrServiceImpl();
			String msg = (String) tuple.getValue(1);
			// bufrServiceImpl.decode(fileName, tables);
			// int length = msg.indexOf(";");
			// msg = msg.substring(length + 1);
			String[] msgs = msg.split(":");
	//		String sod_code = msgs[0];
			String filePath = msgs[1];
			File file = new File(filePath);
			if (!file.exists()) {
				Log.info("File not exist: " + filePath);
				collector.ack(tuple);
			}
			List<String> tableList = BufrConfig.getTableMap().get(section);
			List<StatDi> DI = new ArrayList<>();
			List<RestfulInfo> EI = new ArrayList<>();
			String CCCC = "";
			boolean decode = false;
			
			String fileName = msgs[1];
			int l = fileName.toUpperCase().indexOf("-L-");
			String Priority = fileName.substring(l + 3, l + 5);
			
			if(databaseType == 1 || databaseType == 0)
				decode = bufrServiceImpl.decode(filePath, section, tableList, Priority);
			else
				decode = otsServiceImpl.decode(filePath, section, tableList ,Priority);
			
//			if(databaseType == 1 || databaseType == 0)
//				decode = bufrServiceImpl.decode(filePath, CCCC, section, tableList, DI, EI);
//			else
//				decode = otsServiceImpl.decode(filePath, CCCC, section, tableList, DI, EI);
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
				diInfo.setFields(di);
				diInfos.add(diInfo);
			}
			
			if(decode) {
				collector.emit(new Values(diInfos, EI));
				collector.ack(tuple);
			}else {
				collector.emit(new Values(null, null));
				collector.fail(tuple);
			}
//		}	
	}

	OutputCollector collector;
	private String configPath;
	private String section;
	
//	private boolean writeFile;
//	private String writePath;
	private int databaseType;
	private int fileloop;
	private String dataFlow;
	
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
		
		StringUtil.setConfigPath(this.configPath);
		BufrConfig.init();
		
		bufrServiceImpl = new BufrServiceImpl1();
		otsServiceImpl = new OTSServiceImpl();
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
//		declarer.declareStream("BUFR-DEC-Bolt", new Fields("data", "buffer", "value"));
		declarer.declare(new Fields("diInfo", "eiInfo"));
	}
*/
}
