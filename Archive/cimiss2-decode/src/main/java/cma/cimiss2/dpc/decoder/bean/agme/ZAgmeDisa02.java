package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	农业气象灾害调查子要素
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《农业气象灾害要素—农业灾害调查》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年9月6日 下午3:38:18   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class ZAgmeDisa02 {
	/**
	 * NO: 1.1 <br>
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
	 * nameCN: 调查时间 <br>
	 * unit: 日期 <br>
	 * BUFR FXY: 由V04001、V04002、V04003组成 <br>
	 * descriptionCN: 年月日时分秒<br>
	 * decode rule: 直接取值 <br>
	 * field rule: 使用java时间转化工具类进行转化, 标准形式为：yyyyMMddHHmmss <br>
	 */
	private Date ObservationDate;
	
	/**
	 * NO: 2.2  <br>
	 * nameCN: 灾害名称 <br>
	 * unit: 编码 <br>
	 * BUFR FXY: V71040  <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private int DisaName;
	
	/**
	 * NO: 2.3  <br>
	 * nameCN: 作物名称 <br>
	 * unit: 编码  <br>
	 * BUFR FXY: V71001  <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private int DisaCrop;
	
	/**
	 * NO: 2.4  <br>
	 * nameCN: 器官受害程度  <br>
	 * unit: 5<br>
	 * BUFR FXY: V71042  <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private int DegreeOfOrganDamage;
	
	/**
	 * NO: 2.5  <br>
	 * nameCN: 成灾面积  <br>
	 * unit: 1公顷  <br>
	 * BUFR FXY: V71043  <br>
	 * descriptionCN: <br>
	 * decode rule: 取值 * 0.1<br>
	 * field rule: 直接赋值<br>
	 */
	private float DisaArea;
	
	/**
	 * NO: 2.6  <br>
	 * nameCN: 成灾比例  <br>
	 * unit: % <br>
	 * BUFR FXY: V71044  <br>
	 * descriptionCN: <br>
	 * decode rule: 取值 * 0.1<br>
	 * field rule: 直接赋值<br>
	 */
	private float DisaPercentage;
	
	/**
	 * NO: 2.7  <br>
	 * nameCN: 减产百分率  <br>
	 * unit: % <br>
	 * BUFR FXY: V71083  <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private int ReductionPercentage;
	
	/**
	 * NO: 2.8  <br>
	 * nameCN: 受害征状<br>
	 * unit: 无  <br>
	 * BUFR FXY: V71614 <br>
	 * descriptionCN: 文字描述<br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private String SymptomOfDisaster;
	
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
	 * Gets the disa name.
	 *
	 * @return the disa name
	 */
	public int getDisaName() {
		return DisaName;
	}

	/**
	 * Sets the disa name.
	 *
	 * @param disaName the new disa name
	 */
	public void setDisaName(int disaName) {
		DisaName = disaName;
	}

	/**
	 * Gets the disa crop.
	 *
	 * @return the disa crop
	 */
	public int getDisaCrop() {
		return DisaCrop;
	}

	/**
	 * Sets the disa crop.
	 *
	 * @param disaCrop the new disa crop
	 */
	public void setDisaCrop(int disaCrop) {
		DisaCrop = disaCrop;
	}

	/**
	 * Gets the degree of organ damage.
	 *
	 * @return the degree of organ damage
	 */
	public int getDegreeOfOrganDamage() {
		return DegreeOfOrganDamage;
	}

	/**
	 * Sets the degree of organ damage.
	 *
	 * @param degreeOfOrganDamage the new degree of organ damage
	 */
	public void setDegreeOfOrganDamage(int degreeOfOrganDamage) {
		DegreeOfOrganDamage = degreeOfOrganDamage;
	}

	/**
	 * Gets the disa area.
	 *
	 * @return the disa area
	 */
	public float getDisaArea() {
		return DisaArea;
	}

	/**
	 * Sets the disa area.
	 *
	 * @param disaArea the new disa area
	 */
	public void setDisaArea(float disaArea) {
		DisaArea = disaArea;
	}

	/**
	 * Gets the disa percentage.
	 *
	 * @return the disa percentage
	 */
	public float getDisaPercentage() {
		return DisaPercentage;
	}

	/**
	 * Sets the disa percentage.
	 *
	 * @param disaPercentage the new disa percentage
	 */
	public void setDisaPercentage(float disaPercentage) {
		DisaPercentage = disaPercentage;
	}

	/**
	 * Gets the reduction percentage.
	 *
	 * @return the reduction percentage
	 */
	public int getReductionPercentage() {
		return ReductionPercentage;
	}

	/**
	 * Sets the reduction percentage.
	 *
	 * @param reductionPercentage the new reduction percentage
	 */
	public void setReductionPercentage(int reductionPercentage) {
		ReductionPercentage = reductionPercentage;
	}

	/**
	 * Gets the symptom of disaster.
	 *
	 * @return the symptom of disaster
	 */
	public String getSymptomOfDisaster() {
		return SymptomOfDisaster;
	}

	/**
	 * Sets the symptom of disaster.
	 *
	 * @param symptomOfDisaster the new symptom of disaster
	 */
	public void setSymptomOfDisaster(String symptomOfDisaster) {
		SymptomOfDisaster = symptomOfDisaster;
	}
}
