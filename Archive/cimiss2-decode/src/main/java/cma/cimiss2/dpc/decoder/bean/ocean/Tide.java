package cma.cimiss2.dpc.decoder.bean.ocean;

import java.util.Date;

/**
 * 全球潮位站观测资料实体类
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
   全球潮位站观测资料实体类
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《全球潮位观测数据表.docx》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月24日 上午11:17:06   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class Tide {
	/**
	 * NO: 1.1 <br>
	 * nameCN: 区站号 <br>
	 * unit: <br>
	 * BUFR FXY: V01301 <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private String stationNumberChina;
	
	/**
	 * NO: 1.2  <br>
	 * nameCN: 纬度 <br>
	 * unit:  度  <br>
	 * BUFR FXY: V05001 <br>
	 * descriptionCN:<br>
     * decode rule:从配置文件StationInfo_Config.lua取值<br>
     * field rule:直接赋值
	 */
	private double latitude = 999999.0;
	/**
	 * NO: 1.3  <br>
	 * nameCN: 经度 <br>
	 * unit:  度  <br>
	 * BUFR FXY: V06001 <br>
	 * descriptionCN:<br>
     * decode rule:从配置文件StationInfo_Config.lua取值<br>
     * field rule:直接赋值
	 */
	private double longtitude = 999999.0;
	
	/**
	 * NO: 1.4  <br>
	 * nameCN: 观测时间 <br>
	 * unit:  <br>
	 * BUFR FXY: D_datetime <br>
	 * descriptionCN: <br>
     * decode rule: 取值为“序日.当天过去秒数”，解析为日期时间 <br>
     * field rule:使用java时间转化工具类进行转化,标准形式为：yyyyMMddHHmmss
	 */
	private Date observationTime;
	
	/**
	 * NO: 1.5  <br>
	 * nameCN: 传感器类型 <br>
	 * unit: 代码表 <br>
	 * BUFR FXY: V02007 <br>
	 * descriptionCN: <br>
     * decode rule:从文件名取值，例如Z_OCEN_C_HYJ_20170603183712_O_TIDE-zihu_flt_154.txt，flt为传感器类型<br>
     * field rule:直接赋值
	 */
	private String sensorType;
	/**
	 * NO: 1.6  <br>
	 * nameCN: 本地海图基准面潮位高度  <br>
	 * unit: 米  <br>
	 * BUFR FXY: V22038 <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private double tidalHeightAboveChartDatum = 999999.0;
	
	/**
	 * NO: 1.7  <br>
	 * nameCN: 海温 <br>
	 * unit: 摄氏度  <br>
	 * BUFR FXY: V22042 <br>
	 * descriptionCN: <br>
     * decode rule: 缺省值999998 <br>
     * field rule:直接赋值
	 */
	private double seaTemperature = 999998.0;
	
	/**
	 * NO: 1.8  <br>
	 * nameCN: 自动水位检测 <br>
	 * unit: 代码表  <br>
	 * BUFR FXY: V22120 <br>
	 * descriptionCN: <br>
     * decode rule:缺省值999998<br>
     * field rule:直接赋值
	 */
	private double autoWaterLevelDetection = 999998.0;

	/**
	 * NO: 1.9  <br>
	 * nameCN: 人工水位检测 <br>
	 * unit: 代码表  <br>
	 * BUFR FXY: V22121 <br>
	 * descriptionCN: <br>
     * decode rule: 缺省值999998<br>
     * field rule:直接赋值
	 */
	private double manuallyWaterLevelDetection = 999998.0;
	/**
	 * NO: 1.10  <br>
	 * nameCN: 气象残余潮位高度 <br>
	 * unit:  米  <br>
	 * BUFR FXY: V22039 <br>
	 * descriptionCN: <br>
     * decode rule: 缺省值999998<br>
     * field rule:直接赋值
	 */
	private double residualTidalHeight = 999998.0;
	
	public String getStationNumberChina() {
		return stationNumberChina;
	}
	public void setStationNumberChina(String stationNumberChina) {
		this.stationNumberChina = stationNumberChina;
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
	public Date getObservationTime() {
		return observationTime;
	}
	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}
	public String getSensorType() {
		return sensorType;
	}
	public void setSensorType(String sensorType) {
		this.sensorType = sensorType;
	}
	public double getTidalHeightAboveChartDatum() {
		return tidalHeightAboveChartDatum;
	}
	public void setTidalHeightAboveChartDatum(double tidalHeightAboveChartDatum) {
		this.tidalHeightAboveChartDatum = tidalHeightAboveChartDatum;
	}
	public double getSeaTemperature() {
		return seaTemperature;
	}
	public void setSeaTemperature(double seaTemperature) {
		this.seaTemperature = seaTemperature;
	}
	public double getAutoWaterLevelDetection() {
		return autoWaterLevelDetection;
	}
	public void setAutoWaterLevelDetection(double autoWaterLevelDetection) {
		this.autoWaterLevelDetection = autoWaterLevelDetection;
	}
	public double getManuallyWaterLevelDetection() {
		return manuallyWaterLevelDetection;
	}
	public void setManuallyWaterLevelDetection(double manuallyWaterLevelDetection) {
		this.manuallyWaterLevelDetection = manuallyWaterLevelDetection;
	}
	public double getResidualTidalHeight() {
		return residualTidalHeight;
	}
	public void setResidualTidalHeight(double residualTidalHeight) {
		this.residualTidalHeight = residualTidalHeight;
	}
}
