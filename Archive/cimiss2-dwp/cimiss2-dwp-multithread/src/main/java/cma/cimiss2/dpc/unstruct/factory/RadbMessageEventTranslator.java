package cma.cimiss2.dpc.unstruct.factory;

import com.lmax.disruptor.EventTranslator;

import cma.cimiss2.dpc.unstruct.messge.RadbMessage;
import cma.cimiss2.dpc.unstruct.messge.RmqMessageEx;

public class RadbMessageEventTranslator implements EventTranslator<RadbMessage> {
	private RmqMessageEx rmqMessage;
	public RadbMessageEventTranslator(RmqMessageEx rmqMessage) {
		this.rmqMessage = rmqMessage;
	}
	@Override
	public void translateTo(RadbMessage event, long sequence) {
		event.setCtsCode(this.rmqMessage.getRmqMessage().getCtsCode());
		event.setFullPath(this.rmqMessage.getRmqMessage().getNasFilePath());
	}

}
