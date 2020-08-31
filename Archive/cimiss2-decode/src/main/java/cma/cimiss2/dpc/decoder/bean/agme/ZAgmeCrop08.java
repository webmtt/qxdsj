package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	 植株分器官干物质重量要素<br>
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《农业气象作物要素—植株分器官干物质重量数据表 》 </a>
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
public class ZAgmeCrop08 {

	/**
	 * NO: 1.1  <br>
	 * nameCN: 区站号 <br>
	 * unit: <br>
	 * BUFR FXY: V01301 <br>
	 * descriptionCN: 5位数字或第1位为字母，第2-5位为数字
	 */
	private String stationNumberChina;

	/**
	 * NO: 1.2  <br>
	 * nameCN: 纬度 <br>
	 * unit: 度  <br>
	 * BUFR FXY: V05001 <br>
	 * descriptionCN:<br>
	 * decode rule: 取值为度分秒，转化为度 <br>
	 * field rule: 直接赋值 <br>
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
	 * nameCN: 器官名称 <br>
	 * unit: 编码 <br>
	 * BUFR FXY: V71650 <br>
	 * descriptionCN: 0为整株；1为叶；2为叶鞘；3为茎；4为果实<br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private int OrganName;
	
	/**
	 * NO: 2.5  <br>
	 * nameCN: 株茎鲜重 <br>
	 * unit: 1克  <br>
	 * BUFR FXY: V71651 <br>
	 * descriptionCN: 器官鲜重<br>
	 * decode rule: 取值 * 0.001<br>
	 * field rule: 直接赋值<br>
	 */
	private float PlantFreshWeight;
	
	/**
	 * NO: 2.6  <br>
	 * nameCN: 株茎干重 <br>
	 * unit: 1克  <br>
	 * BUFR FXY: V71652 <br>
	 * descriptionCN: 器官干重<br>
	 * decode rule: 取值 * 0.001<br>
	 * field rule: 直接赋值<br>
	 */
	private float PlantDryWeight;
	
	/**
	 * NO: 2.7  <br>
	 * nameCN: 平方米株茎鲜重 <br>
	 * unit: 1克/平方米  <br>
	 * BUFR FXY: V71653 <br>
	 * descriptionCN: 1平方米器官鲜重<br>
	 * decode rule: 取值 * 0.1<br>
	 * field rule: 直接赋值<br>
	 */
	private float AvgFreshWeightPerSquareMeter;
	
	/**
	 * NO: 2.8  <br>
	 * nameCN: 平方米株茎干重 <br>
	 * unit: 1克/平方米  <br>
	 * BUFR FXY: V71654 <br>
	 * descriptionCN: 1平方米器官干重<br>
	 * decode rule: 取值 * 0.1<br>
	 * field rule: 直接赋值<br>
	 */
	private float AvgDryWeightPerSquareMeter;
	
	/**
	 * NO: 2.9  <br>
	 * nameCN: 含水率 <br>
	 * unit: ％ <br>
	 * BUFR FXY: V71655 <br>
	 * descriptionCN: 器官含水率<br>
	 * decode rule: 取值 * 0.1<br>
	 * field rule: 直接赋值<br>
	 */
	private float PercentageOfWater;
	
	/**
	 * NO: 2.10  <br>
	 * nameCN: 生长率 <br>
	 * unit: 1克/（平方米.日） <br>
	 * BUFR FXY: V71656 <br>
	 * descriptionCN: 器官的干物质增长量<br>
	 * decode rule: 取值 * 0.1<br>
	 * field rule: 直接赋值<br>
	 */
	private float PercentageOfGrowth;


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
	 * Gets the organ name.
	 *
	 * @return the organ name
	 */
	public int getOrganName() {
		return OrganName;
	}

	/**
	 * Sets the organ name.
	 *
	 * @param organName the new organ name
	 */
	public void setOrganName(int organName) {
		OrganName = organName;
	}

	/**
	 * Gets the plant fresh weight.
	 *
	 * @return the plant fresh weight
	 */
	public float getPlantFreshWeight() {
		return PlantFreshWeight;
	}

	/**
	 * Sets the plant fresh weight.
	 *
	 * @param plantFreshWeight the new plant fresh weight
	 */
	public void setPlantFreshWeight(float plantFreshWeight) {
		PlantFreshWeight = plantFreshWeight;
	}

	/**
	 * Gets the plant dry weight.
	 *
	 * @return the plant dry weight
	 */
	public float getPlantDryWeight() {
		return PlantDryWeight;
	}

	/**
	 * Sets the plant dry weight.
	 *
	 * @param plantDryWeight the new plant dry weight
	 */
	public void setPlantDryWeight(float plantDryWeight) {
		PlantDryWeight = plantDryWeight;
	}

	/**
	 * Gets the avg fresh weight per square meter.
	 *
	 * @return the avg fresh weight per square meter
	 */
	public float getAvgFreshWeightPerSquareMeter() {
		return AvgFreshWeightPerSquareMeter;
	}

	/**
	 * Sets the avg fresh weight per square meter.
	 *
	 * @param avgFreshWeightPerSquareMeter the new avg fresh weight per square meter
	 */
	public void setAvgFreshWeightPerSquareMeter(float avgFreshWeightPerSquareMeter) {
		AvgFreshWeightPerSquareMeter = avgFreshWeightPerSquareMeter;
	}

	/**
	 * Gets the avg dry weight per square meter.
	 *
	 * @return the avg dry weight per square meter
	 */
	public float getAvgDryWeightPerSquareMeter() {
		return AvgDryWeightPerSquareMeter;
	}

	/**
	 * Sets the avg dry weight per square meter.
	 *
	 * @param avgDryWeightPerSquareMeter the new avg dry weight per square meter
	 */
	public void setAvgDryWeightPerSquareMeter(float avgDryWeightPerSquareMeter) {
		AvgDryWeightPerSquareMeter = avgDryWeightPerSquareMeter;
	}

	/**
	 * Gets the percentage of water.
	 *
	 * @return the percentage of water
	 */
	public float getPercentageOfWater() {
		return PercentageOfWater;
	}

	/**
	 * Sets the percentage of water.
	 *
	 * @param percentageOfWater the new percentage of water
	 */
	public void setPercentageOfWater(float percentageOfWater) {
		PercentageOfWater = percentageOfWater;
	}

	/**
	 * Gets the percentage of growth.
	 *
	 * @return the percentage of growth
	 */
	public float getPercentageOfGrowth() {
		return PercentageOfGrowth;
	}

	/**
	 * Sets the percentage of growth.
	 *
	 * @param percentageOfGrowth the new percentage of growth
	 */
	public void setPercentageOfGrowth(float percentageOfGrowth) {
		PercentageOfGrowth = percentageOfGrowth;
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
