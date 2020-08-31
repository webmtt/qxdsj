package cma.cimiss2.dpc.unstruct.factory;

import com.lmax.disruptor.EventFactory;

import cma.cimiss2.dpc.unstruct.messge.RadbMessage;

public class RadbMessageFactory implements EventFactory<RadbMessage>{

	@Override
	public RadbMessage newInstance() {
		// TODO Auto-generated method stub
		return new RadbMessage();
	}

}
