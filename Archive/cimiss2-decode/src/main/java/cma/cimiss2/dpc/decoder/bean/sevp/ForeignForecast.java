package cma.cimiss2.dpc.decoder.bean.sevp;

import java.util.Date;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	国外城市预报资料
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《国外城市预报资料要素表》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月24日 上午11:42:45   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class ForeignForecast{
	/**
	 * 资料时间<br>
	 * D_DATETIME<br>
	 * 
	 */
	private Date observationTime;
	/**
	 * 区站号(字符)	<br>
	 * V01301<br>
	 * 
	 */
	private String stationNumberChina = "";
	
	/**
	 * 经度<br>
	 * V06001<br>
	 * 度
	 */
	private double longtitude = 999999;
	/**
	 * 纬度<br>
	 * V05001<br>
	 * 度
	 */
	private double latitude = 999999;
	/**
	 * 高度<br>
	 * V07001<br>
	 * 1m
	 */
	private double height = 999999;
	/**
	 * 预报时效个数<br>
	 * V04320_040<br>
	 */
	private int numberOfForecastEfficiency = 0;
	/**
	 * 要素个数
	 */
	private int eleNum = 0;
	/**
	 * 预报天数<br>
	 * V04322
	 */
	private int forecastNumberOfDays = 0;
	/**
	 * 白天温度<br>
	 * V12052<br>
	 * 摄氏度
	 */
	private double dayTemperature = 999999;
	/**
	 * 晚上温度<br>
	 * V12053 <br>
	 * 摄氏度
	 */
	private double nightTemperature = 999999;
	/**
	 * 白天风向<br>
	 * V11303<br>
	 * 编码
	 */
	private double dayWindDir = 999999;
	/**
	 * 晚上风向<br>
	 * V11303_20_08<br>
	 * 编码
	 */
	private double nightWindDir = 999999;
	/**
	 * 白天风力<br>
	 * V11301<br>
	 * 编码
	 */
	private double dayWindPower = 999999;
	/**
	 * 晚上风力<br>
	 * V11301_20_08<br>
	 * 编码
	 */
	private double nightWindPower = 999999;
	/**
	 * 白天天气现象<br>
	 * V20312_08_20<br>
	 * 编码
	 */
	private int dayWeatherPheno = 999999;
	/**
	 * 晚上天气现象<br>
	 * V20312_20_08<br>
	 * 编码
	 */
	private int nightWeatherPheno = 999999;
	/**
	 * 白天云覆盖率<br>
	 * V20010<br>
	 * %
	 */
	private double dayCloudCoverageRate = 999999;
	/**
	 * 晚上云覆盖率<br>
	 * V20010_20_08<br>
	 * %
	 */
	private double nightCloudCoverageRate = 999999;
	/**
	 * 白天降水量<br>
	 * V13016<br>
	 * 毫米
	 */
	private double dayRainfall = 999999;
	/**
	 * 晚上降水量<br>
	 * V13016_20_08<br>
	 * 毫米
	 */
	private double nightRainfall = 999999;
	/**
	 * 白天降水概率<br>
	 * V13320_08_20<br>
	 * %
	 */
	private int dayPrecipitationProbability = 999999;
	/**
	 * 晚上降水概率<br>
	 * V13320_20_08<br>
	 * %
	 */
	private int nightPrecipitationProbability = 999999;
	/**
	 * 更正报标识<br>
	 * V_BBB
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
	public int getNumberOfForecastEfficiency() {
		return numberOfForecastEfficiency;
	}
	public void setNumberOfForecastEfficiency(int numberOfForecastEfficiency) {
		this.numberOfForecastEfficiency = numberOfForecastEfficiency;
	}
	public int getEleNum() {
		return eleNum;
	}
	public void setEleNum(int eleNum) {
		this.eleNum = eleNum;
	}
	public int getForecastNumberOfDays() {
		return forecastNumberOfDays;
	}
	public void setForecastNumberOfDays(int forecastNumberOfDays) {
		this.forecastNumberOfDays = forecastNumberOfDays;
	}
	public double getDayTemperature() {
		return dayTemperature;
	}
	public void setDayTemperature(double dayTemperature) {
		this.dayTemperature = dayTemperature;
	}
	public double getNightTemperature() {
		return nightTemperature;
	}
	public void setNightTemperature(double nightTemperature) {
		this.nightTemperature = nightTemperature;
	}
	public double getDayWindDir() {
		return dayWindDir;
	}
	public void setDayWindDir(double dayWindDir) {
		this.dayWindDir = dayWindDir;
	}
	public double getNightWindDir() {
		return nightWindDir;
	}
	public void setNightWindDir(double nightWindDir) {
		this.nightWindDir = nightWindDir;
	}
	public int getNightWeatherPheno() {
		return nightWeatherPheno;
	}
	public void setNightWeatherPheno(int nightWeatherPheno) {
		this.nightWeatherPheno = nightWeatherPheno;
	}
	public int getDayWeatherPheno() {
		return dayWeatherPheno;
	}
	public void setDayWeatherPheno(int dayWeatherPheno) {
		this.dayWeatherPheno = dayWeatherPheno;
	}
	public double getNightWindPower() {
		return nightWindPower;
	}
	public void setNightWindPower(double nightWindPower) {
		this.nightWindPower = nightWindPower;
	}
	public double getDayWindPower() {
		return dayWindPower;
	}
	public void setDayWindPower(double dayWindPower) {
		this.dayWindPower = dayWindPower;
	}
	public double getDayCloudCoverageRate() {
		return dayCloudCoverageRate;
	}
	public void setDayCloudCoverageRate(double dayCloudCoverageRate) {
		this.dayCloudCoverageRate = dayCloudCoverageRate;
	}
	public double getNightCloudCoverageRate() {
		return nightCloudCoverageRate;
	}
	public void setNightCloudCoverageRate(double nightCloudCoverageRate) {
		this.nightCloudCoverageRate = nightCloudCoverageRate;
	}
	public int getDayPrecipitationProbability() {
		return dayPrecipitationProbability;
	}
	public void setDayPrecipitationProbability(int dayPrecipitationProbability) {
		this.dayPrecipitationProbability = dayPrecipitationProbability;
	}
	public int getNightPrecipitationProbability() {
		return nightPrecipitationProbability;
	}
	public void setNightPrecipitationProbability(int nightPrecipitationProbability) {
		this.nightPrecipitationProbability = nightPrecipitationProbability;
	}
	public double getNightRainfall() {
		return nightRainfall;
	}
	public void setNightRainfall(double nightRainfall) {
		this.nightRainfall = nightRainfall;
	}
	public double getDayRainfall() {
		return dayRainfall;
	}
	public void setDayRainfall(double dayRainfall) {
		this.dayRainfall = dayRainfall;
	}
	public String getCorrectSign() {
		return correctSign;
	}
	public void setCorrectSign(String correctSign) {
		this.correctSign = correctSign;
	}
	
}