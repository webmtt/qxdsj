package cma.cimiss2.dpc.indb.surf.dc_surf_africa;

import java.io.File;
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
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.surf.AfricaAWS;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.surf.DecodeAfrica;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.surf.dc_surf_africa.service.DbService;
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>

 *
 * <p>
 * notes: 非洲援建站资料解码入库（目录轮询方式）
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《》 </a>
 *      <li> <a href=" "> 《》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2019年9月17日 上午9:28:20   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class FileLoopThread implements Runnable{
	  private BlockingQueue<String> files;
	    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");

	    public FileLoopThread(BlockingQueue<String> files, BlockingQueue<StatDi> diQueues) {
	        this.files = files;
	        DbService.setDiQueues(diQueues);
//	        OTSService.setDiQueues(diQueues);
	    }
	    
	    @Override
	    public void run() {
	        do {
	            if (files.size() > 0) {
	                String filePath = files.poll();
	                messageLogger.info(filePath);
	                DecodeAfrica decodeAfrica  = new DecodeAfrica();
	                ParseResult<AfricaAWS> parseResult = decodeAfrica.DecodeFile(new File(filePath));
	                System.out.println(parseResult);
	                if (parseResult.isSuccess()) {
	                    //获取错误报文集合
	                    List<ReportError> errors = parseResult.getError();
	                    if (errors != null) {
	                        for (ReportError error : errors) {
	                            infoLogger.error("Error reports:" + filePath + "->\n" + error.getSegment() + "\n\t" + error.getMessage());
	                        }
	                    }
	                    DataBaseAction action = null;
	                    Date recv = new Date(new File(filePath).lastModified());
	                    if (StartConfig.getDatabaseType() == 0 || StartConfig.getDatabaseType() == 1) { // xg,rdb
	                        action = DbService.processSuccessReport(parseResult, filePath, recv);
	                    } else if (StartConfig.getDatabaseType() == 2) { // OTS
//	                        action = OTSService.processSuccessReport(parseResult,filePath);
	                    }
	                    // 数据库连接错误
	                    if (action == DataBaseAction.CONNECTION_ERROR) {
	                        // 文件不移走
	                    } else {
	                        boolean isRemoved = FileUtil.moveFile(filePath);
	                        //可能移除到的地方已经存在该文件，则直接删除
	                        if(!isRemoved) {
	                            new File(filePath).delete();
	                        }
	                    }
	                } else { 
	                    //读文件异常
	                    boolean isRemoved = FileUtil.moveFile(filePath);
	                    //可能移除到的地方已经存在该文件，则直接删除
	                    if(!isRemoved) {
	                        new File(filePath).delete();
	                    }
	                    // 获取错误报文集合
	                    List<ReportError> errors = parseResult.getError();
	                    if (errors != null) {
	                        for (ReportError error : errors) {
	                            infoLogger.error("Error reports:" + filePath + "->\n" + error.getSegment() + "\n\t" + error.getMessage());
	                        }
	                    }
	                    ParseInfo parseInfo = parseResult.getParseInfo();
	                    if (parseInfo != null) {
	                        File file = new File(filePath);
	                        infoLogger.error("\n read file error ：" + file.getPath() + "\n Error Description:" + parseInfo.getDescription());
	                        // di信息
	                        String event_type = EIEventType.OP_FILE_ERROR.getCode();
	                        EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
	                        if (ei == null) {
	                            infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: " + event_type);
	                        } else {
	                            ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
	                            ei.setKObject("cma.cimiss2.dpc.indb.surf.dp_surf_africa.FileLoopThread");
	                            ei.setKEvent("解码入库源文件异常");
	                            ei.setKIndex("详细信息：[" + filePath + "->" + parseInfo.getDescription() + "]" );
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
	            } else { // 目录下文件不存在,文件内容为空
	                try {
	                    Thread.sleep(5000);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            }
	        } while (true);
	    }
}
