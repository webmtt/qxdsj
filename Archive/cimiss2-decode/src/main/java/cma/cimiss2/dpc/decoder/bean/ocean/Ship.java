package cma.cimiss2.dpc.decoder.bean.ocean;

import java.util.Date;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
     海上船舶资料实体类
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《国内海上船舶数据表.docx》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月24日 上午11:03:58   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class Ship {
	/**
	 * NO: 1.1  <br>
	 * nameCN: 船舶标识  <br>
	 * unit: <br>
	 * BUFR FXY: V01011 <br>
	 * descriptionCN: 使用英文数字表示，3-8个字符<br>
     * decode rule:直接取值 <br>
     * field rule:直接赋值
	 */
	private String shipID;

	/**
	 * NO: 1.2  <br>
	 * nameCN: 自动气象站安装处船面距海面高度  <br>
	 * unit: 1m <br>
	 * BUFR FXY: V07001 <br>
	 * descriptionCN: <br>
     * decode rule:取值除以10  <br>
     * field rule:直接赋值
	 */
	private double stationHeightAboveSea;
	/**
	 * NO: 1.3  <br>
	 * nameCN: 气压传感器距海面高度  <br>
	 * unit: 1m <br>
	 * BUFR FXY: V07304 <br>
	 * descriptionCN:  <br>
     * decode rule:取值除以10  <br>
     * field rule:直接赋值
	 */
	private double heightOfAirpressureSensor;
	
	/**
	 * NO: 1.4  <br>
	 * nameCN: 风速传感器距船面高度 <br>
	 * unit: 1m <br>
	 * BUFR FXY:  V07301 <br>
	 * descriptionCN:  <br>
     * decode rule:取值除以10  <br>
     * field rule:直接赋值
	 */
	private double heightOfWindSpeedSensor;
	/**
	 * NO: 1.5  <br>
	 * nameCN: 船面距海面高度  <br>
	 * unit: 1m <br>
	 * BUFR FXY: V07305 <br>
	 * descriptionCN:  <br>
     * decode rule:取值除以10  <br>
     * field rule:直接赋值
	 */
	private double distanceBetweenDeckAndSea;
	/**
	 * NO: 1.8  <br>
	 * nameCN:  观测类型 <br>
	 * unit:  <br>
	 * BUFR FXY: V02141 <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */
	private String stationTypeStr;
	/**
	 * NO: 1.8  <br>
	 * nameCN:  测站类型 <br>
	 * unit: <br>
	 * BUFR FXY: V02001 <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */
	private int stationTypeInt;
	/**
	 * NO: 2.1  <br>
	 * nameCN:  观测时间 <br>
	 * unit: <br>
	 * BUFR FXY: D_DATETIME <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:使用java时间转化工具类进行转化,标准形式为：yyyyMMddHHmmss
	 */
	private Date observationTime;
	
	/**
	 * NO: 2.2  <br>
	 * nameCN:  船舶地理位置（经度） <br>
	 * unit: 度  <br>
	 * BUFR FXY: V06001 <br>
	 * descriptionCN:  <br>
     * decode rule: 度分秒转化为度 <br>
     * field rule: 直接赋值
	 */
	private double longtitude;
	
	/**
	 * NO: 2.3  <br>
	 * nameCN:  船舶地理位置（纬度） <br>
	 * unit: 度  <br>
	 * BUFR FXY: V05001 <br>
	 * descriptionCN: <br>
     * decode rule: 度分秒转化为度 <br>
     * field rule: 直接赋值
	 */
	private double latitude;
	
	/**
	 * NO: 2.4  <br>
	 * nameCN:  船舶航行移向 <br>
	 * unit: 度 <br>
	 * BUFR FXY: V01012 <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */
	private int shipMovingDir;
	
	/**
	 * NO: 2.5  <br>
	 * nameCN:  船舶航行速度 <br>
	 * unit: 米/秒，保留1位小数 <br>
	 * BUFR FXY: V01013 <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */
	 private double shipMovingSpeed;
	 
	 /**
	 * NO: 2.6  <br>
	 * nameCN:  船艏方位 <br>
	 * unit:度，保留1位小数 <br>
	 * BUFR FXY: V05021 <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */
	 private double bowAzimuth;
	 /**
	 * NO: 2.7  <br>
	 * nameCN:  天气情况 <br>
	 * unit: 代码 <br>
	 * BUFR FXY: V20003 <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */
	 private int weatherCondition;
	 
	 /**
	 * NO: 2.8  <br>
	 * nameCN:  海平面气压 <br>
	 * unit: hPa，保留1位小数 <br>
	 * BUFR FXY: V10051 <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */
	 private double seaLevelPressure;
	 
	 /**
	 * NO: 2.9  <br>
	 * nameCN:  干球温度 <br>
	 * unit: ℃，保留1位小数<br>
	 * BUFR FXY: V12001 <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */
	  private double dryballTemperature;
	  
	  /**
	 * NO: 2.10  <br>
	 * nameCN: 湿球温度 <br>
	 * unit: ℃，保留1位小数<br>
	 * BUFR FXY: V12002 <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */  
	 private double wetballTemperature;
	  /**
	 * NO: 2.11  <br>
	 * nameCN: 风向 <br>
	 * unit: 1°，原值记录 <br>
	 * BUFR FXY: V11001 <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */
	 private double windDir;
	 
	 /**
	 * NO: 2.12  <br>
	 * nameCN: 风速 <br>
	 * unit: ｍ/ｓ，保留1位小数 <br>
	 * BUFR FXY: V11002 <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */
	 private double windSpeed;
	 
	 /**
	 * NO: 2.13  <br>
	 * nameCN: 风力级别 <br>
	 * unit: 代码 <br>
	 * BUFR FXY: V11301 <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */ 
	 private int windLevel;
	 /**
	 * NO: 2.14  <br>
	 * nameCN: 海水温度 <br>
	 * unit: ℃，保留1位小数 <br>
	 * BUFR FXY: V22042 <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */ 
	 private double seaTemperature;
	 
	 /**
	 * NO: 2.15  <br>
	 * nameCN: 能见度 <br>
	 * unit: 1m，原值记录  <br>
	 * BUFR FXY: V20001 <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */ 
	 private double visibility;
	 
	 /**
	 * NO: 2.16  <br>
	 * nameCN: 云状 <br>
	 * unit: 代码 <br>
	 * BUFR FXY: V20012 <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */ 
	 private int cloudShape;
	 
	 /**
	 * NO: 2.17  <br>
	 * nameCN: 云量 <br>
	 * unit: 代码 <br>
	 * BUFR FXY: V20011 <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */
	 private int cloudAmount;
	 
	 /**
	 * NO: 2.18  <br>
	 * nameCN: 人工测量浪高  <br>
	 * unit: 米，保留1位小数 <br>
	 * BUFR FXY: V22022_1 <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */
	private double  waveHeightManually;
	 /**
	 * NO: 2.19  <br>
	 * nameCN: 仪器测量浪高  <br>
	 * unit: 米，保留1位小数 <br>
	 * BUFR FXY: V22022_2 <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */
	private double waveHeightByInstrument;

	 /**
	 * NO: 2.20  <br>
	 * nameCN: 浪级代码  <br>
	 * unit: 具体编码见表5 <br>
	 * BUFR FXY: V22303 <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */
	 private double waveLevel;
	
	public String getShipID() {
		return shipID;
	}
	public void setShipID(String shipID) {
		this.shipID = shipID;
	}
	public double getStationHeightAboveSea() {
		return stationHeightAboveSea;
	}
	public void setStationHeightAboveSea(double stationHeightAboveSea) {
		this.stationHeightAboveSea = stationHeightAboveSea;
	}
	public double getHeightOfAirpressureSensor() {
		return heightOfAirpressureSensor;
	}
	public void setHeightOfAirpressureSensor(double heightOfAirpressureSensor) {
		this.heightOfAirpressureSensor = heightOfAirpressureSensor;
	}
	public double getHeightOfWindSpeedSensor() {
		return heightOfWindSpeedSensor;
	}
	public void setHeightOfWindSpeedSensor(double heightOfWindSpeedSensor) {
		this.heightOfWindSpeedSensor = heightOfWindSpeedSensor;
	}
	public String getStationTypeStr() {
		return stationTypeStr;
	}
	public void setStationTypeStr(String stationTypeStr) {
		this.stationTypeStr = stationTypeStr;
	}
	public double getDistanceBetweenDeckAndSea() {
		return distanceBetweenDeckAndSea;
	}
	public void setDistanceBetweenDeckAndSea(double distanceBetweenDeckAndSea) {
		this.distanceBetweenDeckAndSea = distanceBetweenDeckAndSea;
	}
	public Date getObservationTime() {
		return observationTime;
	}
	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}
	public double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public int getShipMovingDir() {
		return shipMovingDir;
	}
	public void setShipMovingDir(int shipMovingDir) {
		this.shipMovingDir = shipMovingDir;
	}
	public double getShipMovingSpeed() {
		return shipMovingSpeed;
	}
	public void setShipMovingSpeed(double shipMovingSpeed) {
		this.shipMovingSpeed = shipMovingSpeed;
	}
	public double getBowAzimuth() {
		return bowAzimuth;
	}
	public void setBowAzimuth(double bowAzimuth) {
		this.bowAzimuth = bowAzimuth;
	}
	public int getWeatherCondition() {
		return weatherCondition;
	}
	public void setWeatherCondition(int weatherCondition) {
		this.weatherCondition = weatherCondition;
	}
	public double getSeaLevelPressure() {
		return seaLevelPressure;
	}
	public void setSeaLevelPressure(double seaLevelPressure) {
		this.seaLevelPressure = seaLevelPressure;
	}
	public double getDryballTemperature() {
		return dryballTemperature;
	}
	public void setDryballTemperature(double dryballTemperature) {
		this.dryballTemperature = dryballTemperature;
	}
	public double getWetballTemperature() {
		return wetballTemperature;
	}
	public void setWetballTemperature(double wetballTemperature) {
		this.wetballTemperature = wetballTemperature;
	}
	public double getWindDir() {
		return windDir;
	}
	public void setWindDir(double windDir) {
		this.windDir = windDir;
	}
	public double getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}
	public int getWindLevel() {
		return windLevel;
	}
	public void setWindLevel(int windLevel) {
		this.windLevel = windLevel;
	}
	public double getSeaTemperature() {
		return seaTemperature;
	}
	public void setSeaTemperature(double seaTemperature) {
		this.seaTemperature = seaTemperature;
	}
	public double getVisibility() {
		return visibility;
	}
	public void setVisibility(double visibility) {
		this.visibility = visibility;
	}
	public int getCloudShape() {
		return cloudShape;
	}
	public void setCloudShape(int cloudShape) {
		this.cloudShape = cloudShape;
	}
	public int getCloudAmount() {
		return cloudAmount;
	}
	public void setCloudAmount(int cloudAmount) {
		this.cloudAmount = cloudAmount;
	}
	public double getWaveHeightManually() {
		return waveHeightManually;
	}
	public void setWaveHeightManually(double waveHeightManually) {
		this.waveHeightManually = waveHeightManually;
	}
	public double getWaveHeightByInstrument() {
		return waveHeightByInstrument;
	}
	public void setWaveHeightByInstrument(double waveHeightByInstrument) {
		this.waveHeightByInstrument = waveHeightByInstrument;
	}
	public double getWaveLevel() {
		return waveLevel;
	}
	public void setWaveLevel(double waveLevel) {
		this.waveLevel = waveLevel;
	}
	public int getStationTypeInt() {
		return stationTypeInt;
	}
	public void setStationTypeInt(int stationTypeInt) {
		this.stationTypeInt = stationTypeInt;
	} 
	
}
