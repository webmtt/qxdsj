package cma.cimiss2.dpc.decoder.bean.sand;

import java.util.Date;

/**
 * 
 * <br>
 * @Title:  KoreaSand_PM10.java
 * @Package cma.cimiss2.dpc.decoder.bean.sand.korea
 * @Description:(韩国沙尘暴PM10观测资料实体类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年4月8日 上午11:06:01   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */
public class KoreaSand_PM10 {
	
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
	Double latitude;
	/**
	 * NO: 1.3  <br>
	 * nameCN: 经度 <br>
	 * unit: 度 <br>
	 * BUFR FXY: V06001 <br>
	 * descriptionCN:
	 */
	Double longitude;
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
	 * nameCN: 观测时间间隔 <br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 观测时间间隔。
	 */
	Double timeInterval=999999.0;

	/**
	 * NO: 1.6  <br>
	 * nameCN: 观测时间 <br>
	 * unit: 日期<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 时间（年月日时分秒共计14位，不足位补“0”）。
	 */
	Date observationTime;
	/**
	 * NO: 1.7 <br>
	 * nameCN: 五分钟平均浓度 <br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 五分钟平均浓度
	 */
	Double avgConcentration_5Min=999999.0;
	/**
	 * NO: 1.8 <br>
	 * nameCN: 1小时平均浓度 <br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 1小时平均浓度
	 */
	Double avgConcentration_1Hour=999999.0;
	/**
	 * NO: 1.9 <br>
	 * nameCN: 24小时平均浓度 <br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 24小时平均浓度
	 */
	Double avgConcentration_24Hour=999999.0;
	/**
	 * NO: 2.0 <br>
	 * nameCN: 总质量 <br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 总质量
	 */
	Double totalMass=999999.0;
	/**
	 * NO: 2.1 <br>
	 * nameCN: 主路流量<br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 主路流量
	 */
	Double mainRoadFlow=999999.0;
	/**
	 * NO: 2.2 <br>
	 * nameCN: 旁路流量<br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 旁路流量
	 */
	Double sideRoadFlow=999999.0;
	/**
	 * NO: 2.3 <br>
	 * nameCN: 负载率<br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 负载率
	 */
	Double loadFactor=999999.0;
	/**
	 * NO: 2.4 <br>
	 * nameCN: 频率<br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 频率
	 */
	Double frequency=999999.0;
	/**
	 * NO: 2.5 <br>
	 * nameCN: 噪音<br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 噪音
	 */
	Double noise=999999.0;
	/**
	 * NO: 2.6 <br>
	 * nameCN: 空气温度<br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 空气温度
	 */
	Double airTemperature=999999.0;
	/**
	 * NO: 2.7 <br>
	 * nameCN: 气压<br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 气压
	 */
	Double airPressure=999999.0;
	/**
	 * NO: 2.8 <br>
	 * nameCN: 运行状态码<br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 运行状态码
	 */
	Double runningState=999999.0;
	/**
	 * NO: 2.9 <br>
	 * nameCN: 主路温度<br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 主路温度
	 */
	Double mainRoadTemperature=999999.0;
	/**
	 * NO: 3.0 <br>
	 * nameCN: 主路相对湿度<br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 主路相对湿度
	 */
	Double mainRoadRelativeHumidity=999999.0;
	/**
	 * NO: 3.1 <br>
	 * nameCN: 旁路相对湿度<br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 旁路相对湿度
	 */
	Double sideRoadRelativeHumidity=999999.0;
	/**
	 * NO: 3.2 <br>
	 * nameCN: 空气相对湿度<br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 空气相对湿度
	 */
	Double airRelativeHumidity=999999.0;
	/**
	 * NO: 3.3  <br>
	 * nameCN: 仪器状态码<br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 仪器状态码。
	 */
	String instrumentStatusCode="999999";
	/**
	 * NO: 3.4  <br>
	 * nameCN: 质量浓度<br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 质量浓度。
	 */
	Double massConcentration=999999.0;
	/**
	 * NO: 3.5  <br>
	 * nameCN: 标准流量<br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 标准流量。
	 */
	Double normalFlow=999999.0;
	/**
	 * NO: 3.6 <br>
	 * nameCN: 项目代码<br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 项目代码。
	 */
	Double  projectCode=999999.0;
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
	public Double getTimeInterval() {
		return timeInterval;
	}
	public void setTimeInterval(Double timeInterval) {
		this.timeInterval = timeInterval;
	}
	public Date getObservationTime() {
		return observationTime;
	}
	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}
	public Double getAvgConcentration_5Min() {
		return avgConcentration_5Min;
	}
	public void setAvgConcentration_5Min(Double avgConcentration_5Min) {
		this.avgConcentration_5Min = avgConcentration_5Min;
	}
	public Double getAvgConcentration_1Hour() {
		return avgConcentration_1Hour;
	}
	public void setAvgConcentration_1Hour(Double avgConcentration_1Hour) {
		this.avgConcentration_1Hour = avgConcentration_1Hour;
	}
	public Double getAvgConcentration_24Hour() {
		return avgConcentration_24Hour;
	}
	public void setAvgConcentration_24Hour(Double avgConcentration_24Hour) {
		this.avgConcentration_24Hour = avgConcentration_24Hour;
	}
	public Double getTotalMass() {
		return totalMass;
	}
	public void setTotalMass(Double totalMass) {
		this.totalMass = totalMass;
	}
	public Double getMainRoadFlow() {
		return mainRoadFlow;
	}
	public void setMainRoadFlow(Double mainRoadFlow) {
		this.mainRoadFlow = mainRoadFlow;
	}
	public Double getSideRoadFlow() {
		return sideRoadFlow;
	}
	public void setSideRoadFlow(Double sideRoadFlow) {
		this.sideRoadFlow = sideRoadFlow;
	}
	public Double getLoadFactor() {
		return loadFactor;
	}
	public void setLoadFactor(Double loadFactor) {
		this.loadFactor = loadFactor;
	}
	public Double getFrequency() {
		return frequency;
	}
	public void setFrequency(Double frequency) {
		this.frequency = frequency;
	}
	public Double getNoise() {
		return noise;
	}
	public void setNoise(Double noise) {
		this.noise = noise;
	}
	public Double getAirTemperature() {
		return airTemperature;
	}
	public void setAirTemperature(Double airTemperature) {
		this.airTemperature = airTemperature;
	}
	public Double getAirPressure() {
		return airPressure;
	}
	public void setAirPressure(Double airPressure) {
		this.airPressure = airPressure;
	}
	public Double getRunningState() {
		return runningState;
	}
	public void setRunningState(Double runningState) {
		this.runningState = runningState;
	}
	public Double getMainRoadTemperature() {
		return mainRoadTemperature;
	}
	public void setMainRoadTemperature(Double mainRoadTemperature) {
		this.mainRoadTemperature = mainRoadTemperature;
	}
	public Double getMainRoadRelativeHumidity() {
		return mainRoadRelativeHumidity;
	}
	public void setMainRoadRelativeHumidity(Double mainRoadRelativeHumidity) {
		this.mainRoadRelativeHumidity = mainRoadRelativeHumidity;
	}
	public Double getSideRoadRelativeHumidity() {
		return sideRoadRelativeHumidity;
	}
	public void setSideRoadRelativeHumidity(Double sideRoadRelativeHumidity) {
		this.sideRoadRelativeHumidity = sideRoadRelativeHumidity;
	}
	public Double getAirRelativeHumidity() {
		return airRelativeHumidity;
	}
	public void setAirRelativeHumidity(Double airRelativeHumidity) {
		this.airRelativeHumidity = airRelativeHumidity;
	}
	public String getInstrumentStatusCode() {
		return instrumentStatusCode;
	}
	public void setInstrumentStatusCode(String instrumentStatusCode) {
		this.instrumentStatusCode = instrumentStatusCode;
	}
	public Double getMassConcentration() {
		return massConcentration;
	}
	public void setMassConcentration(Double massConcentration) {
		this.massConcentration = massConcentration;
	}
	public Double getNormalFlow() {
		return normalFlow;
	}
	public void setNormalFlow(Double normalFlow) {
		this.normalFlow = normalFlow;
	}
	public Double getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(Double projectCode) {
		this.projectCode = projectCode;
	}
	
	
}
