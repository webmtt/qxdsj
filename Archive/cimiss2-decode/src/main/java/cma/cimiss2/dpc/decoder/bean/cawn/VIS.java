package cma.cimiss2.dpc.decoder.bean.cawn;

import java.util.Date;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
       气溶胶能见度VIS观测资料实体类。解析时，各要素值均从报文直接取值、入库时各字段直接赋值。
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《大气成分气溶胶能见度（VIS）数据表.docx》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月24日 上午11:19:52   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class VIS {
	/**
	 * NO. 1  <br>
	 * nameCN: 区站号 <br>
	 * unit: <br>
	 * BUFR FXY: V01301 <br>
	 * description: 区站号
	 */
	private String stationNumberChina;
	/**
	 * NO. 2
	 * nameCN:项目代码 <br>
	 * unit: <br>
	 * BUFR FXY: V_ITEM_CODE<br>
	 * descriptionCN:项目代码 代码表
	 */
	private int itemCode = 999998;
	/**
	 * NO. 3
	 * nameCN: 资料观测年 <br>
	 * unit: <br>
	 * BUFR FXY: V04001<br>
	 * descriptionCN:资料观测年
	 */
	private int dataObservationYear;
	/**
	 *  NO. 4
	 * nameCN: 资料观测年序日 <br>
	 * unit: <br>
	 * BUFR FXY: V4002 V4003<br>
	 * descriptionCN:
	 */
	private int julianday;
	/**
	 * NO. 5
	 * nameCN: 观测资料时间（时分秒） <br>
	 * unit: <br>
	 * BUFR FXY: V4004\V4005\V04006<br>
	 * descriptionCN:
	 */
	private String obserHHmmss;
	/**
	 * NO. 5
	 * nameCN: 资料观测时间 <br>
	 * unit: <br>
	 * BUFR FXY: D_DATETIME <br>
	 * descriptionCN: 年月日时分秒
	 */
	private Date dataObservationTime;
	/**
	 *  NO. 6 
	 *  nameCN: 状态码（仪器）
	 *  unit: 代码表 <br>
	 *  BUFR FXY: V02321 <br>
	 *  descriptionCN:  
	 */
	private int stateCode = 999998;
	/**
	 *  NO. 7 
	 *  nameCN: 1分钟平均能见度（米）
	 *  unit: 米 <br>
	 *  BUFR FXY: V20001_701_01 <br>
	 *  descriptionCN:  
	 */
	private double visibility_1min = 999998;
	/**
	 *  NO. 8 
	 *  nameCN: 10分钟平均能见度（米）
	 *  unit: 米 <br>
	 *  BUFR FXY: V20001_701_10 <br>
	 *  descriptionCN:  
	 */
	private double visibility_10min = 999998;
	/**
	 *  NO. 9 
	 *  nameCN: 能见度变化趋势
	 *  unit:  %  <br>
	 *  BUFR FXY: V20311 <br>
	 *  descriptionCN:  
	 */
	private double trend = 999998;
	
	public String getStationNumberChina() {
		return stationNumberChina;
	}
	public void setStationNumberChina(String stationNumberChina) {
		this.stationNumberChina = stationNumberChina;
	}
	public int getItemCode() {
		return itemCode;
	}
	public void setItemCode(int itemCode) {
		this.itemCode = itemCode;
	}
	public int getDataObservationYear() {
		return dataObservationYear;
	}
	public void setDataObservationYear(int dataObservationYear) {
		this.dataObservationYear = dataObservationYear;
	}
	public int getJulianday() {
		return julianday;
	}
	public void setJulianday(int julianday) {
		this.julianday = julianday;
	}
	public Date getDataObservationTime() {
		return dataObservationTime;
	}
	public void setDataObservationTime(Date dataObservationTime) {
		this.dataObservationTime = dataObservationTime;
	}
	public int getStateCode() {
		return stateCode;
	}
	public void setStateCode(int stateCode) {
		this.stateCode = stateCode;
	}
	public double getVisibility_1min() {
		return visibility_1min;
	}
	public void setVisibility_1min(double visibility_1min) {
		this.visibility_1min = visibility_1min;
	}
	public double getVisibility_10min() {
		return visibility_10min;
	}
	public void setVisibility_10min(double visibility_10min) {
		this.visibility_10min = visibility_10min;
	}
	public double getTrend() {
		return trend;
	}
	public void setTrend(double trend) {
		this.trend = trend;
	}
	public String getObserHHmmss() {
		return obserHHmmss;
	}
	public void setObserHHmmss(String obserHHmmss) {
		this.obserHHmmss = obserHHmmss;
	}
}
