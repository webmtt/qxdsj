package cma.cimiss2.dpc.indb.surf.dc_surf_kppr;

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

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.surf.DecodeKPPR;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationKppr;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.surf.dc_surf_kppr.service.DbService;
import cma.cimiss2.dpc.indb.surf.dc_surf_kppr.service.OTSService;

// TODO: Auto-generated Javadoc
/**
 * The Class KPPRFileLoopThread.
 */
public class KPPRFileLoopThread implements Runnable {
	
	/** The Constant infoLogger. */
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	/** The Constant messageLogger. */
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	
	/** The simple date format. */
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/** The files. */
	BlockingQueue<String> files;
	
	/**
	 * Instantiates a new KPPR file loop thread.
	 *
	 * @param files the files
	 * @param diQueues the di queues
	 */
	public  KPPRFileLoopThread(BlockingQueue<String> files, BlockingQueue<StatDi> diQueues) {
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
			if(this.files.size() > 0) {
				String filepath = files.poll();
				messageLogger.info(filepath);
				Action action = processMsg(filepath, new Date());
				if(action == Action.ACCEPT) {
					FileUtil.moveFile(filepath);
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
	
	
	/**
	 * Process msg.
	 *
	 * @param filepath the filepath
	 * @param recv_time the recv time
	 * @return the action
	 */
	private Action processMsg(String filepath, Date recv_time) {
	
		File file = new File(filepath);
		String fileN = file.getName();
		StringBuffer stringBuffer = new StringBuffer();
		if(file != null && file.exists() && file.isFile()){
			stringBuffer.append(filepath);
			recv_time = new Date(file.lastModified());
			DecodeKPPR decodeKPPR = new DecodeKPPR();
			ParseResult<SurfaceObservationKppr> parseResult = decodeKPPR.DecodeFile(file);			
			if(parseResult.isSuccess()){
				List<ReportError> reportErrors = parseResult.getError();
				if(reportErrors != null && reportErrors.size() > 0) {
					for (int i = 0; i < reportErrors.size(); i++) {
						stringBuffer.append("\n Decode failed: "+ file.getPath() +"\n because: "+reportErrors.get(i).getMessage()+": "+reportErrors.get(i).getSegment());
					} // end for
				}
				
				DataBaseAction action = null;
				if(StartConfig.getDatabaseType() == 2){
					action = OTSService.insert_ots(parseResult.getData(), "SURF_WEA_DPRK_PRE_HOR_TAB", recv_time, stringBuffer, fileN, filepath);
					OTSService.reportInfoToDb(parseResult.getReports(), "SURF_WEA_CHN_REP_TAB", recv_time, stringBuffer);
				}
				else{
					action = DbService.processSuccessReport(parseResult, recv_time, fileN, filepath);
				}
				infoLogger.info(stringBuffer.toString());
				if(action == DataBaseAction.CONNECTION_ERROR){
					return Action.RETRY;
				}else {
					return Action.ACCEPT;
				}
			}else {
				List<ReportError> reportErrors = parseResult.getError();
				if(reportErrors != null && reportErrors.size() > 0) {
					for (int i = 0; i < reportErrors.size(); i++) {
						infoLogger.error("\n Decode failed: "+ file.getPath() +"\n because: "+reportErrors.get(i).getMessage()+":"+reportErrors.get(i).getSegment());
					} // end for
				}
				ParseInfo parseInfo = parseResult.getParseInfo();
				if(parseInfo != null) {
					infoLogger.error("\n Read file error："+file.getPath()+"\n error description: "+parseInfo.getDescription());
					String event_type = EIEventType.OP_FILE_ERROR.getCode();
					EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
					if(ei == null) {
						infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: " + event_type);
					}else {
						if(StartConfig.isSendEi()) {
							ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
							ei.setKObject("cma.cimiss2.dpc.indb.surf.kppr.KPPRMultSubThread");
							ei.setKEvent("解码入库源文件异常：");
							ei.setKIndex("详细信息：["+parseInfo.getDescription()+"]"+file.getPath());
							ei.setEVENT_EXT1(fileN);
							RestfulInfo restfulInfo = new RestfulInfo();
							restfulInfo.setType("SYSTEM.ALARM.EI ");
							restfulInfo.setName("数据解码入库EI告警信息");
							restfulInfo.setMessage("数据解码入库EI告警信息");
							
							restfulInfo.setFields(ei);
							List<RestfulInfo> restfulInfos = new ArrayList<>();
							restfulInfos.add(restfulInfo);
							if(StartConfig.isSendEi())
								RestfulSendData.SendData(restfulInfos, SendType.EI);
						}
					}
				}
				return Action.ACCEPT;
			}	
			
		}else {
			infoLogger.error("\n File dose not exist: "+file.getPath());
			
			String event_type = EIEventType.OP_FILE_ERROR.getCode();
			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
			if(ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
			}else {
				if(StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("cma.cimiss2.dpc.indb.surf.kppr.KPPRMultSubThread");
					ei.setKEvent("解码入库源文件不存在：");
					ei.setKIndex("详细信息："+filepath);
					ei.setEVENT_EXT1(fileN);
					RestfulInfo restfulInfo = new RestfulInfo();
					restfulInfo.setType("SYSTEM.ALARM.EI ");
					restfulInfo.setName("数据解码入库EI告警信息");
					restfulInfo.setMessage("数据解码入库EI告警信息");
					
					restfulInfo.setFields(ei);
					List<RestfulInfo> restfulInfos = new ArrayList<>();
					restfulInfos.add(restfulInfo);
					if(StartConfig.isSendEi())
						RestfulSendData.SendData(restfulInfos, SendType.EI);
				}
			}
			return Action.ACCEPT;
		}
	}

}
