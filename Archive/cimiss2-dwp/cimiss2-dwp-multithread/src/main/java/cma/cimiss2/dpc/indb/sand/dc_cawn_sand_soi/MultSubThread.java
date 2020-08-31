package cma.cimiss2.dpc.indb.sand.dc_cawn_sand_soi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.sand.SandChnSoi;
import cma.cimiss2.dpc.decoder.sand.DecodeSandSoi;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.sand.dc_cawn_sand_soi.service.*;

import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.RabbitMQConfig;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.StationInfo;
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
 * <br>
 * @Title:  MultSubThread.java
 * @Package cma.cimiss2.dpc.indb.sand.soi
 * @Description: 消息处理线程
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年1月22日 上午9:46:55   wangzunpeng    Initial creation.
 * </pre>
 * 
 * @author wangzunpeng
 */
public class MultSubThread implements Runnable {

//	private static final int DEFAULT_VALUE = 999999;

	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	HashMap<String, Integer> retryMap;

	public MultSubThread(BlockingQueue<StatDi> diQueues) {
		DbService.setDiQueues(diQueues);
		OTSService.setDiQueues(diQueues);
	}

	Map<String, Object> proMap = StationInfo.getProMap();

	@Override
	public void run() {
		// 连接mq配置文件路径
		RabbitMQConfig rabbitMQConfig = new RabbitMQConfig();
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
			// 创建rabbitMQ连接
			Connection connection = factory.newConnection();
			// 创建通道
			final Channel channel = connection.createChannel();
			// 声明消费队列，如果队列不存在，则自动创建
			channel.queueDeclare(rabbitMQConfig.getQueueName(), true, false, false, null);
			channel.basicQos(10);
			// 定义消费者
			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
					Date recv_time = new Date();
					String message = new String(body, "UTF-8");
					StringBuffer loggerBuffer = new StringBuffer();
            		// 获取消息体
            		messageLogger.info(message + "\n");
            		// 消息处理
            		Action action  = processMsg(message, recv_time,loggerBuffer);
            		
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
			if (ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST：" + event_type);
			} else {
				if(StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject(this.getClass().getName());
					ei.setKEvent("RabbitMQ连接异常");
					ei.setKIndex("连接信息：[主机：" + rabbitMQConfig.getHost() + ",用户名：" + rabbitMQConfig.getUser() + ",密码：" + rabbitMQConfig.getPassword() + ",端口号："
							+ rabbitMQConfig.getPort() + "，对列：" + rabbitMQConfig.getQueueName() + "]");
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
			infoLogger.error("\n rabbitMQ Create Connection Pool Failed： " + e.getMessage());

			System.exit(0);
		}
	}

	private String fileName = null;

	/**
	 * @Title: processMsg
	 * @Description: 消息处理
	 * @param message 监听MQ收到的消息
	 * @param recv_time 收到时间
	 * @param loggerBuffer 
	 * @return: Action
	 * @throws：
	 */
	private Action processMsg(String message, Date recv_time, StringBuffer loggerBuffer) {
		//
		int index = message.indexOf(":");
		String filepath = message.substring(index+1);
		File file = new File(message.substring(index + 1, message.length()));

		fileName = file.getName(); // 文件名
		recv_time = new Date(file.lastModified());
		if (file != null && file.exists() && file.isFile()) {

			DecodeSandSoi dss = new DecodeSandSoi();

			ParseResult<SandChnSoi> parseResult = dss.decode(file);

			if (parseResult.isSuccess()) {

				List<ReportError> reportErrors = parseResult.getError();
				if (reportErrors != null && reportErrors.size() > 0) {
					for (int i = 0; i < reportErrors.size(); i++) {
						loggerBuffer.append("\n"+file.getPath()+"\n ERROR REPORT : "+reportErrors.get(i).getSegment()+"\n"+"\t " + reportErrors.get(i).getMessage() + "\n");// end for
						}
				}

				//DataBaseAction dataBaseAction = insert_db(parseResult, recv_time);
				//DataBaseAction dataBaseAction = DbService.insert_db(parseResult,  recv_time, fileName,loggerBuffer);
				DataBaseAction action = null;
				loggerBuffer.append(filepath);
				if(StartConfig.getDatabaseType() == 2){
					action = OTSService.insert_ots(parseResult ,recv_time, loggerBuffer, fileName) ;
					OTSService.reportInfoToDb(parseResult.getReports(), recv_time, loggerBuffer);
				}else{
					action = DbService.insert_db(parseResult,  recv_time, fileName,loggerBuffer,filepath);
				}
				
				// 如果数据库连接失败，返回重试标识
				if (action == DataBaseAction.CONNECTION_ERROR) {

					return Action.RETRY;
				} else {
					return Action.ACCEPT; // 返回入库成功
				}

			} else {
				List<ReportError> reportErrors = parseResult.getError();
				if(reportErrors != null && reportErrors.size() > 0) {
					for (int i = 0; i < reportErrors.size(); i++) {
						infoLogger.error("\n"+file.getPath()+"\n ERROR REPORT : "+reportErrors.get(i).getSegment()+"\n"+"\t " + reportErrors.get(i).getMessage() + "\n");
					}
				}
				ParseInfo parseInfo = parseResult.getParseInfo();
				if (parseInfo != null) {
					infoLogger.error("\n read file error："+file.getPath()+"\n  error description:"+parseInfo.getDescription());
					String event_type = EIEventType.OP_FILE_ERROR.getCode();
					EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
					if (ei == null) {
						infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST：" + event_type);
					} else {
						if(StartConfig.isSendEi()) {
							ei.setEVENT_TIME(TimeUtil.date2String(new Date(), TimeUtil.DEFAULT_DATETIME_FORMAT));
							ei.setKObject(this.getClass().getName());
							ei.setKEvent("解码入库源文件异常：");
							ei.setKIndex("详细信息：[" + parseInfo.getDescription() + "]" + file.getPath());
							ei.setEVENT_EXT1(fileName);
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
			if (ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST：" + event_type);
			} else {
				if(StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject(this.getClass().getName());
					ei.setKEvent("解码入库源文件不存在：");
					ei.setKIndex("详细信息：" + message);
					ei.setEVENT_EXT1(fileName);
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
