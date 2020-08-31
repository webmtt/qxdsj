package cma.cimiss2.dpc.decoder.bean.surf;

import java.util.Date;

/**
 * *****************************************************************************************<br> 
 * <strong>All Rights Reserved By National Meteorological Information Centre (NMIC)</strong><br>
 * *****************************************************************************************<br>
 * 铁塔数据解析类
 * <p>
 * 
 * notes:
 * <ul>
 * <li>定义参考了一下文档
 * 	<ol>
 * 		<li> <a href=" ">《北京气象塔平均场数据使用手册.doc》</a>
 * 		<li> <a href=" ">《香河102米气象塔平均场数据使用说明-王立志.doc》</a>
 * 	</ol>
 * </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年1月17日 下午3:43:32   wangzunpeng    Initial creation.
 * </pre>
 * 
 * @author wangzunpeng
 */
public class Tower {

	/**
	 * NO: 1.1  <br>
     * nameCN: 字符站号 <br>
     * unit: <br>
     * BUFR FXY: V01301 <br>
     * descriptionCN: CASXH或者CASBJ <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private String stationNumberC;
	/**
	 * NO: 1.2  <br>
     * nameCN: 观测平台距地面的高度 <br>
     * unit: 1m <br>
     * BUFR FXY: V07001 <br>
     * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double hight;
	/**
	 * NO: 1.3  <br>
     * nameCN: 西北方向伸臂上风速传感器观测值  <br>
     * unit: 米/秒 <br>
     * BUFR FXY: V11351_NW <br>
     * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double windSpeedNW;
	/**
	 * NO: 1.4  <br>
     * nameCN:  东南方向伸臂上风速传感器观测值 <br>
     * unit: 米/秒 <br>
     * BUFR FXY: V11351_SE <br>
     * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double windSpeedSE;

	/**
	 * NO: 1.5  <br>
     * nameCN: 风向传感器观测值 <br>
     * unit: 度 <br>
     * BUFR FXY: V11001 <br>
     * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double windDirection;
	/**
	 * NO: 1.6  <br>
     * nameCN: 相对湿度传感器观测值 <br>
     * unit: 100% <br>
     * BUFR FXY: V13003 <br>
     * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double relativeHumidity;
	/**
	 * NO: 1.7  <br>
     * nameCN: 大气温度传感器观测值 <br>
     * unit: 摄氏度 <br>
     * BUFR FXY: V12001 <br>
     * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double temperature;
	/**
	 * NO: 1.8  <br>
     * nameCN: 观测时间 <br>
     * unit: <br>
     * BUFR FXY: D_DATETIME <br>
     * descriptionCN: <br>
     * decode rule: 直接取值 <br>
     * field rule: 使用java时间转化工具类进行转化,标准形式为：yyyyMMddHHmmss
	 */
	private Date obsTime;

	public String getStationNumberC() {
		return stationNumberC;
	}

	public void setStationNumberC(String stationNumberC) {
		this.stationNumberC = stationNumberC;
	}

	public Double getHight() {
		return hight;
	}

	public void setHight(Double hight) {
		this.hight = hight;
	}

	public Double getWindSpeedNW() {
		return windSpeedNW;
	}

	public void setWindSpeedNW(Double windSpeedNW) {
		this.windSpeedNW = windSpeedNW;
	}

	public Double getWindSpeedSE() {
		return windSpeedSE;
	}

	public void setWindSpeedSE(Double windSpeedSE) {
		this.windSpeedSE = windSpeedSE;
	}

	public Double getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(Double windDirection) {
		this.windDirection = windDirection;
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

	public Date getObsTime() {
		return obsTime;
	}

	public void setObsTime(Date obsTime) {
		this.obsTime = obsTime;
	}

}
