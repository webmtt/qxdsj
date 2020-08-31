package cma.cimiss2.dpc.decoder.bean.ocean;

import java.util.Date;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
     中远人工观测船舶资料实体类
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
 * 2019年7月30日 上午10:40:00   liym    Initial creation.
 * </pre>
 * 
 * @author liym
 * @version 0.0.1
 */
public class OriShip {
	
	/**
	 * NO: 1.1  <br>
	 * nameCN: 船舶名称 <br>
	 * unit: <br>
	 * BUFR FXY:  <br>
	 * descriptionCN:船舶拼音名称 <br>
     * decode rule: <br>
     * field rule:
	 */
	private String shipName;

	/**
	 * NO: 1.2  <br>
	 * nameCN: 呼号  <br>
	 * unit:  <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 船舶呼号<br>
     * decode rule: <br>
     * field rule:
	 */
	private String shipCallSign;
	/**
	 * NO: 1.3  <br>
	 * nameCN:  船位时间 <br>
	 * unit:  <br>
	 * BUFR FXY: V07304 <br>
	 * descriptionCN: 格式YYYYMMDDhhmm，其中YYYY表示年，MM表示月，DD表示日，hh表示小时，mm表示分钟。 说明：此时间为北京时。<br>
     * decode rule: <br>
     * field rule:
	 */
	private Date shiptime;	
	/**
	 * NO: 1.4  <br>
	 * nameCN:  经度 <br>
	 * unit: 度  <br>
	 * BUFR FXY: V06001 <br>
	 * descriptionCN:  <br>
     * decode rule: 度分转化为度 <br>
     * field rule: 
	 */
	private double longtitude;
	/**
	 * NO: 1.5  <br>
	 * nameCN:  纬度 <br>
	 * unit: 度  <br>
	 * BUFR FXY: V05001 <br>
	 * descriptionCN:  <br>
     * decode rule: 度分转化为度 <br>
     * field rule: 
	 */
	private double latitude;
	/**
	 * NO: 1.6  <br>
	 * nameCN:  天气情况 <br>
	 * unit: 代码 <br>
	 * BUFR FXY: V20003 <br>
	 * descriptionCN: <br>
     * decode rule: 报文编码转换为天气代码的缩写  <br>
     * field rule:按照天气代码的缩写
	 */
	 private int weatherCondition;
	 /**
	 * NO: 1.7  <br>
	 * nameCN:  海平面气压 <br>
	 * unit: 单位百帕，保留1位小数 <br>
	 * BUFR FXY: V10051 <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */
	 private double seaLevelPressure;
	 /**
	 * NO: 1.8  <br>
	 * nameCN:  干球温度 <br>
	 * unit: 单位度，整数<br>
	 * BUFR FXY: V12001 <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */
	  private double dryballTemperature;
	  
	  /**
	 * NO: 1.9  <br>
	 * nameCN: 湿球温度 <br>
	 * unit:单位度，整数<br>
	 * BUFR FXY: V12002 <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */  
	 private double wetballTemperature;
	 /**
	 * NO: 1.10  <br>
	 * nameCN: 风向 <br>
	 * unit: 用1个或多个英文字母N、S、W、E表示 <br>
	 * BUFR FXY: V11001 <br>
	 * descriptionCN: 解码后需按风向代表码转化为中心角度<br>
     * decode rule:   <br>
     * field rule:
	 */
	 private double windDir;
	 /**
		 * NO: 1.10_2  <br>
		 * nameCN: 风速 <br>
		 * unit: m/s<br>
		 * BUFR FXY: V11001 <br>
		 * descriptionCN: 直接取值<br>
	     * decode rule:   <br>
	     * field rule:
		 */
	private double windSpeed;
	 
	 /**
	 * NO: 1.11  <br>
	 * nameCN: 风力级别 <br>
	 * unit: 数字表示 <br>
	 * BUFR FXY: V11301 <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */ 
	 private int windLevel;
	 /**
	 * NO: 1.12  <br>
	 * nameCN: 海水温度 <br>
	 * unit: 单位度，整数 <br>
	 * BUFR FXY: V22042 <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */ 
	 private double seaTemperature;
	 /**
	 * NO: 1.13 <br>
	 * nameCN: 能见度级别 <br>
	 * unit:   <br>
	 * BUFR FXY:  <br>
	 * descriptionCN: 级别<br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */ 
	 private double visibilityLevel;
	 
	 /**
	 * NO: 1.14  <br>
	 * nameCN: 云状 <br>
	 * unit: 代码 <br>
	 * BUFR FXY: V20012 <br>
	 * descriptionCN: 报文中若无该项数据，以*号替代，代表未观测该项数据<br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */ 
	 private int cloudShape;
	 
	 /**
	 * NO: 1.15  <br>
	 * nameCN: 云量 <br>
	 * unit: 代码 <br>
	 * BUFR FXY: V20011 <br>
	 * descriptionCN:报文中若无该项数据，以*号替代，代表未观测该项数据 <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */
	 private int cloudAmount;
	 /**
	 * NO: 1.16  <br>
	 * nameCN: 浪高  <br>
	 * unit: 米，保留1位小数 <br>
	 * BUFR FXY: V22022_1 <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */
	private double  waveHeightManually;
	/**
	 * NO: 1.17 <br>
	 * nameCN:  船舶航行速度 <br>
	 * unit: 报文单位0.1米/秒，保留1位小数 <br>
	 * BUFR FXY: V01013 <br>
	 * descriptionCN:表结构单位米/秒 <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */
	 private double shipMovingSpeed;
	 /**
	 * NO: 1.18  <br>
	 * nameCN:  船艏方位 <br>
	 * unit:度 <br>
	 * BUFR FXY: V05021 <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值  <br>
     * field rule:直接赋值
	 */
	 private double bowAzimuth;
	public String getShipName() {
		return shipName;
	}
	public String getShipCallSign() {
		return shipCallSign;
	}
	public Date getShiptime() {
		return shiptime;
	}
	public double getLongtitude() {
		return longtitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public int getWeatherCondition() {
		return weatherCondition;
	}
	public double getSeaLevelPressure() {
		return seaLevelPressure;
	}
	public double getDryballTemperature() {
		return dryballTemperature;
	}
	public double getWetballTemperature() {
		return wetballTemperature;
	}
	public double getWindDir() {
		return windDir;
	}
	public int getWindLevel() {
		return windLevel;
	}
	public double getSeaTemperature() {
		return seaTemperature;
	}
	public double getVisibilityLevel() {
		return visibilityLevel;
	}
	public int getCloudShape() {
		return cloudShape;
	}
	public int getCloudAmount() {
		return cloudAmount;
	}
	public double getWaveHeightManually() {
		return waveHeightManually;
	}
	public double getShipMovingSpeed() {
		return shipMovingSpeed;
	}
	public double getBowAzimuth() {
		return bowAzimuth;
	}
	public void setShipName(String shipName) {
		this.shipName = shipName;
	}
	public void setShipCallSign(String shipCallSign) {
		this.shipCallSign = shipCallSign;
	}
	public void setShiptime(Date shiptime) {
		this.shiptime = shiptime;
	}
	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public void setWeatherCondition(int weatherCondition) {
		this.weatherCondition = weatherCondition;
	}
	public void setSeaLevelPressure(double seaLevelPressure) {
		this.seaLevelPressure = seaLevelPressure;
	}
	public void setDryballTemperature(double dryballTemperature) {
		this.dryballTemperature = dryballTemperature;
	}
	public void setWetballTemperature(double wetballTemperature) {
		this.wetballTemperature = wetballTemperature;
	}
	public void setWindDir(double windDir) {
		this.windDir = windDir;
	}
	public void setWindLevel(int windLevel) {
		this.windLevel = windLevel;
	}
	public void setSeaTemperature(double seaTemperature) {
		this.seaTemperature = seaTemperature;
	}
	public void setVisibilityLevel(double visibilityLevel) {
		this.visibilityLevel = visibilityLevel;
	}
	public void setCloudShape(int cloudShape) {
		this.cloudShape = cloudShape;
	}
	public void setCloudAmount(int cloudAmount) {
		this.cloudAmount = cloudAmount;
	}
	public void setWaveHeightManually(double waveHeightManually) {
		this.waveHeightManually = waveHeightManually;
	}
	public void setShipMovingSpeed(double shipMovingSpeed) {
		this.shipMovingSpeed = shipMovingSpeed;
	}
	public void setBowAzimuth(double bowAzimuth) {
		this.bowAzimuth = bowAzimuth;
	}
	public double getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}
	 
	
	
	 
}
