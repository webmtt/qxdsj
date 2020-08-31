package cma.cimiss2.dpc.indb.upar.dc_upar_flsh;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.upar.LightingData;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.decoder.upar.DecodeLDP;
import cma.cimiss2.dpc.indb.upar.dc_upar_flsh.service.*;

import org.cimiss2.dwp.tools.DataBaseAction;

import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.RabbitMQConfig;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * 
 * <br>
 * @Title:  MultSubThread.java   
 * @Package org.cimiss2.dwp.z_light_locat   
 * @Description:(ADTD闪电定位数据解码入库)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月19日 下午6:28:10   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *
 *
 */
public class MultSubThread implements Runnable {
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static String fileN = null;
	public MultSubThread(BlockingQueue<StatDi> diQueues) {
		DbService.setDiQueues(diQueues);
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
            // 网络异常自动连接恢复
            factory.setAutomaticRecoveryEnabled(true);
            // 每10秒尝试重试连接一次
            factory.setNetworkRecoveryInterval(10000);
            // 创建连接
            Connection connection = factory.newConnection();
            // 创建通道
            final Channel channel = connection.createChannel();
            // 声明对列
			channel.queueDeclare(rabbitMQConfig.getQueueName(), true, false, false, null);
			channel.basicQos(10);
			// 定义消费者
			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
						byte[] body) throws IOException {
					Date recv_time = new Date();
					String message = new String(body, "UTF-8");
					// 获取消息体
            		messageLogger.info(message + "\n");
					StringBuffer loggerBuffer = new StringBuffer();
//					try {
//                     	System.out.println("Worker1  Received '" + message + "'");
//                     	processMsg(message, recv_time,loggerBuffer);
////                         throw  new Exception();
//                         //doWork(message);
//                     }catch (Exception e){
//                    	 e.printStackTrace();
//                         channel.abort();
//                     }finally {
//                         System.out.println("Worker1 Done");
//                         channel.basicAck(envelope.getDeliveryTag(), false);
//                         infoLogger.info(loggerBuffer.toString());
//                     }
            		
					// 消息处理函数
					Action action = processMsg(message, recv_time, loggerBuffer);
					if(action == Action.ACCEPT){
            			// 消息消费确认机制
            			channel.basicAck(envelope.getDeliveryTag(), false);
            		}else if (action == Action.RETRY) {
            			channel.basicReject(envelope.getDeliveryTag(), true);
					}
            		
            		infoLogger.info(loggerBuffer.toString());
				}
			};

			channel.basicConsume(rabbitMQConfig.getQueueName(), false, consumer);
		} catch (Exception e) {
			String event_type = EIEventType.RABBIT_CONNECT_ERROR.getCode();
			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
			if(ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST："+event_type);
				}else {
				if(StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("cma.cimiss2.dpc.indb.upar.light_locat.MultSubThread");
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
			infoLogger.error("\n rabbitMQ Create Connection Pool Failed： " +e.getMessage());
			
			System.exit(0);
		}
	}

	private Action processMsg(String message, Date recv_time, StringBuffer loggerBuffer) {		
		int index = message.indexOf(":");
		String cts_code = message.substring(0, index);
		DecodeLDP decodeCLI = new DecodeLDP();
		String filepath = message.substring(index + 1);
		File file = new File(message.substring(index + 1, message.length()));
		fileN = file.getName();
		if (file != null && file.exists() && file.isFile()) {
			loggerBuffer.append(file.getPath());
			recv_time = new Date(file.lastModified());
			// 数据解码返回解码结果集
			ParseResult<LightingData> parseResult = decodeCLI.DecodeLPD(file);
			// 判断是否解码成功
			if (parseResult.isSuccess()) {
				List<ReportError> reportErrors = parseResult.getError();
				for (int i = 0; i < reportErrors.size(); i++) {
					loggerBuffer.append(" ERROR REPORT : "+reportErrors.get(i).getSegment()+"\n"+"\t " + reportErrors.get(i).getMessage() + "\n");
					}
				// 获取解码实例对象集
				//DataBaseAction dataBaseAction = DbService.processSuccessReport(parseResult, recv_time, cts_code, loggerBuffer,fileN);
				DataBaseAction action = null;
				loggerBuffer.append(filepath);
				if (StartConfig.getDatabaseType() == 1 || StartConfig.getDatabaseType() == 0) {
					action = DbService.processSuccessReport(parseResult, recv_time, cts_code, loggerBuffer,filepath);	
				}else if (StartConfig.getDatabaseType() == 2 ) {
					action = OTSService.insert_ots(parseResult, StartConfig.valueTable(), recv_time,loggerBuffer,filepath);
				}
				//DataBaseAction dataBaseAction = insert_db(list, recv_time, cts_code, loggerBuffer);
				if (action == DataBaseAction.CONNECTION_ERROR) {
					return Action.RETRY;
				}
				else {	
					return Action.ACCEPT;
				}
				
			} else {
				List<ReportError> reportErrors = parseResult.getError();
				for (int i = 0; i < reportErrors.size(); i++) {
					loggerBuffer.append(" ERROR REPORT : "+reportErrors.get(i).getSegment()+"\n"+"\t " + reportErrors.get(i).getMessage() + "\n");
				}
				
				ParseInfo parseInfo = parseResult.getParseInfo();
				if(parseInfo != null) {
					infoLogger.error("\n read file error："+file.getPath()+"\n  error description:"+parseInfo.getDescription());
					
					String event_type = EIEventType.OP_FILE_ERROR.getCode();
					EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
					if(ei == null) {
						infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST："+event_type);
						}else {
						if(StartConfig.isSendEi()) {
							ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
							ei.setKObject("cma.cimiss2.dpc.indb.upar.light_locat.MultSubThread");
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
							RestfulSendData.SendData(restfulInfos, SendType.EI);
						}
					}
				}
				return Action.ACCEPT;
			}
		
		} else {
			loggerBuffer.append(" ERROR : not find file " + file.getPath() +"\n");
			String event_type = EIEventType.OP_FILE_ERROR.getCode();
			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
			if(ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST："+event_type);
				}else {
				if(StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("cma.cimiss2.dpc.indb.upar.light_locat.MultSubThread");
					ei.setKEvent("解码入库源文件不存在：");
					ei.setKIndex("详细信息："+message);
					ei.setEVENT_EXT1(fileN);
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
			
			return Action.ACCEPT;
		}
		
	}
	
	
	
	
}
