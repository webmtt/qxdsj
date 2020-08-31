package cma.cimiss2.dpc.indb.framework.rmq.common;

import com.rabbitmq.client.AMQP.BasicProperties;

public class MessageBean {
	// RabbitMQ 接收到消息的内容
	private byte[] body;
	// RabbitMQ 消息的属性
	private BasicProperties properties;
	
	
	public MessageBean() {
	}
	public MessageBean(byte[] body, BasicProperties properties) {
		this.body = body;
		this.properties = properties;
	}
	public byte[] getBody() {
		return body;
	}
	public void setBody(byte[] body) {
		this.body = body;
	}
	public BasicProperties getProperties() {
		return properties;
	}
	public void setProperties(BasicProperties properties) {
		this.properties = properties;
	}
	
	

}
