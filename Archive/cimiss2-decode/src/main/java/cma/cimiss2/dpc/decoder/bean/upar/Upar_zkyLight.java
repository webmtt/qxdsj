package cma.cimiss2.dpc.decoder.bean.upar;

import java.util.Date;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * 中科院闪电数据要素资料实体类
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《中科院闪电数据表》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月27日 下午5:32:02   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class Upar_zkyLight {
	/**
	 * NO: 1  <br>
	 * nameCN: 闪电位置纬度 <br>
	 * unit: 度 <br>
	 * BUFR FXY: V05001 <br>
	 * descriptionCN: 闪电位置纬度<br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private double latitude;
	
	/**
	 * NO: 2  <br>
	 * nameCN: 闪电位置经度 <br>
	 * unit: 度 <br>
	 * BUFR FXY: V06001 <br>
	 * descriptionCN: 闪电位置经度 <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private double longtitude;
	
	/**
	 * NO: 3   <br>
	 * nameCN: 电流强度 <br>
	 * unit: KA <br>
	 * BUFR FXY: V73016 <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private double currentIntensity;
	/**
	 * NO: 4  <br>
	 * nameCN: 回击最大陡度 <br>
	 * unit: KA/us <br>
	 * BUFR FXY: V73023 <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private double maxSlope;
	
	/**
	 * NO: 5  <br>
	 * nameCN: 定位误差  <br>
	 * unit: 1m <br>
	 * BUFR FXY: V73011 <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private double error;
	
	/**
	 * NO: 6  <br>
	 * nameCN: 定位方式  <br>
	 * unit: 代码 <br>
	 * BUFR FXY: V73110 <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private String locationType;
	
	/**
	 * NO: 7 <br>
	 * nameCN: 观测时间  <br>
	 * unit:  <br>
	 * BUFR FXY: D_DATETIME <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:使用java时间转化工具类进行转化,标准形式为：yyyyMMddHHmmss
	 */
	private Date observationTime;
	
	/**
	 * NO: 8 <br>
	 * nameCN: 观测时间 毫秒  <br>
	 * unit:  <br>
	 * BUFR FXY: V04007 <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private int millisecond;
	
	/**
	 * NO: 9 <br>
	 * nameCN: 雷电地理位置信息省 <br>
	 * unit:  <br>
	 * BUFR FXY: V01015_1 <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private String prov;
	/**
	 * NO: 10 <br>
	 * nameCN: 雷电地理位置信息市 <br>
	 * unit:  <br>
	 * BUFR FXY: V01015_2 <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private String district;
	/**
	 * NO: 11 <br>
	 * nameCN: 雷电地理位置信息县 <br>
	 * unit:  <br>
	 * BUFR FXY: V01015_3 <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private String country;
	/**
	 * NO: 12 <br>
	 * nameCN: 标志位 <br>
	 * unit:  <br>
	 * BUFR FXY: V_PROCESSFLAG <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private int processFlag;
	/**
	 * NO: 13 <br>
	 * nameCN: 定位仪编号 <br>
	 * unit:  <br>
	 * BUFR FXY: V_USEDIDS <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private String usedIDs;
	
	/**
	 * NO: 14 <br>
	 * nameCN: 云/地闪 <br>
	 * unit:  <br>
	 * BUFR FXY: V_CG_IC <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private String CG_IC;
	
	/**
	 * NO: 15  <br>
	 * nameCN: 闪电位置高度 <br>
	 * unit: 1m <br>
	 * BUFR FXY: V07001 <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private double height;
	
	public Date getObservationTime() {
		return observationTime;
	}
	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(Double longtitude) {
		this.longtitude = longtitude;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public double getCurrentIntensity() {
		return currentIntensity;
	}
	public void setCurrentIntensity(double currentIntensity) {
		this.currentIntensity = currentIntensity;
	}
	public double getMaxSlope() {
		return maxSlope;
	}
	public void setMaxSlope(double maxSlope) {
		this.maxSlope = maxSlope;
	}
	public double getError() {
		return error;
	}
	public void setError(double error) {
		this.error = error;
	}
	public String getLocationType() {
		return locationType;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	public int getMillisecond() {
		return millisecond;
	}
	public void setMillisecond(int millisecond) {
		this.millisecond = millisecond;
	}
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public int getProcessFlag() {
		return processFlag;
	}
	public void setProcessFlag(int processFlag) {
		this.processFlag = processFlag;
	}
	public String getUsedIDs() {
		return usedIDs;
	}
	public void setUsedIDs(String usedIDs) {
		this.usedIDs = usedIDs;
	}
	public String getCG_IC() {
		return CG_IC;
	}
	public void setCG_IC(String cG_IC) {
		CG_IC = cG_IC;
	}
}
