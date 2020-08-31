package cma.cimiss2.dpc.decoder.bean.sand;

import java.util.Date;

import cma.cimiss2.dpc.decoder.annotation.Column;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;

/**
 * <br>
 * @Title:  SandChnSoi.java
 * @Package cma.cimiss2.dpc.decoder.bean.sand.soi
 * @Description:    TODO(沙尘暴土壤湿度实体类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年2月8日 下午3:44:52   wangzunpeng    Initial creation.
 * </pre>
 * 
 * @author wangzunpeng
 */
public class SandChnSoi extends AgmeReportHeader{

	/**
	 * @Fields dataTime : 资料时间 
	 */
	@Column(value = "D_DATETIME")
	private Date dataTime;

	/**
	 * @Fields areaStationNumber(character) : 区站号(字符) 
	 */
	@Column(value = "V01301")
	private String stationNumberC;

	/**
	 * @Fields areaStationNumber(number) : 区站号(数字) 
	 */
	@Column(value = "V01300")
	private Double stationNumberN;

	/**
	 * @Fields latitude : 纬度 
	 */
	@Column(value = "V05001")
	private Double latitude;

	/**
	 * @Fields longitude : 经度 
	 */
	@Column(value = "V06001")
	private Double longitude;

	/**
	 * @Fields elevationAltitude : 测站海拔高度 
	 */
	@Column(value = "V07001")
	private Double elevationAltitude;

	/**
	 * @Fields adminCode : 中国行政区划代码 
	 */
	@Column(value = "V_ACODE")
	private Double adminCode;

	/**
	 * @Fields observationTimeInterval : 观测时间间隔 
	 */
	@Column(value = "V04016")
	private Double observationTimeInterval;

	/**
	 * @Fields soilWeightMoistureContent_10 : 10厘米土壤重量含水率 
	 */
	@Column(value = "V71104_010")
	private Double soilWeightMoistureContent_10;

	/**
	 * @Fields soilWeightMoistureContent_20 : 20厘米土壤重量含水率 
	 */
	@Column(value = "V71104_020")
	private Double soilWeightMoistureContent_20;

	/**
	 * @Fields soilWeightMoistureContent_30 : 30厘米土壤重量含水率 
	 */
	@Column(value = "V71104_030")
	private Double soilWeightMoistureContent_30;

	/**
	 * @Fields soilWeightMoistureContent_40 : 40厘米土壤重量含水率 
	 */
	@Column(value = "V71104_040")
	private Double soilWeightMoistureContent_40;

	/**
	 * @Fields soilWeightMoistureContent_50 : 50厘米土壤重量含水率 
	 */
	@Column(value = "V71104_050")
	private Double soilWeightMoistureContent_50;

	/**
	 * 资料时间setter 
	 */
	public void setDataTime(Date dataTime) {
		this.dataTime = dataTime;
	}

	/**
	 * 资料时间getter 
	 */
	public Date getDataTime() {
		return dataTime;
	}

	/**
	 * 区站号(字符)setter 
	 */
	public void setStationNumberC(String stationNumberC) {
		this.stationNumberC = stationNumberC;
	}

	/**
	 * 区站号(字符)getter 
	 */
	public String getStationNumberC() {
		return stationNumberC;
	}

	/**
	 * 区站号(数字)setter 
	 */
	public void setStationNumberN(Double stationNumberN) {
		this.stationNumberN = stationNumberN;
	}

	/**
	 * 区站号(数字)getter 
	 */
	public Double getStationNumberN() {
		return stationNumberN;
	}

	/**
	 * 纬度setter 
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * 纬度getter 
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * 经度setter 
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * 经度getter 
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * 测站海拔高度setter 
	 */
	public void setElevationAltitude(Double elevationAltitude) {
		this.elevationAltitude = elevationAltitude;
	}

	/**
	 * 测站海拔高度getter 
	 */
	public Double getElevationAltitude() {
		return elevationAltitude;
	}

	/**
	 * 中国行政区划代码setter 
	 */
	public void setAdminCode(Double adminCode) {
		this.adminCode = adminCode;
	}

	/**
	 * 中国行政区划代码getter 
	 */
	public Double getAdminCode() {
		return adminCode;
	}

	/**
	 * 观测时间间隔setter 
	 */
	public void setObservationTimeInterval(Double observationTimeInterval) {
		this.observationTimeInterval = observationTimeInterval;
	}

	/**
	 * 观测时间间隔getter 
	 */
	public Double getObservationTimeInterval() {
		return observationTimeInterval;
	}

	/**
	 * 10厘米土壤重量含水率setter 
	 */
	public void setSoilWeightMoistureContent_10(Double soilWeightMoistureContent_10) {
		this.soilWeightMoistureContent_10 = soilWeightMoistureContent_10;
	}

	/**
	 * 10厘米土壤重量含水率getter 
	 */
	public Double getSoilWeightMoistureContent_10() {
		return soilWeightMoistureContent_10;
	}

	/**
	 * 20厘米土壤重量含水率setter 
	 */
	public void setSoilWeightMoistureContent_20(Double soilWeightMoistureContent_20) {
		this.soilWeightMoistureContent_20 = soilWeightMoistureContent_20;
	}

	/**
	 * 20厘米土壤重量含水率getter 
	 */
	public Double getSoilWeightMoistureContent_20() {
		return soilWeightMoistureContent_20;
	}

	/**
	 * 30厘米土壤重量含水率setter 
	 */
	public void setSoilWeightMoistureContent_30(Double soilWeightMoistureContent_30) {
		this.soilWeightMoistureContent_30 = soilWeightMoistureContent_30;
	}

	/**
	 * 30厘米土壤重量含水率getter 
	 */
	public Double getSoilWeightMoistureContent_30() {
		return soilWeightMoistureContent_30;
	}

	/**
	 * 40厘米土壤重量含水率setter 
	 */
	public void setSoilWeightMoistureContent_40(Double soilWeightMoistureContent_40) {
		this.soilWeightMoistureContent_40 = soilWeightMoistureContent_40;
	}

	/**
	 * 40厘米土壤重量含水率getter 
	 */
	public Double getSoilWeightMoistureContent_40() {
		return soilWeightMoistureContent_40;
	}

	/**
	 * 50厘米土壤重量含水率setter 
	 */
	public void setSoilWeightMoistureContent_50(Double soilWeightMoistureContent_50) {
		this.soilWeightMoistureContent_50 = soilWeightMoistureContent_50;
	}

	/**
	 * 50厘米土壤重量含水率getter 
	 */
	public Double getSoilWeightMoistureContent_50() {
		return soilWeightMoistureContent_50;
	}

}
