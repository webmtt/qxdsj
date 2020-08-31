package cma.cimiss2.dpc.decoder.bean.sevp;

import java.util.Date;

import cma.cimiss2.dpc.decoder.sevp.BullHeader;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * 城镇天气预报报告实体类
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《城镇天气预报报告（FS）资料要素表》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月24日 上午11:43:32   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class CityWeatherForeCast{
	/**
	 * 报头BullHeader
	 */
	private BullHeader bullHeader;
	/**
	 * 资料时间
	 */
	private Date observationTime;
	/**
	 * 区站号(字符)	V01301
	 */
	private String stationNumberChina;
	/**
	 * 更正报标识
	 */
	private String correctSign = "000";
	/**
	 * 预报时效  V04320
	 */
	private int forecastEfficiency = 0;
	/**
	 * 20时－08时（北京时）天气现象	V20312_20_08  decimal(6) 代码表
	 */
	private int weatherPhenomenon20_08 = 999998;
	/**
	 * 08时－20时（北京时）天气现象	V20312_08_20	decimal(6) 代码表
	 */
	private int weatherPhenomenon08_20 = 999998;
	/**
	 * 风向	V11303	decimal(10,4)	代码表
	 */
	private double windDir = 999998;
	/**
	 * 风的转向	V11313	decimal(10,4) 代码表
	 */
	private double windTurnDir = 999998;
	/**
	 *风力	V11301	decimal(6)	代码表 
	 */
	private int windLevel = 999998;
	/**
	 * 转后风力	V11302	decimal(6)	代码表
	 */
	private int windTurnLevel = 999998;
	/**
	 * 最低气温	V12021	decimal(10，4)	单位：摄氏度
	 */
	private int minTemperature = 999998;
	/**
	 * 最高气温	V12022	decimal(10，4)	单位：摄氏度
	 */
	private int maxTemperature = 999998;
	
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
	public String getCorrectSign() {
		return correctSign;
	}
	public void setCorrectSign(String correctSign) {
		this.correctSign = correctSign;
	}
	public int getForecastEfficiency() {
		return forecastEfficiency;
	}
	public void setForecastEfficiency(int forecastEfficiency) {
		this.forecastEfficiency = forecastEfficiency;
	}
	public int getWeatherPhenomenon20_08() {
		return weatherPhenomenon20_08;
	}
	public void setWeatherPhenomenon20_08(int weatherPhenomenon20_08) {
		this.weatherPhenomenon20_08 = weatherPhenomenon20_08;
	}
	public int getWeatherPhenomenon08_20() {
		return weatherPhenomenon08_20;
	}
	public void setWeatherPhenomenon08_20(int weatherPhenomenon08_20) {
		this.weatherPhenomenon08_20 = weatherPhenomenon08_20;
	}
	public double getWindDir() {
		return windDir;
	}
	public void setWindDir(double windDir) {
		this.windDir = windDir;
	}
	public double getWindTurnDir() {
		return windTurnDir;
	}
	public void setWindTurnDir(double windTurnDir) {
		this.windTurnDir = windTurnDir;
	}
	public int getWindLevel() {
		return windLevel;
	}
	public void setWindLevel(int windLevel) {
		this.windLevel = windLevel;
	}
	public int getWindTurnLevel() {
		return windTurnLevel;
	}
	public void setWindTurnLevel(int windTurnLevel) {
		this.windTurnLevel = windTurnLevel;
	}
	public int getMinTemperature() {
		return minTemperature;
	}
	public void setMinTemperature(int minTemperature) {
		this.minTemperature = minTemperature;
	}
	public int getMaxTemperature() {
		return maxTemperature;
	}
	public void setMaxTemperature(int maxTemperature) {
		this.maxTemperature = maxTemperature;
	}
	public BullHeader getBullHeader() {
		return bullHeader;
	}
	public void setBullHeader(BullHeader bullHeader) {
		this.bullHeader = bullHeader;
	}
}