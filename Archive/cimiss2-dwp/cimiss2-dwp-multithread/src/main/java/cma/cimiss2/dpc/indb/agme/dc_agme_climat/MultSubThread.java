package cma.cimiss2.dpc.indb.agme.dc_agme_climat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
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

import cma.cimiss2.dpc.decoder.agme.DecodeAgmeCli;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.agme.CroplandMicroclimateData;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.agme.dc_agme_climat.service.DbService;
import cma.cimiss2.dpc.indb.agme.dc_agme_climat.service.OTSService;


/**
 * -------------------------------------------------------------------------------
 * <br>
 * @Title:  MultSubThread.java   
 * @Package org.cimiss2.dwp.z_agme.cli   
 * @Description:    TODO(农田小气候数据解码入库线程)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年1月18日 下午5:34:08   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *---------------------------------------------------------------------------------
 */
public class MultSubThread implements Runnable{
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	// 与DI发送线程 共享对列
	
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static String fileN = null;
	/**
	 * @Title:  MultSubThread   
	 * @Description:    TODO(构造函数)   
	 * @param: diQueues  共享内存对列
	 * @throws
	 */
	
	public MultSubThread(BlockingQueue<StatDi> diQueues) {
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
            channel.basicQos(10);
            // 定义消费者
            Consumer consumer = new DefaultConsumer(channel) {
            	@Override
            	public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
            			byte[] body) throws IOException {
            		String message = new String(body, "UTF-8");
            		StringBuffer loggerBuffer = new StringBuffer();
					// 获取消息体
            		messageLogger.info(message + "\n");
            		// 消息处理
            		Action action  = processMsg(message, loggerBuffer);
            		
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
				infoLogger.error("\n EI配置文件中没有事件类型："+event_type);
			}else {
				if(StartConfig.isSendEi() && Integer.parseInt(ei.getEVENT_LEVEL()) > 0) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("org.cimiss2.dwp.z_agme.cli.MultSubThread");
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
			infoLogger.error("\n RabbitMQ create connection error： " +e.getMessage());
			
			System.exit(0);
		}
	}
	
	/** 
	 * @Title: processMsg 消息处理函数
	 * @Description: TODO(消息处理，获取文件路径，对文件解码) 
	 * @param message 消息体内容
	 * @param recv_time  消息接收的时间
	 * @param loggerBuffer
	 * @return  
	 *    Action  消息处理状态
	 * @throws
	 */
	private Action processMsg(String message, StringBuffer loggerBuffer) {
		//
		int index = message.indexOf(":");
		if(index < 0){
			loggerBuffer.append("message is empty! continue......" + "\n");
			return Action.ACCEPT; 
		}
		String cts_code = message.substring(0,index);
		// 定义解码类对象
		DecodeAgmeCli decodeCLI = new DecodeAgmeCli();
		// 初始化文件
		File file = new File(message.substring(index+1, message.length()));
		String filepath=message.substring(index+1, message.length());
		Date recv_time = new Date(file.lastModified());
		fileN = file.getName();
		loggerBuffer.append(" : " + simpleDateFormat.format(recv_time) +" "+ file.getPath() + "\n");
		// 判断文件是否有效和文件是否存在
		if(file != null && file.exists() && file.isFile()){
			if(file.length()  > StartConfig.maxFileSize()){
				infoLogger.info("\n File is too large(" + file.length() + "byte), ignore it! Filepath is: " + filepath);
				return Action.ACCEPT;
			}
			// 获取解码之后的结果集
			ParseResult<CroplandMicroclimateData> parseResult = decodeCLI.decodeFile(file);
			loggerBuffer.append("decode finish time : "+ simpleDateFormat.format(new Date()) +"\n");
			// 判断解码是否成功，如果成功 则结果集中有正确的报文，否则吴正确报文
			if(parseResult.isSuccess()){
				// 获取错误的报文结果集
				List<ReportError> reportErrors = parseResult.getError();
				if(reportErrors != null && reportErrors.size() > 0) {
					for (int i = 0; i < reportErrors.size(); i++) {
						loggerBuffer.append(" ERROR REPORT : "+reportErrors.get(i).getSegment()+"\n"+"\t " + reportErrors.get(i).getMessage() + "\n");
					}
				}
				
				// 获取正确报文的解码对象集合
				List<CroplandMicroclimateData> list = parseResult.getData();
				DataBaseAction dataBaseAction = null;
				if(StartConfig.getDatabaseType() == 1 || StartConfig.getDatabaseType() == 0) {
					dataBaseAction = DbService.insert_db(list, recv_time, loggerBuffer, cts_code, filepath);
				}else if (StartConfig.getDatabaseType() == 2) {
					dataBaseAction = OTSService.insert_ots(list, "AGME_MICLI_CHN_TAB", recv_time, loggerBuffer, cts_code, fileN);
				}else {
					return Action.RETRY;
				}
				
				infoLogger.info(loggerBuffer.toString());
				// 数据库连接错误
				if(dataBaseAction == DataBaseAction.CONNECTION_ERROR){					
					return Action.RETRY;
				// 批量提交错误
				}else {	
					return Action.ACCEPT;
				}					
				
			}else {
				List<ReportError> reportErrors = parseResult.getError();
				if(reportErrors != null && reportErrors.size() > 0) {
					for (int i = 0; i < reportErrors.size(); i++) {
						loggerBuffer.append(" ERROR REPORT : "+reportErrors.get(i).getSegment()+"\n"+"\t " + reportErrors.get(i).getMessage() + "\n");
					}
				}
				ParseInfo parseInfo = parseResult.getParseInfo();
				if(parseInfo != null) {
					infoLogger.error("\n read file error ："+file.getPath()+"\n 错误描述:"+parseInfo.getDescription());
					
					String event_type = EIEventType.OP_FILE_ERROR.getCode();
					EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
					if(ei == null) {
						infoLogger.error("\n EI配置文件中没有事件类型："+event_type);
					}else {
						if(StartConfig.isSendEi() && Integer.parseInt(ei.getEVENT_LEVEL()) > 0 ) {
							ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
							ei.setKObject("org.cimiss2.dwp.z_agme.cli.MultSubThread");
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
				infoLogger.error("\n EI配置文件中没有事件类型："+event_type);
			}else {
				if(StartConfig.isSendEi() && Integer.parseInt(ei.getEVENT_LEVEL()) > 0) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("org.cimiss2.dwp.z_agme.cli.MultSubThread");
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
