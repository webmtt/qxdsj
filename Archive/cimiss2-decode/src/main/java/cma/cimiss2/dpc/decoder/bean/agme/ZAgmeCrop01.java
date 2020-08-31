package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	作物生长发育子要素<br>
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《农业气象作物要素—作物生长发育数据表》 </a>
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
public class ZAgmeCrop01 {

	/**
	 * NO: 1.1 <br>
	 * nameCN: 区站号 <br>
	 * unit: <br>
	 * BUFR FXY: V01301 <br>
	 * descriptionCN: 5位数字或第1位为字母，第2-5位为数字 <br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private String stationNumberChina;
	
	/**
	 * NO: 1.2  <br>
	 * nameCN: 纬度 <br>
	 * unit: 度  <br>
	 * BUFR FXY: V05001 <br>
	 * descriptionCN: <br>
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
	 * decode rule: 取值为度分秒，转化为度 <br>
	 * field rule: 直接赋值 <br>
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
	 * nameCN: 作物名称 <br>
	 * unit: 编码 <br>
	 * BUFR FXY: V71001 <br>
	 * descriptionCN:<br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private int CropName;
	
	/**
	 * NO: 2.2  <br>
	 * nameCN: 发育期 <br>
	 * unit: 编码<br>
	 * BUFR FXY: V71002 <br>
	 * descriptionCN:<br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private int PeriodOfGrowth;
	
	/**
	 * NO: 2.3  <br>
	 * nameCN: 发育时间 <br>
	 * unit: 日期 <br>
	 * BUFR FXY: V04001_03/V04002_03/V04003_03 <br>
	 * descriptionCN: 年月日时分秒(国际时，YYYYMMddhhmmss)<br>
	 * decode rule: 直接取值 <br>
	 * field rule: 使用java时间转化工具类进行转化, 标准形式为：yyyyMMddHHmmss <br>
	 */
	private Date GrowthDate;
	
	/**
	 * NO: 2.4  <br>
	 * nameCN: 发育期距平 <br>
	 * unit: 天<br>
	 * BUFR FXY: V71005 <br>
	 * descriptionCN:<br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private int DevelopAnomaly;
	
	/**
	 * NO: 2.5 <br>
	 * nameCN: 发育期百分率
	 * unit: %<br>
	 * BUFR FXY:  V71010<br>
	 * descriptionCN:<br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private float PercentageOfGrowthPeriod;
	
	/**
	 * NO: 2.6  <br>
	 * nameCN: 生长状况 <br>
	 * unit: 无<br>
	 * BUFR FXY: V71007 <br>
	 * descriptionCN: 1.为一类苗;2.为二类苗;3.为三类苗;4.三类苗以上<br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private int GrowthState;
	
	/**
	 * NO: 2.7 <br>
	 * nameCN: 植株高度 <br>
	 * unit: 厘米 <br>
	 * BUFR FXY: V71006 <br>
	 * descriptionCN:<br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private float PlantHeight;
	
	/**
	 * NO: 2.8 <br>
	 * nameCN: 植株密度 <br>
	 * unit: 1株（茎）数/平方米 <br>
	 * BUFR FXY:  V71008<br>
	 * descriptionCN: <br>
	 * decode rule: 取值 * 0.01<br>
	 * field rule: 直接赋值<br>
	 */
	private float PlantDensity;

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
	 * Gets the growth date.
	 *
	 * @return the growth date
	 */
	public Date getGrowthDate() {
		return GrowthDate;
	}

	/**
	 * Sets the growth date.
	 *
	 * @param growthDate the new growth date
	 */
	public void setGrowthDate(Date growthDate) {
		GrowthDate = growthDate;
	}

	/**
	 * Gets the develop anomaly.
	 *
	 * @return the develop anomaly
	 */
	public int getDevelopAnomaly() {
		return DevelopAnomaly;
	}

	/**
	 * Sets the develop anomaly.
	 *
	 * @param developAnomaly the new develop anomaly
	 */
	public void setDevelopAnomaly(int developAnomaly) {
		DevelopAnomaly = developAnomaly;
	}

	/**
	 * Gets the percentage of growth period.
	 *
	 * @return the percentage of growth period
	 */
	public float getPercentageOfGrowthPeriod() {
		return PercentageOfGrowthPeriod;
	}

	/**
	 * Sets the percentage of growth period.
	 *
	 * @param percentageOfGrowthPeriod the new percentage of growth period
	 */
	public void setPercentageOfGrowthPeriod(float percentageOfGrowthPeriod) {
		PercentageOfGrowthPeriod = percentageOfGrowthPeriod;
	}

	/**
	 * Gets the growth state.
	 *
	 * @return the growth state
	 */
	public int getGrowthState() {
		return GrowthState;
	}

	/**
	 * Sets the growth state.
	 *
	 * @param growthState the new growth state
	 */
	public void setGrowthState(int growthState) {
		GrowthState = growthState;
	}

	/**
	 * Gets the plant height.
	 *
	 * @return the plant height
	 */
	public float getPlantHeight() {
		return PlantHeight;
	}

	/**
	 * Sets the plant height.
	 *
	 * @param plantHeight the new plant height
	 */
	public void setPlantHeight(float plantHeight) {
		PlantHeight = plantHeight;
	}

	/**
	 * Gets the plant density.
	 *
	 * @return the plant density
	 */
	public float getPlantDensity() {
		return PlantDensity;
	}

	/**
	 * Sets the plant density.
	 *
	 * @param plantDensity the new plant density
	 */
	public void setPlantDensity(float plantDensity) {
		PlantDensity = plantDensity;
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
