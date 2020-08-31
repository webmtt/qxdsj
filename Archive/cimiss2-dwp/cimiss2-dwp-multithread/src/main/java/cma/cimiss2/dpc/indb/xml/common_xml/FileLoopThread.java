package cma.cimiss2.dpc.indb.xml.common_xml;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.decoder.xml.DecodeXml;
import cma.cimiss2.dpc.indb.xml.common_xml.service.DbService;
import cma.cimiss2.dpc.indb.xml.common_xml.service.OTSService;

/**
 * ************************************
 * @ClassName: FileLoopThread
 * @Auther: liuwenxia
 * @Date: 2019/3/30 10:38
 * @Description: 模板配置目录轮询
 * @Copyright: All rights reserver.
 * ************************************
 */

public class FileLoopThread implements Runnable {

    private BlockingQueue<String> files;
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");

    public FileLoopThread(BlockingQueue<String> files, BlockingQueue<StatDi> diQueues) {
        this.files = files;
        DbService.diQueues = diQueues;
        OTSService.diQueues = diQueues;
    }

    @Override
    public void run() {
        do {
            if (files.size() > 0) {
                String filePath = files.poll();
                messageLogger.info(filePath);
                DecodeXml decodeXml = new DecodeXml();
                File filepath = new File(filePath);
                Date rece_time = new Date(filepath.lastModified());
                boolean readOver = true;
                int startReadLine = 0;
                int linesCount = 10000;
                do {
	                ParseResult<HashMap<String,List<Map<String,Object>>>> pr = new ParseResult<HashMap<String,List<Map<String,Object>>>>(false);
	                readOver = decodeXml.decode(filePath,rece_time,pr,startReadLine,linesCount);
	                startReadLine = startReadLine + linesCount;
	                if (pr.isSuccess()) {
	                    List<ReportError> errors = pr.getError();
	                    if (errors != null && errors.size() > 0) {
	                        for (ReportError error : errors) {
	                            infoLogger.error("Error reports:" + filePath + "->\n" + error.getSegment() + "\n\t" + error.getMessage());
	                        }
	                    }
	                    DataBaseAction action = null;
	                    if (StartConfig.getDatabaseType() == 0 || StartConfig.getDatabaseType() == 1) { // xg,rdb
	                        action = DbService.processSuccessReport(pr,filePath);
	                    } else if (StartConfig.getDatabaseType() == 2) { // OTS
	                        action = OTSService.processSuccessReport(pr, filePath);
	                    }
	                    if (action == DataBaseAction.CONNECTION_ERROR) {
	                    } else if (readOver) {
	                        boolean isRemoved = FileUtil.moveFile(filePath);
	                        //可能移除到的地方已经存在该文件，则直接删除
	                        if(!isRemoved) {
	                            new File(filePath).delete();
	                        }
	                    }
	                } else{ 
	                    boolean isRemoved = FileUtil.moveFile(filePath);
	                    //可能移除到的地方已经存在该文件，则直接删除
	                    if(!isRemoved) {
	                        new File(filePath).delete();
	                    }
	                    List<ReportError> errors = pr.getError();
	                    if (errors != null && errors.size() >0) {
	                        for (ReportError error : errors) {
	                            infoLogger.error("Error reports:" + filePath + "->\n" + error.getSegment() + "\n\t" + error.getMessage());
	                        }
	                    }
	                    ParseResult.ParseInfo parseInfo = pr.getParseInfo();
	                    if (parseInfo != null) {
	                        File file = new File(filePath);
	                        infoLogger.error("\n read file error ：" + file.getPath() + "\n Error Description:" + parseInfo.getDescription());
	                        String event_type = EIEventType.OP_FILE_ERROR.getCode();
	                        EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
	                        if (ei == null) {
	                            infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: " + event_type);
	                        } else {
	                            ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
	                            ei.setKObject("cma.cimiss2.dpc.indb.xml.common_xml.FileLoopThread");
	                            ei.setKEvent("解码入库异常");
	                            ei.setKIndex("详细信息：" + parseInfo.getDescription() + "]" + file.getPath());
	                            ei.setEVENT_EXT1(file.getName());
	                            RestfulInfo restfulInfo = new RestfulInfo();
	                            restfulInfo.setType("SYSTEM.ALARM.EI ");
	                            restfulInfo.setName("数据解码入库EI告警信息");
	                            restfulInfo.setMessage("数据解码入库EI告警信息伅");
	                            restfulInfo.setFields(ei);
	                            List<RestfulInfo> restfulInfos = new ArrayList<>();
	                            restfulInfos.add(restfulInfo);
	                            
	                            if (StartConfig.isSendEi())
	                                RestfulSendData.SendData(restfulInfos, SendType.EI);
	                        }
	                    }
	                }
                }while(!readOver);
            } else { 
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } while (true);
    }
}
