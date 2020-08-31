package org.cimiss2.dwp;

import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.core.tools.EIConfig;
import cma.cimiss2.dpc.indb.core.tools.RestfulSendData;
import cma.cimiss2.dpc.indb.core.tools.TimeUtil;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: 多线程基类
 * @Aouthor: xzh
 * @create: 2018-01-25 12:04
 */
public abstract class BaseThread implements Runnable {
    public Action action = Action.ACCEPT;
    ExecutorService EIDIexecutor = Executors.newFixedThreadPool(2);


    protected int repCorrectNum;
    protected int repErrorNum;
    protected String host;
    protected String name;
    protected String password;
    protected int port;
    protected String queue;
    protected List<Map<String, Object>> reports;

    public BaseThread() {
    }


    @Override
    public void run() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            factory.setUsername(name);
            factory.setPassword(password);
            factory.setPort(port);
            factory.setAutomaticRecoveryEnabled(true);
            Connection connection = factory.newConnection();
            final Channel channel = connection.createChannel();
//            Ioc ioc = IocBuilder.ioc();
//            final DayLightEtl dayLightEtl = ioc.get(DayLightEtl.class);
            channel.queueDeclare(queue, true, false, false, null);
            Consumer consumer = new DefaultConsumer(channel) {
                long tag;

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    tag = envelope.getDeliveryTag();
                    StringBuffer buffer = new StringBuffer();
                    List<RestfulInfo> diInfo = new ArrayList<>();
                    List<RestfulInfo> eiInfo = new ArrayList<>();
                    String message = new String(body, "UTF-8");
//                    File file = new File(message.split(":")[1]);
//                    MessageLOGGER.info(message);///
                    buffer.append(message.split(":")[1] + "\n");

                    try {
                        etl(message, buffer, diInfo, eiInfo);
                    } catch (Exception e) {
                        action = Action.REJECT;
                        buffer.append("解码入库失败:" + message + "\n" +
                                "错误原因:" + e.getMessage() + "\n");
                    } finally {
                        if (action == Action.ACCEPT) {
                            channel.basicAck(tag, false);
                            finallyAccept(buffer);///
                            EIDIexecutor.execute(new DIEISenderThread(diInfo, 0));
                            EIDIexecutor.execute(new DIEISenderThread(eiInfo, 1));
                        } else if (action == Action.RETRY) {
                            try {
                                System.out.println("进入final====RETRY......");//；
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                channel.basicNack(tag, false, false);
                                System.out.println("进入final====REJECT......");
                                buffer.append("消息错误 REJECT" + "\n");
                                System.out.println(buffer.toString());
                                finallyReject(buffer);
                                EIDIexecutor.execute(new DIEISenderThread(diInfo, 0));
                                EIDIexecutor.execute(new DIEISenderThread(eiInfo, 1));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }

            };
            channel.basicConsume(queue, false, consumer);
        } catch (Exception e) {
            rabbitConnErrot(e);
        }
    }

    private void rabbitConnErrot(Exception e) {
        String event_type = EIEventType.RABBIT_CONNECT_ERROR.getCode();
        EI ei = EIConfig.getEiConfig("").getEiMaps().get(event_type);
        if (ei == null) {
//            LOGGER.error("EI配置文件中没有事件类型：" + event_type);
        } else {
            ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
            ei.setKObject("org.cimiss2.dwp.z_surf_dayLight.DayLightSubThread");
            ei.setKEvent("RabbitMQ连接异常");
            ei.setKIndex("连接信息：[主机：" + host + ",用户名：" + name + ",密码：" + password +
                    ",端口号：" + port + "，对列：" + queue + "]");
            RestfulInfo restfulInfo = new RestfulInfo();
            restfulInfo.setType("SYSTEM.ALARM.EI ");
            restfulInfo.setName("数据解码入库EI告警信息");
            restfulInfo.setMessage("数据解码入库EI告警信息");
            restfulInfo.setFields(ei);
            List<RestfulInfo> restfulInfos = new ArrayList<>();
            restfulInfos.add(restfulInfo);
            RestfulSendData.getInstance("config/RestfulInfo.js").SendData(restfulInfos, 1);
        }
//        LOGGER.error("rabbit MQ创建连接失败： " + e.getMessage());
        System.exit(0);
    }

    public abstract void etl(String message, StringBuffer buffer, List<RestfulInfo> diInfo, List<RestfulInfo> eiInfo);

    public abstract void finallyAccept(StringBuffer buffer);

    public abstract void finallyReject(StringBuffer buffer);


}
