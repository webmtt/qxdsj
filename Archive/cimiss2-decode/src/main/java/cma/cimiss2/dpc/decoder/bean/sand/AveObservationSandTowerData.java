package cma.cimiss2.dpc.decoder.bean.sand;

import java.util.Date;

public class AveObservationSandTowerData {
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
    
	/**NO: 1.5  <br>
	 * nameCN: 观测时间间隔 <br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 观测时间间隔。
	 */
    Double timeInterval;
    
	public Double getTimeInterval() {
		return timeInterval;
	}
	public void setTimeInterval(Double timeInterval) {
		this.timeInterval = timeInterval;
	}
	/**NO: 1.5  <br>
	 * nameCN: 观测时间 <br>
	 * unit: 日期<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 时间（年月日时分秒共计14位，不足位补“0”）。
	 */
	Date observationTime;
	/**
	 * NO:1.6   <br>
	 * nameCN:  1米风速<br>
	 * unit: <br>
	 * descriptionCN: 1米，数据单位m/s。
	 */
	double windSpeed_1;

	/**
	 * NO: 1.7  <br>
	 * nameCN:  2m风速<br>
	 * unit: <br>
	 * descriptionCN: 2m风速，数据单位m/s
	 */
	double windSpeed_2;

	/**
	 * NO: 1.8  <br>
	 * nameCN: 4m风速<br>
	 * unit: <br>
	 * descriptionCN: 4m风速，数据单位m/s
	 */
	double windSpeed_4;

	/**
	 * NO: 1.9  <br>
	 * nameCN:  10m风速<br>
	 * unit: <br>
	 * descriptionCN: 10m风速，数据单位m/s
	 */
	double windSpeed_10;

	/**
	 * NO: 2.0  <br>
	 * nameCN: 20m风速<br>
	 * unit: <br>
	 * descriptionCN: 20m风速，数据单位m/s
	 */
	double windSpeed_20;
	/**
	 * NO: 2.1  <br>
	 * nameCN:1米风向<br>
	 * unit: DEC<br>
	 * BUFR FXY:<br>
	 * descriptionCN:1米,数据单位DEC。
	 */
	double windDirection_1;
	/**
	 * NO: 2.2  <br>
	 * nameCN:4米风向<br>
	 * unit: DEC<br>
	 * BUFR FXY:<br>
	 * descriptionCN:4米,数据单位DEC。
	 */
	double windDirection_4;
	/**
	 * NO: 2.3  <br>
	 * nameCN:风向<br>
	 * unit: DEC<br>
	 * BUFR FXY:<br>
	 * descriptionCN:20米,数据单位DEC。
	 */
	double windDirection_20;
	/**
	 * NO: 2.4  <br>
	 * nameCN:1米温度 <br>
	 * unit: 1℃ <br>
	 * descriptionCN: 温度
	 */
	double temperature_1;
	
	/**
	 * NO: 2.4 <br>
	 * nameCN:2米温度 <br>
	 * unit: 1℃ <br>
	 * descriptionCN: 温度
	 */
	double temperature_2;
	/**
	 * NO: 2.6 <br>
	 * nameCN:4米温度 <br>
	 * unit: 1℃ <br>
	 * descriptionCN: 温度
	 */
	double temperature_4;
	/**
	 * NO: 2.5 <br>
	 * nameCN:10米温度 <br>
	 * unit: 1℃ <br>
	 * descriptionCN: 温度
	 */
	double temperature_10;
	/**
	 * NO: 2.6 <br>
	 * nameCN:20米温度 <br>
	 * unit: 1℃ <br>
	 * descriptionCN: 温度
	 */
	double temperature_20;
	//五层的湿度（1米、2米、4米、10米、20米），数据单位%。
	/**
	 * NO: 2.7 <br>
	 * nameCN:1m湿度 <br>
	 * unit: 1℃ <br>
	 * descriptionCN: 五层的湿度1米湿度
	 */
	double  humidity_1;
	/**
	 * NO: 2.8 <br>
	 * nameCN:2m湿度 <br>
	 * unit: % <br>
	 * descriptionCN: 五层的湿度2米湿度
	 */
	double  humidity_2;
	/**
	 * NO: 2.9 <br>
	 * nameCN:4m湿度 <br>
	 * unit: % <br>
	 * descriptionCN: 五层的湿度4米湿度
	 */
	double  humidity_4;
	/**
	 * NO: 3.0 <br>
	 * nameCN:10m湿度 <br>
	 * unit: % <br>
	 * descriptionCN: 五层的湿度10米湿度
	 */
	double  humidity_10;
	/**
	 * NO: 3.1 <br>
	 * nameCN:20m湿度 <br>
	 * unit: % <br>
	 * descriptionCN: 五层的湿度20米湿度
	 */
	double  humidity_20;
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
	public double getWindSpeed_1() {
		return windSpeed_1;
	}
	public void setWindSpeed_1(double windSpeed_1) {
		this.windSpeed_1 = windSpeed_1;
	}
	public double getWindSpeed_2() {
		return windSpeed_2;
	}
	public void setWindSpeed_2(double windSpeed_2) {
		this.windSpeed_2 = windSpeed_2;
	}
	public double getWindSpeed_4() {
		return windSpeed_4;
	}
	public void setWindSpeed_4(double windSpeed_4) {
		this.windSpeed_4 = windSpeed_4;
	}
	public double getWindSpeed_10() {
		return windSpeed_10;
	}
	public void setWindSpeed_10(double windSpeed_10) {
		this.windSpeed_10 = windSpeed_10;
	}
	public double getWindSpeed_20() {
		return windSpeed_20;
	}
	public void setWindSpeed_20(double windSpeed_20) {
		this.windSpeed_20 = windSpeed_20;
	}
	public double getWindDirection_1() {
		return windDirection_1;
	}
	public void setWindDirection_1(double windDirection_1) {
		this.windDirection_1 = windDirection_1;
	}
	public double getWindDirection_4() {
		return windDirection_4;
	}
	public void setWindDirection_4(double windDirection_4) {
		this.windDirection_4 = windDirection_4;
	}
	public double getWindDirection_20() {
		return windDirection_20;
	}
	public void setWindDirection_20(double windDirection_20) {
		this.windDirection_20 = windDirection_20;
	}
	public double getTemperature_1() {
		return temperature_1;
	}
	public void setTemperature_1(double temperature_1) {
		this.temperature_1 = temperature_1;
	}
	public double getTemperature_2() {
		return temperature_2;
	}
	public void setTemperature_2(double temperature_2) {
		this.temperature_2 = temperature_2;
	}
	public double getTemperature_4() {
		return temperature_4;
	}
	public void setTemperature_4(double temperature_4) {
		this.temperature_4 = temperature_4;
	}
	public double getTemperature_10() {
		return temperature_10;
	}
	public void setTemperature_10(double temperature_10) {
		this.temperature_10 = temperature_10;
	}
	public double getTemperature_20() {
		return temperature_20;
	}
	public void setTemperature_20(double temperature_20) {
		this.temperature_20 = temperature_20;
	}
	public double getHumidity_1() {
		return humidity_1;
	}
	public void setHumidity_1(double humidity_1) {
		this.humidity_1 = humidity_1;
	}
	public double getHumidity_2() {
		return humidity_2;
	}
	public void setHumidity_2(double humidity_2) {
		this.humidity_2 = humidity_2;
	}
	public double getHumidity_4() {
		return humidity_4;
	}
	public void setHumidity_4(double humidity_4) {
		this.humidity_4 = humidity_4;
	}
	public double getHumidity_10() {
		return humidity_10;
	}
	public void setHumidity_10(double humidity_10) {
		this.humidity_10 = humidity_10;
	}
	public double getHumidity_20() {
		return humidity_20;
	}
	public void setHumidity_20(double humidity_20) {
		this.humidity_20 = humidity_20;
	}
	

}
