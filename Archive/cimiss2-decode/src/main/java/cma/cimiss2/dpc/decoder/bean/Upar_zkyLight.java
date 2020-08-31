package cma.cimiss2.dpc.decoder.bean;

import java.util.Date;

// TODO: Auto-generated Javadoc
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
	
	/** NO: 1  <br> nameCN: 闪电位置纬度 <br> unit: 度 <br> BUFR FXY: V05001 <br> descriptionCN: 闪电位置纬度<br> decode rule:直接取值<br> field rule:直接赋值. */
	private double latitude;
	
	/** NO: 2  <br> nameCN: 闪电位置经度 <br> unit: 度 <br> BUFR FXY: V06001 <br> descriptionCN: 闪电位置经度 <br> decode rule:直接取值<br> field rule:直接赋值. */
	private double longtitude;
	
	/** NO: 3   <br> nameCN: 电流强度 <br> unit: KA <br> BUFR FXY: V73016 <br> descriptionCN: <br> decode rule:直接取值<br> field rule:直接赋值. */
	private double currentIntensity;
	
	/** NO: 4  <br> nameCN: 回击最大陡度 <br> unit: KA/us <br> BUFR FXY: V73023 <br> descriptionCN: <br> decode rule:直接取值<br> field rule:直接赋值. */
	private double maxSlope;
	
	/** NO: 5  <br> nameCN: 定位误差  <br> unit: 1m <br> BUFR FXY: V73011 <br> descriptionCN: <br> decode rule:直接取值<br> field rule:直接赋值. */
	private double error;
	
	/** NO: 6  <br> nameCN: 定位方式  <br> unit: 代码 <br> BUFR FXY: V73110 <br> descriptionCN: <br> decode rule:直接取值<br> field rule:直接赋值. */
	private String locationType;
	
	/** NO: 7 <br> nameCN: 观测时间  <br> unit:  <br> BUFR FXY: D_DATETIME <br> descriptionCN: <br> decode rule:直接取值<br> field rule:使用java时间转化工具类进行转化,标准形式为：yyyyMMddHHmmss. */
	private Date observationTime;
	
	/** NO: 8 <br> nameCN: 观测时间 毫秒  <br> unit:  <br> BUFR FXY: V04007 <br> descriptionCN: <br> decode rule:直接取值<br> field rule:直接赋值. */
	private int millisecond;
	
	/** NO: 9 <br> nameCN: 雷电地理位置信息省 <br> unit:  <br> BUFR FXY: V01015_1 <br> descriptionCN: <br> decode rule:直接取值<br> field rule:直接赋值. */
	private String prov;
	
	/** NO: 10 <br> nameCN: 雷电地理位置信息市 <br> unit:  <br> BUFR FXY: V01015_2 <br> descriptionCN: <br> decode rule:直接取值<br> field rule:直接赋值. */
	private String district;
	
	/** NO: 11 <br> nameCN: 雷电地理位置信息县 <br> unit:  <br> BUFR FXY: V01015_3 <br> descriptionCN: <br> decode rule:直接取值<br> field rule:直接赋值. */
	private String country;
	
	/** NO: 12 <br> nameCN: 标志位 <br> unit:  <br> BUFR FXY: V_PROCESSFLAG <br> descriptionCN: <br> decode rule:直接取值<br> field rule:直接赋值. */
	private int processFlag;
	
	/** NO: 13 <br> nameCN: 定位仪编号 <br> unit:  <br> BUFR FXY: V_USEDIDS <br> descriptionCN: <br> decode rule:直接取值<br> field rule:直接赋值. */
	private String usedIDs;
	
	/** NO: 14 <br> nameCN: 云/地闪 <br> unit:  <br> BUFR FXY: V_CG_IC <br> descriptionCN: <br> decode rule:直接取值<br> field rule:直接赋值. */
	private String CG_IC;
	
	/** NO: 15  <br> nameCN: 闪电位置高度 <br> unit: 1m <br> BUFR FXY: V07001 <br> descriptionCN: <br> decode rule:直接取值<br> field rule:直接赋值. */
	private double height;
	
	/**
	 * Gets the observation time.
	 *
	 * @return the observation time
	 */
	public Date getObservationTime() {
		return observationTime;
	}
	
	/**
	 * Sets the observation time.
	 *
	 * @param observationTime the new observation time
	 */
	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}
	
	/**
	 * Gets the latitude.
	 *
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}
	
	/**
	 * Sets the latitude.
	 *
	 * @param latitude the new latitude
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * Gets the longtitude.
	 *
	 * @return the longtitude
	 */
	public Double getLongtitude() {
		return longtitude;
	}
	
	/**
	 * Sets the longtitude.
	 *
	 * @param longtitude the new longtitude
	 */
	public void setLongtitude(Double longtitude) {
		this.longtitude = longtitude;
	}
	
	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public double getHeight() {
		return height;
	}
	
	/**
	 * Sets the height.
	 *
	 * @param height the new height
	 */
	public void setHeight(double height) {
		this.height = height;
	}
	
	/**
	 * Gets the current intensity.
	 *
	 * @return the current intensity
	 */
	public double getCurrentIntensity() {
		return currentIntensity;
	}
	
	/**
	 * Sets the current intensity.
	 *
	 * @param currentIntensity the new current intensity
	 */
	public void setCurrentIntensity(double currentIntensity) {
		this.currentIntensity = currentIntensity;
	}
	
	/**
	 * Gets the max slope.
	 *
	 * @return the max slope
	 */
	public double getMaxSlope() {
		return maxSlope;
	}
	
	/**
	 * Sets the max slope.
	 *
	 * @param maxSlope the new max slope
	 */
	public void setMaxSlope(double maxSlope) {
		this.maxSlope = maxSlope;
	}
	
	/**
	 * Gets the error.
	 *
	 * @return the error
	 */
	public double getError() {
		return error;
	}
	
	/**
	 * Sets the error.
	 *
	 * @param error the new error
	 */
	public void setError(double error) {
		this.error = error;
	}
	
	/**
	 * Gets the location type.
	 *
	 * @return the location type
	 */
	public String getLocationType() {
		return locationType;
	}
	
	/**
	 * Sets the location type.
	 *
	 * @param locationType the new location type
	 */
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	
	/**
	 * Gets the millisecond.
	 *
	 * @return the millisecond
	 */
	public int getMillisecond() {
		return millisecond;
	}
	
	/**
	 * Sets the millisecond.
	 *
	 * @param millisecond the new millisecond
	 */
	public void setMillisecond(int millisecond) {
		this.millisecond = millisecond;
	}
	
	/**
	 * Gets the prov.
	 *
	 * @return the prov
	 */
	public String getProv() {
		return prov;
	}
	
	/**
	 * Sets the prov.
	 *
	 * @param prov the new prov
	 */
	public void setProv(String prov) {
		this.prov = prov;
	}
	
	/**
	 * Gets the district.
	 *
	 * @return the district
	 */
	public String getDistrict() {
		return district;
	}
	
	/**
	 * Sets the district.
	 *
	 * @param district the new district
	 */
	public void setDistrict(String district) {
		this.district = district;
	}
	
	/**
	 * Gets the country.
	 *
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	
	/**
	 * Sets the country.
	 *
	 * @param country the new country
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	
	/**
	 * Gets the process flag.
	 *
	 * @return the process flag
	 */
	public int getProcessFlag() {
		return processFlag;
	}
	
	/**
	 * Sets the process flag.
	 *
	 * @param processFlag the new process flag
	 */
	public void setProcessFlag(int processFlag) {
		this.processFlag = processFlag;
	}
	
	/**
	 * Gets the used I ds.
	 *
	 * @return the used I ds
	 */
	public String getUsedIDs() {
		return usedIDs;
	}
	
	/**
	 * Sets the used I ds.
	 *
	 * @param usedIDs the new used I ds
	 */
	public void setUsedIDs(String usedIDs) {
		this.usedIDs = usedIDs;
	}
	
	/**
	 * Gets the cg ic.
	 *
	 * @return the cg ic
	 */
	public String getCG_IC() {
		return CG_IC;
	}
	
	/**
	 * Sets the cg ic.
	 *
	 * @param cG_IC the new cg ic
	 */
	public void setCG_IC(String cG_IC) {
		CG_IC = cG_IC;
	}
}
