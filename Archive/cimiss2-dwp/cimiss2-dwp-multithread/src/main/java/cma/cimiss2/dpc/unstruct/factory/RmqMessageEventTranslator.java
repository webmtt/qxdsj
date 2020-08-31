package cma.cimiss2.dpc.unstruct.factory;

import com.lmax.disruptor.EventTranslator;

import cma.cimiss2.dpc.unstruct.messge.RmqMessage;
import cma.cimiss2.dpc.unstruct.messge.RmqMessageEx;

public class RmqMessageEventTranslator implements EventTranslator<RmqMessageEx>{
	private RmqMessage rmqMessage;
	
	public RmqMessageEventTranslator(RmqMessage rmqMessage) {
		this.rmqMessage = rmqMessage;
	}
	@Override
	public void translateTo(RmqMessageEx event, long sequence) {
		
		event.setRmqMessage(this.rmqMessage);
		event.setSequence(sequence);
		
	}

}
