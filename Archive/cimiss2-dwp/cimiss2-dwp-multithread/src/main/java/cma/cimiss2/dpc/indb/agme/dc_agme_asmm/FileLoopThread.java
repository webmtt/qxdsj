package cma.cimiss2.dpc.indb.agme.dc_agme_asmm;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.CTSCodeMap;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.FileUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cma.cimiss2.dpc.decoder.agme.DecodeASM;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.cawn.ZAgmeASM;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.agme.dc_agme_asmm.service.DbService;
import cma.cimiss2.dpc.indb.agme.dc_agme_asmm.service.OTSService;


/**
 * -------------------------------------------------------------------------------
 * <br>
 * @Title:  FileLoopThread.java   
 * @Package cma.cimiss2.dpc.indb.agme.cli   
 * @Description:    TODO(文件目录轮询处理线程)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年7月9日 下午11:01:34   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *---------------------------------------------------------------------------------
 */
public class FileLoopThread implements Runnable{
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static String fileN = null;
	public static boolean isRevised = false;
	public static String v_bbb = "000";
	Map<String, Object> proMap = StationInfo.getProMap();	
	BlockingQueue<String> files;
	ArrayList<CTSCodeMap> ctsCodeMaps;
	
	public FileLoopThread(BlockingQueue<String> files, BlockingQueue<StatDi> diQueues, ArrayList<CTSCodeMap> ctsCodeMaps) {
		this.files = files;
		this.ctsCodeMaps = ctsCodeMaps;
		DbService.setDiQueues(diQueues);
		OTSService.setDiQueues(diQueues);
	}

	@Override
	public void run() {
		while(true) {
			if(files.size() > 0) {
				String filepath = files.poll();
				messageLogger.info(filepath);
				File file = new File(filepath);
				DecodeASM decodeAsm = new DecodeASM();
				if(file.exists() && file.isFile()) {
					
					if(file.length()  > StartConfig.maxFileSize()){
						infoLogger.info("\n File is too large(" + file.length() + "byte), ignore it! Filepath is: " + filepath);
						continue;
					}
					
					fileN = file.getName();
					// 判断文件是否为  更正报文
					if(fileN.contains("-CC")){
						isRevised = true;
						v_bbb = fileN.substring(fileN.indexOf("-CC") + 1, fileN.indexOf("-CC") + 4);
					}
					else{
						isRevised = false;
						v_bbb = "000";
					}
					
					
					Date recv_time = new Date(file.lastModified());
					StringBuffer loggerBuffer = new StringBuffer();
					loggerBuffer.append(" : " + simpleDateFormat.format(new Date(file.lastModified())) +" "+ file.getPath() + "\n");
					// 解析文件  返回结果集
					ParseResult<ZAgmeASM> parseResult = decodeAsm.decodeFile(file);
					
					
					if(parseResult.isSuccess()){
						// 获取错误的报文结果集
						List<ReportError> reportErrors = parseResult.getError();
						if(reportErrors != null && reportErrors.size() > 0) {
							for (int i = 0; i < reportErrors.size(); i++) {
								loggerBuffer.append(" ERROR REPORT : "+reportErrors.get(i).getSegment()+"\n"+"\t " + reportErrors.get(i).getMessage() + "\n");
							}
						}
						
						DataBaseAction dataBaseAction;
						
						if(StartConfig.getDatabaseType() == 2) {
							dataBaseAction = OTSService.processSuccessReport(parseResult.getData(), recv_time,StartConfig.ctsCode(), fileN, isRevised,v_bbb, loggerBuffer,ctsCodeMaps
									);
							String[] fnames = fileN.split("_");
							List<ReportInfo> reportInfos = parseResult.getReports();
							OTSService.reportInfoToDb(reportInfos, v_bbb, recv_time, fnames[3], fnames[1],loggerBuffer,ctsCodeMaps);
							//OTSReportInfoService.reportInfoToDb(reportInfos, v_bbb, recv_time, fnames[3], fnames[1],loggerBuffer,ctsCodeMaps);
						}else {
							dataBaseAction = DbService.processSuccessReport(parseResult, recv_time,StartConfig.ctsCode(), fileN, isRevised,v_bbb, loggerBuffer,ctsCodeMaps );
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
								infoLogger.error("\n EI配置文件中没有事件类型："+event_type);
							}else {
								if(StartConfig.isSendEi() && Integer.parseInt(ei.getEVENT_LEVEL()) > 0 ) {
									ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
									ei.setKObject("org.cimiss2.dwp.z_agme.cli.MultSubThread");
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
