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
* <br><b>ClassName: 归档数据请求返回信息</b> ReponseInfo
* <br> 一个消息只包含一个
* <reponseInfo>
		<realDataStartTime> 实际提取tar包数据的开始时间 （UTC）YYYY-MM-DD hh:mm:ss(不足补0）</realDataStartTime>  
		<realDataEndTime> 实际提取数据的结束时间       （UTC）YYYY-MM-DD hh:mm:ss(不足补0）</realDataEndTime>	 
		<missionStartTime> 归档系统响应请求开始时间  （UTC）YYYY-MM-DD hh:mm:ss(不足补0）</missionStartTime>   
		<missionEndTime> 
			归档系统响应请求结束时间（文件推送到指定目录，给DPC的消息发送成功。 UTC YYYY-MM-DD hh:mm:ss(不足补0）   
		</missionEndTime>	                                                                                     
		<filesDir>/交换根目录/请求号_CTS编码</fileDir>       
	</reponseInfo>
* <br><b>Date:</b> 2019年5月8日 下午4:25:14
 */
@XmlRootElement(name="reponseInfo")
public class ReponseInfo {
	// 实际提取tar包数据的开始时间 （UTC）YYYY-MM-DD hh:mm:ss(不足补0）
	private String realDataStartTime;
	// 实际提取数据的结束时间       （UTC）YYYY-MM-DD hh:mm:ss(不足补0）
	private String realDataEndTime;
	// 归档系统响应请求开始时间  （UTC）YYYY-MM-DD hh:mm:ss(不足补0）
	private String missionStartTime;
	// 归档系统响应请求结束时间（文件推送到指定目录，给DPC的消息发送成功。 UTC YYYY-MM-DD hh:mm:ss(不足补0）
	private String missionEndTime;
	// 交换根目录/请求号_CTS编码
	private String filesDir;
	public String getRealDataStartTime() {
		return realDataStartTime;
	}
	@XmlElement(name="realDataStartTime")
	public void setRealDataStartTime(String realDataStartTime) {
		this.realDataStartTime = realDataStartTime;
	}
	public String getRealDataEndTime() {
		return realDataEndTime;
	}
	@XmlElement(name="realDataEndTime")
	public void setRealDataEndTime(String realDataEndTime) {
		this.realDataEndTime = realDataEndTime;
	}
	public String getMissionStartTime() {
		return missionStartTime;
	}
	@XmlElement(name="missionStartTime")
	public void setMissionStartTime(String missionStartTime) {
		this.missionStartTime = missionStartTime;
	}
	public String getMissionEndTime() {
		return missionEndTime;
	}
	@XmlElement(name="missionEndTime")
	public void setMissionEndTime(String missionEndTime) {
		this.missionEndTime = missionEndTime;
	}
	public String getFilesDir() {
		return filesDir;
	}
	@XmlElement(name="filesDir")
	public void setFilesDir(String filesDir) {
		this.filesDir = filesDir;
	}
	
	

}
