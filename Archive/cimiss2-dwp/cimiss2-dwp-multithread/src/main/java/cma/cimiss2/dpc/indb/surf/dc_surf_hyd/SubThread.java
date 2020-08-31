package cma.cimiss2.dpc.indb.surf.dc_surf_hyd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.apache.tools.ant.types.resources.Last;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.RabbitMQConfig;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.surf.HdsqBean;
import cma.cimiss2.dpc.decoder.surf.DecodeHdsq;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.surf.dc_surf_hyd.service.*;
/**
 * ************************************
 * @ClassName: SubThread
 * @Auther: fengmingyang
 * @Date: 2019/4/1 10:57
 * @Description: 水利部河道水情资料解析--ascii--线程--接受消息
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
                    messageLogger.info("\n PROCESS MESSAGE BODY : " + msg );
                    int length = msg.indexOf(":");
                    String filePath = msg.substring(length + 1);
                    Action action = processMsg(filePath);
                    if (action == Action.ACCEPT) {
                        // 入库成功确认消息
                        infoLogger.info("ack message : " + filePath);
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                }

            };
            // 绑定消费者
            channel.basicConsume(rabbitMQConfig.getQueueName(), false, consumer);
        } catch (Exception e) {
            String event = EIEventType.RABBIT_CONNECT_ERROR.getCode();
            EI ei = EIConfig.getEiConfig().getEiMaps().get(event);
            if (ei == null) {
                infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: " + event);
            } else {
                ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                ei.setKObject("cma.cimiss2.dpc.indb.ocen.hdsq.SubThread");
                ei.setKEvent("RabbitMQ连接异常");
                ei.setKIndex("连接信息：[主机：" + rabbitMQConfig.getHost() + ",用户名：" + rabbitMQConfig.getUser() + ",密码："
                        + rabbitMQConfig.getPassword() + ",端口号：" + rabbitMQConfig.getPort() + "，对列："
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
     * 处理接受的消息
     * @param fileName
     * @return Action
     */
    private Action processMsg(String filePath) {
        DecodeHdsq decodeHdsq  = new DecodeHdsq();
        ParseResult<HdsqBean> parseResult = decodeHdsq.decodeFile(filePath);
        DataBaseAction action = null;
        infoLogger.info(filePath+"\n");
        if (parseResult.isSuccess()) { // 读取文件成功
            // 获取错误报文集合
            List<ReportError> errors = parseResult.getError();
            if (errors != null) {
                for (ReportError error : errors) {
                    infoLogger.error("Error reports:" + filePath + "->\n" + error.getSegment() + "\n\t" + error.getMessage());
                }
            }
            Date recv = new Date(new File(filePath).lastModified());
            if (StartConfig.getDatabaseType() == 0 || StartConfig.getDatabaseType() == 1) { // xg,rdb
                action = DbService.processSuccessReport(parseResult, filePath, recv);
            } else if (StartConfig.getDatabaseType() == 2) { // ots
                action = OTSService.processSuccessReport(parseResult,filePath);
            }
            if (DataBaseAction.CONNECTION_ERROR == action) {
                return Action.RETRY; // 数据库连接错误,批量提交失败，单条sql入库失败
            } else {
                return Action.ACCEPT;
            }
        } else { // 失败 文件不存在,文件内容为空
        	 List<ReportError> errors = parseResult.getError();
             if (errors != null) {
                 for (ReportError error : errors) {
                     infoLogger.error("Error reports:" + filePath + "->\n" + error.getSegment() + "\n\t" + error.getMessage());
                 }
             }
            ParseResult.ParseInfo parseInfo = parseResult.getParseInfo();
            if (parseInfo != null) {
                infoLogger.error("\n Read file error: " + filePath + "\n error description: "
                        + parseInfo.getDescription());
                String event = EIEventType.RABBIT_CONNECT_ERROR.getCode();
                EI ei = EIConfig.getEiConfig().getEiMaps().get(event);
                if (ei == null) {
                    infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: " + event);
                } else {
                    ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    ei.setKObject("cma.cimiss2.dpc.indb.ocen.hdsq.SubThread");
                    ei.setKEvent("解码入库源文件异常");
                    ei.setKIndex("详细信息：[" + parseInfo.getDescription() + "]" + filePath);
                    ei.setEVENT_EXT1(new File(filePath).getName());
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
