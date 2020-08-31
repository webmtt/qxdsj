package cma.cimiss2.dpc.unstruct;

import com.lmax.disruptor.dsl.Disruptor;

import cma.cimiss2.dpc.unstruct.disruptor.CmaDataforRadbDisruptor;
import cma.cimiss2.dpc.unstruct.disruptor.IndexDataForFidbDisruptor;
import cma.cimiss2.dpc.unstruct.disruptor.MicapsDataForRadbDisruptor;
import cma.cimiss2.dpc.unstruct.factory.RmqMessageEventTranslator;
import cma.cimiss2.dpc.unstruct.messge.RmqMessage;
import cma.cimiss2.dpc.unstruct.messge.RmqMessageEx;

public class Test {
	
	public static void main(String[] args) {
		IndexDataForFidbDisruptor.init();
		MicapsDataForRadbDisruptor.init();
		CmaDataforRadbDisruptor.init();
		Disruptor<RmqMessageEx> disruptor = IndexDataForFidbDisruptor.getDisruptor();
//		RingBuffer<RmqMessage> ringBuffer = disruptor.getRingBuffer();
		for (int i = 0; i < 1000; i++) {
			RmqMessage rmqMessage = new RmqMessage();
			rmqMessage.setBody(("message" + i).getBytes());
			rmqMessage.setCtsCode("J.0001.0001.R001");
			disruptor.publishEvent(new RmqMessageEventTranslator(rmqMessage));
		}
		
		
	}

}
