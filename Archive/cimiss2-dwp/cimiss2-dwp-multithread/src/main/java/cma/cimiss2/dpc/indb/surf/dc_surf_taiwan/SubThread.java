package cma.cimiss2.dpc.indb.surf.dc_surf_taiwan;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.surf.TwSiteData;
import cma.cimiss2.dpc.decoder.surf.DecodeTwSiteDataXml;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.surf.dc_surf_taiwan.service.DbService;
import cma.cimiss2.dpc.indb.surf.dc_surf_taiwan.service.OTSService;
import com.rabbitmq.client.*;
import org.apache.commons.collections.CollectionUtils;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.RabbitMQConfig;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * ************************************
 * @ClassName: SubThread
 * @Auther: dangjinhu
 * @Date: 2019/8/8 09:52
 * @Description:
 * @Copyright: All rights reserver.
 * ************************************
 */

public class SubThread implements Runnable {

    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");

    public SubThread(BlockingQueue<StatDi> diQueues) {
        DbService.setDiQueues(diQueues);
        OTSService.setDiQueues(diQueues);
    }

    @Override
    public void run() {
        RabbitMQConfig rabbitMQConfig = new RabbitMQConfig();
        try {
            ConnectionFactory factory = new ConnectionFactory();
            // 获取rabbitMQ连接信息
            factory.setHost(rabbitMQConfig.getHost());
            factory.setUsername(rabbitMQConfig.getUser());
            factory.setPassword(rabbitMQConfig.getPassword());
            factory.setPort(rabbitMQConfig.getPort());
            // 创建RabbitMQ连接
            Connection connection = factory.newConnection();
            // 创建通道
            final Channel channel = connection.createChannel();
            // 声明消费对列，如果队列不存在，则自动创建
            channel.queueDeclare(rabbitMQConfig.getQueueName(), true, false, false, null);
            // 定义消费者
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleCancelOk(String consumerTag) {
                    super.handleCancelOk(consumerTag);
                }

                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body, "UTF-8");
                 // 获取消息体
            		messageLogger.info("\n PROCESS MESSAGE BODY: " + msg );
                    int length = msg.indexOf(":");
                    String fileName = msg.substring(length + 1);
                    File file = new File(fileName);
                    Date rece_time = new Date(file.lastModified());
                    Action action = processMsg(file.getName(), fileName, rece_time);
                    if (action == Action.ACCEPT) {
                        // 入库成功确认消息
                        infoLogger.info("ack message : " + fileName);
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                }
            };
            // 绑定消费者
            channel.basicConsume(rabbitMQConfig.getQueueName(), false, consumer);
        } catch (Exception e) {
            // ei
            String event = EIEventType.RABBIT_CONNECT_ERROR.getCode();
            EI ei = EIConfig.getEiConfig().getEiMaps().get(event);
            if (ei == null) {
                infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: " + event);
            } else {
                ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                ei.setKObject("cma.cimiss2.dpc.indb.surf.taiwan.SubThread");
                ei.setKEvent("RabbitMQ连接异常");
                ei.setKIndex("连接信息：[主机：" + rabbitMQConfig.getHost() + ",用户名：" + rabbitMQConfig.getUser() + ",密码："
                        + rabbitMQConfig.getPassword() + ",端口号：" + rabbitMQConfig.getPort() + ",对列："
                        + rabbitMQConfig.getQueueName() + "]");
                RestfulInfo restfulInfo = new RestfulInfo();
                restfulInfo.setType("SYSTEM.ALARM.EI ");
                restfulInfo.setName("数据解码入库EI告警信息");
                restfulInfo.setMessage("数据解码入库EI告警信息");
                restfulInfo.setFields(ei);
                List<RestfulInfo> restfulInfos = new ArrayList<>();
                restfulInfos.add(restfulInfo);
                if (StartConfig.isSendEi())
                    RestfulSendData.SendData(restfulInfos, SendType.EI);
            }
            infoLogger.error("\n rabbitMQ create connection failed: " + e.getMessage());
            System.exit(0);
        }

    }

    /**
     * 处理消息
     * @param fileN
     * @param fileName
     * @param rece_time
     * @return
     */
    private Action processMsg(String fileN, String fileName, Date rece_time) {
        DecodeTwSiteDataXml decodeTwSiteDataXml = new DecodeTwSiteDataXml();
        ParseResult<TwSiteData> pr = decodeTwSiteDataXml.decodeFile(fileName);
        DataBaseAction action = null;
        StringBuffer log = new StringBuffer(); // 日志记录
        if (pr.isSuccess()) { // 读取文件成功
            // 先获取错误报文集合
            List<ReportError> error = pr.getError();
            if (!CollectionUtils.isEmpty(error)) {
                for (ReportError anError : error) {
                    log.append("error report :").append(anError.getSegment()).append("\n").append("\t").append(anError.getMessage());
                }
            }
            if (StartConfig.getDatabaseType() == 0 || StartConfig.getDatabaseType() == 1) { // xg,rdb
                action = DbService.processSuccessReport(log, pr, rece_time, fileName);
            } else if (StartConfig.getDatabaseType() == 2) { // ots
                action =  OTSService.processSuccessReport(pr,rece_time,fileName, log);
            }
            infoLogger.info(log.toString()); // 日志写入infoLogger

            if (DataBaseAction.CONNECTION_ERROR == action) {
                return Action.RETRY; // 数据库连接错误,可重试,其它返回accept,mq确认消息后清除此消息
            } else {
                return Action.ACCEPT;
            }
        } else { // 失败，文件未找到||文件为空
            ParseResult.ParseInfo parseInfo = pr.getParseInfo();
            if (parseInfo != null) {
                infoLogger.error("\n Read file error: " + fileName + "\n error description: "
                        + parseInfo.getDescription());
                String event = EIEventType.RABBIT_CONNECT_ERROR.getCode();
                EI ei = EIConfig.getEiConfig().getEiMaps().get(event);
                if (ei == null) {
                    infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: " + event);
                } else {
                    ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    ei.setKObject("cma.cimiss2.dpc.indb.upar.hklight.SubThread");
                    ei.setKEvent("解码入库源文件异常");
                    ei.setKIndex("详细信息：[" + parseInfo.getDescription() + "]" + fileName);
                    ei.setEVENT_EXT1(fileN);
                    RestfulInfo restfulInfo = new RestfulInfo();
                    restfulInfo.setType("SYSTEM.ALARM.EI ");
                    restfulInfo.setName("数据解码入库EI告警信息");
                    restfulInfo.setMessage("数据解码入库EI告警信息");
                    restfulInfo.setFields(ei);
                    List<RestfulInfo> restfulInfos = new ArrayList<>();
                    restfulInfos.add(restfulInfo);
                    if (StartConfig.isSendEi())
                        RestfulSendData.SendData(restfulInfos, SendType.EI);
                }
            }
            return Action.ACCEPT;
        }
    }
}
