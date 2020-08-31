package cma.cimiss2.dpc.indb.agme.dc_agme_manl_pheno;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cma.cimiss2.dpc.decoder.agme.DecodeAgmePheno;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Pheno;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.agme.dc_agme_manl_pheno.service.*;

import org.cimiss2.dwp.tools.DataBaseAction;

import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.CTSCodeMap;
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
 * @Title:  MultSubThread2.java
 * @Package 
 * @Description: 农业气象自然物候要素数据入库多线程
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月28日 上午9:35:07   wangzunpeng    Initial creation.
 * </pre>
 * 
 * @author wangzunpeng
 */
public class MultSubThread implements Runnable {
	
	List<CTSCodeMap> ctsCodeMaps;
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	ReadIni ini = ReadIni.getIni();

	public MultSubThread(BlockingQueue<StatDi> diQueues, List<CTSCodeMap> ctsCodeMaps) {
		this.ctsCodeMaps = ctsCodeMaps;
		DbService.diQueues = diQueues;
		OTSService.diQueues = diQueues;
	}

	private String v_bbb = "000";
	// 是否是更新文件
	private boolean isUpdate = false;

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
					// 获取消息体
            		messageLogger.info(message + "\n");
            		StringBuffer loggerBuffer = new StringBuffer();
            		
					// 消息处理
					Action action = processMsg(message, recv_time,loggerBuffer);


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
				if (StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject(this.getClass().getName());
					ei.setKEvent("RabbitMQ连接异常");
					ei.setKIndex("连接信息：[主机：" + rabbitMQConfig.getHost() + ",用户名：" + rabbitMQConfig.getUser() + ",密码：" + rabbitMQConfig.getPassword() + ",端口号："
							+ rabbitMQConfig.getPort() + "，队列：" + rabbitMQConfig.getQueueName() + "]");
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
	private final static String UPDATE_FILE_REG = "^Z_AGME_\\w+_\\d{14}_O_PHENO-[A-Z]{3}.txt$";

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

		File file = new File(message.substring(index + 1, message.length()));
		String filepath = message.substring(index + 1);
		fileName = file.getName(); // 文件名
		recv_time = new Date(file.lastModified());
		if (file != null && file.exists() && file.isFile()) {
			
			if(file.length()  > StartConfig.maxFileSize()){
				infoLogger.info("\n File is too large(" + file.length() + "byte), ignore it! Filepath is: " + filepath);
				return Action.ACCEPT;
			}
			// 判断文件是否需要更新

			Matcher m = Pattern.compile(UPDATE_FILE_REG, Pattern.CASE_INSENSITIVE).matcher(fileName);

			if (m.find()) {
				v_bbb = fileName.split("\\.|-")[1];
				isUpdate = true;
			} else {
				isUpdate = false;
				v_bbb = "000";
			}
			DecodeAgmePheno decodeAgmePheno = new DecodeAgmePheno();
			ParseResult<Agme_Pheno> parseResult = decodeAgmePheno.decode(file);
			loggerBuffer.append(filepath);
			if (parseResult.isSuccess()) {

				List<ReportError> reportErrors = parseResult.getError();
				if (reportErrors != null && reportErrors.size() > 0) {
					for (int i = 0; i < reportErrors.size(); i++) {
//						infoLogger.error(" ERROR REPORT : "+reportErrors.get(i).getSegment()+"\n"+"\t " + reportErrors.get(i).getMessage() + "\n");
						loggerBuffer.append(" ERROR REPORT : "+reportErrors.get(i).getSegment()+"\n"+"\t " + reportErrors.get(i).getMessage() + "\n");
					}
				}

				// List<Agme_Pheno> list = parseResult.getData();
				// if (list != null && list.size() > 0) {

				//DataBaseAction dataBaseAction = DbService.insert_db(parseResult, recv_time,fileName,isUpdate,v_bbb,loggerBuffer);
				
				DataBaseAction action;
//				loggerBuffer.append(filepath);
				if(StartConfig.getDatabaseType() == 2) {
					action = OTSService.processSuccessReport(parseResult, recv_time,fileName, isUpdate,v_bbb,loggerBuffer);
					String[] fnames = fileName.split("_");
					List<ReportInfo> reportInfos = parseResult.getReports();
					//OTSReportInfoService.reportInfoToDb(reportInfos, v_bbb, recv_time, fnames[3], fnames[1],loggerBuffer);
					OTSService.reportInfoToDb(reportInfos, v_bbb, recv_time, fnames[3], fnames[1],loggerBuffer,ctsCodeMaps);
				}else {
					action = DbService.insert_db(parseResult, recv_time,filepath,isUpdate,v_bbb,loggerBuffer,ctsCodeMaps);
				}
				// 如果数据库连接失败，返回重试标识
				if (action == DataBaseAction.CONNECTION_ERROR) {

					return Action.RETRY;
				} else {
					return Action.ACCEPT; // 返回入库成功
				}

				// } else {
				// logger.error("\n 文件: " + fileName + " 为空文件");
				// return Action.REJECT;
				// }
			} else {
				List<ReportError> reportErrors = parseResult.getError();
				if(reportErrors != null && reportErrors.size() > 0) {
					for (int i = 0; i < reportErrors.size(); i++) {
//						infoLogger.error(file.getPath()+"\n ERROR REPORT : "+reportErrors.get(i).getSegment()+"\n"+"\t " + reportErrors.get(i).getMessage() + "\n");
						loggerBuffer.append(" ERROR REPORT : "+reportErrors.get(i).getSegment()+"\n"+"\t " + reportErrors.get(i).getMessage() + "\n");
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
			infoLogger.error(" ERROR : not find file " + file.getPath() +"\n");
			String event_type = EIEventType.OP_FILE_ERROR.getCode();
			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
			if (ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST：" + event_type);
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
