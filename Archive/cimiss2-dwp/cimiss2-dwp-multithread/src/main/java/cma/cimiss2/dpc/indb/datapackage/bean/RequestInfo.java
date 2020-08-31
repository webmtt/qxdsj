package cma.cimiss2.dpc.indb.datapackage.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
* *******************************************************************************************<br>
* <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
* *******************************************************************************************<br>
* <b>Description:</b><br> 
* @author wuzuoqiang 
* @version 1.0
* @Note
* <b>ProjectName:</b> cimiss2-dwp-multithread
* <br><b>PackageName:</b> cma.cimiss2.dpc.indb.datapackage.bean
* <br><b>ClassName: 归档数据请求信息</b> RequestInfo
* <br> 
* <requestInfo>
		<requestNo>归档收到的请求号，各个系统保持一致</requestNo>
		<requestDataType>	请求的CTS编码</requestDataType>
		<requestStartTime> 请求数据的开始时间 （UTC）YYYY-MM-DD hh:mm:ss(不足补0）</requestStartTime> 
		<requestEndTime>   请求数据的结束时间 （UTC）YYYY-MM-DD hh:mm:ss(不足补0）</requestEndTime>   
	</requestInfo>
* <br><b>Date:</b> 2019年5月8日 下午4:23:23
 */
@XmlRootElement(name="requestInfo")
public class RequestInfo {
	// 归档收到的请求号，各个系统保持一致
	private String requestNo;
	// 请求的CTS编码
	private String requestDataType;
	// 请求数据的开始时间 （UTC）YYYY-MM-DD hh:mm:ss(不足补0）
	private String requestStartTime;
	// 请求数据的结束时间 （UTC）YYYY-MM-DD hh:mm:ss(不足补0）
	private String requestEndTime;
	
	public String getRequestNo() {
		return requestNo;
	}
	@XmlElement(name="requestNo")
	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}
	public String getRequestDataType() {
		return requestDataType;
	}
	@XmlElement(name="requestDataType")
	public void setRequestDataType(String requestDataType) {
		this.requestDataType = requestDataType;
	}
	public String getRequestStartTime() {
		return requestStartTime;
	}
	@XmlElement(name="requestStartTime")
	public void setRequestStartTime(String requestStartTime) {
		this.requestStartTime = requestStartTime;
	}
	public String getRequestEndTime() {
		return requestEndTime;
	}
	@XmlElement(name="requestEndTime")
	public void setRequestEndTime(String requestEndTime) {
		this.requestEndTime = requestEndTime;
	}
	
	

}
