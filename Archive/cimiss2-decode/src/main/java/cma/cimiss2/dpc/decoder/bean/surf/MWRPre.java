package cma.cimiss2.dpc.decoder.bean.surf;

import java.util.Date;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	水利部降雨资料实体
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《水利部降水数据表》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月24日 下午5:16:46   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class MWRPre {
	
	/**
	 * NO: 1  <br>
	 * nameCN: 区站号 <br>
	 * unit: <br>
	 * BUFR FXY: V01301 <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private String stationNumberChina;
	
	/**
	 * NO: 2  <br>
	 * nameCN: 资料时间  <br>
	 * unit: <br>
	 * BUFR FXY: V04001\V04002\V04003\V04004 <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:使用java时间转化工具类进行转化,标准形式为：yyyyMMddHHmmss
	 */
	private Date observationTime;
	
	/**
	 * NO: 3  <br>
	 * nameCN: 24小时（日）雨量  <br>
	 * unit:  1mm<br>
	 * BUFR FXY: V13023 <br>
	 * descriptionCN: <br>
	 * decode rule:取值除以10<br>
     * field rule:直接赋值
	 */
	private float  dailyRainfall;
	
	/**
	 * NO:4  <br>
	 * nameCN: 6小时雨量  <br>
	 * unit:  <br>
	 * BUFR FXY: V13021 <br>
	 * descriptionCN: <br>
	 * decode rule:取值除以10<br>
     * field rule:直接赋值
	 */
	private float hourlyRainfall;
	
	/**
	 * NO:5  <br>
	 * nameCN: 纬度  <br>
	 * unit: 度 <br>
	 * BUFR FXY: V05001 <br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private double latitude = 999999;
	
	/**
	 * NO:6  <br>
	 * nameCN: 经度  <br>
	 * unit: 度 <br>
	 * BUFR FXY: V06001 <br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private double longtitude = 999999;
	
	/**
	 * NO:7  <br>
	 * nameCN: 小时还是日值数据  <br>
	 * unit:  <br>
	 * BUFR FXY: V_TIMESCALE <br>
	 * descriptionCN: <br>
	 * decode rule: 由文件名获取：文件名以PAD、pad开始、或者以D、d结束的为日值数据，其它为小时数据<br>
     * field rule:直接赋值
	 */
	private String timeScale = "";
	
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

	public float getDailyRainfall() {
		return dailyRainfall;
	}

	public void setDailyRainfall(float dailyRainfall) {
		this.dailyRainfall = dailyRainfall;
	}

	public float getHourlyRainfall() {
		return hourlyRainfall;
	}

	public void setHourlyRainfall(float hourlyRainfall) {
		this.hourlyRainfall = hourlyRainfall;
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

	public String getTimeScale() {
		return timeScale;
	}

	public void setTimeScale(String timeScale) {
		this.timeScale = timeScale;
	}
}
