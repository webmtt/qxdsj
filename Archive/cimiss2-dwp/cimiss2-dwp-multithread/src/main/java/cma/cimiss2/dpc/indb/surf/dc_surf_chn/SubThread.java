package cma.cimiss2.dpc.indb.surf.dc_surf_chn;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.RabbitMQConfig;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hitec.bufr.util.FileUtil;
import com.hitec.bufr.util.StringUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.common.BufrConfig;
import cma.cimiss2.dpc.indb.surf.dc_surf_chn.service.BufrServiceImpl4;

import com.rabbitmq.client.AMQP.BasicProperties;

// TODO: Auto-generated Javadoc
/**
 * The Class SubThread.
 */
public class SubThread implements Runnable {
	
	/** The Constant messageLogger. */
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	
	/** The Constant infoLogger. */
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	/** The table section. */
	private String tableSection;
	
	/**
	 * Instantiates a new sub thread.
	 *
	 * @param tableSection the table section
	 * @param diQueues the di queues
	 */
	public SubThread(String tableSection, BlockingQueue<StatDi> diQueues) {
		this.tableSection = tableSection;
		BufrServiceImpl4.setDiQueues(diQueues);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
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
            // 声明消费对列，如果队列不存在，则自动创建
            channel.queueDeclare(rabbitMQConfig.getQueueName(), true, false, false, null);
            channel.basicQos(5);
            // 定义消费者
            Consumer consumer = new DefaultConsumer(channel) {
				public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
					SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String msg = new String(body, "ISO-8859-1");
					Date recv = new Date();
					String CCCC = "";
					String fileName = "";
					// 消息头
					Map<String, Object> map = properties.getHeaders();
					if(map != null){
						fileName = map.get("FileName").toString();
						String SendTime = map.get("SendTime").toString();
						CCCC = map.get("CCCC").toString();
						
						if(SendTime != null){
							int idx = SendTime.lastIndexOf(".");
							if(idx > -1){ // 精确到秒
								SendTime = SendTime.substring(0, idx);  
							}
							try{
								if(SendTime.length() == 14)
									recv = dateFormat.parse(SendTime);
								else if(SendTime.length() == 19)
									recv = dateFormat2.parse(SendTime);
							}catch (Exception e) {
								try {
									recv = dateFormat2.parse(SendTime);
								} catch (ParseException e1) {
									infoLogger.error("文件"+ fileName + "的 SendTime 异常" + SendTime + "使用当前系统时间: " + TimeUtil.date2String(recv, "yyyy-MM-dd HH:mm:ss"));
								}
							}
						}
					}
						
					int length = msg.indexOf(";");
					if(length <= -1)
						length = -1;
					String msgbody = msg.substring(length + 1);
					byte[] messageBytes = msgbody.getBytes("ISO-8859-1");
					if(StartConfig.isWriteFile() && !fileName.isEmpty()) {
						String filePath = StringUtil.replaceFileNameExp(fileName, StartConfig.writeFilePath()) + fileName;
						infoLogger.info("Write File: " + filePath);
						FileUtil.writeBytesToFile(messageBytes, filePath);
						messageLogger.info(filePath + "\n");
					}
					
					List<String> tableList = new ArrayList<String>();
					tableList = BufrConfig.getTableMap().get(tableSection);
					boolean decode = false;
					try{
						if(StartConfig.getDatabaseType() == 0 || StartConfig.getDatabaseType() == 1){
							BufrServiceImpl4 bufrServiceImpl4 = new BufrServiceImpl4();
							decode = bufrServiceImpl4.decode(recv, fileName,  messageBytes, tableSection, tableList);
						}
						else {
							//入库 OTS 
						}
					}catch (Exception e) {
						e.printStackTrace();
						channel.abort();
					}
					finally {
						if (decode) {
							// 入库成功确认消息
							infoLogger.info("ack message : " + fileName);
							channel.basicAck(envelope.getDeliveryTag(), false);
						} else {
							// 入库失败，消息重新排队
							infoLogger.info("入库失败,重新排队 : " + fileName);
							channel.basicAck(envelope.getDeliveryTag(), true);
						}
					}
				}
			};
			// 绑定消费者
			channel.basicConsume(rabbitMQConfig.getQueueName(), false, consumer);
		} catch (Exception e) {
			String event_type = EIEventType.RABBIT_CONNECT_ERROR.getCode();
			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
			if(ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
			}else {
				if(StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("cma.cimiss2.dpc.indb.surf.dc_surf_chn.SubThread");
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
			infoLogger.error("\n rabbitMQ create connection failed: " +e.getMessage());
			System.exit(0);
		}
	}
}
