package cma.cimiss2.dpc.indb.cawn.dc_cawn_bufr_ranion;

import java.io.File;
import java.io.IOException;
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
import cma.cimiss2.dpc.indb.cawn.dc_cawn_bufr_ranion.service.CawnServiceImpl;
import cma.cimiss2.dpc.indb.cawn.dc_cawn_bufr_ranion.service.OTSService;
import cma.cimiss2.dpc.indb.common.BufrConfig;

import com.rabbitmq.client.AMQP.BasicProperties;

public class SubThread implements Runnable {
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private String tableSection;
	public SubThread(String tableSection, BlockingQueue<StatDi> diQueues) {
		this.tableSection = tableSection;
		CawnServiceImpl.setDiQueues(diQueues);
		OTSService.setDiQueues(diQueues);
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
            factory.setAutomaticRecoveryEnabled(true);
            // 每十秒尝试重新连接一次
            factory.setNetworkRecoveryInterval(10000);
            infoLogger.info("ceshi");
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
					Map<String, Object> queueHeaders = properties.getHeaders();
					// modified by chy 2018-09-25 消息是文件路径还是数据流的形式 的区别
//					String msg = new String(body, "ISO-8859-1");
					//文件 
					/*String msg = new String(body, "UTF-8");
					int length = msg.indexOf(":");
					msg = msg.substring(length + 1);
					List<String> tableList = new ArrayList<String>();
					tableList = BufrConfig.getTableMap().get(tableSection);
					// modified by chy 2018-09-25 消息是文件路径还是数据流的形式 的区别
					String fileName = msg;
					if(queueHeaders != null)
						fileName = queueHeaders.get("FileName").toString();
					boolean decode = false;
					if(StartConfig.getDatabaseType() == 0 || StartConfig.getDatabaseType() == 1){
						 CawnServiceImpl oceanServiceImpl = new  CawnServiceImpl();
						decode = oceanServiceImpl.decode(fileName, tableSection, tableList);
						infoLogger.info(tableSection + " ack message : " + fileName);
					}
					else {
						OTSOceanServiceImpl otsServiceImpl = new OTSOceanServiceImpl();
						decode = otsServiceImpl.decode(fileName, tableSection, tableList);
						infoLogger.info(tableSection + " ack message : " + fileName);
					}*/
					//消息  msg=Ver=3,Sender=RabbitMQ57359,TypeTag=1,TYPE=G.0003.0004.R001,IIIII=57359,CCCC=BCWH,OTime=20181129001800
					int fileOrBytes=StartConfig.getDataPattern();
					if (fileOrBytes==1) {//文件格式
						List<String> tableList = new ArrayList<String>();
						tableList = BufrConfig.getTableMap().get(tableSection);
						String message = new String(body, "UTF-8");
						int index = message.indexOf(":");
						String filepath = message.substring(index+1);
						File file = new File(filepath);
						boolean decode = false;
						try{
							if(StartConfig.getDatabaseType() == 0 || StartConfig.getDatabaseType() == 1){
								 CawnServiceImpl oceanServiceImpl = new  CawnServiceImpl();
								decode = oceanServiceImpl.decode(filepath, tableSection, tableList);
								infoLogger.info(tableSection + " ack message : " + filepath);
							}
							else {
								OTSService otsService = new OTSService();
								decode = otsService.decode(filepath, tableSection, tableList);
								infoLogger.info(tableSection + " ack message : " + filepath);
							}
						}catch(Exception e){
							e.printStackTrace();
							infoLogger.info("出错"+e.getMessage() + filepath);
//							channel.abort();
						}
						finally{
							if (decode) {
								// 入库成功确认消息
								infoLogger.info("ack message : " + filepath);
								channel.basicAck(envelope.getDeliveryTag(), false);
								// channel.basicAck(envelope.getDeliveryTag(), true);
							} else {
								// 入库失败，消息重新排队
								infoLogger.info("入库失败,重新排队 : " + filepath);
								channel.basicAck(envelope.getDeliveryTag(), true);
							}
						}
					}else if(fileOrBytes==0){
						String msg = new String(body, "ISO-8859-1");
						int length = msg.indexOf(";");
						String msgHead = msg.substring(0, length);
						
						String[] hs = msgHead.split(",");
						
						String fileName = "";
						String CCCC = "";
						for(String ev : hs) {
							String[] evs = ev.split("=");
							if("FileName".equalsIgnoreCase(evs[0]) && evs.length == 2) {
								fileName = evs[1];
								//break;
							}
							else if("CCCC".equalsIgnoreCase(evs[0]) && evs.length == 2){
								CCCC = evs[1];
							}
						}
						String msgbody = msg.substring(length + 1);
						byte[] messageBytes = msgbody.getBytes("ISO-8859-1");
						List<String> tableList = new ArrayList<String>();
						tableList = BufrConfig.getTableMap().get(tableSection);
						boolean decode = false;
						String writePath = "D:\\WriteBufrRain\\";
						if(StartConfig.isWriteFile() && !fileName.isEmpty()) {
							String filePath = StringUtil.replaceFileNameExp(fileName, StartConfig.writeFilePath()) + fileName;
							infoLogger.info("Write File: " + filePath);
							FileUtil.writeBytesToFile(messageBytes, filePath);
							messageLogger.info(filePath + "\n");
						}
						try{
							if(StartConfig.getDatabaseType() == 0 || StartConfig.getDatabaseType() == 1){
								 CawnServiceImpl oceanServiceImpl = new  CawnServiceImpl();
								decode = oceanServiceImpl.decode(fileName,messageBytes, tableSection, tableList);
								infoLogger.info(tableSection + " ack message : " + fileName);
							}
							else {
								OTSService otsService = new OTSService();
								decode = otsService.decode(fileName, tableSection, tableList);
								infoLogger.info(tableSection + " ack message : " + fileName);
							}
						}catch(Exception e){
							e.printStackTrace();
							infoLogger.info("出错"+e.getMessage() + fileName);
//							channel.abort();
						}
						finally{
							if (decode) {
								// 入库成功确认消息
								infoLogger.info("ack message : " + fileName);
								channel.basicAck(envelope.getDeliveryTag(), false);
								// channel.basicAck(envelope.getDeliveryTag(), true);
							} else {
								// 入库失败，消息重新排队
								infoLogger.info("入库失败,重新排队 : " + fileName);
								channel.basicAck(envelope.getDeliveryTag(), true);
							}
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
					ei.setKObject("cma.cimiss2.dpc.indb.cawn.bufr.ar.SubThread");
					ei.setKEvent("RabbitMQ连接异常");
					ei.setKIndex("连接信息：[主机："+rabbitMQConfig.getHost()+",用户名："+rabbitMQConfig.getUser()+",密码："+rabbitMQConfig.getPassword()+
							",端口号："+rabbitMQConfig.getPort()+"，对列："+rabbitMQConfig.getQueueName()+"]");
					RestfulInfo restfulInfo = new RestfulInfo();
					restfulInfo.setType("SYSTEM.ALARM.EI");
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
