package cma.cimiss2.dpc.indb.agme.dc_agme_asmm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

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

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

import cma.cimiss2.dpc.decoder.agme.DecodeASM;
import cma.cimiss2.dpc.decoder.bean.*;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.cawn.ZAgmeASM;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.agme.dc_agme_asmm.service.*;
/**
 * -------------------------------------------------------------------------------
 * <br>
 * @Title:  ASM_MultSubThread.java   
 * @Package org.cimiss2.dwp.z_agme.asm   
 * @Description:    TODO(自动土壤水分观测数据)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年1月19日 上午8:30:11   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *---------------------------------------------------------------------------------
 */
public class ASMMultSubThread implements Runnable{
	
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static String fileN = null;
	public static boolean isRevised = false;
	public static String v_bbb = "000";
	List<CTSCodeMap> ctsCodeMaps;
	Map<String, Object> proMap = StationInfo.getProMap();	
	/**
	 * @param ctsCodeMaps 
	 * @Title:  ASM_MultSubThread   
	 * @Description:    TODO(这里用一句话描述这个方法的作用)   
	 * @param:  @param diQueues  
	 * @throws
	 */
	public ASMMultSubThread(BlockingQueue<StatDi> diQueues, ArrayList<CTSCodeMap> ctsCodeMaps) {
		this.ctsCodeMaps = ctsCodeMaps;
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
            		Date recv_time = new Date();
            		String message = new String(body, "UTF-8");
					// 获取消息体
            		messageLogger.info(message + "\n");
            		// 消息处理
            		Action action  = processMsg(message, recv_time);
            		
//            		try {
//                     	System.out.println("Worker1  Received '" + message + "'");
//                     	processMsg(message, recv_time);
////                         throw  new Exception();
//                         //doWork(message);
//                     }catch (Exception e){
//                    	 e.printStackTrace();
//                         channel.abort();
//                     }finally {
//                         System.out.println("Worker1 Done");
//                         channel.basicAck(envelope.getDeliveryTag(),false);
//                     }
            		
            		if(action == Action.ACCEPT){
            			// 消息确认消费并将消息从消息队列中删除
            			channel.basicAck(envelope.getDeliveryTag(), false);
            		}else {
            			// 被拒绝的消息重新放回消息队列
						channel.basicNack(envelope.getDeliveryTag(), false, true);
					}
            	}            	
            };
            // 绑定消费者
            channel.basicConsume(rabbitMQConfig.getQueueName(), false, consumer);
		} catch (Exception e) {
			String event_type = EIEventType.RABBIT_CONNECT_ERROR.getCode();
			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
			if(ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST："+event_type);
			}else {
				if(StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("cma.cimiss2.dpc.indb.agme.asm.ASMMultSubThread");
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
			infoLogger.error("\n rabbitMQ Create Connection Pool Failed： " +e.getMessage());
			
			System.exit(0);
		}
	}
	/**
	 * 
	 * @Title: processMsg   
	 * @Description: TODO(消息处理函数)   
	 * @param: message  消息体内容
	 * @param: recv_time  消息接收时间
	 * @param:     
	 * @return: Action 处理状态    s
	 * @throws
	 */
	
	private Action processMsg(String message, Date recv_time) {
		
		if(message.length() < 16 ) {
			return Action.ACCEPT;
		}
		int index = message.indexOf(":");
		if(index < 15 || message.length() < 16) {
			return Action.ACCEPT;
		}
		String cts_code = message.substring(0, index);
		// 获取消息中的文件路径
		String filepath = message.substring(index + 1);
		File file = new File(filepath);
		fileN = file.getName();
		// 判断文件是否为  更正报文
		if(fileN.contains("-CC")){
			isRevised = true;
			v_bbb = fileN.substring(fileN.indexOf("-CC") + 1, fileN.indexOf("-CC") + 4);
		}
		else{
			isRevised = false;
			v_bbb = "000";
		}
		StringBuffer loggerBuffer = new StringBuffer();
		loggerBuffer.append(filepath + "\n");
		// 判断文件是否存在
		if(file != null && file.exists() && file.isFile()){

			if(file.length()  > StartConfig.maxFileSize()){
				infoLogger.info("\n File is too large(" + file.length() + "byte), ignore it! Filepath is: " + filepath);
				return Action.ACCEPT;
			}
			
			recv_time = new Date(file.lastModified());
			DecodeASM decodeAsm = new DecodeASM();
			// 解析文件  返回结果集
			ParseResult<ZAgmeASM> parseResult = decodeAsm.decodeFile(file);
			if(parseResult.isSuccess()){
				List<ReportError> reportErrors = parseResult.getError();
				if(reportErrors != null && reportErrors.size() > 0) {
					for (int i = 0; i < reportErrors.size(); i++) {
						loggerBuffer.append(" ERROR REPORT : "+reportErrors.get(i).getSegment()+"\n"+"\t " + reportErrors.get(i).getMessage() + "\n");
					}
				}//end for
				
				
				DataBaseAction action;
				
				if(StartConfig.getDatabaseType() == 2) {
					action = OTSService.processSuccessReport(parseResult.getData(), recv_time,cts_code, fileN, isRevised,v_bbb, loggerBuffer,ctsCodeMaps);
					String[] fnames = fileN.split("_");
					List<ReportInfo> reportInfos = parseResult.getReports();
					OTSService.reportInfoToDb(reportInfos, v_bbb, recv_time, fnames[3], fnames[1],loggerBuffer,ctsCodeMaps);
					//OTSReportInfoService.reportInfoToDb(reportInfos, v_bbb, recv_time, fnames[3], fnames[1],loggerBuffer,ctsCodeMaps);
				}else {
					action = DbService.processSuccessReport(parseResult, recv_time,cts_code, filepath, isRevised,v_bbb, loggerBuffer,ctsCodeMaps );
				}
				
				infoLogger.info(loggerBuffer.toString());
				
				// 数据库连接错误
				if(action == DataBaseAction.CONNECTION_ERROR){					
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
					infoLogger.info(loggerBuffer.toString());
				}//end for
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
							ei.setKObject("cma.cimiss2.dpc.indb.agme.asm.ASMMultSubThread");
							ei.setKEvent("解码入库源文件异常");
							ei.setKIndex("详细信息：["+parseInfo.getDescription()+"]"+file.getPath());
							ei.setEVENT_EXT1(fileN);
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
				}			
				return Action.ACCEPT;
			}	
			
		}else {
			infoLogger.error(" ERROR : not find file " + file.getPath() +"\n");
			
			String event_type = EIEventType.OP_FILE_ERROR.getCode();
			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
			if(ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST："+event_type);
			}else {
				if(StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("cma.cimiss2.dpc.indb.agme.asm.ASMMultSubThread");
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
