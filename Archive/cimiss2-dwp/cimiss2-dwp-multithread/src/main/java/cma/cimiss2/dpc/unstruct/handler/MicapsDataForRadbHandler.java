package cma.cimiss2.dpc.unstruct.handler;

import com.lmax.disruptor.EventHandler;

import cma.cimiss2.dpc.unstruct.messge.RadbMessage;

public class MicapsDataForRadbHandler implements EventHandler<RadbMessage>{

	@Override
	public void onEvent(RadbMessage event, long sequence, boolean endOfBatch) throws Exception {
		System.out.println("MicapsDataForRadbHandler" + event.getCtsCode() +">>>>>"+Thread.currentThread().getName());
		
	}

}
