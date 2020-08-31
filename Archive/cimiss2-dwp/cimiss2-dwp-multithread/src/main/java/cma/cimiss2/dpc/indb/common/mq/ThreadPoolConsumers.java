package cma.cimiss2.dpc.indb.common.mq;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ThreadPoolConsumers {
	ThreadPoolExecutor threadPool;
	private final ThreadPoolConsumerBuilder threadPoolConsumerBuilder;
	public static class ThreadPoolConsumerBuilder {
		int threadCount;
		
		public int getThreadCount() {
			return threadCount;
		}

		public ThreadPoolConsumerBuilder setThreadCount(int threadCount) {
			this.threadCount = threadCount;
			return this;
		}

		public ThreadPoolConsumers build() {
	        return new ThreadPoolConsumers(this);
	    }
	}

	private ThreadPoolConsumers(ThreadPoolConsumerBuilder threadPoolConsumerBuilder) {
		this.threadPoolConsumerBuilder = threadPoolConsumerBuilder;
		LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(5);
		threadPool = new ThreadPoolExecutor(1, 5, 60, TimeUnit.SECONDS, workQueue);
	}
	
	public void start() {
		
	}

}
