package cma.cimiss2.dpc.decoder.bean.sevp;

import java.util.Date;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	国外城市实况数据实体类
 *	
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《国外城市气象站实况资料要素表》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月24日 上午11:42:02   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class ForeignLive{
	/**
	 * 资料时间 <br>
	 * D_DATETIME
	 */
	private Date observationTime;
	/**
	 * 区站号(字符)<br>
	 * V01301<br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private String stationNumberChina = "";
	/**
	 * 经度<br>
	 * V06001<br>
	 * 度<br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private double longtitude = 999999;
	/**
	 * 纬度<br>
	 * V05001<br>
	 * 度<br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private double latitude = 999999;
	/**
	 * 高度<br>
	 * V07001<br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private double height = 999999;
	/**
	 * 温度 <br>
	 * V12001<br>
	 * 摄氏度<br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private double temperature = 999999;
	/**
	 * 湿度<br>
	 * V13003<br>
	 * %<br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private double humidity = 999999;
	/**
	 * 露点温度<br>
	 * V12003<br>
	 * 摄氏度<br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private double dewpoint = 999999;
	/**
	 * 风向<br>
	 * V11001<br>
	 * 编码<br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private String windDir = "999999";
	/**
	 * 风速<br>
	 * V11002<br>
	 * m/s<br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private double windSpeed = 999999;
	/**
	 * 气压<br>
	 * V10004<br>
	 * hPa<br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private double airPressure = 999999;
	/**
	 * 天气现象<br>
	 * V20312<br>
	 * 编码<br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private int weatherPheno = 999999;
	/**
	 * 能见度<br>
	 * V20001<br>
	 * 1m<br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private double visibility = 999999;
	/**
	 * 云覆盖率<br>
	 * V20010<br>
	 * %<br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private double cloudCoverageRate = 999999;
	/**
	 * 体表温度<br>
	 * V12510<br>
	 * 摄氏度<br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private double surfaceTemperature = 999999;
	/**
	 * 更正报标识<br>
	 * V_BBB<br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private String correctSign = "000";
	
	public Date getObservationTime() {
		return observationTime;
	}
	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}
	public String getStationNumberChina() {
		return stationNumberChina;
	}
	public void setStationNumberChina(String stationNumberChina) {
		this.stationNumberChina = stationNumberChina;
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
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public double getHumidity() {
		return humidity;
	}
	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}
	public double getDewpoint() {
		return dewpoint;
	}
	public void setDewpoint(double dewpoint) {
		this.dewpoint = dewpoint;
	}
	public String getWindDir() {
		return windDir;
	}
	public void setWindDir(String windDir) {
		this.windDir = windDir;
	}
	public double getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}
	public double getAirPressure() {
		return airPressure;
	}
	public void setAirPressure(double airPressure) {
		this.airPressure = airPressure;
	}
	public int getWeatherPheno() {
		return weatherPheno;
	}
	public void setWeatherPheno(int weatherPheno) {
		this.weatherPheno = weatherPheno;
	}
	public double getVisibility() {
		return visibility;
	}
	public void setVisibility(double visibility) {
		this.visibility = visibility;
	}
	public double getCloudCoverageRate() {
		return cloudCoverageRate;
	}
	public void setCloudCoverageRate(double cloudCoverageRate) {
		this.cloudCoverageRate = cloudCoverageRate;
	}
	public double getSurfaceTemperature() {
		return surfaceTemperature;
	}
	public void setSurfaceTemperature(double surfaceTemperature) {
		this.surfaceTemperature = surfaceTemperature;
	}
	public String getCorrectSign() {
		return correctSign;
	}
	public void setCorrectSign(String correctSign) {
		this.correctSign = correctSign;
	}
	
}