package cma.cimiss2.dpc.unstruct.disruptor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.TimeoutBlockingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import cma.cimiss2.dpc.unstruct.factory.RadbMessageFactory;
import cma.cimiss2.dpc.unstruct.handler.CmaDataforRadbHandler;
import cma.cimiss2.dpc.unstruct.messge.RadbMessage;

public class CmaDataforRadbDisruptor {
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");

    private static Disruptor<RadbMessage> disruptor;   
    
    @SuppressWarnings("unchecked")
	public static void init() {
    	ExecutorService executor = Executors.newFixedThreadPool(5);
		/*
		 *
			名称	                             措施               	                               适用场景
			BlockingWaitStrategy	         加锁	                             cpu资源紧缺，吞吐量和延迟并不重要的场景
			BusySpinWaitStrategy	         自旋	                             通过不断重试，减少切换线程导致的系统调用，而降低延迟。推荐在线程绑定到固定的cpu的场景下使用
			PhasedBackoffWaitStrategy	     自旋 + yield + 自定义策略	             cpu资源紧缺，吞吐量和延迟并不重要的场景
			SleepingWaitStrategy	         自旋 + yield + sleep	             性能和cpu资源之间有很好的折中。延迟不均匀
			TimeoutBlockingWaitStrategy	     加锁，有超时限制	                     cpu资源紧缺，吞吐量和延迟并不重要的场景
			YieldingWaitStrategy	         自旋 + yield + 自旋	                 性能和cpu资源之间有很好的折中。延迟比较均匀
		 */
        String disruptorStrategy = "";
        WaitStrategy waitStrategy = null;
        if(disruptorStrategy.equalsIgnoreCase("BlockingWaitStrategy")){
            waitStrategy = new BlockingWaitStrategy();
        }else if(disruptorStrategy.equalsIgnoreCase("BusySpinWaitStrategy")){
            waitStrategy = new BusySpinWaitStrategy();
        }else if(disruptorStrategy.equalsIgnoreCase("PhasedBackoffWaitStrategy")){
//			waitStrategy = new PhasedBackoffWaitStrategy(0, 0, null, waitStrategy);
        }else if(disruptorStrategy.equalsIgnoreCase("SleepingWaitStrategy")){
            waitStrategy = new SleepingWaitStrategy();
        }else if(disruptorStrategy.equalsIgnoreCase("TimeoutBlockingWaitStrategy")){
            waitStrategy = new TimeoutBlockingWaitStrategy(0, TimeUnit.SECONDS);
        }else if(disruptorStrategy.equalsIgnoreCase("YieldingWaitStrategy")){
            waitStrategy = new YieldingWaitStrategy();
        }
        Disruptor<RadbMessage> disruptor = new Disruptor<>(new RadbMessageFactory(), 1024, executor,
                ProducerType.MULTI, waitStrategy);
        disruptor.handleEventsWith(new CmaDataforRadbHandler());
        disruptor.start();

        setDisruptor(disruptor);
    }

	public static Disruptor<RadbMessage> getDisruptor() {
		return disruptor;
	}

	public static void setDisruptor(Disruptor<RadbMessage> disruptor) {
		CmaDataforRadbDisruptor.disruptor = disruptor;
	}
    
    

}
