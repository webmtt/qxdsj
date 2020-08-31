package cma.cimiss2.dpc.unstruct.messge;

public class RmqMessageEx {
	private RmqMessage rmqMessage;
	private long sequence;
	public RmqMessage getRmqMessage() {
		return rmqMessage;
	}
	public void setRmqMessage(RmqMessage rmqMessage) {
		this.rmqMessage = rmqMessage;
	}
	public long getSequence() {
		return sequence;
	}
	public void setSequence(long sequence) {
		this.sequence = sequence;
	}

}
