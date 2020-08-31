package cma.cimiss2.dpc.indb.surf.dc_surf_rd_reg;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceRD;
import cma.cimiss2.dpc.decoder.surf.DecodeRD;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.surf.dc_surf_rd_reg.service.DbService;
import cma.cimiss2.dpc.indb.surf.dc_surf_rd_reg.service.OTSService;

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
 * @Title:  RDMultSubThread.java   
 * @Package org.cimiss2.dwp.z_rd_reg   
 * @Description:    TODO(路面交通数据解码入库线程)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年1月3日 下午3:48:12   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 *
 *
 */
public class RDMultSubThread implements Runnable{
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static String fileN = null;
	public static String cts_code = StartConfig.ctsCode();
	public static String report_type = "RD";
	public RDMultSubThread(BlockingQueue<StatDi> diQueues) {
		DbService.setDiQueues(diQueues);
		OTSService.setDiQueues(diQueues);
	}
	
	@Override
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
            factory.setVirtualHost("/");
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
            		messageLogger.info("\n PROCESS MESSAGE BODY: " + message);
            		// 消息处理
//            		Action action  = processMsg(message, recv_time);
            		
            		 try {
                     	System.out.println("Worker1  Received '" + message + "'");
                     	processMsg(message, recv_time);
//                         throw  new Exception();
                         //doWork(message);
                     }catch (Exception e){
                    	 e.printStackTrace();
                         channel.abort();
                     }finally {
                         System.out.println("Worker1 Done");
                         channel.basicAck(envelope.getDeliveryTag(),false);
                     }
            		
//            		if(action == Action.ACCEPT){
//            			// 消息确认机制
//            			channel.basicAck(envelope.getDeliveryTag(), false);
//            		}
////            		else if (action == Action.RETRY) {
////						channel.basicReject(envelope.getDeliveryTag(), true);
////					}
            		
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
					ei.setKObject("cma.cimiss2.dpc.indb.surf.rd_reg.RDMultSubThread");
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
	/**
	 * 
	 * @Title: processMsg   
	 * @Description: TODO(消息处理函数)   
	 * @param  message 消息体内容
	 * @param  recv_time 消息接收时间     
	 * @return: Action  处理状态
	 * @throws:
	 */
	public static Action processMsg(String message, Date recv_time) {
		int index = message.indexOf(":");
		cts_code = message.substring(0, index);
		String filepath = message.substring(index+1);
		File file = new File(filepath);
		fileN = file.getName();
		
		
		if(file != null && file.exists() && file.isFile()){
			StringBuffer loggerBuffer = new StringBuffer();
			loggerBuffer.append(filepath);
			
			if(file.length()  > StartConfig.maxFileSize){
				infoLogger.info("\n File is too large(" + file.length() + "byte), ignore it! Filepath is: " + filepath);
				return Action.ACCEPT;
			}
			
			
			DecodeRD decodeRD = new DecodeRD();
			ParseResult<SurfaceRD> parseResult = decodeRD.DecodeFile(file);			
			if(parseResult.isSuccess()){
				List<ReportError> reportErrors = parseResult.getError();
				if(reportErrors != null && reportErrors.size() > 0) {
					for (int i = 0; i < reportErrors.size(); i++) {
						loggerBuffer.append("\n Decode failed: "+ file.getPath() +"\n because: "+reportErrors.get(i).getMessage()+":"+reportErrors.get(i).getSegment());
					} // end for
				}
				
				DataBaseAction action = null;
				String[] fnames = fileN.split("_"); 

				if (StartConfig.getDatabaseType() == 2) {
					action = OTSService.processSuccessReport(parseResult.getData(), recv_time, filepath, loggerBuffer);
					OTSService.reportInfoToDb(parseResult.getReports(), recv_time, fnames[3].split("-")[0], report_type, loggerBuffer);
				}
				else{
					action = DbService.processSuccessReport(parseResult, filepath, recv_time, fileN);
				}
				infoLogger.info(loggerBuffer.toString());
				if(action == DataBaseAction.CONNECTION_ERROR){
					System.out.println("入库成功" + action);
					return Action.RETRY;
				}else {
					return Action.ACCEPT;
				}
			}else {
				List<ReportError> reportErrors = parseResult.getError();
				if(reportErrors != null && reportErrors.size() > 0) {
					for (int i = 0; i < reportErrors.size(); i++) {
						loggerBuffer.append("\n Decode failed: "+ file.getPath() +"\n because: "+reportErrors.get(i).getMessage()+":"+reportErrors.get(i).getSegment());
					} // end for
					infoLogger.info(loggerBuffer.toString());
				}
				ParseInfo parseInfo = parseResult.getParseInfo();
				if(parseInfo != null) {
					infoLogger.error("\n Read file error: "+file.getPath()+"\n description: " +parseInfo.getDescription());
					String event_type = EIEventType.OP_FILE_ERROR.getCode();
					EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
					if(ei == null) {
						infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
					}else {
						if(StartConfig.isSendEi()) {
							ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
							ei.setKObject("cma.cimiss2.dpc.indb.surf.rd_reg.RDMultSubThread");
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
			infoLogger.error("\n File does not exist: "+file.getPath());
			
			String event_type = EIEventType.OP_FILE_ERROR.getCode();
			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
			if(ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST:"+event_type);
			}else {
				if(StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("cma.cimiss2.dpc.indb.surf.rd_reg.RDMultSubThread");
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
