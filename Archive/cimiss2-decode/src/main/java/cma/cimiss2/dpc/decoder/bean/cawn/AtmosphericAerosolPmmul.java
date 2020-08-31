package cma.cimiss2.dpc.decoder.bean.cawn;

import java.util.Date;

import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;

/**
 * <br>
 * @Title:  AtmosphericAerosolPmmul.java
 * @Package cma.cimiss2.dpc.decoder.bean.cawn
 * @Description:    气溶胶质量浓度观测数据pmmul实体类
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月1日 上午11:27:40   wangzunpeng    Initial creation.
 * </pre>
 * 
 * @author wangzunpeng
 */
public class AtmosphericAerosolPmmul extends AgmeReportHeader {

	/**
	 * @Fields stationNumberChina : 区站号(字符) 
	 */
	private String stationNumberChina = "999999";

	/**
	 * @Fields latitude : 纬度 
	 */
	private double latitude = 999999;

	/**
	 * @Fields longitude : 经度 
	 */
	private double longitude = 999999;

	/**
	 * @Fields stationHeight : 测站高度 
	 */
	private double stationHeight = 999999;

	private Date d_datetime;

	/**
	 * @Fields typeOfStorageInstrument : 存储仪器类型 
	 */
	private String typeOfStorageInstrument;

	/**
	 * @Fields projectCode : 项目代码 
	 */
	private String projectCode = "999998";

	/**
	 * @Fields storageLocation : 存储位置 
	 */
	private double storageLocation = 999998;

	/**
	 * @Fields weightFactor : 重量因数 
	 */
	private double weightFactor = 999998;

	/**
	 * @Fields errorCode : 错误代码 
	 */
	private double errorCode = 999998;

	/**
	 * @Fields batteryVoltageCode : 电池电压代码 
	 */
	private double batteryVoltageCode = 999998;

	/**
	 * @Fields valveCurrent : 阀电流 
	 */
	private double valveCurrent = 999998;

	/**
	 * @Fields comprehensiveCorrectionCount : 综合订正计数 
	 */
	private double comprehensiveCorrectionCount = 999998;

	/**
	 * @Fields barometricCount : 气压计数 
	 */
	private double barometricCount = 999998;

	/**
	 * @Fields humidityCount : 湿度计数 
	 */
	private double humidityCount = 999998;

	/**
	 * @Fields temperatureCount : 温度计数 
	 */
	private double temperatureCount = 999998;

	/**
	 * @Fields timeInterval : 时间间隔 
	 */
	private double timeInterval = 999998;

	/**
	 * @Fields windSpeedMeasurementFactor : 风速计量因子 
	 */
	private double windSpeedMeasurementFactor = 999998;

	/**
	 * @Fields windDirectionMeasurementFactor : 风向计量因子 
	 */
	private double windDirectionMeasurementFactor = 999998;

	/**
	 * @Fields precipitationMeasurementFactor : 降水计量因子 
	 */
	private double precipitationMeasurementFactor = 999998;

	/**
	 * @Fields temperatureLinearSlopeRate : 温度线性转化后斜率 
	 */
	private double temperatureLinearSlopeRate = 999998;

	/**
	 * @Fields humidityLinearSlopeRate : 湿度线性转化后斜率 
	 */
	private double humidityLinearSlopeRate = 999998;

	/**
	 * @Fields pressureLinearSlopeRate : 气压线性转化后斜率 
	 */
	private double pressureLinearSlopeRate = 999998;

	/**
	 * @Fields temperatureLinearIntercept : 温度线性转化后截距 
	 */
	private double temperatureLinearIntercept = 999998;

	/**
	 * @Fields humidityLinearIntercept : 湿度线性转化后截距 
	 */
	private double humidityLinearIntercept = 999998;

	/**
	 * @Fields pressureLinearIntercept : 气压线性转化后截距 
	 */
	private double pressureLinearIntercept = 999998;

	/**
	 * @Fields windSpeedSensitivity : 风速灵敏度 
	 */
	private double windSpeedSensitivity = 999998;

	/**
	 * @Fields windDirectionAngle : 风向倾角 
	 */
	private double windDirectionAngle = 999998;

	/**
	 * @Fields precipitationSensorCorrectionFactor : 降水传感器订正因子 
	 */
	private double precipitationSensorCorrectionFactor = 999998;

	/**
	 * @Fields pressure : 气压 
	 */
	private double pressure = 999998;

	/**
	 * @Fields relativeHumidity : 相对湿度 
	 */
	private double relativeHumidity = 999998;

	/**
	 * @Fields temperature : 温度 
	 */
	private double temperature = 999998;

	/**
	 * @Fields windSpeed : 风速 
	 */
	private double windSpeed = 999998;

	/**
	 * @Fields windDirection : 风向 
	 */
	private double windDirection = 999998;

	/**
	 * @Fields precipitation : 降水 
	 */
	private double precipitation = 999998;

	/**
	 * @Fields massConcentrationOfPM10 : PM10质量浓度 
	 */
	private double massConcentrationOfPM10 = 999998;

	/**
	 * @Fields massConcentrationOfPM25 : PM2.5质量浓度 
	 */
	private double massConcentrationOfPM2p5 = 999998;

	/**
	 * @Fields massConcentrationOfPM1 : PM1质量浓度 
	 */
	private double massConcentrationOfPM1 = 999998;

	/**
	 * @Fields measurementDataFlag_pm10 : PM10测量数据标识 
	 */
	private String measurementDataFlag_pm10 = "999998";

	/**
	 * @Fields measurementDataFlag_pm2p5 : PM2.5测量数据标识 
	 */
	private String measurementDataFlag_pm2p5 = "999998";

	/**
	 * @Fields measurementDataFlag_pm1 : PM1测量数据标识 
	 */
	private String measurementDataFlag_pm1 = "999998";

	/**
	 * @Fields flowRate_pm10 : PM10仪器测量的流量 
	 */
	private double flowRate_pm10 = 999998;

	/**
	 * @Fields temperature_pm10 : PM10仪器测量的气温 
	 */
	private double temperature_pm10 = 999998;

	/**
	 * @Fields pressure_pm10 : PM10仪器测量的气压 
	 */
	private double pressure_pm10 = 999998;

	/**
	 * @Fields relativeHumidity_pm10 : PM10仪器测量的相对湿度 
	 */
	private double relativeHumidity_pm10 = 999998;

	/**
	 * @Fields flowRate_pm2p5 : PM2.5仪器测量的流量 
	 */
	private double flowRate_pm2p5 = 999998;

	/**
	 * @Fields temperature_pm2p5 : PM2.5仪器测量的气温 
	 */
	private double temperature_pm2p5 = 999998;

	/**
	 * @Fields pressure_pm2p5 : PM2.5仪器测量的气压 
	 */
	private double pressure_pm2p5 = 999998;

	/**
	 * @Fields relativeHumidity_pm2p5 : PM2.5仪器测量的相对湿度 
	 */
	private double relativeHumidity_pm2p5 = 999998;

	/**
	 * @Fields flowRate_pm1 : PM1.0仪器测量的流量 
	 */
	private double flowRate_pm1 = 999998;

	/**
	 * @Fields temperature_pm1 : PM1.0仪器测量的气温 
	 */
	private double temperature_pm1 = 999998;

	/**
	 * @Fields pressure_pm1 : PM1.0仪器测量的气压 
	 */
	private double pressure_pm1 = 999998;

	/**
	 * @Fields relativeHumidity_pm1 : PM1.0仪器测量的相对湿度 
	 */
	private double relativeHumidity_pm1 = 999998;
	/**
	 * 1小时 PM10质量浓度
	 */
	private double massConcentrationOfPM10_1hour = 999998;
	/**
	 * 24小时 PM10质量浓度
	 */
	private double massConcentrationOfPM10_24hour = 999998;
	
	/**
	 * 1小时 PM2.5质量浓度
	 */
	private double massConcentrationOfPM2p5_1hour = 999998;
	/**
	 * 24小时 PM2.5质量浓度
	 */
	private double massConcentrationOfPM2p5_24hour = 999998;
	/**
	 * 1小时 PM1质量浓度
	 */
	private double massConcentrationOfPM1_1hour = 999998;
	/**
	 * 24小时 PM1质量浓度
	 */
	private double massConcentrationOfPM1_24hour = 999998;
	/**
	 * 旁路流量
	 */
	private double  bypassFlow=999998;
	/**
	 * PM10-2.5主路流量
	 */
	private double  mainRoadFlowOfPM10_2p5=999998;
	/**
	 * 负载率
	 */
	private double  loadRate=999998;
	/**
	 * 频率
	 */
	private double  frequency=999998;
	/**
	 * 噪音
	 */
	private double  noise=999998;
	/**
	 * 运行状态码
	 */
	private double  runningStatusCode=999998;
	
	public double getMassConcentrationOfPM10_1hour() {
		return massConcentrationOfPM10_1hour;
	}

	public double getMassConcentrationOfPM10_24hour() {
		return massConcentrationOfPM10_24hour;
	}

	public double getMassConcentrationOfPM2p5_1hour() {
		return massConcentrationOfPM2p5_1hour;
	}

	public double getMassConcentrationOfPM2p5_24hour() {
		return massConcentrationOfPM2p5_24hour;
	}

	public double getMassConcentrationOfPM1_1hour() {
		return massConcentrationOfPM1_1hour;
	}

	public double getMassConcentrationOfPM1_24hour() {
		return massConcentrationOfPM1_24hour;
	}

	public double getBypassFlow() {
		return bypassFlow;
	}

	public double getMainRoadFlowOfPM10_2p5() {
		return mainRoadFlowOfPM10_2p5;
	}

	public double getLoadRate() {
		return loadRate;
	}

	public double getFrequency() {
		return frequency;
	}

	public double getNoise() {
		return noise;
	}

	public double getRunningStatusCode() {
		return runningStatusCode;
	}

	public void setMassConcentrationOfPM10_1hour(double massConcentrationOfPM10_1hour) {
		this.massConcentrationOfPM10_1hour = massConcentrationOfPM10_1hour;
	}

	public void setMassConcentrationOfPM10_24hour(double massConcentrationOfPM10_24hour) {
		this.massConcentrationOfPM10_24hour = massConcentrationOfPM10_24hour;
	}

	public void setMassConcentrationOfPM2p5_1hour(double massConcentrationOfPM2p5_1hour) {
		this.massConcentrationOfPM2p5_1hour = massConcentrationOfPM2p5_1hour;
	}

	public void setMassConcentrationOfPM2p5_24hour(double massConcentrationOfPM2p5_24hour) {
		this.massConcentrationOfPM2p5_24hour = massConcentrationOfPM2p5_24hour;
	}

	public void setMassConcentrationOfPM1_1hour(double massConcentrationOfPM1_1hour) {
		this.massConcentrationOfPM1_1hour = massConcentrationOfPM1_1hour;
	}

	public void setMassConcentrationOfPM1_24hour(double massConcentrationOfPM1_24hour) {
		this.massConcentrationOfPM1_24hour = massConcentrationOfPM1_24hour;
	}

	public void setBypassFlow(double bypassFlow) {
		this.bypassFlow = bypassFlow;
	}

	public void setMainRoadFlowOfPM10_2p5(double mainRoadFlowOfPM10_2p5) {
		this.mainRoadFlowOfPM10_2p5 = mainRoadFlowOfPM10_2p5;
	}

	public void setLoadRate(double loadRate) {
		this.loadRate = loadRate;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}

	public void setNoise(double noise) {
		this.noise = noise;
	}

	public void setRunningStatusCode(double runningStatusCode) {
		this.runningStatusCode = runningStatusCode;
	}

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
	public void setLatitude(double latitude) {
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
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * 经度getter 
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * 测站高度setter 
	 */
	public void setStationHeight(double stationHeight) {
		this.stationHeight = stationHeight;
	}

	/**
	 * 测站高度getter 
	 */
	public double getStationHeight() {
		return stationHeight;
	}

	/**
	 * 资料时间getter
	 */
	public Date getD_datetime() {
		return d_datetime;
	}

	/**
	 * 资料时间setter
	 */
	public void setD_datetime(Date d_datetime) {
		this.d_datetime = d_datetime;
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
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	/**
	 * 项目代码getter 
	 */
	public String getProjectCode() {
		return projectCode;
	}

	/**
	 * 存储位置setter 
	 */
	public void setStorageLocation(double storageLocation) {
		this.storageLocation = storageLocation;
	}


	/**
	 * 存储位置getter 
	 */
	public double getStorageLocation() {
		return storageLocation;
	}

	/**
	 * 重量因数setter 
	 */
	public void setWeightFactor(double weightFactor) {
		this.weightFactor = weightFactor;
	}

	/**
	 * 重量因数getter 
	 */
	public double getWeightFactor() {
		return weightFactor;
	}

	/**
	 * 错误代码setter 
	 */
	public void setErrorCode(double errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * 错误代码getter 
	 */
	public double getErrorCode() {
		return errorCode;
	}

	/**
	 * 电池电压代码setter 
	 */
	public void setBatteryVoltageCode(double batteryVoltageCode) {
		this.batteryVoltageCode = batteryVoltageCode;
	}

	/**
	 * 电池电压代码getter 
	 */
	public double getBatteryVoltageCode() {
		return batteryVoltageCode;
	}

	/**
	 * 阀电流setter 
	 */
	public void setValveCurrent(double valveCurrent) {
		this.valveCurrent = valveCurrent;
	}

	/**
	 * 阀电流getter 
	 */
	public double getValveCurrent() {
		return valveCurrent;
	}

	/**
	 * 综合订正计数setter 
	 */
	public void setComprehensiveCorrectionCount(double comprehensiveCorrectionCount) {
		this.comprehensiveCorrectionCount = comprehensiveCorrectionCount;
	}

	/**
	 * 综合订正计数getter 
	 */
	public double getComprehensiveCorrectionCount() {
		return comprehensiveCorrectionCount;
	}

	/**
	 * 气压计数setter 
	 */
	public void setBarometricCount(double barometricCount) {
		this.barometricCount = barometricCount;
	}

	/**
	 * 气压计数getter 
	 */
	public double getBarometricCount() {
		return barometricCount;
	}

	/**
	 * 湿度计数setter 
	 */
	public void setHumidityCount(double humidityCount) {
		this.humidityCount = humidityCount;
	}

	/**
	 * 湿度计数getter 
	 */
	public double getHumidityCount() {
		return humidityCount;
	}

	/**
	 * 温度计数setter 
	 */
	public void setTemperatureCount(double temperatureCount) {
		this.temperatureCount = temperatureCount;
	}

	/**
	 * 温度计数getter 
	 */
	public double getTemperatureCount() {
		return temperatureCount;
	}

	/**
	 * 时间间隔setter 
	 */
	public void setTimeInterval(double timeInterval) {
		this.timeInterval = timeInterval;
	}

	/**
	 * 时间间隔getter 
	 */
	public double getTimeInterval() {
		return timeInterval;
	}

	/**
	 * 风速计量因子setter 
	 */
	public void setWindSpeedMeasurementFactor(double windSpeedMeasurementFactor) {
		this.windSpeedMeasurementFactor = windSpeedMeasurementFactor;
	}

	/**
	 * 风速计量因子getter 
	 */
	public double getWindSpeedMeasurementFactor() {
		return windSpeedMeasurementFactor;
	}

	/**
	 * 风向计量因子setter 
	 */
	public void setWindDirectionMeasurementFactor(double windDirectionMeasurementFactor) {
		this.windDirectionMeasurementFactor = windDirectionMeasurementFactor;
	}

	/**
	 * 风向计量因子getter 
	 */
	public double getWindDirectionMeasurementFactor() {
		return windDirectionMeasurementFactor;
	}

	/**
	 * 降水计量因子setter 
	 */
	public void setPrecipitationMeasurementFactor(double precipitationMeasurementFactor) {
		this.precipitationMeasurementFactor = precipitationMeasurementFactor;
	}

	/**
	 * 降水计量因子getter 
	 */
	public double getPrecipitationMeasurementFactor() {
		return precipitationMeasurementFactor;
	}

	/**
	 * 温度线性转化后斜率setter 
	 */

	public void setTemperatureLinearSlopeRate(double temperatureLinearSlopeRate) {
		this.temperatureLinearSlopeRate = temperatureLinearSlopeRate;
	}

	/**
	 * 温度线性转化后斜率getter 
	 */
	public double getTemperatureLinearSlopeRate() {
		return temperatureLinearSlopeRate;
	}

	/**
	 * 湿度线性转化后斜率setter 
	 */

	public void setHumidityLinearSlopeRate(double humidityLinearSlopeRate) {
		this.humidityLinearSlopeRate = humidityLinearSlopeRate;
	}

	/**
	 * 湿度线性转化后斜率getter 
	 */
	public double getHumidityLinearSlopeRate() {
		return humidityLinearSlopeRate;
	}

	/**
	 * 气压线性转化后斜率setter 
	 */

	public void setPressureLinearSlopeRate(double pressureLinearSlopeRate) {
		this.pressureLinearSlopeRate = pressureLinearSlopeRate;
	}

	/**
	 * 气压线性转化后斜率getter 
	 */
	public double getPressureLinearSlopeRate() {
		return pressureLinearSlopeRate;
	}

	/**
	 * 温度线性转化后截距setter 
	 */

	public void setTemperatureLinearIntercept(double temperatureLinearIntercept) {
		this.temperatureLinearIntercept = temperatureLinearIntercept;
	}

	/**
	 * 温度线性转化后截距getter 
	 */
	public double getTemperatureLinearIntercept() {
		return temperatureLinearIntercept;
	}

	/**
	 * 湿度线性转化后截距setter 
	 */

	public void setHumidityLinearIntercept(double humidityLinearIntercept) {
		this.humidityLinearIntercept = humidityLinearIntercept;
	}

	/**
	 * 湿度线性转化后截距getter 
	 */
	public double getHumidityLinearIntercept() {
		return humidityLinearIntercept;
	}

	/**
	 * 气压线性转化后截距setter 
	 */
	public void setPressureLinearIntercept(double pressureLinearIntercept) {
		this.pressureLinearIntercept = pressureLinearIntercept;
	}

	/**
	 * 气压线性转化后截距getter 
	 */
	public double getPressureLinearIntercept() {
		return pressureLinearIntercept;
	}

	/**
	 * 风速灵敏度setter 
	 */
	public void setWindSpeedSensitivity(double windSpeedSensitivity) {
		this.windSpeedSensitivity = windSpeedSensitivity;
	}

	/**
	 * 风速灵敏度getter 
	 */
	public double getWindSpeedSensitivity() {
		return windSpeedSensitivity;
	}

	/**
	 * 风向倾角setter 
	 */
	public void setWindDirectionAngle(double windDirectionAngle) {
		this.windDirectionAngle = windDirectionAngle;
	}

	/**
	 * 风向倾角getter 
	 */
	public double getWindDirectionAngle() {
		return windDirectionAngle;
	}

	/**
	 * 降水传感器订正因子setter 
	 */
	public void setPrecipitationSensorCorrectionFactor(double precipitationSensorCorrectionFactor) {
		this.precipitationSensorCorrectionFactor = precipitationSensorCorrectionFactor;
	}

	/**
	 * 降水传感器订正因子getter 
	 */
	public double getPrecipitationSensorCorrectionFactor() {
		return precipitationSensorCorrectionFactor;
	}

	/**
	 * 气压setter 
	 */
	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	/**
	 * 气压getter 
	 */
	public double getPressure() {
		return pressure;
	}

	/**
	 * 相对湿度setter 
	 */
	public void setRelativeHumidity(double relativeHumidity) {
		this.relativeHumidity = relativeHumidity;
	}

	/**
	 * 相对湿度getter 
	 */
	public double getRelativeHumidity() {
		return relativeHumidity;
	}

	/**
	 * 温度setter 
	 */
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	/**
	 * 温度getter 
	 */
	public double getTemperature() {
		return temperature;
	}

	/**
	 * 风速setter 
	 */
	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}

	/**
	 * 风速getter 
	 */
	public double getWindSpeed() {
		return windSpeed;
	}

	/**
	 * 风向setter 
	 */
	public void setWindDirection(double windDirection) {
		this.windDirection = windDirection;
	}

	/**
	 * 风向getter 
	 */
	public double getWindDirection() {
		return windDirection;
	}

	/**
	 * 降水setter 
	 */
	public void setPrecipitation(double precipitation) {
		this.precipitation = precipitation;
	}

	/**
	 * 降水getter 
	 */
	public double getPrecipitation() {
		return precipitation;
	}

	/**
	 * PM10质量浓度setter 
	 */
	public void setMassConcentrationOfPM10(double massConcentrationOfPM10) {
		this.massConcentrationOfPM10 = massConcentrationOfPM10;
	}

	/**
	 * PM10质量浓度getter 
	 */
	public double getMassConcentrationOfPM10() {
		return massConcentrationOfPM10;
	}

	/**
	 * PM2.5质量浓度setter 
	 */

	public void setMassConcentrationOfPM2p5(double massConcentrationOfPM2p5) {
		this.massConcentrationOfPM2p5 = massConcentrationOfPM2p5;
	}

	/**
	 * PM2.5质量浓度getter 
	 */
	public double getMassConcentrationOfPM2p5() {
		return massConcentrationOfPM2p5;
	}

	/**
	 * PM1质量浓度setter 
	 */
	public void setMassConcentrationOfPM1(double massConcentrationOfPM1) {
		this.massConcentrationOfPM1 = massConcentrationOfPM1;
	}

	/**
	 * PM1质量浓度getter 
	 */
	public double getMassConcentrationOfPM1() {
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

	public void setFlowRate_pm10(double flowRate_pm10) {
		this.flowRate_pm10 = flowRate_pm10;
	}

	/**
	 * PM10仪器测量的流量getter 
	 */
	public double getFlowRate_pm10() {
		return flowRate_pm10;
	}

	/**
	 * PM10仪器测量的气温setter 
	 */

	public void setTemperature_pm10(double temperature_pm10) {
		this.temperature_pm10 = temperature_pm10;
	}

	/**
	 * PM10仪器测量的气温getter 
	 */
	public double getTemperature_pm10() {
		return temperature_pm10;
	}

	/**
	 * PM10仪器测量的气压setter 
	 */

	public void setPressure_pm10(double pressure_pm10) {
		this.pressure_pm10 = pressure_pm10;
	}

	/**
	 * PM10仪器测量的气压getter 
	 */
	public double getPressure_pm10() {
		return pressure_pm10;
	}

	/**
	 * PM10仪器测量的相对湿度setter 
	 */
	public void setRelativeHumidity_pm10(double relativeHumidity_pm10) {
		this.relativeHumidity_pm10 = relativeHumidity_pm10;
	}

	/**
	 * PM10仪器测量的相对湿度getter 
	 */

	public double getRelativeHumidity_pm10() {
		return relativeHumidity_pm10;
	}

	/**
	 * PM2.5仪器测量的流量setter 
	 */
	public void setFlowRate_pm2p5(double flowRate_pm2p5) {
		this.flowRate_pm2p5 = flowRate_pm2p5;
	}

	/**
	 * PM2.5仪器测量的流量getter 
	 */

	public double getFlowRate_pm2p5() {
		return flowRate_pm2p5;
	}

	/**
	 * PM2.5仪器测量的气温setter 
	 */
	public void setTemperature_pm2p5(double temperature_pm2p5) {
		this.temperature_pm2p5 = temperature_pm2p5;
	}

	/**
	 * PM2.5仪器测量的气温getter 
	 */

	public double getTemperature_pm2p5() {
		return temperature_pm2p5;
	}

	/**
	 * PM2.5仪器测量的气压setter 
	 */
	public void setPressure_pm2p5(double pressure_pm2p5) {
		this.pressure_pm2p5 = pressure_pm2p5;
	}

	/**
	 * PM2.5仪器测量的气压getter 
	 */

	public double getPressure_pm2p5() {
		return pressure_pm2p5;
	}

	/**
	 * PM2.5仪器测量的相对湿度setter 
	 */
	public void setRelativeHumidity_pm2p5(double relativeHumidity_pm2p5) {
		this.relativeHumidity_pm2p5 = relativeHumidity_pm2p5;
	}

	/**
	 * PM2.5仪器测量的相对湿度getter 
	 */

	public double getRelativeHumidity_pm2p5() {
		return relativeHumidity_pm2p5;
	}

	/**
	 * PM1.0仪器测量的流量setter 
	 */
	public void setFlowRate_pm1(double flowRate_pm1) {
		this.flowRate_pm1 = flowRate_pm1;
	}

	/**
	 * PM1.0仪器测量的流量getter 
	 */

	public double getFlowRate_pm1() {
		return flowRate_pm1;
	}

	/**
	 * PM1.0仪器测量的气温setter 
	 */
	public void setTemperature_pm1(double temperature_pm1) {
		this.temperature_pm1 = temperature_pm1;
	}

	/**
	 * PM1.0仪器测量的气温getter 
	 */

	public double getTemperature_pm1() {
		return temperature_pm1;
	}

	/**
	 * PM1.0仪器测量的气压setter 
	 */
	public void setPressure_pm1(double pressure_pm1) {
		this.pressure_pm1 = pressure_pm1;
	}

	/**
	 * PM1.0仪器测量的气压getter 
	 */

	public double getPressure_pm1() {
		return pressure_pm1;
	}

	/**
	 * PM1.0仪器测量的相对湿度setter 
	 */
	public void setRelativeHumidity_pm1(double relativeHumidity_pm1) {
		this.relativeHumidity_pm1 = relativeHumidity_pm1;
	}

	/**
	 * PM1.0仪器测量的相对湿度getter 
	 */

	public double getRelativeHumidity_pm1() {
		return relativeHumidity_pm1;
	}

}
