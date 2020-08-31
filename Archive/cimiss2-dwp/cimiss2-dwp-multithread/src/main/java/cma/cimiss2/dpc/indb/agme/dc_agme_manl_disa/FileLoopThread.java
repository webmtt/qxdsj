package cma.cimiss2.dpc.indb.agme.dc_agme_manl_disa;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import cma.cimiss2.dpc.decoder.agme.DecodeDisa;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeDisa;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;

import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.CTSCodeMap;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.FileUtil;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.indb.agme.OTSReportInfoService;
import cma.cimiss2.dpc.indb.agme.dc_agme_manl_disa.service.*;
// TODO: Auto-generated Javadoc

/**
 * -------------------------------------------------------------------------------
 * <br>.
 *
 * @author wuzuoqiang
 * ---------------------------------------------------------------------------------
 * @Title:  FileLoopThread.java
 * @Package cma.cimiss2.dpc.indb.agme.disa
 * @Description:    TODO(文件目录轮询处理线程)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年7月9日 下午11:01:34   wuzuoqiang    Initial creation.
 * </pre>
 */
public class FileLoopThread implements Runnable{
	
	/** The Constant infoLogger. */
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	/** The Constant messageLogger. */
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	
	/** The simple date format. */
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/** The files. */
	BlockingQueue<String> files;
	
	/** The cts code maps. */
	List<CTSCodeMap> ctsCodeMaps;
	
	/** The file N. */
	public static String fileN = null;
	
	/** The v bbb. */
	public static String v_bbb = "000";
	
	/** The is revised. */
	public static boolean isRevised = false;
	
	/**
	 * Instantiates a new file loop thread.
	 *
	 * @param files the files
	 * @param diQueues the di queues
	 * @param ctsCodeMaps the cts code maps
	 */
	public FileLoopThread(BlockingQueue<String> files, BlockingQueue<StatDi> diQueues, List<CTSCodeMap> ctsCodeMaps) {
		this.files = files;
		this.ctsCodeMaps = ctsCodeMaps;
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
				fileN = file.getName();
				if(fileN.contains("-CC")){
					isRevised = true;
					v_bbb = fileN.substring(fileN.indexOf("-CC") + 1, fileN.indexOf("-CC") + 4);
				}
				else{
					isRevised = false;
					v_bbb = "000";
				}
				DecodeDisa decodeDisa = new DecodeDisa();
				if(file.exists() && file.isFile()) {
					Date recv_time = new Date(file.lastModified());
					StringBuffer loggerBuffer = new StringBuffer();
					loggerBuffer.append(" : " + simpleDateFormat.format(new Date(file.lastModified())) +" "+ file.getPath() + "\n");
				
					if(file.length()  > StartConfig.maxFileSize()){
						infoLogger.info("\n File is too large(" + file.length() + "byte), ignore it! Filepath is: " + filepath);
						continue;
					}
					
					ParseResult<ZAgmeDisa> parseResult = decodeDisa.decodeFile(file);	
					if(parseResult.isSuccess()){
						// 获取错误的报文结果集
						List<ReportError> reportErrors = parseResult.getError();
						if(reportErrors != null && reportErrors.size() > 0) {
							for (int i = 0; i < reportErrors.size(); i++) {
								loggerBuffer.append(" ERROR REPORT : "+reportErrors.get(i).getSegment()+"\n"+"\t " + reportErrors.get(i).getMessage() + "\n");
							}
						}
						
						// 获取正确报文的解码对象集合
						DataBaseAction dataBaseAction = null;
						if(StartConfig.getDatabaseType() == 1 || StartConfig.getDatabaseType() == 0) {
							dataBaseAction =  DbService.processSuccessReport(parseResult, recv_time, fileN,v_bbb,isRevised,loggerBuffer,ctsCodeMaps);
						}else if (StartConfig.getDatabaseType() == 2) {
							dataBaseAction = OTSService.processSuccessReport(parseResult, recv_time,fileN, isRevised,v_bbb,loggerBuffer,ctsCodeMaps);
							String[] fnames = fileN.split("_");
							List<ReportInfo> reportInfos = parseResult.getReports();
							OTSReportInfoService.reportInfoToDb(reportInfos, v_bbb, recv_time, fnames[3], fnames[1],loggerBuffer,ctsCodeMaps);
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
						List<ReportError> reportErrors = parseResult.getError();
						if(reportErrors != null && reportErrors.size() > 0) {
							for (int i = 0; i < reportErrors.size(); i++) {
								loggerBuffer.append(" ERROR REPORT : "+reportErrors.get(i).getSegment()+"\n"+"\t " + reportErrors.get(i).getMessage() + "\n");
							}
						}
						ParseInfo parseInfo = parseResult.getParseInfo();
						if(parseInfo != null) {
							infoLogger.error("\n read file error ："+file.getPath()+"\n 错误描述:"+parseInfo.getDescription());
							
							String event_type = EIEventType.OP_FILE_ERROR.getCode();
							EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
							if(ei == null) {
								infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST："+event_type);
							}else {
								if(StartConfig.isSendEi() && Integer.parseInt(ei.getEVENT_LEVEL()) > 0 ) {
									ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
									ei.setKObject("cma.cimiss2.dpc.indb.agme.disa.FileLoopThread");
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
