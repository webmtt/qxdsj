package cma.cimiss2.dpc.fileloop.rabbitmq;
import com.lmax.disruptor.EventFactory;
import cma.cimiss2.dpc.fileloop.rabbitmq.bean.MessageEvent;


public class MessageEventFactory  implements EventFactory<MessageEvent>{

	@Override
	public MessageEvent newInstance() {
		return new MessageEvent();
	}

}
