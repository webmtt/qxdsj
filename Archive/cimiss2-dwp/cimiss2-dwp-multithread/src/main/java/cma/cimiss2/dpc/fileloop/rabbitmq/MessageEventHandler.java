package cma.cimiss2.dpc.fileloop.rabbitmq;

import com.lmax.disruptor.EventHandler;
import cma.cimiss2.dpc.fileloop.rabbitmq.bean.MessageEvent;


public class MessageEventHandler implements EventHandler<MessageEvent> {

	@Override
	public void onEvent(MessageEvent event, long sequence, boolean endOfBatch) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
