package cma.cimiss2.dpc.indb.upar.dc_upar_wind_rada;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.upar.WindRada;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.decoder.upar.DecodeWindRada;
import cma.cimiss2.dpc.indb.upar.dc_upar_wind_rada.service.DbService;
import cma.cimiss2.dpc.indb.upar.dc_upar_wind_rada.service.OTSService;

import org.apache.commons.collections.CollectionUtils;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.FileUtil;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * ************************************
 * @ClassName: FileLoopThread
 * @Auther: dangjinhu
 * @Date: 2019/3/27 09:45
 * @Description: 风廓线雷达数据解析--ascii--线程--目录轮询
 * @Copyright: All rights reserver.
 * ************************************
 */

public class FileLoopThread implements Runnable {

    private BlockingQueue<String> files;
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");

    public FileLoopThread(BlockingQueue<String> files, BlockingQueue<StatDi> diQueues) {
        this.files = files;
        DbService.setDiQueues(diQueues);
        OTSService.setDiQueues(diQueues);
    }

    @Override
    public void run() {
        // while
        do {
            if (files.size() > 0) {
                String fileName = files.poll();
                File file = new File(fileName);
                Date rece_time = new Date(file.lastModified());
                DecodeWindRada windRada = new DecodeWindRada();
                ParseResult<WindRada> pr = windRada.decodeFile(fileName);
                StringBuffer log = new StringBuffer();
                if (pr.isSuccess()) {
                    // 获取错误报文集合
                    List<ReportError> error = pr.getError();
                    if (!CollectionUtils.isEmpty(error)) {
                        for (ReportError anError : error) {
                            log.append("error report :").append(anError.getSegment()).append("\n").append("\t").append(anError.getMessage());
                        }
                    }
                    DataBaseAction action = null;
                    if (StartConfig.getDatabaseType() == 0 || StartConfig.getDatabaseType() == 1) { // xg,rdb
                        action = DbService.processSuccessReport(pr, rece_time, fileName, log);
                    } else if (StartConfig.getDatabaseType() == 2) { // ots
                        action = OTSService.processSuccessReport(pr, rece_time, fileName, log);
                    }
                    infoLogger.info(log.toString()); // 日志记录
                    // 数据库连接错误
                    if (action == DataBaseAction.CONNECTION_ERROR) {
                        // 文件不移走
                        // 批量提交错误
                    } else {
                        messageLogger.info(fileName);
                        FileUtil.moveFile(fileName);
                    }
                } else { // 读文件异常
                    FileUtil.moveFile(fileName);
                    // 获取错误报文集合
                    List<ReportError> error = pr.getError();
                    if (!CollectionUtils.isEmpty(error)) {
                        for (ReportError anError : error) {
                            log.append("error report :").append(anError.getSegment()).append("\n").append("\t").append(anError.getMessage());
                        }
                    }
                    ParseResult.ParseInfo parseInfo = pr.getParseInfo();
                    if (parseInfo != null) {
                        infoLogger.error("\n read file error ：" + file.getPath() + "\n Error Description:" + parseInfo.getDescription());
                        // di信息
                        String event_type = EIEventType.OP_FILE_ERROR.getCode();
                        EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
                        if (ei == null) {
                            infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: " + event_type);
                        } else {
                            ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                            ei.setKObject("cma.cimiss2.dpc.indb.upar.wind_rada.FileLoopThread");
                            ei.setKEvent("解码入库源文件异常：");
                            ei.setKIndex("详细信息：[" + parseInfo.getDescription() + "]" + file.getPath());
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
