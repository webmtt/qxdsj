package cma.cimiss2.dpc.decoder.bean.sand;

import java.util.Date;

import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
/**
 * 
 * <br>
 * @Title:  SandAerosolPmm.java
 * @Package cma.cimiss2.dpc.decoder.bean.sand.pmm
 * @Description:    TODO(沙尘暴气溶胶质量浓度（PMM)资料实体类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月15日 下午5:37:23   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */
public class SandAerosolPmm extends AgmeReportHeader{
	

	/**
	 * @Fields stationNumberChina : 区站号(字符) 
	 */
	private String stationNumberChina = "999999";

	/**
	 * @Fields latitude : 纬度 
	 */
	private Double latitude = 999999.0;

	/**
	 * @Fields longitude : 经度 
	 */
	private Double longitude = 999999.0;

	/**
	 * @Fields stationHeight : 测站高度 
	 */
	private Double heightOfSationGroundAboveMeanSeaLevel = 999999.0;

	private Date observationTime;

	public Double getHeightOfSationGroundAboveMeanSeaLevel() {
		return heightOfSationGroundAboveMeanSeaLevel;
	}

	public void setHeightOfSationGroundAboveMeanSeaLevel(Double heightOfSationGroundAboveMeanSeaLevel) {
		this.heightOfSationGroundAboveMeanSeaLevel = heightOfSationGroundAboveMeanSeaLevel;
	}

	public Date getObservationTime() {
		return observationTime;
	}

	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}

	/**
	 * @Fields typeOfStorageInstrument : 存储仪器类型 
	 */
	private String typeOfStorageInstrument="999999";

	/**
	 * @Fields projectCode : 项目代码 
	 */
	private Double projectCode = 999999.0;

	/**
	 * @Fields storageLocation : 存储位置 
	 */
	private Double storageLocation = 999999.0;

	/**
	 * @Fields weightFactor : 重量因数 
	 */
	private Double weightFactor = 999999.0;

	/**
	 * @Fields errorCode : 错误代码 
	 */
	private Double errorCode = 999999.0;

	/**
	 * @Fields batteryVoltageCode : 电池电压代码 
	 */
	private Double batteryVoltageCode = 999999.0;

	/**
	 * @Fields valveCurrent : 阀电流 
	 */
	private Double valveCurrent = 999999.0;

	/**
	 * @Fields comprehensiveCorrectionCount : 综合订正计数 
	 */
	private Double comprehensiveCorrectionCount = 999999.0;

	/**
	 * @Fields barometricCount : 气压计数 
	 */
	private Double barometricCount = 999999.0;

	/**
	 * @Fields humidityCount : 湿度计数 
	 */
	private Double humidityCount = 999999.0;

	/**
	 * @Fields temperatureCount : 温度计数 
	 */
	private Double temperatureCount = 999999.0;

	/**
	 * @Fields timeInterval : 时间间隔 
	 */
	private Double timeInterval = 999999.0;

	/**
	 * @Fields windSpeedMeasurementFactor : 风速计量因子 
	 */
	private Double windSpeedMeasurementFactor = 999999.0;

	/**
	 * @Fields windDirectionMeasurementFactor : 风向计量因子 
	 */
	private Double windDirectionMeasurementFactor = 999999.0;

	/**
	 * @Fields precipitationMeasurementFactor : 降水计量因子 
	 */
	private Double precipitationMeasurementFactor = 999999.0;

	/**
	 * @Fields temperatureLinearSlopeRate : 温度线性转化后斜率 
	 */
	private Double temperatureLinearSlopeRate = 999999.0;

	/**
	 * @Fields humidityLinearSlopeRate : 湿度线性转化后斜率 
	 */
	private Double humidityLinearSlopeRate = 999999.0;

	/**
	 * @Fields pressureLinearSlopeRate : 气压线性转化后斜率 
	 */
	private Double pressureLinearSlopeRate = 999999.0;

	/**
	 * @Fields temperatureLinearIntercept : 温度线性转化后截距 
	 */
	private Double temperatureLinearIntercept = 999999.0;

	/**
	 * @Fields humidityLinearIntercept : 湿度线性转化后截距 
	 */
	private Double humidityLinearIntercept = 999999.0;

	/**
	 * @Fields pressureLinearIntercept : 气压线性转化后截距 
	 */
	private Double pressureLinearIntercept = 999999.0;

	/**
	 * @Fields windSpeedSensitivity : 风速灵敏度 
	 */
	private Double windSpeedSensitivity = 999999.0;

	/**
	 * @Fields windDirectionAngle : 风向倾角 
	 */
	private Double windDirectionAngle = 999999.0;

	/**
	 * @Fields precipitationSensorCorrectionFactor : 降水传感器订正因子 
	 */
	private Double precipitationSensorCorrectionFactor = 999999.0;

	/**
	 * @Fields pressure : 气压 
	 */
	private Double pressure = 999999.0;

	/**
	 * @Fields relativeHumidity : 相对湿度 
	 */
	private Double relativeHumidity = 999999.0;

	/**
	 * @Fields temperature : 温度 
	 */
	private Double temperature = 999999.0;

	/**
	 * @Fields windSpeed : 风速 
	 */
	private Double windSpeed = 999999.0;

	/**
	 * @Fields windDirection : 风向 
	 */
	private Double windDirection = 999999.0;

	/**
	 * @Fields precipitation : 降水 
	 */
	private Double precipitation = 999999.0;

	/**
	 * @Fields massConcentrationOfPM10 : PM10质量浓度 
	 */
	private Double massConcentrationOfPM10 = 999999.0;

	/**
	 * @Fields massConcentrationOfPM25 : PM2.5质量浓度 
	 */
	private Double massConcentrationOfPM2p5 = 999999.0;

	/**
	 * @Fields massConcentrationOfPM1 : PM1质量浓度 
	 */
	private Double massConcentrationOfPM1 = 999999.0;

	/**
	 * @Fields measurementDataFlag_pm10 : PM10测量数据标识 
	 */
	private String measurementDataFlag_pm10 = "1";

	/**
	 * @Fields measurementDataFlag_pm2p5 : PM2.5测量数据标识 
	 */
	private String measurementDataFlag_pm2p5 = "1";

	/**
	 * @Fields measurementDataFlag_pm1 : PM1测量数据标识 
	 */
	private String measurementDataFlag_pm1 = "1";

	/**
	 * @Fields flowRate_pm10 : PM10仪器测量的流量 
	 */
	private Double flowRate_pm10 = 999999.0;

	/**
	 * @Fields temperature_pm10 : PM10仪器测量的气温 
	 */
	private Double temperature_pm10 = 999999.0;

	/**
	 * @Fields pressure_pm10 : PM10仪器测量的气压 
	 */
	private Double pressure_pm10 = 999999.0;

	/**
	 * @Fields relativeHumidity_pm10 : PM10仪器测量的相对湿度 
	 */
	private Double relativeHumidity_pm10 = 999999.0;

	/**
	 * @Fields flowRate_pm2p5 : PM2.5仪器测量的流量 
	 */
	private Double flowRate_pm2p5 = 999999.0;

	/**
	 * @Fields temperature_pm2p5 : PM2.5仪器测量的气温 
	 */
	private Double temperature_pm2p5 = 999999.0;

	/**
	 * @Fields pressure_pm2p5 : PM2.5仪器测量的气压 
	 */
	private Double pressure_pm2p5 = 999999.0;

	/**
	 * @Fields relativeHumidity_pm2p5 : PM2.5仪器测量的相对湿度 
	 */
	private Double relativeHumidity_pm2p5 = 999999.0;

	/**
	 * @Fields flowRate_pm1 : PM1.0仪器测量的流量 
	 */
	private Double flowRate_pm1 = 999999.0;

	/**
	 * @Fields temperature_pm1 : PM1.0仪器测量的气温 
	 */
	private Double temperature_pm1 = 999999.0;

	/**
	 * @Fields pressure_pm1 : PM1.0仪器测量的气压 
	 */
	private Double pressure_pm1 = 999999.0;

	/**
	 * @Fields relativeHumidity_pm1 : PM1.0仪器测量的相对湿度 
	 */
	private Double relativeHumidity_pm1 = 999999.0;

	/**
	 * 区站号(字符)setter 
	 */
	public void setStationNumberChina(String stationNumberChina) {
		this.stationNumberChina = stationNumberChina;
	}

	/**
	 * 区站号(字符)getter 
	 */

	public String getStationNumberChina() {
		return stationNumberChina;
	}

	/**
	 * 纬度setter 
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * 纬度getter 
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * 经度setter 
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * 经度getter 
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * 存储仪器类型setter 
	 */
	public void setTypeOfStorageInstrument(String typeOfStorageInstrument) {
		this.typeOfStorageInstrument = typeOfStorageInstrument;
	}

	/**
	 * 存储仪器类型getter 
	 */
	public String getTypeOfStorageInstrument() {
		return typeOfStorageInstrument;
	}

	/**
	 * 项目代码setter 
	 */
	public void setProjectCode(Double projectCode) {
		this.projectCode = projectCode;
	}

	/**
	 * 项目代码getter 
	 */
	public Double getProjectCode() {
		return projectCode;
	}

	/**
	 * 存储位置setter 
	 */
	public void setStorageLocation(Double storageLocation) {
		this.storageLocation = storageLocation;
	}

	/**
	 * 存储位置getter 
	 */
	public Double getStorageLocation() {
		return storageLocation;
	}

	/**
	 * 重量因数setter 
	 */
	public void setWeightFactor(Double weightFactor) {
		this.weightFactor = weightFactor;
	}

	/**
	 * 重量因数getter 
	 */
	public Double getWeightFactor() {
		return weightFactor;
	}

	/**
	 * 错误代码setter 
	 */
	public void setErrorCode(Double errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * 错误代码getter 
	 */
	public Double getErrorCode() {
		return errorCode;
	}

	/**
	 * 电池电压代码setter 
	 */
	public void setBatteryVoltageCode(Double batteryVoltageCode) {
		this.batteryVoltageCode = batteryVoltageCode;
	}

	/**
	 * 电池电压代码getter 
	 */
	public Double getBatteryVoltageCode() {
		return batteryVoltageCode;
	}

	/**
	 * 阀电流setter 
	 */
	public void setValveCurrent(Double valveCurrent) {
		this.valveCurrent = valveCurrent;
	}

	/**
	 * 阀电流getter 
	 */
	public Double getValveCurrent() {
		return valveCurrent;
	}

	/**
	 * 综合订正计数setter 
	 */
	public void setComprehensiveCorrectionCount(Double comprehensiveCorrectionCount) {
		this.comprehensiveCorrectionCount = comprehensiveCorrectionCount;
	}

	/**
	 * 综合订正计数getter 
	 */
	public Double getComprehensiveCorrectionCount() {
		return comprehensiveCorrectionCount;
	}

	/**
	 * 气压计数setter 
	 */
	public void setBarometricCount(Double barometricCount) {
		this.barometricCount = barometricCount;
	}

	/**
	 * 气压计数getter 
	 */
	public Double getBarometricCount() {
		return barometricCount;
	}

	/**
	 * 湿度计数setter 
	 */
	public void setHumidityCount(Double humidityCount) {
		this.humidityCount = humidityCount;
	}

	/**
	 * 湿度计数getter 
	 */
	public Double getHumidityCount() {
		return humidityCount;
	}

	/**
	 * 温度计数setter 
	 */
	public void setTemperatureCount(Double temperatureCount) {
		this.temperatureCount = temperatureCount;
	}

	/**
	 * 温度计数getter 
	 */
	public Double getTemperatureCount() {
		return temperatureCount;
	}

	/**
	 * 时间间隔setter 
	 */
	public void setTimeInterval(Double timeInterval) {
		this.timeInterval = timeInterval;
	}

	/**
	 * 时间间隔getter 
	 */
	public Double getTimeInterval() {
		return timeInterval;
	}

	/**
	 * 风速计量因子setter 
	 */
	public void setWindSpeedMeasurementFactor(Double windSpeedMeasurementFactor) {
		this.windSpeedMeasurementFactor = windSpeedMeasurementFactor;
	}

	/**
	 * 风速计量因子getter 
	 */
	public Double getWindSpeedMeasurementFactor() {
		return windSpeedMeasurementFactor;
	}

	/**
	 * 风向计量因子setter 
	 */
	public void setWindDirectionMeasurementFactor(Double windDirectionMeasurementFactor) {
		this.windDirectionMeasurementFactor = windDirectionMeasurementFactor;
	}

	/**
	 * 风向计量因子getter 
	 */
	public Double getWindDirectionMeasurementFactor() {
		return windDirectionMeasurementFactor;
	}

	/**
	 * 降水计量因子setter 
	 */
	public void setPrecipitationMeasurementFactor(Double precipitationMeasurementFactor) {
		this.precipitationMeasurementFactor = precipitationMeasurementFactor;
	}

	/**
	 * 降水计量因子getter 
	 */
	public Double getPrecipitationMeasurementFactor() {
		return precipitationMeasurementFactor;
	}

	/**
	 * 温度线性转化后斜率setter 
	 */

	public void setTemperatureLinearSlopeRate(Double temperatureLinearSlopeRate) {
		this.temperatureLinearSlopeRate = temperatureLinearSlopeRate;
	}

	/**
	 * 温度线性转化后斜率getter 
	 */
	public Double getTemperatureLinearSlopeRate() {
		return temperatureLinearSlopeRate;
	}

	/**
	 * 湿度线性转化后斜率setter 
	 */

	public void setHumidityLinearSlopeRate(Double humidityLinearSlopeRate) {
		this.humidityLinearSlopeRate = humidityLinearSlopeRate;
	}

	/**
	 * 湿度线性转化后斜率getter 
	 */
	public Double getHumidityLinearSlopeRate() {
		return humidityLinearSlopeRate;
	}

	/**
	 * 气压线性转化后斜率setter 
	 */

	public void setPressureLinearSlopeRate(Double pressureLinearSlopeRate) {
		this.pressureLinearSlopeRate = pressureLinearSlopeRate;
	}

	/**
	 * 气压线性转化后斜率getter 
	 */
	public Double getPressureLinearSlopeRate() {
		return pressureLinearSlopeRate;
	}

	/**
	 * 温度线性转化后截距setter 
	 */

	public void setTemperatureLinearIntercept(Double temperatureLinearIntercept) {
		this.temperatureLinearIntercept = temperatureLinearIntercept;
	}

	/**
	 * 温度线性转化后截距getter 
	 */
	public Double getTemperatureLinearIntercept() {
		return temperatureLinearIntercept;
	}

	/**
	 * 湿度线性转化后截距setter 
	 */

	public void setHumidityLinearIntercept(Double humidityLinearIntercept) {
		this.humidityLinearIntercept = humidityLinearIntercept;
	}

	/**
	 * 湿度线性转化后截距getter 
	 */
	public Double getHumidityLinearIntercept() {
		return humidityLinearIntercept;
	}

	/**
	 * 气压线性转化后截距setter 
	 */
	public void setPressureLinearIntercept(Double pressureLinearIntercept) {
		this.pressureLinearIntercept = pressureLinearIntercept;
	}

	/**
	 * 气压线性转化后截距getter 
	 */
	public Double getPressureLinearIntercept() {
		return pressureLinearIntercept;
	}

	/**
	 * 风速灵敏度setter 
	 */
	public void setWindSpeedSensitivity(Double windSpeedSensitivity) {
		this.windSpeedSensitivity = windSpeedSensitivity;
	}

	/**
	 * 风速灵敏度getter 
	 */
	public Double getWindSpeedSensitivity() {
		return windSpeedSensitivity;
	}

	/**
	 * 风向倾角setter 
	 */
	public void setWindDirectionAngle(Double windDirectionAngle) {
		this.windDirectionAngle = windDirectionAngle;
	}

	/**
	 * 风向倾角getter 
	 */
	public Double getWindDirectionAngle() {
		return windDirectionAngle;
	}

	/**
	 * 降水传感器订正因子setter 
	 */
	public void setPrecipitationSensorCorrectionFactor(Double precipitationSensorCorrectionFactor) {
		this.precipitationSensorCorrectionFactor = precipitationSensorCorrectionFactor;
	}

	/**
	 * 降水传感器订正因子getter 
	 */
	public Double getPrecipitationSensorCorrectionFactor() {
		return precipitationSensorCorrectionFactor;
	}

	/**
	 * 气压setter 
	 */
	public void setPressure(Double pressure) {
		this.pressure = pressure;
	}

	/**
	 * 气压getter 
	 */
	public Double getPressure() {
		return pressure;
	}

	/**
	 * 相对湿度setter 
	 */
	public void setRelativeHumidity(Double relativeHumidity) {
		this.relativeHumidity = relativeHumidity;
	}

	/**
	 * 相对湿度getter 
	 */
	public Double getRelativeHumidity() {
		return relativeHumidity;
	}

	/**
	 * 温度setter 
	 */
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	/**
	 * 温度getter 
	 */
	public Double getTemperature() {
		return temperature;
	}

	/**
	 * 风速setter 
	 */
	public void setWindSpeed(Double windSpeed) {
		this.windSpeed = windSpeed;
	}

	/**
	 * 风速getter 
	 */
	public Double getWindSpeed() {
		return windSpeed;
	}

	/**
	 * 风向setter 
	 */
	public void setWindDirection(Double windDirection) {
		this.windDirection = windDirection;
	}

	/**
	 * 风向getter 
	 */
	public Double getWindDirection() {
		return windDirection;
	}

	/**
	 * 降水setter 
	 */
	public void setPrecipitation(Double precipitation) {
		this.precipitation = precipitation;
	}

	/**
	 * 降水getter 
	 */
	public Double getPrecipitation() {
		return precipitation;
	}

	/**
	 * PM10质量浓度setter 
	 */
	public void setMassConcentrationOfPM10(Double massConcentrationOfPM10) {
		this.massConcentrationOfPM10 = massConcentrationOfPM10;
	}

	/**
	 * PM10质量浓度getter 
	 */
	public Double getMassConcentrationOfPM10() {
		return massConcentrationOfPM10;
	}

	/**
	 * PM2.5质量浓度setter 
	 */

	public void setMassConcentrationOfPM2p5(Double massConcentrationOfPM2p5) {
		this.massConcentrationOfPM2p5 = massConcentrationOfPM2p5;
	}

	/**
	 * PM2.5质量浓度getter 
	 */
	public Double getMassConcentrationOfPM2p5() {
		return massConcentrationOfPM2p5;
	}

	/**
	 * PM1质量浓度setter 
	 */
	public void setMassConcentrationOfPM1(Double massConcentrationOfPM1) {
		this.massConcentrationOfPM1 = massConcentrationOfPM1;
	}

	/**
	 * PM1质量浓度getter 
	 */
	public Double getMassConcentrationOfPM1() {
		return massConcentrationOfPM1;
	}

	/**
	 * PM10测量数据标识setter 
	 */
	public void setMeasurementDataFlag_pm10(String measurementDataFlag_pm10) {
		this.measurementDataFlag_pm10 = measurementDataFlag_pm10;
	}

	/**
	 * PM10测量数据标识getter 
	 */
	public String getMeasurementDataFlag_pm10() {
		return measurementDataFlag_pm10;
	}

	/**
	 * PM2.5测量数据标识setter 
	 */
	public void setMeasurementDataFlag_pm2p5(String measurementDataFlag_pm2p5) {
		this.measurementDataFlag_pm2p5 = measurementDataFlag_pm2p5;
	}

	/**
	 * PM2.5测量数据标识getter 
	 */
	public String getMeasurementDataFlag_pm2p5() {
		return measurementDataFlag_pm2p5;
	}

	/**
	 * PM1测量数据标识setter 
	 */

	public void setMeasurementDataFlag_pm1(String measurementDataFlag_pm1) {
		this.measurementDataFlag_pm1 = measurementDataFlag_pm1;
	}

	/**
	 * PM1测量数据标识getter 
	 */
	public String getMeasurementDataFlag_pm1() {
		return measurementDataFlag_pm1;
	}

	/**
	 * PM10仪器测量的流量setter 
	 */

	public void setFlowRate_pm10(Double flowRate_pm10) {
		this.flowRate_pm10 = flowRate_pm10;
	}

	/**
	 * PM10仪器测量的流量getter 
	 */
	public Double getFlowRate_pm10() {
		return flowRate_pm10;
	}

	/**
	 * PM10仪器测量的气温setter 
	 */

	public void setTemperature_pm10(Double temperature_pm10) {
		this.temperature_pm10 = temperature_pm10;
	}

	/**
	 * PM10仪器测量的气温getter 
	 */
	public Double getTemperature_pm10() {
		return temperature_pm10;
	}

	/**
	 * PM10仪器测量的气压setter 
	 */

	public void setPressure_pm10(Double pressure_pm10) {
		this.pressure_pm10 = pressure_pm10;
	}

	/**
	 * PM10仪器测量的气压getter 
	 */
	public Double getPressure_pm10() {
		return pressure_pm10;
	}

	/**
	 * PM10仪器测量的相对湿度setter 
	 */
	public void setRelativeHumidity_pm10(Double relativeHumidity_pm10) {
		this.relativeHumidity_pm10 = relativeHumidity_pm10;
	}

	/**
	 * PM10仪器测量的相对湿度getter 
	 */

	public Double getRelativeHumidity_pm10() {
		return relativeHumidity_pm10;
	}

	/**
	 * PM2.5仪器测量的流量setter 
	 */
	public void setFlowRate_pm2p5(Double flowRate_pm2p5) {
		this.flowRate_pm2p5 = flowRate_pm2p5;
	}

	/**
	 * PM2.5仪器测量的流量getter 
	 */

	public Double getFlowRate_pm2p5() {
		return flowRate_pm2p5;
	}

	/**
	 * PM2.5仪器测量的气温setter 
	 */
	public void setTemperature_pm2p5(Double temperature_pm2p5) {
		this.temperature_pm2p5 = temperature_pm2p5;
	}

	/**
	 * PM2.5仪器测量的气温getter 
	 */

	public Double getTemperature_pm2p5() {
		return temperature_pm2p5;
	}

	/**
	 * PM2.5仪器测量的气压setter 
	 */
	public void setPressure_pm2p5(Double pressure_pm2p5) {
		this.pressure_pm2p5 = pressure_pm2p5;
	}

	/**
	 * PM2.5仪器测量的气压getter 
	 */

	public Double getPressure_pm2p5() {
		return pressure_pm2p5;
	}

	/**
	 * PM2.5仪器测量的相对湿度setter 
	 */
	public void setRelativeHumidity_pm2p5(Double relativeHumidity_pm2p5) {
		this.relativeHumidity_pm2p5 = relativeHumidity_pm2p5;
	}

	/**
	 * PM2.5仪器测量的相对湿度getter 
	 */

	public Double getRelativeHumidity_pm2p5() {
		return relativeHumidity_pm2p5;
	}

	/**
	 * PM1.0仪器测量的流量setter 
	 */
	public void setFlowRate_pm1(Double flowRate_pm1) {
		this.flowRate_pm1 = flowRate_pm1;
	}

	/**
	 * PM1.0仪器测量的流量getter 
	 */

	public Double getFlowRate_pm1() {
		return flowRate_pm1;
	}

	/**
	 * PM1.0仪器测量的气温setter 
	 */
	public void setTemperature_pm1(Double temperature_pm1) {
		this.temperature_pm1 = temperature_pm1;
	}

	/**
	 * PM1.0仪器测量的气温getter 
	 */

	public Double getTemperature_pm1() {
		return temperature_pm1;
	}

	/**
	 * PM1.0仪器测量的气压setter 
	 */
	public void setPressure_pm1(Double pressure_pm1) {
		this.pressure_pm1 = pressure_pm1;
	}

	/**
	 * PM1.0仪器测量的气压getter 
	 */

	public Double getPressure_pm1() {
		return pressure_pm1;
	}

	/**
	 * PM1.0仪器测量的相对湿度setter 
	 */
	public void setRelativeHumidity_pm1(Double relativeHumidity_pm1) {
		this.relativeHumidity_pm1 = relativeHumidity_pm1;
	}

	/**
	 * PM1.0仪器测量的相对湿度getter 
	 */

	public Double getRelativeHumidity_pm1() {
		return relativeHumidity_pm1;
	}

	


}
