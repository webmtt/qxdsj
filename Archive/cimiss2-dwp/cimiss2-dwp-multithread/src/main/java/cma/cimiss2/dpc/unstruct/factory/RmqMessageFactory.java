package cma.cimiss2.dpc.unstruct.factory;

import com.lmax.disruptor.EventFactory;

import cma.cimiss2.dpc.unstruct.messge.RmqMessageEx;

public class RmqMessageFactory implements EventFactory<RmqMessageEx>  {

	@Override
	public RmqMessageEx newInstance() {
		// TODO Auto-generated method stub
		return new RmqMessageEx();
	}

}
