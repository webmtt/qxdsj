package cma.cimiss2.dpc.indb.ocen.dc_ocen_CMan_gdas;

import java.io.IOException;
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
import cma.cimiss2.dpc.indb.ocen.dc_ocen_CMan_gdas.service.CManServiceImpl;

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
		CManServiceImpl.setDiQueues(diQueues);
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
					Map<String, Object> queueHeaders = properties.getHeaders();
					String msg = new String(body, "UTF-8");
					messageLogger.info(msg + "\n");

					int length = msg.indexOf(":");
					msg = msg.substring(length + 1);
					List<String> tableList = new ArrayList<String>();
					tableList = BufrConfig.getTableMap().get(tableSection);
					// modified by chy 2018-09-25 消息是文件路径还是数据流的形式 的区别
					String fileName = msg;
					if(queueHeaders != null){
						if(queueHeaders.get("FileName")!=null)
						fileName = queueHeaders.get("FileName").toString();
					}
					
//					boolean decode = bufrServiceImpl.decode(fileName, msg.getBytes("ISO-8859-1"), tableSection, tableList);
					boolean decode = false;
					try{
						if(StartConfig.getDatabaseType() == 0 || StartConfig.getDatabaseType() == 1){
							CManServiceImpl oceanServiceImpl = new CManServiceImpl();
							decode = oceanServiceImpl.decode(fileName, tableSection, tableList);
						}
						else {

						}
					}catch(Exception e){
						e.printStackTrace();
						channel.abort();
					}finally{
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
					ei.setKObject("cma.cimiss2.dpc.indb.ocen.dc_ocen_CMan_gads.SubThread");
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
}
