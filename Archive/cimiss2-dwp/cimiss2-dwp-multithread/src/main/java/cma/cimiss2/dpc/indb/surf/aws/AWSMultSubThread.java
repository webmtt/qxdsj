package cma.cimiss2.dpc.indb.surf.aws;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.RabbitMQConfig;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.surf.AWS;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.surf.DecodeAWS;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.surf.aws.service.*;
/**
 * 
 * <br>
 * @Title:  AWSMultSubThread.java   
 * @Package cma.cimiss2.dpc.indb.surf.aws  
 * @Description:    TODO(地面设备状态信息资料解码入库线程)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年10月10日 下午3:25:41   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 *
 *
 */
public class AWSMultSubThread implements Runnable{
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	public AWSMultSubThread(BlockingQueue<StatDi> diQueues) {
		DbService.setDiQueues(diQueues);
		OTSService.setDiQueues(diQueues);
	}
	
	public void run() {
		// 读取rabbitmq 配置文件
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
            channel.basicQos(10);
            // 定义消费者
            Consumer consumer = new DefaultConsumer(channel) {
            	@Override
            	public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
            			byte[] body) throws IOException {
            		Date recv_time = new Date();
            		String message = new String(body, "UTF-8");
            		// 获取消息体
            		messageLogger.info("\n PROCESS MESSAGE BODY : " + message );
            		// 消息处理
            		Action action = processMsg(message, recv_time);
        	    	if(action == Action.ACCEPT)
            			// 消息确认机制
            			channel.basicAck(envelope.getDeliveryTag(), false);
            		else 
            			channel.basicReject(envelope.getDeliveryTag(), true);
					
            	}            	
            };
           
            channel.basicConsume(rabbitMQConfig.getQueueName(), false, consumer);
		} catch (Exception e) {
			e.printStackTrace();
			String event_type = EIEventType.RABBIT_CONNECT_ERROR.getCode();
			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
			if(ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: " +event_type);
			}else {
				if(StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("cma.cimiss2.dpc.indb.surf.aws.AWSMultSubThread");
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
					if(StartConfig.isSendEi())
						RestfulSendData.SendData(restfulInfos, SendType.EI);
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
	 */
	private Action processMsg(String message, Date recv_time) {
		if(StartConfig.getDataPattern()==1){//文件
			int index = message.indexOf(":");
			// 获取消息中的文件路径
			String filepath = message.substring(index + 1);
			File file = new File(filepath);
			if(file != null && file.exists() && file.isFile()){
				if(file.length() <= 0){
					infoLogger.error("\n File is empty: "+ filepath);
					return  Action.ACCEPT;
				}
				String fileN = file.getName();
				SAXReader reader = new SAXReader();
				Document doc = null;
				try {
					doc = reader.read(filepath);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					infoLogger.error("\n Can not read the file: "+ filepath);
					return  Action.ACCEPT;
				}
				String string = doc.asXML();
				DecodeAWS decodeAWS = new DecodeAWS();
				ParseResult<AWS> parseResult = decodeAWS.parseFile("Z_SURF_I_57625_20181006150800_R_AWS_FTM.XML", string);
				StringBuffer stringBuffer = new StringBuffer();
				if(parseResult.isSuccess()){
					List<ReportError> reportErrors = parseResult.getError();
					if(reportErrors != null && reportErrors.size() > 0) {
						for (int i = 0; i < reportErrors.size(); i++) {
							stringBuffer.append("\n Report error: "+ filepath +"\n because: "+reportErrors.get(i).getMessage()+": "+reportErrors.get(i).getSegment());
						} // end for
					}
					DataBaseAction action = null;
					if(StartConfig.getDatabaseType() == 2){
						action = OTSService.insert_ots(parseResult.getData(), StartConfig.valueTable(), recv_time, stringBuffer, filepath);
					}
					else{
						action = DbService.processSuccessReport(parseResult, recv_time, filepath);
					}
					infoLogger.info(stringBuffer.toString());
					if(action == DataBaseAction.CONNECTION_ERROR){
						return Action.RETRY;
					}else {
						return Action.ACCEPT;
					}
				}else{
					List<ReportError> reportErrors = parseResult.getError();
					if(reportErrors != null && reportErrors.size() > 0) {
						for (int i = 0; i < reportErrors.size(); i++) {
							infoLogger.error("\n Decode failed: "+ filepath +"\n because: "+reportErrors.get(i).getMessage()+":"+reportErrors.get(i).getSegment());
						} // end for
					}
					ParseInfo parseInfo = parseResult.getParseInfo();
					if(parseInfo != null) {
						infoLogger.error("\n Read file error："+ filepath +"\n error description: "+parseInfo.getDescription());
						String event_type = EIEventType.OP_FILE_ERROR.getCode();
						EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
						if(ei == null) {
							infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: " + event_type);
						}else {
							if(StartConfig.isSendEi()) {
								ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
								ei.setKObject("cma.cimiss2.dpc.indb.surf.aws.AWSMultSubThread");
								ei.setKEvent("解码入库源文件异常：");
								ei.setKIndex("详细信息：["+parseInfo.getDescription()+"]"+ filepath);
								ei.setEVENT_EXT1(filepath);
								RestfulInfo restfulInfo = new RestfulInfo();
								restfulInfo.setType("SYSTEM.ALARM.EI ");
								restfulInfo.setName("数据解码入库EI告警信息");
								restfulInfo.setMessage("数据解码入库EI告警信息");
								
								restfulInfo.setFields(ei);
								List<RestfulInfo> restfulInfos = new ArrayList<>();
								restfulInfos.add(restfulInfo);
								if(StartConfig.isSendEi())
									RestfulSendData.SendData(restfulInfos, SendType.EI);
							}
						}
					}
					return Action.ACCEPT;
				}
			}else{
				infoLogger.error("\n File dose not exist: "+ filepath);
				String event_type = EIEventType.OP_FILE_ERROR.getCode();
				EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
				if(ei == null) {
					infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
				}else {
					if(StartConfig.isSendEi()) {
						ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
						ei.setKObject("cma.cimiss2.dpc.indb.surf.aws.AWSMultSubThread");
						ei.setKEvent("解码入库源文件不存在：");
						ei.setKIndex("详细信息："+message);
						ei.setEVENT_EXT1(filepath);
						RestfulInfo restfulInfo = new RestfulInfo();
						restfulInfo.setType("SYSTEM.ALARM.EI ");
						restfulInfo.setName("数据解码入库EI告警信息");
						restfulInfo.setMessage("数据解码入库EI告警信息");
						
						restfulInfo.setFields(ei);
						List<RestfulInfo> restfulInfos = new ArrayList<>();
						restfulInfos.add(restfulInfo);
						if(StartConfig.isSendEi())
							RestfulSendData.SendData(restfulInfos, SendType.EI);
					}
				}
				return Action.ACCEPT;
			}
			
		}else{//二进制
			
			Pattern ctsType = Pattern.compile("(TYPE=.*?)(,IIIII)"); 
			Pattern FileName = Pattern.compile("(FileName=.*?)(,BBB)");
			Matcher matchercts = ctsType.matcher(message);
			Matcher matcherfn = FileName.matcher(message);
			String cts_code = null;
			String fn = null;
			String fileContent = null;
			if(matchercts.find() && matcherfn.find()){
				cts_code = matchercts.group(1).substring(5).trim();
				fn = matcherfn.group(1).substring(9).trim();
			}
			int idx = message.indexOf("<root>"); // ; 后是XML文件的协议和文件内容
			if(idx != -1){
				fileContent = message.substring(idx);
			}
			
			StringBuffer stringBuffer = new StringBuffer();
			if(cts_code != null && fn != null && fileContent != null){
				stringBuffer.append(fn);
				DecodeAWS decodeAWS = new DecodeAWS();
				ParseResult<AWS> parseResult = decodeAWS.parseFile(fn, fileContent);			
				if(parseResult.isSuccess()){
					List<ReportError> reportErrors = parseResult.getError();
					if(reportErrors != null && reportErrors.size() > 0) {
						for (int i = 0; i < reportErrors.size(); i++) {
							stringBuffer.append("\n Report error: "+ fn +"\n because: "+reportErrors.get(i).getMessage()+": "+reportErrors.get(i).getSegment());
						} // end for
					}
					
					DataBaseAction action = null;
					if(StartConfig.getDatabaseType() == 2){
						action = OTSService.insert_ots(parseResult.getData(), StartConfig.valueTable(), recv_time, stringBuffer, fn);
					}
					else{
						action = DbService.processSuccessReport(parseResult, recv_time, fn);
					}
					infoLogger.info(stringBuffer.toString());
					if(action == DataBaseAction.CONNECTION_ERROR){
						return Action.RETRY;
					}else {
						return Action.ACCEPT;
					}
				}else {
					List<ReportError> reportErrors = parseResult.getError();
					if(reportErrors != null && reportErrors.size() > 0) {
						for (int i = 0; i < reportErrors.size(); i++) {
							infoLogger.error("\n Decode failed: "+ fn +"\n because: "+reportErrors.get(i).getMessage()+":"+reportErrors.get(i).getSegment());
						} // end for
					}
					ParseInfo parseInfo = parseResult.getParseInfo();
					if(parseInfo != null) {
						infoLogger.error("\n Read file error："+ fn +"\n error description: "+parseInfo.getDescription());
						String event_type = EIEventType.OP_FILE_ERROR.getCode();
						EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
						if(ei == null) {
							infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: " + event_type);
						}else {
							if(StartConfig.isSendEi()) {
								ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
								ei.setKObject("cma.cimiss2.dpc.indb.surf.aws.AWSMultSubThread");
								ei.setKEvent("解码入库源文件异常：");
								ei.setKIndex("详细信息：["+parseInfo.getDescription()+"]"+ fn);
								ei.setEVENT_EXT1(fn);
								RestfulInfo restfulInfo = new RestfulInfo();
								restfulInfo.setType("SYSTEM.ALARM.EI ");
								restfulInfo.setName("数据解码入库EI告警信息");
								restfulInfo.setMessage("数据解码入库EI告警信息");
								
								restfulInfo.setFields(ei);
								List<RestfulInfo> restfulInfos = new ArrayList<>();
								restfulInfos.add(restfulInfo);
								if(StartConfig.isSendEi())
									RestfulSendData.SendData(restfulInfos, SendType.EI);
							}
						}
					}
					return Action.ACCEPT;
				}	
				
			}else {
				infoLogger.error("\n File dose not exist: "+ fn);
				
				String event_type = EIEventType.OP_FILE_ERROR.getCode();
				EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
				if(ei == null) {
					infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
				}else {
					if(StartConfig.isSendEi()) {
						ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
						ei.setKObject("cma.cimiss2.dpc.indb.surf.aws.AWSMultSubThread");
						ei.setKEvent("解码入库源文件不存在：");
						ei.setKIndex("详细信息："+message);
						ei.setEVENT_EXT1(fn);
						RestfulInfo restfulInfo = new RestfulInfo();
						restfulInfo.setType("SYSTEM.ALARM.EI ");
						restfulInfo.setName("数据解码入库EI告警信息");
						restfulInfo.setMessage("数据解码入库EI告警信息");
						
						restfulInfo.setFields(ei);
						List<RestfulInfo> restfulInfos = new ArrayList<>();
						restfulInfos.add(restfulInfo);
						if(StartConfig.isSendEi())
							RestfulSendData.SendData(restfulInfos, SendType.EI);
					}
				}
				return Action.ACCEPT;
			}
		}
	}
}
