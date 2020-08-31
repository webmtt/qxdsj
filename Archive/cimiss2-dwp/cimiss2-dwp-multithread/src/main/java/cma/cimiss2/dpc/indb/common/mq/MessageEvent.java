package cma.cimiss2.dpc.indb.common.mq;

import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

public class MessageEvent {
	private String consumerTag;
	private byte[] body;   
	private BasicProperties properties;
	private Envelope envelope;
	
	
	
	public MessageEvent() {
		super();
	}




	public MessageEvent(byte[] body, BasicProperties properties) {
		super();
		this.body = body;
		this.properties = properties;
	}
	
	
	
	
	public MessageEvent(byte[] body, BasicProperties properties, Envelope envelope) {
		super();
		this.body = body;
		this.properties = properties;
		this.envelope = envelope;
	}




	public MessageEvent(String consumerTag, byte[] body, BasicProperties properties, Envelope envelope) {
		super();
		this.consumerTag = consumerTag;
		this.body = body;
		this.properties = properties;
		this.envelope = envelope;
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


	public String getConsumerTag() {
		return consumerTag;
	}


	public void setConsumerTag(String consumerTag) {
		this.consumerTag = consumerTag;
	}


	public Envelope getEnvelope() {
		return envelope;
	}

	public void setEnvelope(Envelope envelope) {
		this.envelope = envelope;
	}
	
	

}
