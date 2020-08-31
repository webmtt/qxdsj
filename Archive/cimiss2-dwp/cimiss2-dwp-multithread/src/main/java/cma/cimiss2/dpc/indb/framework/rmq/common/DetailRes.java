package cma.cimiss2.dpc.indb.framework.rmq.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DetailRes<T> {
	// 消息是否处理成功 如果为true 则消息消费后自动确认，如果为false 则消息不确认
	private boolean isSuccess;
	// 如果消息处理失败，这里描述处理失败的原因
    private String errMsg;
    private List<T> queue;
    
	public DetailRes() {
		this.queue = new ArrayList ();
		
	}
	@SuppressWarnings("unchecked")
	public DetailRes(boolean isSuccess, String errMsg) {
		this.queue =  new ArrayList ();
		this.isSuccess = isSuccess;
		this.errMsg = errMsg;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	
	
	public List<T> getQueue() {
		return queue;
	}
	public void setQueue(List<T> queue) {
		this.queue = queue;
	}
	public void put(T obj) {
		this.queue.add(obj);
	}
    
    
}
