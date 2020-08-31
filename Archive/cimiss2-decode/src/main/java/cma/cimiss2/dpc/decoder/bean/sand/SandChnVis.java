package cma.cimiss2.dpc.decoder.bean.sand;

import java.util.Date;

import cma.cimiss2.dpc.decoder.annotation.Column;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;

/**
 * <br>
 * @Title:  SandChnVis.java
 * @Package cma.cimiss2.dpc.decoder.bean.sand.vis
 * @Description:    TODO(沙尘暴大气能见度实体类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年2月8日 下午3:47:42   wangzunpeng    Initial creation.
 * </pre>
 * 
 * @author wangzunpeng
 */
public class SandChnVis extends AgmeReportHeader {

	/**
	 * @Fields dataTime : 资料时间 
	 */
	@Column(value = "D_DATETIME")
	private Date d_dateTime;

	/**
	 * @Fields latitude : 纬度 
	 */
	@Column(value = "V05001")
	private Double latitude=999999.0;

	/**
	 * @Fields longitude : 经度 
	 */
	@Column(value = "V06001")
	private Double longitude=999999.0;

	/**
	 * @Fields elevationAltitude : 测站海拔高度 
	 */
	@Column(value = "V07001")
	private Double elevationAltitude=999999.0;

	/**
	 * @Fields adminCode : 中国行政区划代码 
	 */
	@Column(value = "V_ACODE")
	private Double adminCode;

	/**
	 * @Fields observationTimeInterval : 观测时间间隔 
	 */
	@Column(value = "V04016")
	private Double observationTimeInterval=999998.0;

	/**
	 * @Fields projectCode : 项目代码 
	 */
	@Column(value = "V_ITEM_CODE")
	private Double projectCode = (double)999998;

	/**
	 * @Fields stateCode(instrument) : 状态码（仪器） 
	 */
	@Column(value = "V02321")
	private Double stateCode = (double) 999998;

	/**
	 * @Fields 1MinuteAverageVisibility : 1分钟平均能见度 
	 */
	@Column(value = "V20001_701_01")
	private Double averageVisibility_1min;

	/**
	 * @Fields 10MinuteAverageVisibility : 10分钟平均能见度 
	 */
	@Column(value = "V20001_701_10")
	private Double averageVisibility_10min;

	/**
	 * @Fields trendOfVisibilityChange : 能见度变化趋势 
	 */
	@Column(value = "V20311")
	private Double trendOfVisibilityChange;

	/**
	 * 资料时间setter 
	 */
	public void setD_dateTime(Date d_dateTime) {
		this.d_dateTime = d_dateTime;
	}

	/**
	 * 资料时间getter 
	 */
	public Date getD_dateTime() {
		return d_dateTime;
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
	 * 项目代码setter 
	 */
	public void setProjectCode(Double projectCode) {
		this.projectCode = projectCode;
	}

	/**
	 * 项目代码getter 
	 */
	public Double getProjectCode() {
		return projectCode;
	}

	/**
	 * 状态码（仪器）setter 
	 */
	public void setStateCode(Double stateCode) {
		this.stateCode = stateCode;
	}

	/**
	 * 状态码（仪器）getter 
	 */
	public Double getStateCode() {
		return stateCode;
	}

	/**
	 * 1分钟平均能见度setter 
	 */
	public void setAverageVisibility_1min(Double averageVisibility_1min) {
		this.averageVisibility_1min = averageVisibility_1min;
	}

	/**
	 * 1分钟平均能见度getter 
	 */
	public Double getAverageVisibility_1min() {
		return averageVisibility_1min;
	}

	/**
	 * 10分钟平均能见度setter 
	 */
	public void setAverageVisibility_10min(Double averageVisibility_10min) {
		this.averageVisibility_10min = averageVisibility_10min;
	}

	/**
	 * 10分钟平均能见度getter 
	 */
	public Double getAverageVisibility_10min() {
		return averageVisibility_10min;
	}

	/**
	 * 能见度变化趋势setter 
	 */
	public void setTrendOfVisibilityChange(Double trendOfVisibilityChange) {
		this.trendOfVisibilityChange = trendOfVisibilityChange;
	}

	/**
	 * 能见度变化趋势getter 
	 */
	public Double getTrendOfVisibilityChange() {
		return trendOfVisibilityChange;
	}
}
