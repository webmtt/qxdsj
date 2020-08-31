package cma.cimiss2.dpc.unstruct.rmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.rabbitmq.client.AMQP.BasicProperties;
import cma.cimiss2.dpc.indb.framework.rmq.common.RabbitMQConf;
import cma.cimiss2.dpc.unstruct.common.MessageUtil;
import cma.cimiss2.dpc.unstruct.disruptor.IndexDataForFidbDisruptor;
import cma.cimiss2.dpc.unstruct.factory.RmqMessageEventTranslator;
import cma.cimiss2.dpc.unstruct.messge.RmqMessage;
import cma.cimiss2.dpc.unstruct.messge.RmqMessageEx;

/**
* *******************************************************************************************<br>
* <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
* *******************************************************************************************<br>
* <b>Description:</b><br> 
* @author wuzuoqiang 
* @version 1.0
* @Note
* <b>ProjectName:</b> cimiss2-dwp-multithread
* <br><b>PackageName:</b> cma.cimiss2.dpc.unstruct.rmq
* <br><b>ClassName:</b> MQAccessBuilder
* <br><b>Date:</b> 2020年4月5日 下午7:22:03
 */
public class MQAccessBuilder {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private ConnectionFactory connectionFactory;	
	public MQAccessBuilder(String config) {
		this.connectionFactory = RabbitMQConf.getRabbitMQConf(config).getConnectionFactory();
    }
	
	
	public MessageConsumer buildMessageConsumer(String exchange, String routingKey, final String queue, String type) throws IOException, TimeoutException {
		final Connection connection = connectionFactory.newConnection();
		infoLogger.info("RabbitMQ connection success, host :" +this.connectionFactory.getHost() );
		//1
        buildQueue(exchange, routingKey, queue, connection, type);
        
        return new MessageConsumer() {
			Channel channel;
			{
				channel = connection.createChannel();
			}
			@Override
			public void consume() {

				 // 定义消费者
	            Consumer consumer = new DefaultConsumer(channel) {
	            	@Override
	            	public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
	            			byte[] body) throws IOException {
	            		
	            		// 消息体格式检查
	            		if(MessageUtil.messageCheck(body)) {
	            			String message = new String(body,"UTF-8");
	            			Disruptor<RmqMessageEx> disruptor = IndexDataForFidbDisruptor.getDisruptor();
		            		RmqMessage rmqMessage = new RmqMessage();
		            		rmqMessage.setCtsCode(message.substring(0, 16));
		            		rmqMessage.setFilePath(message.substring(16+1));
			            	rmqMessage.setBody(body);
		            		rmqMessage.setConsumerTag(consumerTag);
		            		rmqMessage.setEnvelope(envelope);
		            		rmqMessage.setProperties(properties);
		            		rmqMessage.setChannel(channel);

		        			disruptor.publishEvent(new RmqMessageEventTranslator(rmqMessage));
	            		}else {
							// 错误格式的消息，直接确认，记录日志文件
	            			channel.basicAck(envelope.getDeliveryTag(), false);
						}

	            	}
	            };
	            
	            try {
					channel.basicConsume(queue, false, consumer);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					infoLogger.info("channel.basicConsume(queue, false, consumer)", e);
					
				}
			}
		};
		
	}


	private void buildQueue(String exchange, String routingKey, final String queue, Connection connection, String type)
			throws IOException {
		Channel channel = connection.createChannel();

		if (type.equals("direct")) {
			channel.exchangeDeclare(exchange, "direct", true, false, null);
		} else if (type.equals("topic")) {
			channel.exchangeDeclare(exchange, "topic", true, false, null);
		}

		channel.queueDeclare(queue, true, false, false, null);
		channel.queueBind(queue, exchange, routingKey);
		infoLogger.info("channel.queueBind success : exchange" + exchange+"&routingKey"+routingKey+"&queue"+queue+"&type"+type);
		try {
			channel.close();
		} catch (TimeoutException e) {
			infoLogger.info("close channel time out ", e);
		}
	}
}
