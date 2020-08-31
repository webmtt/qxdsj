package cma.cimiss2.dpc.decoder.bean.agme;

// TODO: Auto-generated Javadoc
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	  县产量水平子要素<br>
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《农业气象作物要素—县产量水平 》 </a>
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
public class ZAgmeCrop07 {

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
	 * nameCN: 年度  <br>
	 * unit: 日期  <br>
	 * BUFR FXY: V04001<br>
	 * descriptionCN: 年度<br>
	 * decode rule: 直接取值 <br>
	 * field rule: 直接赋值 <br>
	 */
	private int Year;
	
	/**
	 * NO: 2.2  <br>
	 * nameCN: 作物名称 <br>
	 * unit: 编码 <br>
	 * BUFR FXY:  V71001<br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值 <br>
	 * field rule: 直接赋值 <br>
	 */
	private int CropName;
	
	/**
	 * NO: 2.3  <br>
	 * nameCN: 测站产量水平 <br>
	 * unit: 1公斤/公顷 <br>
	 * BUFR FXY: V71601 <br>
	 * descriptionCN: <br>
	 * decode rule: 取值 * 0.1 <br>
	 * field rule: 直接赋值 <br>
	 */
	private float OutputLevelOfStation;
	
	/**
	 * NO: 2.4  <br>
	 * nameCN: 县平均单产 <br>
	 * unit: 1公斤/公顷 <br>
	 * BUFR FXY: V71091 <br>
	 * descriptionCN:  <br>
	 * decode rule: 取值 * 0.1 <br>
	 * field rule: 直接赋值 <br>
	 */
	private float CountyAverageOutput;
	
	/**
	 * NO: 2.5  <br>
	 * nameCN: 县产量增减产百分率 <br>
	 * unit: % <br>
	 * BUFR FXY: V71083 <br>
	 * descriptionCN:  <br>
	 * decode rule: 取值 * 0.1 <br>
	 * field rule: 直接赋值 <br>
	 */
	private float CountyOutputChangeRate;

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
	 * Gets the year.
	 *
	 * @return the year
	 */
	public int getYear() {
		return Year;
	}

	/**
	 * Sets the year.
	 *
	 * @param year the new year
	 */
	public void setYear(int year) {
		Year = year;
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
	 * Gets the output level of station.
	 *
	 * @return the output level of station
	 */
	public float getOutputLevelOfStation() {
		return OutputLevelOfStation;
	}

	/**
	 * Sets the output level of station.
	 *
	 * @param outputLevelOfStation the new output level of station
	 */
	public void setOutputLevelOfStation(float outputLevelOfStation) {
		OutputLevelOfStation = outputLevelOfStation;
	}

	/**
	 * Gets the county average output.
	 *
	 * @return the county average output
	 */
	public float getCountyAverageOutput() {
		return CountyAverageOutput;
	}

	/**
	 * Sets the county average output.
	 *
	 * @param countyAverageOutput the new county average output
	 */
	public void setCountyAverageOutput(float countyAverageOutput) {
		CountyAverageOutput = countyAverageOutput;
	}

	/**
	 * Gets the county output change rate.
	 *
	 * @return the county output change rate
	 */
	public float getCountyOutputChangeRate() {
		return CountyOutputChangeRate;
	}

	/**
	 * Sets the county output change rate.
	 *
	 * @param countyOutputChangeRate the new county output change rate
	 */
	public void setCountyOutputChangeRate(float countyOutputChangeRate) {
		CountyOutputChangeRate = countyOutputChangeRate;
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
