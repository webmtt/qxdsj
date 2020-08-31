package cma.cimiss2.dpc.decoder.bean.sand;

import java.util.Date;

public class AveObservationSandTowData {
	/**
     * NO: 1.1  <br>
     * nameCN: 区站号 <br>
     * unit: <br>
     * BUFR FXY: V01301 <br>
     * descriptionCN: 取报文名称以_为分割的第五个数值
     */
    String stationNumberChina;
    /**
     * NO: 1.2  <br>
     * nameCN: 传感器层数 <br>
     * unit: 1m <br>
     * BUFR FXY:  <br>
     * descriptionCN: 报文第一列
     */
    int numberOfSensorLayers;
    /**
     * NO: 1.3  <br>
     * nameCN: 传感器高度 <br>
     * unit: 1m <br>
     * BUFR FXY:  <br>
     * descriptionCN: 报文第二列
     */
    Double SensorHeight;
	/**NO: 1.4  <br>
	 * nameCN: 开始观测时间 <br>
	 * unit: 日期<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 时间（年月日时分秒共计14位，不足位补“0”）。
	 */
	Date startTimeOfobservation;
	/**NO: 1.5  <br>
	 * nameCN: 结束观测时间 <br>
	 * unit: 日期<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 时间（年月日时分秒共计14位，不足位补“0”）。
	 */
	Date endTimeOfobservation;
	/**NO: 1.6  <br>
	 * nameCN: 平均风速 <br>
	 * unit: 日期<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 报文第三列。
	 */
	Double meanWindSpeed;
	/**NO: 1.7  <br>
	 * nameCN: 平均风向 <br>
	 * unit: 日期<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 报文第四列。
	 */
	Double meanWindSpod;
	/**NO: 1.8  <br>
	 * nameCN: 相对湿度 <br>
	 * unit: 日期<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 报文第五列。
	 */
	Double relativeHumidity;
	/**NO: 1.8  <br>
	 * nameCN: 温度 <br>
	 * unit: 日期<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 报文第四列。
	 */
	Double temperature;
	/**NO: 1.4  <br>
	 * nameCN: 资料观测时间 <br>
	 * unit: 日期<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 时间（年月日时分秒共计14位，不足位补“0”）。
	 */
	Date observationTime;
	
	public String getStationNumberChina() {
		return stationNumberChina;
	}
	public void setStationNumberChina(String stationNumberChina) {
		this.stationNumberChina = stationNumberChina;
	}
	public int getNumberOfSensorLayers() {
		return numberOfSensorLayers;
	}
	public void setNumberOfSensorLayers(int numberOfSensorLayers) {
		this.numberOfSensorLayers = numberOfSensorLayers;
	}
	public Double getSensorHeight() {
		return SensorHeight;
	}
	public void setSensorHeight(Double sensorHeight) {
		SensorHeight = sensorHeight;
	}
	public Date getStartTimeOfobservation() {
		return startTimeOfobservation;
	}
	public void setStartTimeOfobservation(Date startTimeOfobservation) {
		this.startTimeOfobservation = startTimeOfobservation;
	}
	public Date getEndTimeOfobservation() {
		return endTimeOfobservation;
	}
	public void setEndTimeOfobservation(Date endTimeOfobservation) {
		this.endTimeOfobservation = endTimeOfobservation;
	}
	public Double getMeanWindSpeed() {
		return meanWindSpeed;
	}
	public void setMeanWindSpeed(Double meanWindSpeed) {
		this.meanWindSpeed = meanWindSpeed;
	}
	public Double getMeanWindSpod() {
		return meanWindSpod;
	}
	public void setMeanWindSpod(Double meanWindSpod) {
		this.meanWindSpod = meanWindSpod;
	}
	public Double getRelativeHumidity() {
		return relativeHumidity;
	}
	public void setRelativeHumidity(Double relativeHumidity) {
		this.relativeHumidity = relativeHumidity;
	}
	public Double getTemperature() {
		return temperature;
	}
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	
	public Date getObservationTime() {
		return observationTime;
	}
	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}
	@Override
	public String toString() {
		return "AveObservationSandTowData [stationNumberChina=" + stationNumberChina + ", numberOfSensorLayers="
				+ numberOfSensorLayers + ", SensorHeight=" + SensorHeight + ", startTimeOfobservation="
				+ startTimeOfobservation + ", endTimeOfobservation=" + endTimeOfobservation + ", meanWindSpeed="
				+ meanWindSpeed + ", meanWindSpod=" + meanWindSpod + ", relativeHumidity=" + relativeHumidity
				+ ", temperature=" + temperature + ", observationTime=" + observationTime + "]";
	}
	
	
}
