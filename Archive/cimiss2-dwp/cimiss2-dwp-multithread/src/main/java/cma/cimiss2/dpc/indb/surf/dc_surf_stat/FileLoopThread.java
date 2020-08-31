package cma.cimiss2.dpc.indb.surf.dc_surf_stat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.FileUtil;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.agme.PropertiesUtil;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.surf.DecodeStat;
import cma.cimiss2.dpc.decoder.surf.SurfStatParseConfig;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.surf.dc_surf_stat.service.DbService;
import cma.cimiss2.dpc.indb.surf.dc_surf_stat.service.OTSService;

// TODO: Auto-generated Javadoc
/**
 * The Class FileLoopThread.
 */
public class FileLoopThread implements Runnable{
	
	/** The Constant infoLogger. */
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	/** The Constant messageLogger. */
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	
	/** The file N. */
	public static String fileN = null;
	
	/** The simple date format. */
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/** The files. */
	BlockingQueue<String> files;
	
	/**
	 * Instantiates a new file loop thread.
	 *
	 * @param files the files
	 * @param diQueues the di queues
	 */
	public FileLoopThread(BlockingQueue<String> files, BlockingQueue<StatDi> diQueues) {
		this.files = files;
		DbService.setDiQueues(diQueues);
		OTSService.setDiQueues(diQueues);
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while(true) {
			if(files.size() > 0) {
				String filepath = files.poll();
				messageLogger.info(filepath);
				File file = new File(filepath);
				fileN = file.getName(); // 文件名
				if(file.exists() && file.isFile()) {
					Date recv_time = new Date(file.lastModified());
					StringBuffer loggerBuffer = new StringBuffer();
					 String[] names=fileN.split("_");
			        String tableProp = "config/Surf_Stat_"+names[7]+"_TAB.xml";
			        if(names[7].equals("WEA")) {
			        	names[7]="DAY";
			        }
			        String dataProp = "config/Surf_Stat_"+names[7]+"_setups.properties";
					PropertiesUtil.setConfigFile(dataProp);
					SurfStatParseConfig.config = tableProp;
					DecodeStat decodeStat = new DecodeStat();
					ParseResult<String> parseResult = decodeStat.decodeFile(filepath, "	", recv_time);
					loggerBuffer.append(" : " + simpleDateFormat.format(new Date(file.lastModified())) +" "+ file.getPath() + "\n");
					
					if(parseResult.isSuccess()){
						// 获取错误的报文结果集
						List<ReportError> reportErrors = parseResult.getError();
						if(reportErrors != null && reportErrors.size() > 0) {
							for (int i = 0; i < reportErrors.size(); i++) {
								loggerBuffer.append(" ERROR REPORT : "+reportErrors.get(i).getSegment()+"\n"+"\t " + reportErrors.get(i).getMessage() + "\n");
							}
						}
						DataBaseAction dataBaseAction = null;
						// 获取正确报文的解码对象集合
						if(StartConfig.getDatabaseType() == 1 || StartConfig.getDatabaseType() == 0) {
							dataBaseAction = DbService.processSuccessReport(parseResult, recv_time, fileN, loggerBuffer,filepath);	
						}else if (StartConfig.getDatabaseType() == 2) {
							dataBaseAction = OTSService.processSuccessReport(parseResult,recv_time, fileN, loggerBuffer, StartConfig.valueTable());
						}else {
							// 文件不移走
						}
						
						infoLogger.info(loggerBuffer.toString());
						// 数据库连接错误
						if(dataBaseAction == DataBaseAction.CONNECTION_ERROR){					
							// 文件不移走
						// 批量提交错误
						}else {	
							FileUtil.moveFile(filepath);
						}					
						
					}else {
						FileUtil.moveFile(filepath);
						List<ReportError> reportErrors = parseResult.getError();
						if(reportErrors != null && reportErrors.size() > 0) {
							for (int i = 0; i < reportErrors.size(); i++) {
								loggerBuffer.append(" ERROR REPORT : "+reportErrors.get(i).getSegment()+"\n"+"\t " + reportErrors.get(i).getMessage() + "\n");
							}
						}
						ParseInfo parseInfo = parseResult.getParseInfo();
						if(parseInfo != null) {
							infoLogger.error("\n read file error ："+file.getPath()+"\n Error Description:"+parseInfo.getDescription());
							
							String event_type = EIEventType.OP_FILE_ERROR.getCode();
							EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
							if(ei == null) {
								infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
							}else {
								if(StartConfig.isSendEi() && Integer.parseInt(ei.getEVENT_LEVEL()) > 0 ) {
									ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
									ei.setKObject("cma.cimiss2.dpc.indb.upar.dc_upar_hkflash.FileLoopThread");
									ei.setKEvent("解码入库源文件异常：");
									ei.setKIndex("详细信息：["+parseInfo.getDescription()+"]"+file.getPath());
									ei.setEVENT_EXT1(file.getName());
									RestfulInfo restfulInfo = new RestfulInfo();
									restfulInfo.setType("SYSTEM.ALARM.EI ");
									restfulInfo.setName("数据解码入库EI告警信息");
									restfulInfo.setMessage("数据解码入库EI告警信息");
									
									restfulInfo.setFields(ei);
									List<RestfulInfo> restfulInfos = new ArrayList<>();
									restfulInfos.add(restfulInfo);
									RestfulSendData.SendData(restfulInfos, SendType.EI);
								}
							}
						}			
						//// 文件不移走
					}	
				}else {
					// 文件不存在
				}
			}else {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
}
