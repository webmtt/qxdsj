package cma.cimiss2.dpc.fileloop.rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

import cma.cimiss2.dpc.fileloop.rabbitmq.coon.BaseConnection;

public class QueueConsumer extends BaseConnection implements Consumer {

	public QueueConsumer(String queueName) throws TimeoutException, IOException {
		super(queueName);
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
