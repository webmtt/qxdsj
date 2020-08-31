package cma.cimiss2.dpc.unstruct.messge;

import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
/**
* *******************************************************************************************<br>
* <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
* *******************************************************************************************<br>
* <b>Description: 消息实体类  主要包括RabbitMQ消息的消息内容，消息属性信息</b><br> 
* @author wuzuoqiang 
* @version 1.0
* @Note
* <b>ProjectName:</b> cimiss2-dwp-multithread
* <br><b>PackageName:</b> cma.cimiss2.dpc.unstruct.messge
* <br><b>ClassName:</b> RmqMessage
* <br><b>Date:</b> 2020年4月5日 上午8:58:35
 */
public class RmqMessage implements Cloneable{
	// 消息消费者标识
	private String consumerTag;
	private Envelope envelope;
	// RabbitMQ 消息的属性信息
	private BasicProperties properties;
	// RabbitMQ 中获取的消息的内容
	private String ctsCode;
	private String filePath;
	private byte[] body;
	private Channel channel;
	private String nasFilePath;
	
	
	public Channel getChannel() {
		return channel;
	}
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	public RmqMessage() {
		super();
	}
	public RmqMessage(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) {
		super();
		this.consumerTag = consumerTag;
		this.envelope = envelope;
		this.properties = properties;
		this.body = body;
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
	public BasicProperties getProperties() {
		return properties;
	}
	public void setProperties(BasicProperties properties) {
		this.properties = properties;
	}
	public byte[] getBody() {
		return body;
	}
	public void setBody(byte[] body) {
		this.body = body;
	}
	public String getCtsCode() {
		return ctsCode;
	}
	public void setCtsCode(String ctsCode) {
		this.ctsCode = ctsCode;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getNasFilePath() {
		return nasFilePath;
	}
	public void setNasFilePath(String nasFilePath) {
		this.nasFilePath = nasFilePath;
	}
	
	@Override
	public RmqMessage clone() throws CloneNotSupportedException {
		
		RmqMessage rmqMessage = null;
		try {
			rmqMessage = (RmqMessage) super.clone();
		} catch (Exception e) {
			return null;
		}
		return rmqMessage;
	}

}
