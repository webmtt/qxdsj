package cma.cimiss2.dpc.decoder.bean.sevp;

import java.util.Date;

import cma.cimiss2.dpc.decoder.bean.QCElement;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	旅游景区负氧离子观测资料实体类
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《旅游区负氧离子观测资料要素表》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月24日 上午11:41:23   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class NegOxygenIon{
	/**
	 * 区站号(字符)<br>
	 * V01301 <br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private String stationNumberChina = "";
	/**
	 * 资料时间<br>  
	 * D_DATETIME<br>
	 * decode rule:直接取值<br>
     * field rule:使用java时间转化工具类进行转化,标准形式为：yyyyMMddHHmmss
	 */
	private Date observationTime;
	/**
	 * 纬度<br>
	 * V05001<br>
	 * 度<br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private double latitude = 999999;
	/**
	 * 经度<br>
	 * V06001<br>
	 * 度<br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private double longtitude = 999999;
	/**
	 * 测站海拔高度<br>
	 * V07001<br>
	 * 1m<br>
	 * decode rule:取值除以10<br>
	 * filed rule: 直接赋值
	 */
	private double height = 999999; 
	/**
	 * 测站地段标识 <br>
	 * V02001<br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private int siteMarkOfStation = 999999;
	/**
	 * 质量控制标识 <br>
	 * V02301<br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private int qualityContorl = 9;
	/**
	 * 负氧离子浓度<br>  
	 * V15551<br>
	 * 个/cm3<br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private QCElement<Double> negOxyIonConcentration;
	/**
	 * 最高负氧离子浓度  <br>
	 * V15551_005<br>
	 * 个/cm3<br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private QCElement<Double> maxNegOxyIonConcentration;
	/**
	 * 最高负氧离子浓度出现时间 <br>
	 * V15551_005_052<br>
	 * 时分<br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private QCElement<Integer> timeOfMaxNegOxyIonConcentration;
	/**
	 * 最低负氧离子浓度<br>
	 * V15551_006<br>
	 * 个/cm3<br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private QCElement<Double> minNegOxyIonConcentration;
	/**
	 * 最低负氧离子浓度出现时间 <br>
	 * V15551_006_052<br>
	 * 时分<br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private QCElement<Integer> timeOfMinNegOxyIonConcentration;
	/**
	 * 气温 <br>
	 * V12001<br>
	 * 摄氏度<br>
	 * decode rule:取值除以10<br>
	 * filed rule: 直接赋值
	 */
	private QCElement<Double> temperature ;
	/**
	 * 最高气温<br>
	 * V12011<br>
	 * 摄氏度<br>
	 * decode rule:取值除以10<br>
	 * filed rule: 直接赋值
	 */
	private QCElement<Double> maxTemperature ;
	/**
	 * 最高气温出现时间<br>
	 * V12011_052<br>
	 * 时分<br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private QCElement<Integer> timeOfMaxTemperature;
	/**
	 * 最低气温<br>
	 * V12012<br>
	 * 摄氏度<br>
	 * decode rule:取值除以10<br>
	 * filed rule: 直接赋值
	 */
	private QCElement<Double> minTemperature;
	/**
	 * 最低气温出现时间	<br>
	 * V12012_052<br>
	 * 时分<br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private QCElement<Integer> timeOfMinTemperature;
	/**
	 * 相对湿度<br>
	 * V13003<br>
	 * %<br>
	 * decode rule:取值除以10<br>
	 * filed rule: 直接赋值
	 */
	private QCElement<Double> relativeHumidity;
	/**
	 * 最大相对湿度	<br>
	 * V13008<br>
	 * %<br>
	 * decode rule:取值除以10<br>
	 * filed rule: 直接赋值
	 */
	private QCElement<Double> maxRelativeHumidity;
	/**
	 * 最大相对湿度出现时间   <br>
	 * V13008_052<br>
	 * 时分<br>
	 * decode rule:直接取值<br>
	 * filed rule: 直接赋值
	 */
	private QCElement<Integer> timeOfMaxRelativeHumidity;
	/**
	 * 露点温度	<br>
	 * V12003<br>
	 * 摄氏度<br>
	 * decode rule:取值除以10<br>
	 * filed rule: 直接赋值
	 */
	private QCElement<Double> dewPoint;
	/**
	 * 水汽压<br>
	 * V13004<br>
	 * 百帕<br>
	 * decode rule:取值除以10<br>
	 * filed rule: 直接赋值
	 */
	private QCElement<Double> waterVaporPressure;
	
	public String getStationNumberChina() {
		return stationNumberChina;
	}
	public void setStationNumberChina(String stationNumberChina) {
		this.stationNumberChina = stationNumberChina;
	}
	public Date getObservationTime() {
		return observationTime;
	}
	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public int getSiteMarkOfStation() {
		return siteMarkOfStation;
	}
	public void setSiteMarkOfStation(int siteMarkOfStation) {
		this.siteMarkOfStation = siteMarkOfStation;
	}
	public int getQualityContorl() {
		return qualityContorl;
	}
	public void setQualityContorl(int qualityContorl) {
		this.qualityContorl = qualityContorl;
	}
	public QCElement<Double> getNegOxyIonConcentration() {
		return negOxyIonConcentration;
	}
	public void setNegOxyIonConcentration(QCElement<Double> negOxyIonConcentration) {
		this.negOxyIonConcentration = negOxyIonConcentration;
	}
	public QCElement<Double> getMaxNegOxyIonConcentration() {
		return maxNegOxyIonConcentration;
	}
	public void setMaxNegOxyIonConcentration(QCElement<Double> maxNegOxyIonConcentration) {
		this.maxNegOxyIonConcentration = maxNegOxyIonConcentration;
	}
	public QCElement<Double> getMinNegOxyIonConcentration() {
		return minNegOxyIonConcentration;
	}
	public void setMinNegOxyIonConcentration(QCElement<Double> minNegOxyIonConcentration) {
		this.minNegOxyIonConcentration = minNegOxyIonConcentration;
	}
	public QCElement<Integer> getTimeOfMaxNegOxyIonConcentration() {
		return timeOfMaxNegOxyIonConcentration;
	}
	public void setTimeOfMaxNegOxyIonConcentration(QCElement<Integer> timeOfMaxNegOxyIonConcentration) {
		this.timeOfMaxNegOxyIonConcentration = timeOfMaxNegOxyIonConcentration;
	}
	public QCElement<Double> getRelativeHumidity() {
		return relativeHumidity;
	}
	public void setRelativeHumidity(QCElement<Double> relativeHumidity) {
		this.relativeHumidity = relativeHumidity;
	}
	public QCElement<Integer> getTimeOfMinNegOxyIonConcentration() {
		return timeOfMinNegOxyIonConcentration;
	}
	public void setTimeOfMinNegOxyIonConcentration(QCElement<Integer> timeOfMinNegOxyIonConcentration) {
		this.timeOfMinNegOxyIonConcentration = timeOfMinNegOxyIonConcentration;
	}
	public QCElement<Double> getMaxRelativeHumidity() {
		return maxRelativeHumidity;
	}
	public void setMaxRelativeHumidity(QCElement<Double> maxRelativeHumidity) {
		this.maxRelativeHumidity = maxRelativeHumidity;
	}
	public QCElement<Integer> getTimeOfMaxRelativeHumidity() {
		return timeOfMaxRelativeHumidity;
	}
	public void setTimeOfMaxRelativeHumidity(QCElement<Integer> timeOfMaxRelativeHumidity) {
		this.timeOfMaxRelativeHumidity = timeOfMaxRelativeHumidity;
	}
	public QCElement<Double> getDewPoint() {
		return dewPoint;
	}
	public void setDewPoint(QCElement<Double> dewPoint) {
		this.dewPoint = dewPoint;
	}
	public QCElement<Double> getWaterVaporPressure() {
		return waterVaporPressure;
	}
	public void setWaterVaporPressure(QCElement<Double> waterVaporPressure) {
		this.waterVaporPressure = waterVaporPressure;
	}
	public QCElement<Double> getTemperature() {
		return temperature;
	}
	public void setTemperature(QCElement<Double> temperature) {
		this.temperature = temperature;
	}
	public QCElement<Double> getMaxTemperature() {
		return maxTemperature;
	}
	public void setMaxTemperature(QCElement<Double> maxTemperature) {
		this.maxTemperature = maxTemperature;
	}
	public QCElement<Integer> getTimeOfMaxTemperature() {
		return timeOfMaxTemperature;
	}
	public void setTimeOfMaxTemperature(QCElement<Integer> timeOfMaxTemperature) {
		this.timeOfMaxTemperature = timeOfMaxTemperature;
	}
	public QCElement<Integer> getTimeOfMinTemperature() {
		return timeOfMinTemperature;
	}
	public void setTimeOfMinTemperature(QCElement<Integer> timeOfMinTemperature) {
		this.timeOfMinTemperature = timeOfMinTemperature;
	}
	public QCElement<Double> getMinTemperature() {
		return minTemperature;
	}
	public void setMinTemperature(QCElement<Double> minTemperature) {
		this.minTemperature = minTemperature;
	}

  
}