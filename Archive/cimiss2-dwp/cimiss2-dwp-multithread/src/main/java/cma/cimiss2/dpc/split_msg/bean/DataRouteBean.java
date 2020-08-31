package cma.cimiss2.dpc.split_msg.bean;


/**
* *******************************************************************************************<br>
* <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
* *******************************************************************************************<br>
* <b>Description:</b><br> 
* @author wuzuoqiang 
* @version 1.0
* @Note
* <b>ProjectName:</b> cimiss2-dwp-split-msg
* <br><b>PackageName:</b> org.cimiss2.dwp.split.msg.bean
* <br><b>ClassName:</b> DataRouteBean
* <br><b>Date:</b> 2019年9月20日 下午2:19:23
 */
public class DataRouteBean {
//	<DataType>F.0027.0001.S001</DataType>
	private String dataType;
//	<ExchangeName>X.OBS.CMADAAS</ExchangeName>
	private String exchangeName;
//	<ExchangeType>topic</ExchangeType>
	private String exchangeType;
//	<RoutingKey>NAFP_ORI_F.0027.0001_R001_002</RoutingKey>
	private String routingKey;
//	<QueueName>NAFP_ORI_F.0027.0001_R001_002</QueueName>
	private String queueName;
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getExchangeName() {
		return exchangeName;
	}
	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}
	public String getExchangeType() {
		return exchangeType;
	}
	public void setExchangeType(String exchangeType) {
		this.exchangeType = exchangeType;
	}
	public String getRoutingKey() {
		return routingKey;
	}
	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}
	public String getQueueName() {
		return queueName;
	}
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
}
