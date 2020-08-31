package cma.cimiss2.dpc.fileloop.rabbitmq.bean;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

public class MessageEvent {
	
	private Channel channel;
	private byte[] body;
	private String consumerTag;
	private Envelope env;
	private BasicProperties props;
	
	
	
	public MessageEvent() {
		super();
	}
	public MessageEvent(Channel channel, byte[] body, String consumerTag, Envelope env, BasicProperties props) {
		super();
		this.channel = channel;
		this.body = body;
		this.consumerTag = consumerTag;
		this.env = env;
		this.props = props;
	}
	public Channel getChannel() {
		return channel;
	}
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	public byte[] getBody() {
		return body;
	}
	public void setBody(byte[] body) {
		this.body = body;
	}
	public String getConsumerTag() {
		return consumerTag;
	}
	public void setConsumerTag(String consumerTag) {
		this.consumerTag = consumerTag;
	}
	public Envelope getEnv() {
		return env;
	}
	public void setEnv(Envelope env) {
		this.env = env;
	}
	public BasicProperties getProps() {
		return props;
	}
	public void setProps(BasicProperties props) {
		this.props = props;
	}
	
}
