
package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	 大田生育状况调查要素<br>
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《 农业气象作物要素—大田生育状况调查数据表》 </a>
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
public class ZAgmeCrop10 {

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
	 * descriptionCN: 若低于海平面，为负值 <br>
	 * decode rule: 取值 * 0.1<br>
	 * field rule: 直接赋值<br>
	 */
	private Double heightOfSationGroundAboveMeanSeaLevel;
	
	/**
	 * NO: 1.5  <br>
	 * nameCN: 气压传感器拔海高度 <br>
	 * unit: 1m <br>
	 * BUFR FXY: V07031 <br>
	 * descriptionCN:  <br>
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
	 * nameCN: 大田水平  <br>
	 * unit: 无  <br>
	 * BUFR FXY: V71903 <br>
	 * descriptionCN: 0为上等田；1为中等田；2为下等田<br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private int FieldLevel;
	
	/**
	 * NO: 2.3  <br>
	 * nameCN: 作物名称 <br>
	 * unit: 编码 <br>
	 * BUFR FXY: V71001 <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private int CropName;
	
	/**
	 * NO: 2.4  <br>
	 * nameCN: 发育期 <br>
	 * unit: 编码 <br>
	 * BUFR FXY: V71002 <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private int PeriodOfGrowth;
	
	/**
	 * NO: 2.5 <br>
	 * nameCN: 植株高度 <br>
	 * unit: 厘米 <br>
	 * BUFR FXY: V71006 <br>
	 * descriptionCN:<br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private float PlantHeight;
	
	/**
	 * NO: 2.6 <br>
	 * nameCN: 植株密度 <br>
	 * unit: 1株（茎）数/平方米 <br>
	 * BUFR FXY:  V71008<br>
	 * descriptionCN: <br>
	 * decode rule: 取值*0.01<br>
	 * field rule: 直接赋值<br>
	 */
	private float PlantDensity;
	
	/**
	 * NO: 2.7  <br>
	 * nameCN: 生长状况 <br>
	 * unit: 无<br>
	 * BUFR FXY: V71007 <br>
	 * descriptionCN: 1.为一类苗;2.为二类苗;3.为三类苗;4.三类苗以上<br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private int GrowthState;
	
	/**
	 * NO: 2.8  <br>
	 * nameCN: 产量因素名称1 <br>
	 * unit: 编码 <br>
	 * BUFR FXY: V71630_1 <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private int OutputFactorName1;
	
	/**
	 * NO: 2.9  <br>
	 * nameCN: 产量因素测量值1 <br>
	 * unit: 1 <br>
	 * BUFR FXY: V71632_1 <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值 * 0.01<br>
	 * field rule: 直接赋值<br>
	 */
	private float OutputFactorMeasureValue1;
	
	/**
	 * NO: 2.10  <br>
	 * nameCN: 产量因素名称2 <br>
	 * unit: 编码 <br>
	 * BUFR FXY: V71630_2 <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private int OutputFactorName2;
	
	/**
	 * NO: 2.11  <br>
	 * nameCN: 产量因素测量值1 <br>
	 * unit: 1 <br>
	 * BUFR FXY: V71632_2 <br>
	 * descriptionCN: <br>
	 * decode rule: 取值 * 0.01<br>
	 * field rule: 直接赋值<br>
	 */
	private float OutputFactorMeasureValue2;
	
	/**
	 * NO: 2.12  <br>
	 * nameCN: 产量因素名称3 <br>
	 * unit: 编码 <br>
	 * BUFR FXY: V71630_3  <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private int OutputFactorName3;
	
	/**
	 * NO: 2.13  <br>
	 * nameCN: 产量因素测量值3 <br>
	 * unit: 1 <br>
	 * BUFR FXY: V71632_3 <br>
	 * descriptionCN: <br>
	 * decode rule: 取值 * 0.01<br>
	 * field rule: 直接赋值<br>
	 */
	private float OutputFactorMeasureValue3;
	
	/**
	 * NO: 2.14  <br>
	 * nameCN: 产量因素名称4 <br>
	 * unit: 编码 <br>
	 * BUFR FXY: V71630_4 <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private int OutputFactorName4;
	
	/**
	 * NO: 2.15  <br>
	 * nameCN: 产量因素测量值4 <br>
	 * unit: 1 <br>
	 * BUFR FXY: V71632_4 <br>
	 * descriptionCN: <br>
	 * decode rule: 取值 * 0.01<br>
	 * field rule: 直接赋值<br>
	 */
	private float OutputFactorMeasureValue4;


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
	 * Gets the field level.
	 *
	 * @return the field level
	 */
	public int getFieldLevel() {
		return FieldLevel;
	}

	/**
	 * Sets the field level.
	 *
	 * @param fieldLevel the new field level
	 */
	public void setFieldLevel(int fieldLevel) {
		FieldLevel = fieldLevel;
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
	 * Gets the output factor name 1.
	 *
	 * @return the output factor name 1
	 */
	public int getOutputFactorName1() {
		return OutputFactorName1;
	}

	/**
	 * Sets the output factor name 1.
	 *
	 * @param outputFactorName1 the new output factor name 1
	 */
	public void setOutputFactorName1(int outputFactorName1) {
		OutputFactorName1 = outputFactorName1;
	}

	/**
	 * Gets the output factor measure value 1.
	 *
	 * @return the output factor measure value 1
	 */
	public float getOutputFactorMeasureValue1() {
		return OutputFactorMeasureValue1;
	}

	/**
	 * Sets the output factor measure value 1.
	 *
	 * @param outputFactorMeasureValue1 the new output factor measure value 1
	 */
	public void setOutputFactorMeasureValue1(float outputFactorMeasureValue1) {
		OutputFactorMeasureValue1 = outputFactorMeasureValue1;
	}

	/**
	 * Gets the output factor name 2.
	 *
	 * @return the output factor name 2
	 */
	public int getOutputFactorName2() {
		return OutputFactorName2;
	}

	/**
	 * Sets the output factor name 2.
	 *
	 * @param outputFactorName2 the new output factor name 2
	 */
	public void setOutputFactorName2(int outputFactorName2) {
		OutputFactorName2 = outputFactorName2;
	}

	/**
	 * Gets the output factor measure value 2.
	 *
	 * @return the output factor measure value 2
	 */
	public float getOutputFactorMeasureValue2() {
		return OutputFactorMeasureValue2;
	}

	/**
	 * Sets the output factor measure value 2.
	 *
	 * @param outputFactorMeasureValue2 the new output factor measure value 2
	 */
	public void setOutputFactorMeasureValue2(float outputFactorMeasureValue2) {
		OutputFactorMeasureValue2 = outputFactorMeasureValue2;
	}

	/**
	 * Gets the output factor name 3.
	 *
	 * @return the output factor name 3
	 */
	public int getOutputFactorName3() {
		return OutputFactorName3;
	}

	/**
	 * Sets the output factor name 3.
	 *
	 * @param outputFactorName3 the new output factor name 3
	 */
	public void setOutputFactorName3(int outputFactorName3) {
		OutputFactorName3 = outputFactorName3;
	}

	/**
	 * Gets the output factor measure value 3.
	 *
	 * @return the output factor measure value 3
	 */
	public float getOutputFactorMeasureValue3() {
		return OutputFactorMeasureValue3;
	}

	/**
	 * Sets the output factor measure value 3.
	 *
	 * @param outputFactorMeasureValue3 the new output factor measure value 3
	 */
	public void setOutputFactorMeasureValue3(float outputFactorMeasureValue3) {
		OutputFactorMeasureValue3 = outputFactorMeasureValue3;
	}

	/**
	 * Gets the output factor name 4.
	 *
	 * @return the output factor name 4
	 */
	public int getOutputFactorName4() {
		return OutputFactorName4;
	}

	/**
	 * Sets the output factor name 4.
	 *
	 * @param outputFactorName4 the new output factor name 4
	 */
	public void setOutputFactorName4(int outputFactorName4) {
		OutputFactorName4 = outputFactorName4;
	}

	/**
	 * Gets the output factor measure value 4.
	 *
	 * @return the output factor measure value 4
	 */
	public float getOutputFactorMeasureValue4() {
		return OutputFactorMeasureValue4;
	}

	/**
	 * Sets the output factor measure value 4.
	 *
	 * @param outputFactorMeasureValue4 the new output factor measure value 4
	 */
	public void setOutputFactorMeasureValue4(float outputFactorMeasureValue4) {
		OutputFactorMeasureValue4 = outputFactorMeasureValue4;
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