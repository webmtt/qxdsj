package cma.cimiss2.dpc.decoder.bean.atmo_turbidity;

import java.util.Date;

public class AtmosphericTurbidityData {
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
	 * nameCN: 数据识别码 <br>
	 * unit: 日期<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 数据识别码。
	 */
	String data_code;
	/**
	 * NO: 1.8 <br>
	 * nameCN: 粒子散射系数<br>
	 * unit:m <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 粒子散射系数。
	 */
	Double particleScatterCoefficient;
	/**
	 * NO: 1.9 <br>
	 * nameCN: 环境温度<br>
	 * unit:℃ <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 环境温度，数据单位℃。
	 */
	Double temperature;
	/**
	 * NO: 2.0  <br>
	 * nameCN: 环境相对湿度<br>
	 * unit:% <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 环境相对湿度，数据单位%。
	 */
	Double humidity;
	/**
	 * NO: 2.1  <br>
	 * nameCN: 环境气压<br>
	 * unit:数据单位hPa <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 环境气压，。
	 */
	Double pressure;
	/**
	 * NO: 2.2  <br>
	 * nameCN: 腔体温度<br>
	 * unit:℃<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 腔体温度
	 */
	Double cavityTemperature;
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
	public String getData_code() {
		return data_code;
	}
	public void setData_code(String data_code) {
		this.data_code = data_code;
	}
	public Double getParticleScatterCoefficient() {
		return particleScatterCoefficient;
	}
	public void setParticleScatterCoefficient(Double particleScatterCoefficient) {
		this.particleScatterCoefficient = particleScatterCoefficient;
	}
	public Double getTemperature() {
		return temperature;
	}
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	public Double getHumidity() {
		return humidity;
	}
	public void setHumidity(Double humidity) {
		this.humidity = humidity;
	}
	public Double getPressure() {
		return pressure;
	}
	public void setPressure(Double pressure) {
		this.pressure = pressure;
	}
	public Double getCavityTemperature() {
		return cavityTemperature;
	}
	public void setCavityTemperature(Double cavityTemperature) {
		this.cavityTemperature = cavityTemperature;
	}

}
