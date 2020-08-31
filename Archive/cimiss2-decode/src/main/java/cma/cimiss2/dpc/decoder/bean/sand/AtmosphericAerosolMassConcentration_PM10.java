package cma.cimiss2.dpc.decoder.bean.sand;

import java.util.Date;
/**
 * -------------------------------------------------------------------------------
 * <br>
 * @Title:  AtmosphericAerosolMassConcentration_PM10.java   
 * @Package cma.cimiss2.dpc.decoder.bean.sand_pm10   
 * @Description:    TODO(沙尘暴气溶胶质量浓度PM10资料实体类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年2月6日 上午9:54:18   maxiyue    Initial creation.
 * </pre>
 * 
 * @author maxiyue
 *---------------------------------------------------------------------------------
 */
public class AtmosphericAerosolMassConcentration_PM10 {
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
	Double heightOfSationGroundAboveMeanSeaLevel;

	/**
	 * NO: 1.5  <br>
	 * nameCN: 观测时间间隔 <br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 观测时间间隔。
	 */
	Double timeInterval;

	/**
	 * NO: 1.6  <br>
	 * nameCN: 观测时间 <br>
	 * unit: 日期<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 时间（年月日时分秒共计14位，不足位补“0”）。
	 */
	Date observationTime;
	/**
	 * NO: 1.7  <br>
	 * nameCN: 仪器状态码<br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 仪器状态码。
	 */
	String instrumentStatusCode;
	/**
	 * NO: 1.8  <br>
	 * nameCN: 质量浓度<br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 质量浓度。
	 */
	Double massConcentration;
	/**
	 * NO: 1.9  <br>
	 * nameCN: 总质量<br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 总质量。
	 */
	Double totalMass;
	/**
	 * NO: 2.0  <br>
	 * nameCN: 标准流量<br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 标准流量。
	 */
	Double normalFlow;
	/**
	 * NO: 2.1  <br>
	 * nameCN: 1小时质量浓度平均值<br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 1小时质量浓度平均值。
	 */
	Double 	averageMassConcentration_1;
	/**
	 * NO: 2.2  <br>
	 * nameCN: 24小时质量浓度平均值<br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 24小时质量浓度平均值。
	 */
	Double 	averageMassConcentration_24;
	/**
	 * NO: 2.3  <br>
	 * nameCN: 环境温度<br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 环境温度。
	 */
	Double 	environmentTemperature;
	/**
	 * NO: 2.4  <br>
	 * nameCN: 环境气压<br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 环境气压。
	 */
	Double 	environmentPressure;
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
	public Double getTotalMass() {
		return totalMass;
	}
	public void setTotalMass(Double totalMass) {
		this.totalMass = totalMass;
	}
	public Double getNormalFlow() {
		return normalFlow;
	}
	public void setNormalFlow(Double normalFlow) {
		this.normalFlow = normalFlow;
	}
	public Double getAverageMassConcentration_1() {
		return averageMassConcentration_1;
	}
	public void setAverageMassConcentration_1(Double averageMassConcentration_1) {
		this.averageMassConcentration_1 = averageMassConcentration_1;
	}
	public Double getAverageMassConcentration_24() {
		return averageMassConcentration_24;
	}
	public void setAverageMassConcentration_24(Double averageMassConcentration_24) {
		this.averageMassConcentration_24 = averageMassConcentration_24;
	}
	public Double getEnvironmentTemperature() {
		return environmentTemperature;
	}
	public void setEnvironmentTemperature(Double environmentTemperature) {
		this.environmentTemperature = environmentTemperature;
	}
	public Double getEnvironmentPressure() {
		return environmentPressure;
	}
	public void setEnvironmentPressure(Double environmentPressure) {
		this.environmentPressure = environmentPressure;
	}
	
	

}
