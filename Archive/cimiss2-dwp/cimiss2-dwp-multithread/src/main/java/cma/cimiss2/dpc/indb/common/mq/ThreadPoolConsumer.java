package cma.cimiss2.dpc.indb.common.mq;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;


public class ThreadPoolConsumer {
	private ExecutorService executor;
	private volatile boolean stop = false;
	private final ThreadPoolConsumerBuilder threadPoolConsumerBuilder;
	//构造器
    public static class ThreadPoolConsumerBuilder{
    	int threadCount;
        long intervalMils;
        String queueName;
		public int getThreadCount() {
			return threadCount;
		}
		public ThreadPoolConsumerBuilder setThreadCount(int threadCount) {
			this.threadCount = threadCount;
			return this;
		}
		public long getIntervalMils() {
			return intervalMils;
		}
		public ThreadPoolConsumerBuilder setIntervalMils(long intervalMils) {
			this.intervalMils = intervalMils;
			return this;
		}
		public String getQueueName() {
			return queueName;
		}
		public ThreadPoolConsumerBuilder setQueueName(String queueName) {
			this.queueName = queueName;
			return this;
		}
		
		public ThreadPoolConsumer build() {
	        return new ThreadPoolConsumer(this);
	    }
		
        
    }
    
    
    
    private ThreadPoolConsumer(ThreadPoolConsumerBuilder threadPoolConsumerBuilder) {
    	this.threadPoolConsumerBuilder = threadPoolConsumerBuilder;
    	executor = Executors.newFixedThreadPool(threadPoolConsumerBuilder.threadCount);
    }
    
    public void start() throws TimeoutException, IOException {
    	for (int i = 0; i < this.threadPoolConsumerBuilder.threadCount; i++) {
    		QueueConsumer queueConsumer = new QueueConsumer(this.threadPoolConsumerBuilder.queueName);
    		executor.execute(new Runnable() {
				
				@Override
				public void run() {
					while (!stop) {
						
						queueConsumer.run();
					}
					
				}
			});
		}
    	
    	Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }
    
    public void stop() {
    	this.stop = true;
    	try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
