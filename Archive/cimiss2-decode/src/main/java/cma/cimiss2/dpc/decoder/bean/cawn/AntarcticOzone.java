package cma.cimiss2.dpc.decoder.bean.cawn;

import java.util.Date;

import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
/**
 * 
 * <br>
 * @Title:  AntarcticOzone.java
 * @Package cma.cimiss2.dpc.decoder.bean.cawn
 * @Description:(南极臭氧资料实体类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年4月12日 下午2:47:23   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */
public class AntarcticOzone extends AgmeReportHeader{
	
	/**
	 * NO: 1.1  <br>
	 * nameCN: 区站号 <br>
	 * unit: <br>
	 * BUFR FXY: V01301 <br>
	 * descriptionCN: 5位数字或第1位为字母，第2-5位为数字
	 */
	String stationNumberChina;
	/**
	 * NO: 1.2 <br>
	 * nameCN: 纬度 <br>
	 * unit: 度 <br>
	 * BUFR FXY: V05001 <br>
	 * descriptionCN:
	 */
	Double latitude=999999.0;
	/**
	 * NO: 1.3  <br>
	 * nameCN: 经度 <br>
	 * unit: 度 <br>
	 * BUFR FXY: V06001 <br>
	 * descriptionCN:
	 */
	Double longitude=999999.0;
	/**
	 * NO: 1.4  <br>
	 * nameCN: 观测场拔海高度 <br>
	 * unit: 1m <br>
	 * BUFR FXY: V07001 <br>
	 * descriptionCN: 若低于海平面，为负值
	 */
	Double heightOfSationGroundAboveMeanSeaLevel=999999.0;
	/**
	 * NO: 1.5  <br>
	 * nameCN: 观测时间 <br>
	 * unit: 日期<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 时间（年月日时分秒共计14位，不足位补“0”）。
	 */
	Date observationTime;
	/**
	 * NO: 1.6  <br>
	 * nameCN: Ds臭氧总量 <br>
	 * unit: <br>
	 * BUFR FXY: V15020_1 <br>
	 * descriptionCN:
	 */
	Double O3_Ds=999999.0;
	/**
	 * NO: 1.  <br>
	 * nameCN: Zs臭氧总量 <br>
	 * unit: <br>
	 * BUFR FXY: V15020_2<br>
	 * descriptionCN:
	 */
	Double O3_Zs=999999.0;
	public String getStationNumberChina() {
		return stationNumberChina;
	}
	public void setStationNumberChina(String stationNumberChina) {
		this.stationNumberChina = stationNumberChina;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getHeightOfSationGroundAboveMeanSeaLevel() {
		return heightOfSationGroundAboveMeanSeaLevel;
	}
	public void setHeightOfSationGroundAboveMeanSeaLevel(Double heightOfSationGroundAboveMeanSeaLevel) {
		this.heightOfSationGroundAboveMeanSeaLevel = heightOfSationGroundAboveMeanSeaLevel;
	}
	public Date getObservationTime() {
		return observationTime;
	}
	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}
	public Double getO3_Ds() {
		return O3_Ds;
	}
	public void setO3_Ds(Double o3_Ds) {
		O3_Ds = o3_Ds;
	}
	public Double getO3_Zs() {
		return O3_Zs;
	}
	public void setO3_Zs(Double o3_Zs) {
		O3_Zs = o3_Zs;
	}
	
	
	
}
