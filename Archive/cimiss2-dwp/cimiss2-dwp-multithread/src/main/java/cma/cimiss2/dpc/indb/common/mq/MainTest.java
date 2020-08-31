package cma.cimiss2.dpc.indb.common.mq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MainTest {
	private static ThreadPoolConsumer threadPoolConsumer;
	public static void main(String[] args) throws TimeoutException, IOException {
		threadPoolConsumer = new ThreadPoolConsumer.ThreadPoolConsumerBuilder().setThreadCount(5).setIntervalMils(100).setQueueName("SURF_ORI_A.0001.0005.R001_001").build();
		threadPoolConsumer.start();
	}

}
