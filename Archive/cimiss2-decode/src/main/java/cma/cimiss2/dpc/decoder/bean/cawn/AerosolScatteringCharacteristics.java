package cma.cimiss2.dpc.decoder.bean.cawn;

import java.util.Date;

import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
/**
 * 
 * <br>
 * @Title:  AerosolScatteringCharacteristics.java
 * @Package cma.cimiss2.dpc.decoder.bean.cawn
 * @Description:(大气成分气溶胶散射特性（ASP）实体类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年4月2日 下午4:50:23   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */
public class AerosolScatteringCharacteristics extends AgmeReportHeader {
	/**
	 * @Fields stationNumberChina : 区站号 
	 */
	String stationNumberChina;
	/**
	 * @Fields latitude : 纬度 
	 */
	double latitude = 999999;

	/**
	 * @Fields longitude : 经度 
	 */
	double longitude = 999999;
	/**
	 * @Fields heightOfSationGroundAboveMeanSeaLevel : 测站海拔高度
	 */
	double heightOfSationGroundAboveMeanSeaLevel = 999999;

	/**
	 * @Fields itemCode : 项目代码
	 */
	String itemCode="999998";
	
	/**
	 * @Fields observationTime : 观测时间
	 */
	Date observationTime;
	
	/**
	 * @Fields timeSign :时间标记
	 */
	double  timeSign= 999998;;
	
	/**
	 * @Fields searchSign :检索标志
	 */
	double  searchSign= 999998;;
	
	/**
	 * @Fields recordType :记录种类
	 */
	double  recordType= 999998;
	
	/**
	 * @Fields scatteringCoefficient_Avg :散射系数平均值
	 */
	double  scatteringCoefficient_Avg= 999998;
	
	/**
	 * @Fields atmosphericTemperature_Avg :大气温度平均值
	 */
	double  atmosphericTemperature_Avg= 999998;

	/**
	 * @Fields sampleChamberTemperature_Avg :样气室温度平均值
	 */
	double  sampleChamberTemperature_Avg= 999998;
	
	/**
	 * @Fields relativeHumidity_Avg :相对湿度平均值
	 */
	double   relativeHumidity_Avg= 999998;
	
	/**
	 * @Fields atmosphericPressure_Avg :大气压平均值
	 */
	double   atmosphericPressure_Avg= 999998;
	
	/**
	 * @Fields darkCountDiagnosis :暗计数诊断
	 */
	double   darkCountDiagnosis= 999998;
	
	/**
	 * @Fields shutterCountDiagnosis :快门计数诊断
	 */
	double   shutterCountDiagnosis= 999998;
	
	/**
	 * @Fields measurementRatioDiagnosis :测量比率诊断
	 */
	double   measurementRatioDiagnosis= 999998;
	
	/**
	 * @Fields finalTestRatioDiagnosis :最后测试比率诊断
	 */
	double   finalTestRatioDiagnosis= 999998;
	
	/**
	 * @Fields M_STATE :
	 */
	double  M_STATE = 999998;
	
	/**
	 * @Fields D_STATE :
	 */
	double  D_STATE = 999998;
	
	/**
	 * @Fields dataRecordFrequency :数据记录频率
	 */
	double  dataRecordFrequency = 999998;

	/**
	 * Scat总散射系数450nm
	 */
	private double ScatTotal_450nm = 999998;
	/**
	 * Scat总散射系数525nm
	 */
	private double ScatTotal_525nm = 999998;
	/**
	 * Scat总散射系数635nm
	 */
	private double ScatTotal_635nm = 999998;
	/**
	 * Scat前散射系数450nm
	 */
	private double ScatBefore_450nm = 999998;
	/**
	 * Scat前散射系数525nm
	 */
	private double ScatBefore_525nm = 999998;
	/**
	 * Scat前散射系数635nm
	 */
	private double ScatBefore_635nm = 999998;
	/**
	 * Scat后散射系数450nm
	 */
	private double ScatAfter_450nm = 999998;
	/**
	 * Scat后散射系数525nm
	 */
	private double ScatAfter_525nm = 999998;
	/**
	 * Scat后散射系数635nm
	 */
	private double ScatAfter_635nm = 999998;
	
	public double getScatTotal_450nm() {
		return ScatTotal_450nm;
	}

	public void setScatTotal_450nm(double scatTotal_450nm) {
		ScatTotal_450nm = scatTotal_450nm;
	}

	public double getScatTotal_525nm() {
		return ScatTotal_525nm;
	}

	public void setScatTotal_525nm(double scatTotal_525nm) {
		ScatTotal_525nm = scatTotal_525nm;
	}

	public double getScatTotal_635nm() {
		return ScatTotal_635nm;
	}

	public void setScatTotal_635nm(double scatTotal_635nm) {
		ScatTotal_635nm = scatTotal_635nm;
	}

	public double getScatBefore_450nm() {
		return ScatBefore_450nm;
	}

	public void setScatBefore_450nm(double scatBefore_450nm) {
		ScatBefore_450nm = scatBefore_450nm;
	}

	public double getScatBefore_525nm() {
		return ScatBefore_525nm;
	}

	public void setScatBefore_525nm(double scatBefore_525nm) {
		ScatBefore_525nm = scatBefore_525nm;
	}

	public double getScatBefore_635nm() {
		return ScatBefore_635nm;
	}

	public void setScatBefore_635nm(double scatBefore_635nm) {
		ScatBefore_635nm = scatBefore_635nm;
	}

	public double getScatAfter_450nm() {
		return ScatAfter_450nm;
	}

	public void setScatAfter_450nm(double scatAfter_450nm) {
		ScatAfter_450nm = scatAfter_450nm;
	}

	public double getScatAfter_525nm() {
		return ScatAfter_525nm;
	}

	public void setScatAfter_525nm(double scatAfter_525nm) {
		ScatAfter_525nm = scatAfter_525nm;
	}

	public double getScatAfter_635nm() {
		return ScatAfter_635nm;
	}

	public void setScatAfter_635nm(double scatAfter_635nm) {
		ScatAfter_635nm = scatAfter_635nm;
	}
	
	public String getStationNumberChina() {
		return stationNumberChina;
	}

	public void setStationNumberChina(String stationNumberChina) {
		this.stationNumberChina = stationNumberChina;
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

	public Double getHeightOfSationGroundAboveMeanSeaLevel() {
		return heightOfSationGroundAboveMeanSeaLevel;
	}

	public void setHeightOfSationGroundAboveMeanSeaLevel(Double heightOfSationGroundAboveMeanSeaLevel) {
		this.heightOfSationGroundAboveMeanSeaLevel = heightOfSationGroundAboveMeanSeaLevel;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Date getObservationTime() {
		return observationTime;
	}

	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}

	public double getTimeSign() {
		return timeSign;
	}

	public void setTimeSign(double timeSign) {
		this.timeSign = timeSign;
	}

	public double getSearchSign() {
		return searchSign;
	}

	public void setSearchSign(double searchSign) {
		this.searchSign = searchSign;
	}

	public double getRecordType() {
		return recordType;
	}

	public void setRecordType(double recordType) {
		this.recordType = recordType;
	}

	public double getScatteringCoefficient_Avg() {
		return scatteringCoefficient_Avg;
	}

	public void setScatteringCoefficient_Avg(double scatteringCoefficient_Avg) {
		this.scatteringCoefficient_Avg = scatteringCoefficient_Avg;
	}

	public double getAtmosphericTemperature_Avg() {
		return atmosphericTemperature_Avg;
	}

	public void setAtmosphericTemperature_Avg(double atmosphericTemperature_Avg) {
		this.atmosphericTemperature_Avg = atmosphericTemperature_Avg;
	}

	public double getSampleChamberTemperature_Avg() {
		return sampleChamberTemperature_Avg;
	}

	public void setSampleChamberTemperature_Avg(double sampleChamberTemperature_Avg) {
		this.sampleChamberTemperature_Avg = sampleChamberTemperature_Avg;
	}

	public double getRelativeHumidity_Avg() {
		return relativeHumidity_Avg;
	}

	public void setRelativeHumidity_Avg(double relativeHumidity_Avg) {
		this.relativeHumidity_Avg = relativeHumidity_Avg;
	}

	public double getAtmosphericPressure_Avg() {
		return atmosphericPressure_Avg;
	}

	public void setAtmosphericPressure_Avg(double atmosphericPressure_Avg) {
		this.atmosphericPressure_Avg = atmosphericPressure_Avg;
	}

	public double getDarkCountDiagnosis() {
		return darkCountDiagnosis;
	}

	public void setDarkCountDiagnosis(double darkCountDiagnosis) {
		this.darkCountDiagnosis = darkCountDiagnosis;
	}

	public double getShutterCountDiagnosis() {
		return shutterCountDiagnosis;
	}

	public void setShutterCountDiagnosis(double shutterCountDiagnosis) {
		this.shutterCountDiagnosis = shutterCountDiagnosis;
	}

	public double getMeasurementRatioDiagnosis() {
		return measurementRatioDiagnosis;
	}

	public void setMeasurementRatioDiagnosis(double measurementRatioDiagnosis) {
		this.measurementRatioDiagnosis = measurementRatioDiagnosis;
	}

	public double getFinalTestRatioDiagnosis() {
		return finalTestRatioDiagnosis;
	}

	public void setFinalTestRatioDiagnosis(double finalTestRatioDiagnosis) {
		this.finalTestRatioDiagnosis = finalTestRatioDiagnosis;
	}

	public double getM_STATE() {
		return M_STATE;
	}

	public void setM_STATE(double m_STATE) {
		M_STATE = m_STATE;
	}

	public double getD_STATE() {
		return D_STATE;
	}

	public void setD_STATE(double d_STATE) {
		D_STATE = d_STATE;
	}

	public double getDataRecordFrequency() {
		return dataRecordFrequency;
	}

	public void setDataRecordFrequency(double dataRecordFrequency) {
		this.dataRecordFrequency = dataRecordFrequency;
	};
	
	
}
