package cma.cimiss2.dpc.indb.framework.rmq;

import cma.cimiss2.dpc.indb.framework.rmq.common.DetailRes;
import cma.cimiss2.dpc.indb.framework.rmq.common.MessageBean;

public class UserMessageProcess implements MessageProcess {

	@Override
	public DetailRes process(MessageBean messageBean) {
		System.out.println(messageBean.getBody());
		
		DetailRes detailRes = new DetailRes<>(true, null);
		detailRes.put("");
		return new DetailRes(true, "");
	}
}
