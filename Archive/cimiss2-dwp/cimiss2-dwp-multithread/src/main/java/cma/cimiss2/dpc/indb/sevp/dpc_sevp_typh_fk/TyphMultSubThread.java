package cma.cimiss2.dpc.indb.sevp.dpc_sevp_typh_fk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.sevp.Typh;
import cma.cimiss2.dpc.decoder.sevp.DecodeTyphFK;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.sevp.dpc_sevp_typh.service.DbService;
import cma.cimiss2.dpc.indb.sevp.dpc_sevp_typh.service.OTSService;

import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.config.RabbitMQConfig;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <br>
 * @Title:  TyphMultSubThread.java   
 * @Package cma.cimiss2.dpc.indb.sevp.typh   
 * @Description:    TODO(台风实况与预测资料解码入库线程)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月8日 下午4:01:13   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 *
 *
 */
public class TyphMultSubThread implements Runnable {
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static String fileN = null;
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public static String sod_report_code = StartConfig.reportSodCode();
	public TyphMultSubThread(BlockingQueue<StatDi> diQueues) {
		DbService.setDiQueues(diQueues);
		OTSService.setDiQueues(diQueues);
	}
	
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
            	@Override
            	public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
            			byte[] body) throws IOException {
            		Date recv_time = new Date();
            		String message = new String(body, "UTF-8");
					// 获取消息体
            		messageLogger.info("\n PROCESS MESSAGE BODY : " + message);
            		// 消息处理
            		Action action  = processMsg(message, recv_time);
            		if(action == Action.ACCEPT){
            			// 消息确认机制
            			channel.basicAck(envelope.getDeliveryTag(), false);
            		}
            		else{
            			channel.basicReject(envelope.getDeliveryTag(), true);
            		}
            	}            	
            };
           
            channel.basicConsume(rabbitMQConfig.getQueueName(), false, consumer);
		} catch (Exception e) {
			String event_type = EIEventType.RABBIT_CONNECT_ERROR.getCode();
			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
			if(ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
			}else {
				if(StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("cma.cimiss2.dpc.indb.sevp.typh.TyphMultSubThread");
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
					RestfulSendData.SendData(restfulInfos);
				}
			}
			infoLogger.error("\n rabbitMQ create connection failed: " +e.getMessage());
			
			System.exit(0);
		}
	}
	/**
	 * 
	 * @Title: processMsg   
	 * @Description: TODO(消息处理函数)   
	 * @param  message 消息体内容
	 * @param recv_time 消息接收时间 
	 * @return: Action  处理状态
	 * @throws:
	 */
	private Action processMsg(String message, Date recv_time) {
		int index = message.indexOf(":");
		cts_code = message.substring(0, index);
		String filepath = message.substring(index+1);
		File file = new File(filepath);
		fileN = file.getName();
		recv_time = new Date(file.lastModified());
		if(file != null && file.exists() && file.isFile()){
			DecodeTyphFK decodeTyph = new DecodeTyphFK();
			ParseResult<Typh> parseResult = decodeTyph.DecodeFile(file);			
			if(parseResult.isSuccess()){
				List<ReportError> reportErrors = parseResult.getError();
				if(reportErrors != null && reportErrors.size() > 0) {
					for (int i = 0; i < reportErrors.size(); i++) {
						infoLogger.error(" ERROR REPORT : "+ filepath +", "+ reportErrors.get(i).getSegment()+"\n"+reportErrors.get(i).getMessage() + "\n");
					} 
				}
				StringBuffer loggerBuffer = new StringBuffer();
				DataBaseAction action = null;
				loggerBuffer.append(filepath + "\n");
				if(StartConfig.getDatabaseType() == 2){
					action = OTSService.processSuccessReport(parseResult.getData(), recv_time, filepath, filepath, loggerBuffer);
					OTSService.reportInfoToDb(parseResult.getReports(), recv_time, loggerBuffer);
				}else{
					action = DbService.processSuccessReport(parseResult, filepath, recv_time, filepath, loggerBuffer);
				}
				infoLogger.info(loggerBuffer.toString());
				if(action == DataBaseAction.CONNECTION_ERROR){
					return Action.RETRY;
				}else {
					return Action.ACCEPT;
				}
			}else {
				ParseInfo parseInfo = parseResult.getParseInfo();
				if(parseInfo != null) {
					infoLogger.error("\n Read file error: "+file.getPath()+"\n description: "+parseInfo.getDescription());
					String event_type = EIEventType.OP_FILE_ERROR.getCode();
					EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
					if(ei == null) {
						infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
					}else {
						if(StartConfig.isSendEi()) {
							ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
							ei.setKObject("cma.cimiss2.dpc.indb.sevp.typh.TyphMultSubThread");
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
							RestfulSendData.SendData(restfulInfos);
						}
					}
				}
				return Action.ACCEPT;
			}	
		}else {
			infoLogger.error(" ERROR : not find file " + file.getPath() +"\n");
			
			String event_type = EIEventType.OP_FILE_ERROR.getCode();
			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
			if(ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
			}else {
				if(StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("cma.cimiss2.dpc.indb.sevp.typh.TyphMultSubThread");
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
					RestfulSendData.SendData(restfulInfos);
				}
			}
			return Action.ACCEPT;
		}
	}
	
	
}
