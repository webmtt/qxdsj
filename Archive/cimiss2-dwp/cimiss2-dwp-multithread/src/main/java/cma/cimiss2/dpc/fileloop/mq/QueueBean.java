package cma.cimiss2.dpc.fileloop.mq;

public class QueueBean {
	
	private String queueName;
	private String exchangeName;
	private String routingKey;
	
	
	
	public QueueBean(String queueName, String exchangeName, String routingKey) {
		super();
		this.queueName = queueName;
		this.exchangeName = exchangeName;
		this.routingKey = routingKey;
	}
	public String getQueueName() {
		return queueName;
	}
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	public String getExchangeName() {
		return exchangeName;
	}
	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}
	public String getRoutingKey() {
		return routingKey;
	}
	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}
	
	
}
