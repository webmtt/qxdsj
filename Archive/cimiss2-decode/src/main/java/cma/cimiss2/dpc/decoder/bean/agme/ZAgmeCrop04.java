package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	 产量因素子要素<br>
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《农业气象作物要素—产量因素与产量结构 》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月21日 下午11:58:05   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class ZAgmeCrop04 {

	/**
	 * NO: 1.1  <br>
	 * nameCN: 区站号 <br>
	 * unit: <br>
	 * BUFR FXY: V01301 <br>
	 * descriptionCN: 5位数字或第1位为字母，第2-5位为数字<br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private String stationNumberChina;

	/**
	 * NO: 1.2  <br>
	 * nameCN: 纬度 <br>
	 * unit: 度  <br>
	 * BUFR FXY: V05001 <br>
	 * descriptionCN:<br>
	 * decode rule: 取值为度分秒，转化为度<br>
	 * field rule: 直接赋值<br>
	 */
	private Double latitude;
	/**
	 * NO: 1.3  <br>
	 * nameCN: 经度 <br>
	 * unit: 度  <br>
	 * BUFR FXY: V06001 <br>
	 * descriptionCN:<br>
	 * decode rule: 取值为度分秒，转化为度<br>
	 * field rule: 直接赋值<br>
	 */
	private Double longitude;
	/**
	 * NO: 1.4  <br>
	 * nameCN: 观测场拔海高度 <br>
	 * unit: 1m <br>
	 * BUFR FXY: V07001 <br>
	 * descriptionCN: 若低于海平面，为负值<br>
	 * decode rule: 取值 * 0.1<br>
	 * field rule: 直接赋值<br>
	 */
	private Double heightOfSationGroundAboveMeanSeaLevel;
	
	/**
	 * NO: 1.5  <br>
	 * nameCN: 气压传感器拔海高度 <br>
	 * unit: 1m <br>
	 * BUFR FXY: V07031 <br>
	 * descriptionCN: <br>
	 * decode rule: 取值 * 0.1<br>
	 * field rule: 直接赋值<br>
	 */
	private Double heightOfPressureSensor;
	
	/**
	 * NO: 2.1  <br>
	 * nameCN: 测定时间 <br>
	 * unit: 日期 <br>
	 * BUFR FXY: V04001/V04002/V04003 <br>
	 * descriptionCN: 年月日时分秒<br>
	 * decode rule: 直接取值 <br>
	 * field rule: 使用java时间转化工具类进行转化, 标准形式为：yyyyMMddHHmmss <br>
	 */
	private Date ObservationDate;
	
	/**
	 * NO: 2.2  <br>
	 * nameCN: 作物名称 <br>
	 * unit: 编码 <br>
	 * BUFR FXY:  V71001<br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private int CropName;
	
	/**
	 * NO: 2.3  <br>
	 * nameCN: 发育期 <br>
	 * unit: 编码 <br>
	 * BUFR FXY: V71002 <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private int PeriodOfGrowth;
	
	/**
	 * NO: 2.4  <br>
	 * nameCN: 项目名称 <br>
	 * unit: 编码 <br>
	 * BUFR FXY: V71616_01 <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private int ItemName1;
	
	/**
	 * NO: 2.5  <br>
	 * nameCN: 测定值 <br>
	 * unit: 1 <br>
	 * BUFR FXY: V71632 <br>
	 * descriptionCN: 各项目测值均保留2位小数 <br>
	 * decode rule: 取值  * 0.01<br>
	 * field rule: 直接赋值<br>
	 */
	private float ObservationValue;

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
	 * Gets the longitude.
	 *
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * Sets the longitude.
	 *
	 * @param longitude the new longitude
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * Gets the height of sation ground above mean sea level.
	 *
	 * @return the height of sation ground above mean sea level
	 */
	public Double getHeightOfSationGroundAboveMeanSeaLevel() {
		return heightOfSationGroundAboveMeanSeaLevel;
	}

	/**
	 * Sets the height of sation ground above mean sea level.
	 *
	 * @param heightOfSationGroundAboveMeanSeaLevel the new height of sation ground above mean sea level
	 */
	public void setHeightOfSationGroundAboveMeanSeaLevel(Double heightOfSationGroundAboveMeanSeaLevel) {
		this.heightOfSationGroundAboveMeanSeaLevel = heightOfSationGroundAboveMeanSeaLevel;
	}

	/**
	 * Gets the height of pressure sensor.
	 *
	 * @return the height of pressure sensor
	 */
	public Double getHeightOfPressureSensor() {
		return heightOfPressureSensor;
	}

	/**
	 * Sets the height of pressure sensor.
	 *
	 * @param heightOfPressureSensor the new height of pressure sensor
	 */
	public void setHeightOfPressureSensor(Double heightOfPressureSensor) {
		this.heightOfPressureSensor = heightOfPressureSensor;
	}

	/**
	 * Gets the observation date.
	 *
	 * @return the observation date
	 */
	public Date getObservationDate() {
		return ObservationDate;
	}

	/**
	 * Sets the observation date.
	 *
	 * @param observationDate the new observation date
	 */
	public void setObservationDate(Date observationDate) {
		ObservationDate = observationDate;
	}

	/**
	 * Gets the crop name.
	 *
	 * @return the crop name
	 */
	public int getCropName() {
		return CropName;
	}

	/**
	 * Sets the crop name.
	 *
	 * @param cropName the new crop name
	 */
	public void setCropName(int cropName) {
		CropName = cropName;
	}

	/**
	 * Gets the period of growth.
	 *
	 * @return the period of growth
	 */
	public int getPeriodOfGrowth() {
		return PeriodOfGrowth;
	}

	/**
	 * Sets the period of growth.
	 *
	 * @param periodOfGrowth the new period of growth
	 */
	public void setPeriodOfGrowth(int periodOfGrowth) {
		PeriodOfGrowth = periodOfGrowth;
	}

	/**
	 * Gets the item name 1.
	 *
	 * @return the item name 1
	 */
	public int getItemName1() {
		return ItemName1;
	}

	/**
	 * Sets the item name 1.
	 *
	 * @param itemName1 the new item name 1
	 */
	public void setItemName1(int itemName1) {
		ItemName1 = itemName1;
	}

	/**
	 * Gets the observation value.
	 *
	 * @return the observation value
	 */
	public float getObservationValue() {
		return ObservationValue;
	}

	/**
	 * Sets the observation value.
	 *
	 * @param observationValue the new observation value
	 */
	public void setObservationValue(float observationValue) {
		ObservationValue = observationValue;
	}

	/**
	 * Gets the station number china.
	 *
	 * @return the station number china
	 */
	public String getStationNumberChina() {
		return stationNumberChina;
	}

	/**
	 * Sets the station number china.
	 *
	 * @param stationNumberChina the new station number china
	 */
	public void setStationNumberChina(String stationNumberChina) {
		this.stationNumberChina = stationNumberChina;
	}
	
}
