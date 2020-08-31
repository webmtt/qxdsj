package cma.cimiss2.dpc.indb.framework.rmq;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import org.cimiss2.dwp.tools.config.StartConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class ThreadPoolConsumer {
	
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private ExecutorService executor;
	private final ThreadPoolConsumerBuilder infoHolder;

	// 构造器
	public static class ThreadPoolConsumerBuilder{
		// 消费者个数
		private int consumerCount;
		MQAccessBuilder mqAccessBuilder;
		// RabbitMQ交换机名称
		private String exchange;
		// RabbitMQ 路由名称
		private String routingKey;
		// RabbitMQ 对列名称
		private String queue;
		String type = "direct";
		MessageProcess messageProcess;

		private ThreadPoolConsumerBuilder setThreadCount(int consumerCount) {
			this.consumerCount = consumerCount;
			return this;
		}

		private ThreadPoolConsumerBuilder setMQAccessBuilder(MQAccessBuilder mqAccessBuilder) {
			this.mqAccessBuilder = mqAccessBuilder;

			return this;
		}

		private ThreadPoolConsumerBuilder setExchange(String exchange) {
			this.exchange = exchange;

			return this;
		}

		private ThreadPoolConsumerBuilder setRoutingKey(String routingKey) {
			this.routingKey = routingKey;

			return this;
		}

		private ThreadPoolConsumerBuilder setQueue(String queue) {
			this.queue = queue;

			return this;
		}

		private ThreadPoolConsumerBuilder setType(String type) {
			this.type = type;

			return this;
		}

		public ThreadPoolConsumerBuilder setMessageProcess(MessageProcess messageProcess) {
			this.messageProcess = messageProcess;

			return this;
		}

		public ThreadPoolConsumer build() {
			MQAccessBuilder mqAccessBuilder = new MQAccessBuilder(StartConfig.configFile);

			this.setThreadCount(StartConfig.getThreadCount());
			if(StartConfig.getQueueName() != null) {
				this.setQueue(StartConfig.getQueueName());
			}else {
				infoLogger.error(" RabbitMQ QueueName not find in config ");
			}
			
			if(StartConfig.getExChange() != null) {
				this.setExchange(StartConfig.getExChange());
			}else {
				infoLogger.error(" RabbitMQ ExChange not find in config ");
			}
			
			if(StartConfig.getRoutingKey() != null) {
				this.setRoutingKey(StartConfig.getRoutingKey());
			}else {
				infoLogger.error(" RabbitMQ RoutingKey not find in config ");
			}
			
			this.setType("topic");
			
			this.setMQAccessBuilder(mqAccessBuilder);
			return new ThreadPoolConsumer(this);
		}

	}

	private ThreadPoolConsumer(ThreadPoolConsumerBuilder threadPoolConsumerBuilder) {
		this.infoHolder = threadPoolConsumerBuilder;
		executor = Executors.newFixedThreadPool(threadPoolConsumerBuilder.consumerCount);
	}

	// 1 构造messageConsumer
	// 2 执行consume
	public void start() throws IOException, TimeoutException {
		for (int i = 0; i < infoHolder.consumerCount; i++) {
			// 1
			MessageConsumer messageConsumer = infoHolder.mqAccessBuilder.buildMessageConsumer(infoHolder.exchange,
					infoHolder.routingKey, infoHolder.queue, infoHolder.messageProcess, infoHolder.type);
		

			executor.execute(new Runnable() {
				@Override
				public void run() {

					messageConsumer.consume();

				}
			});
		}
	}

	

}
