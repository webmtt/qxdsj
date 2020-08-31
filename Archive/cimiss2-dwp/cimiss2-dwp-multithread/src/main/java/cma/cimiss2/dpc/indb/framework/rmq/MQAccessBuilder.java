package cma.cimiss2.dpc.indb.framework.rmq;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

import org.cimiss2.dwp.tools.config.StartConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

import cma.cimiss2.dpc.indb.framework.di.DISubThread;
import cma.cimiss2.dpc.indb.framework.rmq.common.DetailRes;
import cma.cimiss2.dpc.indb.framework.rmq.common.MessageBean;
import cma.cimiss2.dpc.indb.framework.rmq.common.RabbitMQConf;

public class MQAccessBuilder {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private ConnectionFactory connectionFactory;
	private BlockingQueue<Object> queue = new LinkedBlockingQueue<Object>();
	
	public MQAccessBuilder(String config) {
		this.connectionFactory = RabbitMQConf.getRabbitMQConf(config).getConnectionFactory();
    }
	
	
	public MessageConsumer buildMessageConsumer(String exchange, String routingKey, final String queue,
            final MessageProcess messageProcess, String type) throws IOException, TimeoutException {
		final Connection connection = connectionFactory.newConnection();
		//1
        buildQueue(exchange, routingKey, queue, connection, type);
        
        return new MessageConsumer() {
			Channel channel;
			{
				channel = connection.createChannel();
			}
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void consume() {
//				if(StartConfig.isSendDi()) {
//        			// 启动固定线程池
//    		        ExecutorService service = Executors.newFixedThreadPool(StartConfig.getDiThreadCount());        			
//        			// 修改为线程数
//        			for (int i = 0; i < StartConfig.getDiThreadCount(); i++) {
//        		        service.execute(new DISubThread(MQAccessBuilder.this.queue));
//					}
//        		}
				
				 // 定义消费者
	            Consumer consumer = new DefaultConsumer(channel) {
	            	@Override
	            	public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
	            			byte[] body) throws IOException {
	            		
	            		
	            		DetailRes<?> detailRes = messageProcess.process(new MessageBean(body, properties));
	            		
	            		if(StartConfig.isSendDi()) {
	            			for (Object obj : detailRes.getQueue()) {
								MQAccessBuilder.this.queue.offer(obj);
							}
	            		}else {
							detailRes.getQueue().clear();
						}
	            		
	            		//4
	                    if (detailRes.isSuccess()) {
	                        channel.basicAck(envelope.getDeliveryTag(), false);
	                    } else {
	                       
	                    	infoLogger.info("process message failed: " + detailRes.getErrMsg());
	                        channel.basicNack(envelope.getDeliveryTag(), false, true);
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

		try {
			channel.close();
		} catch (TimeoutException e) {
			infoLogger.info("close channel time out ", e);
		}
	}
}
