package cma.cimiss2.dpc.indb.cawn.dc_cawn_nsd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.cawn.AtmosphericCompositionAerosolConcentration;
import cma.cimiss2.dpc.decoder.cawn.DecodeNsd;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.cawn.dc_cawn_nsd.service.*;

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
 * 
 * <br>
 * @Title:  NSDMultSubThread.java
 * @Package org.cimiss2.dwp.z_cawn.nsd
 * @Description:(大气成分气溶胶数浓度（NSD）资料入库线程)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月30日 上午10:57:12   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */

public class NSDMultSubThread implements Runnable {
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static String fileN = null;
    public static String cts_code = StartConfig.ctsCode();
	
	public NSDMultSubThread(BlockingQueue<StatDi> diQueues) {
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
            // 创建RabbitMQ连接
            Connection connection = factory.newConnection();
            // 创建通道
            final Channel channel = connection.createChannel();
            // 声明消费对列，如果对列不存在，则自动创建
            channel.queueDeclare(rabbitMQConfig.getQueueName(), true, false, false, null);
            channel.basicQos(20);
            // 定义消费者
            Consumer consumer = new DefaultConsumer(channel) { 	
            	@Override
            	public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
            			byte[] body) throws IOException {
            		Date recv_time = new Date();
            		String message = new String(body, "UTF-8");
            		StringBuffer loggerBuffer = new StringBuffer();
					// 获取消息体
            		messageLogger.info("PROCESS MESSAGE BODY : " + message );
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
			if(ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
			}else {
				if(StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("cma.cimiss2.dpc.indb.cawn.nsd.NSDMultSubThread");
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
			infoLogger.error("\n rabbitMQ create connection failed:  " +e.getMessage());
			System.exit(0);
		}
	}
	
	/**
	 * @param loggerBuffer  
	 * @Title: processMsg 消息处理函数
	 * @Description: (消息处理，获取文件路径，对文件解码) 
	 * @param message 消息体内容
	 * @param recv_time  消息接收的时间
	 * @param loggerBuffer
	 * @return  
	 *    Action  消息处理状态
	 * @throws
	 */
	private Action processMsg(String message, Date recv_time, StringBuffer loggerBuffer) {
		int index = message.indexOf(":");
		String filepath = message.substring(index+1);
		File file = new File(filepath);
		fileN = file.getName();
		recv_time = new Date(file.lastModified());
		if(file != null && file.exists() && file.isFile()){
			
			if(file.length()  > StartConfig.maxFileSize){
				infoLogger.info("\n File is too large(" + file.length() + "byte), ignore it! Filepath is: " + filepath);
				return Action.ACCEPT;
			}
			
			loggerBuffer.append(filepath+"\n");
			DecodeNsd decodeNsd = new DecodeNsd();
			ParseResult<AtmosphericCompositionAerosolConcentration> parseResult = decodeNsd.decodeFile(file);			
			if(parseResult.isSuccess()){
				List<ReportError> reportErrors = parseResult.getError();
				if(reportErrors != null && reportErrors.size() > 0) {
					for (int i = 0; i < reportErrors.size(); i++) {
						loggerBuffer.append(" ERROR REPORT : "+reportErrors.get(i).getSegment()+"\n"+"\t " + reportErrors.get(i).getMessage() + "\n");} // end for
				}
				DataBaseAction action = null;
				if(StartConfig.getDatabaseType() == 2){
					action = OTSService.insert_ots(parseResult.getData(), StartConfig.valueTable(), recv_time, loggerBuffer, filepath);
					OTSService.reportInfoToDb(parseResult.getReports(), StartConfig.reportTable(), recv_time, loggerBuffer, "9999");
				}else{
					action = DbService.processSuccessReport(parseResult, fileN, recv_time,loggerBuffer,filepath);
				}
				
				if(action == DataBaseAction.CONNECTION_ERROR){
					return Action.RETRY;
				}else {
					return Action.ACCEPT;
				}
			}else {
				List<ReportError> reportErrors = parseResult.getError();
				if(reportErrors != null && reportErrors.size() > 0) {
					for (int i = 0; i < reportErrors.size(); i++) {
						loggerBuffer.append(" ERROR REPORT : "+reportErrors.get(i).getSegment()+"\n"+"\t " + reportErrors.get(i).getMessage() + "\n");} // end for
				}
				ParseInfo parseInfo = parseResult.getParseInfo();
				if(parseInfo != null) {
					infoLogger.error("\n File read error: "+file.getPath()+"\n description: "+parseInfo.getDescription());
					String event_type = EIEventType.OP_FILE_ERROR.getCode();
					EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
					if(ei == null) {
						infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
					}else {
						if (StartConfig.isSendEi()) {
							ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
							ei.setKObject("cma.cimiss2.dpc.indb.cawn.nsd.NSDMultSubThread");
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
			
		}else {
			loggerBuffer.append(" ERROR : not find file " + file.getPath() +"\n");
			String event_type = EIEventType.OP_FILE_ERROR.getCode();
			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
			if(ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST:"+event_type);
			}else {
				if (StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("cma.cimiss2.dpc.indb.cawn.nsd.NSDMultSubThread");
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