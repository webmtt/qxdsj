package cma.cimiss2.dpc.indb.cawn.dc_cawn_pmmul;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.cawn.AtmosphericAerosolPmmul;
import cma.cimiss2.dpc.decoder.cawn.DecodePmmul;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.cawn.dc_cawn_pmmul.service.*;

import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.RabbitMQConfig;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

/**
 * <br>
 * @Title:  MultSubThread.java
 * @Package org.cimiss2.dwp.z_cawn.pummul
 * @Description:    大气成分气溶胶PM10/PM2.5/PM1质量浓度（PMMUL）消息处理线程
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月2日 下午2:34:06   wangzunpeng    Initial creation.
 * </pre>
 * 
 * @author wangzunpeng
 */
public class MultSubThread implements Runnable {

	// private static final int DEFAULT_VALUE = 999999;

	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");

	public MultSubThread(BlockingQueue<StatDi> diQueues) {
		DbService.setDiQueues(diQueues);
		OTSService.setDiQueues(diQueues);
	}

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
			channel.basicQos(20);
			// 定义消费者
			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
					// Date recv_time = new Date();
					String message = new String(body, "UTF-8");
					// 获取消息体
					messageLogger.info("\n PROCESS MESSAGE BODY: " + message);
					// 消息处理
					Action action = processMsg(message);

					if (action == Action.ACCEPT) {
						// 消息确认机制
						channel.basicAck(envelope.getDeliveryTag(), false);
					}else if (action == Action.RETRY) {
						channel.basicReject(envelope.getDeliveryTag(), true);
					}
					
				}
			};

			channel.basicConsume(rabbitMQConfig.getQueueName(), false, consumer);
		} catch (Exception e) {
			String event_type = EIEventType.RABBIT_CONNECT_ERROR.getCode();
			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
			if (ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: " + event_type);
			} else {
				if (StartConfig.isSendEi()) {
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
			infoLogger.error("\n rabbitMQ  create connection failed: " + e.getMessage());

			System.exit(0);
		}
	}

	private String fileName = null;

	// typeOfStorageInstrument : 存储仪器类型
	

	/**
	 * @Title: processMsg
	 * @Description: 大气成分气溶胶PM10/PM2.5/PM1质量浓度（PMMUL）消息处理
	 * @param message 监听MQ收到的消息
	 * @param recv_time 收到时间
	 * @return: Action
	 * @throws：
	 */
	private Action processMsg(String message) {
		//
		int index = message.indexOf(":");

		File file = new File(message.substring(index + 1, message.length()));

		fileName = file.getName(); // 文件名
		// ---------------------文件名检查-----------------------
		// String fileNameStartFlag = "Z_CAWN";
		
//		if (stationFlag.equals("C")) {
//			if (!Pattern.matches("[A-Z]{4}", stationNum)) {
//				infoLogger.error("\n File name format error: " + fileName);
//				return Action.ACCEPT;
//			}
//		} else if (stationFlag.equals("I")) {
//			if (!Pattern.matches("[A-Z0-9]{1}\\d{4}", stationNum)) {
//				infoLogger.error("\n File name format error: " + fileName);
//				return Action.ACCEPT;
//			}
//		}
		// } else {
		// logger.error("\n 文件名不是以" + fileNameStartFlag + "开头: " + fileName);
		// return Action.REJECT;
		// }
		// ----------------------- 文件名检查 ---------------------
		StringBuffer loggerBuffer = new StringBuffer();
		if (file != null && file.exists() && file.isFile()) {
			if(file.length()  > StartConfig.maxFileSize){
				infoLogger.info("\n File is too large(" + file.length() + "byte), ignore it! Filepath is: " + file.getPath());
				return Action.ACCEPT;
			}
			
			DecodePmmul dp = new DecodePmmul();
			ParseResult<AtmosphericAerosolPmmul> parseResult = dp.decode(file);
			String typeOfStorageInstrument = "999999";
			String tempFileName = fileName.toUpperCase();
			// if (tempFileName.startsWith(fileNameStartFlag)) {
			String[] fs = tempFileName.split("_|-|\\.");
			String stationFlag = fs[2];
			String stationNum = fs[3];
			if (fs.length == 11) {
				typeOfStorageInstrument = fs[9];
			}
			loggerBuffer.append(file.getPath());
			if (parseResult.isSuccess()) {
				List<ReportError> reportErrors = parseResult.getError();
				if (reportErrors != null && reportErrors.size() > 0) {
					for (int i = 0; i < reportErrors.size(); i++) {
						loggerBuffer.append("\n Decode failed:  because: " + reportErrors.get(i).getMessage() + ":" + reportErrors.get(i).getSegment());
					}
				}
				Date recv_time = new Date(file.lastModified());
				DataBaseAction dataBaseAction = null;
				if(StartConfig.getDatabaseType() == 2){
					dataBaseAction = OTSService.insert_ots(parseResult.getData(), StartConfig.valueTable(), recv_time, loggerBuffer, fileName, typeOfStorageInstrument);
					OTSService.reportInfoToDb(parseResult.getReports(), StartConfig.reportTable(), recv_time, loggerBuffer, "9999");
				}
				else{
					dataBaseAction = DbService.insert_db(parseResult, recv_time, fileName, typeOfStorageInstrument, loggerBuffer, file.getPath());
				}
				infoLogger.info(loggerBuffer.toString());
				// 如果数据库连接失败，返回重试标识
				if (dataBaseAction == DataBaseAction.CONNECTION_ERROR) {
					return Action.RETRY;
				} else {
					return Action.ACCEPT; // 返回入库成功
				}

			} else {
				ParseInfo parseInfo = parseResult.getParseInfo();
				if (parseInfo != null) {
					infoLogger.error("\n Read file error: " + file.getPath() + "\n description: " + parseInfo.getDescription());

					String event_type = EIEventType.OP_FILE_ERROR.getCode();
					EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
					if (ei == null) {
						infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: " + event_type);
					} else {
						if (StartConfig.isSendEi()) {
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
			infoLogger.error("\n File does not exist: " + file.getPath());

			String event_type = EIEventType.OP_FILE_ERROR.getCode();
			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
			if (ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: " + event_type);
			} else {
				if (StartConfig.isSendEi()) {
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
