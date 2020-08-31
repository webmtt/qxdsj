package cma.cimiss2.dpc.decoder.bean.upar;

import java.util.Date;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * 
 * 国家 闪电 数据资料实体类
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《ADTD系统雷电定位资料数据表》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月28日 上午10:20:38   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class LightingData {

	/**
	 * NO: 1.1  <br>
	 * nameCN: 雷电序号 <br>
	 * unit: <br>
	 * BUFR FXY: V08300 <br>
	 * descriptionCN: 数字 <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	String ldpId;
	/**
	 * NO: 1.2  <br>
	 * nameCN: 观测时间 <br>
	 * unit: <br>
	 * BUFR FXY: V04001/V04002/V04003/V04004/V04005/V04006 <br>
	 * descriptionCN:<br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	Date observationTime;

	/**
	 * NO: 1.3  <br>
	 * nameCN: 观测时间 毫秒 <br>
	 * unit: <br>
	 * BUFR FXY: V04007 <br>
	 * descriptionCN:<br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	int Millis;

	/**
	 * NO: 1.4  <br>
	 * nameCN: 纬度 <br>
	 * unit: 度 <br>
	 * BUFR FXY: V05001 <br>
	 * descriptionCN:<br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	Double latitude;

	/**
	 * NO: 1.5  <br>
	 * nameCN: 经度 <br>
	 * unit: 度 <br>
	 * BUFR FXY: V06001 <br>
	 * descriptionCN:<br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	Double longitude;

	/**
	 * NO: 1.6  <br>
	 * nameCN: 强度 <br>
	 * unit: KA <br>
	 * BUFR FXY:V73016  <br>
	 * descriptionCN:回击峰值强度<br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	Double intensity;

	/**
	 * NO: 1.7  <br>
	 * nameCN: 陡度 <br>
	 * unit: KA/us <br>
	 * BUFR FXY: V73023 <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	Double steepness;

	/**
	 * NO: 1.8  <br>
	 * nameCN: 误差值 <br>
	 * unit: 1m <br>
	 * BUFR FXY: V73011 <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	Double errorValue;

	/**
	 * NO: 1.9  <br>
	 * nameCN: 定位方式 <br>
	 * unit: 代码表 <br>
	 * BUFR FXY: V73110 <br>
	 * descriptionCN: 定位方式 <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	int locateMode;

	/**
	 * NO: 1.10  <br>
	 * nameCN: 雷电地理位置信息省 <br>
	 * unit: 无 <br>
	 * BUFR FXY: V01015_1 <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	String province;

	/**
	 * NO: 1.11  <br>
	 * nameCN: 雷电地理位置信息市 <br>
	 * unit: 无 <br>
	 * BUFR FXY: V01015_2<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	String city;

	/**
	 * NO: 1.12  <br>
	 * nameCN: 雷电地理位置信息县 <br>
	 * unit: 无 <br>
	 * BUFR FXY: V01015_3 <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	String county;

	public String getLdpId() {
		return ldpId;
	}

	public void setLdpId(String ldpId) {
		this.ldpId = ldpId;
	}

	public Date getObservationTime() {
		return observationTime;
	}

	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}

	public int getMillis() {
		return Millis;
	}

	public void setMillis(int millis) {
		Millis = millis;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getIntensity() {
		return intensity;
	}

	public void setIntensity(Double intensity) {
		this.intensity = intensity;
	}

	public Double getSteepness() {
		return steepness;
	}

	public void setSteepness(Double steepness) {
		this.steepness = steepness;
	}

	public Double getErrorValue() {
		return errorValue;
	}

	public void setErrorValue(Double errorValue) {
		this.errorValue = errorValue;
	}

	public int getLocateMode() {
		return locateMode;
	}

	public void setLocateMode(int locateMode) {
		this.locateMode = locateMode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

}
