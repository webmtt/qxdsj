package cma.cimiss2.dpc.indb.radi.dc_bufr_radi_minute;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.RabbitMQConfig;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hitec.bufr.util.FileUtil;
import com.hitec.bufr.util.StringUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.radi.dc_bufr_radi_minute.service.OTSRadiServiceImpl;
import cma.cimiss2.dpc.indb.radi.dc_bufr_radi_minute.service.RadiServiceImpl2;

import com.rabbitmq.client.AMQP.BasicProperties;

public class SubThread implements Runnable {
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private String tableSection;
	public SubThread(String tableSection, BlockingQueue<StatDi> diQueues) {
		this.tableSection = tableSection;
		RadiServiceImpl2.setDiQueues(diQueues);
		OTSRadiServiceImpl.setDiQueues(diQueues);
	}
	@Override
	public void run() {
		RabbitMQConfig  rabbitMQConfig = new RabbitMQConfig();
		try {
			ConnectionFactory factory = new ConnectionFactory();
			// 获取rabbitMQ连接信息
            factory.setHost(rabbitMQConfig.getHost());
            factory.setUsername(rabbitMQConfig.getUser());
            factory.setPassword(rabbitMQConfig.getPassword());
            factory.setPort(rabbitMQConfig.getPort());
            // 网络异常自动连接恢复
            factory.setAutomaticRecoveryEnabled(true);
            // 每10秒尝试重试连接一次
            factory.setNetworkRecoveryInterval(10000);
            // 创建RabbitMQ连接
            Connection connection = factory.newConnection();
            // 创建通道
            final Channel channel = connection.createChannel();
            // 声明消费对列，如果队列不存在，则自动创建
            channel.queueDeclare(rabbitMQConfig.getQueueName(), true, false, false, null);
            channel.basicQos(5);
            // 定义消费者
            Consumer consumer = new DefaultConsumer(channel) {
				public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
					SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					int fileOrBytes=StartConfig.getDataPattern();
					if (fileOrBytes==1) {//文件格式
						
						String message = new String(body, "UTF-8");
						int index = message.indexOf(":");
						String filepath = message.substring(index+1);
						File file = new File(filepath);
						int length=(int) file.length();
						Date recv= new Date(file.lastModified());
						try{
							if(StartConfig.getDatabaseType() == 0 || StartConfig.getDatabaseType() == 1){
								RadiServiceImpl2 Impl = new RadiServiceImpl2();
								Impl.decodeFile(filepath, tableSection, length, recv);
							}else {
								OTSRadiServiceImpl otsServiceImpl = new OTSRadiServiceImpl();
								otsServiceImpl.decodeFile(filepath, tableSection);
							}
						}catch (Exception e) {
							e.printStackTrace();
							channel.abort();
						}
						finally {
							infoLogger.info("ack message : " + filepath);
								channel.basicAck(envelope.getDeliveryTag(), false);
						}
					}else if(fileOrBytes==0){//二进制流格式
						Map<String, Object> queueHeaders = properties.getHeaders();
						String msg = new String(body, "ISO-8859-1");
						int length = msg.indexOf(";");
						String msgHead = msg.substring(0, length);
						String[] hs = msgHead.split(",");
						String fileName = "";
						String CCCC = "";
						Date recv = new Date();
						for(String ev : hs) {
							String[] evs = ev.split("=");
							if("FileName".equalsIgnoreCase(evs[0]) && evs.length == 2) {
								fileName = evs[1];
							}
							else if("CCCC".equalsIgnoreCase(evs[0]) && evs.length == 2){
								CCCC = evs[1];
							}
							else if("SendTime".equalsIgnoreCase(evs[0]) && evs.length == 2){
								int idx = evs[1].lastIndexOf(".");
								if(idx > -1){ // 精确到秒
									String sendtime = evs[1].substring(0, idx);  
									try{
										if(sendtime.length() == 14)
											recv = dateFormat.parse(sendtime);
										else if(sendtime.length() == 19)
											recv = dateFormat2.parse(sendtime);
									}catch (Exception e) {
										try {
											recv = dateFormat2.parse(sendtime);
										} catch (ParseException e1) {
											infoLogger.error("文件"+ fileName + "的 SendTime 异常" + sendtime + "使用当前系统时间: " + TimeUtil.date2String(recv, "yyyy-MM-dd HH:mm:ss"));
										}
									}
								}
								
							}
						}
						
						String msgbody = msg.substring(length + 1);
						byte[] messageBytes = msgbody.getBytes("ISO-8859-1");
						if(StartConfig.isWriteFile() && !fileName.isEmpty()) {
							String filePath = StringUtil.replaceFileNameExp(fileName, StartConfig.writeFilePath()) + fileName;
							infoLogger.info("Write File: " + filePath);
							FileUtil.writeBytesToFile(messageBytes, filePath);
							messageLogger.info(filePath + "\n");
						}
						
						try{
							if(StartConfig.getDatabaseType() == 0 || StartConfig.getDatabaseType() == 1){
								RadiServiceImpl2 Impl = new RadiServiceImpl2();
								Impl.decodeStream(fileName,messageBytes, tableSection, recv);
							}
							else {
								OTSRadiServiceImpl otsServiceImpl = new OTSRadiServiceImpl();
								otsServiceImpl.decodeStream(fileName,messageBytes, tableSection);
							}
						}catch (Exception e) {
							e.printStackTrace();
							channel.abort();
						}
						finally{
							// 入库成功确认消息
							infoLogger.info("ack message : " + fileName);
							channel.basicAck(envelope.getDeliveryTag(), false);
						}
					 }
				}
					
				
			};
			// 绑定消费者
			channel.basicConsume(rabbitMQConfig.getQueueName(), false, consumer);
		} catch (Exception e) {
			String event_type = EIEventType.RABBIT_CONNECT_ERROR.getCode();
			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
			if(ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
			}else {
				if(StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("cma.cimiss2.dpc.indb.radi.bufr.minute.SubThread");
					ei.setKEvent("RabbitMQ连接异常");
					ei.setKIndex("连接信息：[主机："+rabbitMQConfig.getHost()+",用户名："+rabbitMQConfig.getUser()+",密码："+rabbitMQConfig.getPassword()+
							",端口号："+rabbitMQConfig.getPort()+"，对列："+rabbitMQConfig.getQueueName()+"]");
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
			infoLogger.error("\n rabbitMQ create connection failed: " +e.getMessage());
			System.exit(0);
		}
	}
}
