package cma.cimiss2.dpc.unstruct.handler;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.Disruptor;

import cma.cimiss2.dpc.unstruct.disruptor.CmaDataforRadbDisruptor;
import cma.cimiss2.dpc.unstruct.disruptor.IndexDataForFidbDisruptor;
import cma.cimiss2.dpc.unstruct.disruptor.MicapsDataForRadbDisruptor;
import cma.cimiss2.dpc.unstruct.factory.RadbMessageEventTranslator;
import cma.cimiss2.dpc.unstruct.messge.RadbMessage;
import cma.cimiss2.dpc.unstruct.messge.RmqMessage;
import cma.cimiss2.dpc.unstruct.messge.RmqMessageEx;


public class IndexDataHandler implements EventHandler<RmqMessageEx>{
	
	@Override
	public void onEvent(RmqMessageEx event, long sequence, boolean endOfBatch) throws Exception {
		
		System.out.println(new String(event.getRmqMessage().getBody()) + ">>>>" + Thread.currentThread().getName());
		Disruptor<RadbMessage> disruptor = CmaDataforRadbDisruptor.getDisruptor();
		disruptor.publishEvent(new RadbMessageEventTranslator(event));
//		MicapsDataForRadbDisruptor.getDisruptor().publishEvent(new RadbMessageEventTranslator(event));
	}

}
