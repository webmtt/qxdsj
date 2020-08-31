package cma.cimiss2.dpc.indb.agme.dc_agme_manl_disa;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import cma.cimiss2.dpc.decoder.agme.DecodeDisa;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeDisa;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;

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

import cma.cimiss2.dpc.indb.agme.OTSReportInfoService;
import cma.cimiss2.dpc.indb.agme.dc_agme_manl_disa.service.*;

// TODO: Auto-generated Javadoc
/**
 * <br>.
 *
 * @author cuihongyuan
 * @Title:  DISAMultSubThread.java
 * @Package org.cimiss2.dwp.z_agme.disa
 * @Description:    TODO(灾害资料入库)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月26日 下午3:27:16   cuihongyuan    Initial creation.
 * </pre>
 */
public class DISAMultSubThread implements Runnable {

	/** The Constant messageLogger. */
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	
	/** The Constant infoLogger. */
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	/** The file N. */
	public static String fileN = null;
	
	/** The v bbb. */
	public static String v_bbb = "000";
	
	/** The code type. */
	public static String code_type = null;
	
	/** The is revised. */
	public static boolean isRevised = false;
	
	/** The default int. */
	public int defaultInt = 999999;
	
	/** The pro map. */
	Map<String, Object> proMap = StationInfo.getProMap();
	
	/** The cts code maps. */
	List<CTSCodeMap> ctsCodeMaps;
	
	/**
	 * Instantiates a new DISA mult sub thread.
	 *
	 * @param diQueues the di queues
	 * @param ctsCodeMaps the cts code maps
	 */
	public DISAMultSubThread(BlockingQueue<StatDi> diQueues, List<CTSCodeMap> ctsCodeMaps) {
		this.ctsCodeMaps = ctsCodeMaps;
		DbService.diQueues = diQueues;
		OTSService.diQueues = diQueues;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
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
           
            final Channel channel = connection.createChannel();
            
            channel.queueDeclare(rabbitMQConfig.getQueueName(), true, false, false, null);
            channel.basicQos(10);
            Consumer consumer = new DefaultConsumer(channel) {
            	@Override
            	public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
            			byte[] body) throws IOException {
            		Date recv_time = new Date();
            		String message = new String(body, "UTF-8");
            		StringBuffer loggerBuffer = new StringBuffer();
            		
            		messageLogger.info("message:"+message);
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
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST："+event_type);
			}else {
				if(StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("cma.cimiss2.dpc.indb.agme.disa.DISAMultSubThread");
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
			infoLogger.error("rabbitMQ Create Connection Pool Failed： " +e.getMessage());
			
			System.exit(0);
		}
	}
	
	/**
	 * Process msg.
	 *
	 * @param message the message
	 * @param recv_time the recv time
	 * @param loggerBuffer the logger buffer
	 * @return the action
	 * @Title: processMsg
	 * @Description: TODO(消息处理过程)
	 * @param: @param message
	 * @param: @param recv_time
	 * @param: @return
	 * @return: Action
	 * @throws: 
	 */
	private Action processMsg(String message, Date recv_time, StringBuffer loggerBuffer) {
		int index = message.indexOf(":");
		String cts_code = message.substring(0, index);
		code_type = cts_code;
		String filepath = message.substring(index+1);
		File file = new File(filepath);
		fileN = file.getName();
		
		if(fileN.contains("-CC")){
			isRevised = true;
			v_bbb = fileN.substring(fileN.indexOf("-CC") + 1, fileN.indexOf("-CC") + 4);
		}
		else{
			isRevised = false;
			v_bbb = "000";
		}
		
		if(file != null && file.exists() && file.isFile()) {
			
			if(file.length()  > StartConfig.maxFileSize()){
				infoLogger.info("\n File is too large(" + file.length() + "byte), ignore it! Filepath is: " + filepath);
				return Action.ACCEPT;
			}
			recv_time.setTime(file.lastModified());
			DecodeDisa decodeDisa = new DecodeDisa();
			ParseResult<ZAgmeDisa> parseResult = decodeDisa.decodeFile(file);
			if(parseResult.isSuccess()) {
				List<ReportError> reportErrors = parseResult.getError();
				if(reportErrors != null && reportErrors.size() > 0) {
					for (int i = 0; i < reportErrors.size(); i++) {
						infoLogger.error(" ERROR REPORT : "+ file.getPath() + "\n" + reportErrors.get(i).getSegment()+"\n"+"\t " + reportErrors.get(i).getMessage() + "\n");
						}
				}
				//DataBaseAction action = DbService.processSuccessReport(parseResult, filepath, recv_time, fileN,isRevised,v_bbb);
				DataBaseAction action;
				loggerBuffer.append(filepath);
				if(StartConfig.getDatabaseType() == 2) {
					action = OTSService.processSuccessReport(parseResult, recv_time,fileN, isRevised,v_bbb,loggerBuffer,ctsCodeMaps);
					String[] fnames = fileN.split("_");
					List<ReportInfo> reportInfos = parseResult.getReports();
					OTSReportInfoService.reportInfoToDb(reportInfos, v_bbb, recv_time, fnames[3], fnames[1],loggerBuffer,ctsCodeMaps);
				}else {
					action = DbService.processSuccessReport(parseResult, recv_time, filepath,v_bbb,isRevised,loggerBuffer,ctsCodeMaps);
				}
				infoLogger.info(loggerBuffer.toString());
				if(action == DataBaseAction.CONNECTION_ERROR){
					return Action.RETRY;
				}else {
					return Action.ACCEPT;
				}
			}else {
				List<ReportError> reportErrors = parseResult.getError();
				if(reportErrors != null && reportErrors.size() > 0) {
					for (int i = 0; i < reportErrors.size(); i++) {
						infoLogger.error(" ERROR REPORT : "+ file.getPath()+ "\n" +reportErrors.get(i).getSegment()+"\n"+"\t " + reportErrors.get(i).getMessage() + "\n");
					}
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
							ei.setKObject("cma.cimiss2.dpc.indb.agme.disa.DISAMultSubThread");
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
		}
		else {
			infoLogger.error(" ERROR : not find file " + file.getPath() +"\n");
			String event_type = EIEventType.OP_FILE_ERROR.getCode();
			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
			if(ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST："+event_type);
			}else {
				if(StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("cma.cimiss2.dpc.indb.agme.disa.DISAMultSubThread");
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
