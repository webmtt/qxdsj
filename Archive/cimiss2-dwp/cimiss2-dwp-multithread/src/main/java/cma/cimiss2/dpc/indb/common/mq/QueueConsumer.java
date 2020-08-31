package cma.cimiss2.dpc.indb.common.mq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

public class QueueConsumer extends EndPoint implements Consumer {
	
	public QueueConsumer(String queueName) throws TimeoutException, IOException {
		super(queueName);
		
	}
	
	public void run() {
		try {
			// start consuming messages. Auto acknowledge messages.
			channel.basicConsume(queueName, false, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public void handleConsumeOk(String consumerTag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleCancelOk(String consumerTag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleCancel(String consumerTag) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleDelivery(String consumerTag, Envelope env, BasicProperties props, byte[] body) throws IOException {
		// TODO Auto-generated method stub
		System.out.println(new String(body));
//		boolean isSuccess = processMessage(props, body);
//		if(isSuccess) {
//			channel.basicAck(env.getDeliveryTag(), true);
//		}
		
	}

	public boolean processMessage(BasicProperties props, byte[] body) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleRecoverOk(String consumerTag) {
		// TODO Auto-generated method stub

	}

}
