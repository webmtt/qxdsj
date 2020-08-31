package cma.cimiss2.dpc.decoder.bean.cawn;

import java.util.Date;

import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;

/**
 * 
 * <br>
 * @Title:  AtmosphericCompositionAerosolConcentration.java
 * @Package cma.cimiss2.dpc.decoder.bean.cawn
 * @Description:(大气成分气溶胶数浓度（NSD）资料实体类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月30日 上午9:44:02   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */
public class AtmosphericCompositionAerosolConcentration extends AgmeReportHeader {
	/**
	 * NO: 1.1  <br>
	 * nameCN: 区站号 <br>
	 * unit: <br>
	 * BUFR FXY: V01301 V01300 <br>
	 * description: 区站号
	 */
	String stationNumberChina;
	/**
	 * @Fields latitude : 纬度 
	 */
	private Double latitude = 999999.0;

	/**
	 * @Fields longitude : 经度 
	 */
	private Double longitude = 999999.0;
	/**
	 * nameCN:项目代码 <br>
	 * unit: <br>
	 * BUFR FXY:V_ITEM_CODE<br>
	 * descriptionCN:项目代码代码表
	 */
	double itemCode;
	/**
	 * NO: 1.6  <br>
	 * nameCN: 观测时间 <br>
	 * unit: 日期<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 时间（年月日时分秒共计14位，不足位补“0”）。
	 */
	Date observationTime;
	/**
	 * NO: 3.1  <br>
	 * nameCN:存储位置 <br>
	 * unit: <br>
	 * BUFR FXY:V_STORAGE_PLACE<br>
	 * descriptionCN:存储位置 
	 */
	double storagePlace;
	/**
	 * NO: 3.1  <br>
	 * nameCN:重量因数 <br>
	 * unit: <br>
	 * BUFR FXY:V_WEIGHT_FACTOR<br>
	 * descriptionCN:重量因数
	 */
	double weightFactor;
	/**
	 * NO: 3.1  <br>
	 * nameCN:错误代码 <br>
	 * unit: <br>
	 * BUFR FXY:V_ERROR_CODE<br>
	 * descriptionCN:错误代码
	 */
	double errorCode;
	/**
	 * NO: 3.1  <br>
	 * nameCN:电池电压代码<br>
	 * unit: <br>
	 * BUFR FXY:V15752<br>
	 * descriptionCN:电池电压代码
	 */
	double batteryVoltageCode;
	/**
	 * NO: 3.1  <br>
	 * nameCN:阀电流<br>
	 * unit:安培 <br>
	 * BUFR FXY:V15765<br>
	 * descriptionCN:阀电流
	 */
	double valveCurrent;
	/**
	 * NO: 3.1  <br>
	 * nameCN:综合订正计数<br>
	 * unit: <br>
	 * BUFR FXY:V_CORRECT_COUNT<br>
	 * descriptionCN:综合订正计数
	 */
	double correctCount;
	/**
	 * NO: 3.1  <br>
	 * nameCN:气压计数<br>
	 * unit: 百帕<br>
	 * BUFR FXY:V_PCOUNT<br>
	 * descriptionCN:气压计数
	 */
	double pressureCount;
	/**
	 * NO: 3.1  <br>
	 * nameCN:备用1<br>
	 * unit: <br>
	 * BUFR FXY:V_BACKUP1<br>
	 * descriptionCN:备用1
	 */
	double backUp1 = 999998;
	/**
	 * nameCN:湿度计数<br>
	 * unit: %<br>
	 * BUFR FXY:V13003_040<br>
	 * descriptionCN:湿度计数
	 */
	double humidityCount;
	/**
	 * nameCN:温度计数<br>
	 * unit: 摄氏度<br>
	 * BUFR FXY:V12001_040<br>
	 * descriptionCN:温度计数
	 */
	double temperatureCount;
	/**
	 * nameCN:时间间隔<br>
	 * unit: 秒<br>
	 * BUFR FXY:V_TIME_SPACING<br>
	 * descriptionCN:时间间隔
	 */
	double timeInterval;
	/**
	 * nameCN:风速计量因子<br>
	 * unit: 伏<br>
	 * BUFR FXY:V11002_071<br>
	 * descriptionCN:风速计量因子
	 */
	double windSpeedMeteringFactor;
	/**
	 * nameCN:风向计量因子<br>
	 * unit: 伏<br>
	 * BUFR FXY:V11001_071<br>
	 * descriptionCN:风向计量因子
	 */
	double windDirectionMeteringFactor;
	/**
	 * nameCN:降水计量因子<br>
	 * unit: 毫米/伏<br>
	 * BUFR FXY:V13011_071<br>
	 * descriptionCN:降水计量因子
	 */
	double precipitationMeteringFactor;
	/**
	 * nameCN:温度斜率订正<br>
	 * unit: 摄氏度/伏<br>
	 * BUFR FXY:V12001_074<br>
	 * descriptionCN:温度斜率订正
	 */
	double  temperatureSlopeCorrect;
	/**
	 * nameCN:湿度斜率订正<br>
	 * unit: %/伏<br>
	 * BUFR FXY:V13003_074<br>
	 * descriptionCN:湿度斜率订正
	 */
	double humiditySlopeCorrect;
	/**
	 * nameCN:气压斜率订正<br>
	 * unit: 百帕/伏<br>
	 * BUFR FXY:V10004_074<br>
	 * descriptionCN:气压斜率订正
	 */
	double pressureSlopeCorrect;
	/**
	 * nameCN:温度偏移订正<br>
	 * unit: 伏<br>
	 * BUFR FXY:V12001_075<br>
	 * descriptionCN:温度偏移订正
	 */
	double temperatureBiasCorrect;
	/**
	 * nameCN:湿度偏移订正<br>
	 * unit: 伏<br>
	 * BUFR FXY:V13003_075<br>
	 * descriptionCN:湿度偏移订正
	 */
	double humidityBiasCorrect;
	/**
	 * nameCN:气压偏移订正<br>
	 * unit: 伏<br>
	 * BUFR FXY:V10004_075<br>
	 * descriptionCN:气压偏移订正
	 */
	double pressureBiasCorrect;
	/**
	 * nameCN:风速灵敏度<br>
	 * unit: m/s/v<br>
	 * BUFR FXY:V11450<br>
	 * descriptionCN:风速灵敏度
	 */
	double windSpeedSensitivity;
	/**
	 * nameCN:风向倾角<br>
	 * unit: 度/伏<br>
	 * BUFR FXY:V11451<br>
	 * descriptionCN:风向倾角
	 */
	double windDirectionDip;
	/**
	 * nameCN:降水传感器订正因子<br>
	 * unit: 毫米/伏<br>
	 * BUFR FXY:V02440<br>
	 * descriptionCN:降水传感器订正因子
	 */
	double precipitationSensorCorrectFactor;
	/**
	 * nameCN:气压<br>
	 * unit: 百帕<br>
	 * BUFR FXY:V10004<br>
	 * descriptionCN:百帕
	 */
	double airPressure;
	/**
	 * nameCN:备用2<br>
	 * unit: <br>
	 * BUFR FXY:V_BACKUP2<br>
	 * descriptionCN:备用2
	 */
	double backUp2 = 999998;
	/**
	 * nameCN:湿度<br>
	 * unit: %<br>
	 * BUFR FXY:V13003<br>
	 * descriptionCN:湿度
	 */
	double humidity;
	/**
	 * nameCN:温度<br>
	 * unit: 摄氏度<br>
	 * BUFR FXY:V12001<br>
	 * descriptionCN:温度
	 */
	double temperature;
	/**
	 * nameCN:风速<br>
	 * unit: 米/秒<br>
	 * BUFR FXY:V11002<br>
	 * descriptionCN:风速
	 */
	double windSpeed;
	/**
	 * nameCN:风向<br>
	 * unit: 度<br>
	 * BUFR FXY:V11001<br>
	 * descriptionCN:风向
	 */
	double windDirection;
	/**
	 * nameCN:降水<br>
	 * unit: 毫米<br>
	 * BUFR FXY:V13011<br>
	 * descriptionCN:降水
	 */
	double precipitation;
	/**
	 * nameCN:C1通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C01<br>
	 * descriptionCN:C1通道数浓度
	 */
	double channelConcentration_C1;
	/**
	 * nameCN:C2通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C02<br>
	 * descriptionCN:C2通道数浓度
	 */
	double channelConcentration_C2;
	/**
	 * nameCN:C3通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C03<br>
	 * descriptionCN:C3通道数浓度
	 */
	double channelConcentration_C3;
	/**
	 * nameCN:C4通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C04<br>
	 * descriptionCN:C4通道数浓度
	 */
	double channelConcentration_C4;
	/**
	 * nameCN:C5通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C05<br>
	 * descriptionCN:C5通道数浓度
	 */
	double channelConcentration_C5;
	/**
	 * nameCN:C6通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C06<br>
	 * descriptionCN:C6通道数浓度
	 */
	double channelConcentration_C6;
	/**
	 * nameCN:C7通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C07<br>
	 * descriptionCN:C7通道数浓度
	 */
	double channelConcentration_C7;
	/**
	 * nameCN:C8通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C08<br>
	 * descriptionCN:C8通道数浓度
	 */
	double channelConcentration_C8;
	/**
	 * nameCN:C9通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C09<br>
	 * descriptionCN:C9通道数浓度
	 */
	double channelConcentration_C9;
	/**
	 * nameCN:C10通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C10<br>
	 * descriptionCN:C10通道数浓度
	 */
	double channelConcentration_C10;
	/**
	 * nameCN:C11通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C11<br>
	 * descriptionCN:C11通道数浓度
	 */
	double channelConcentration_C11;
	/**
	 * nameCN:C12通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C12<br>
	 * descriptionCN:C12通道数浓度
	 */
	double channelConcentration_C12;
	/**
	 * nameCN:C13通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C13<br>
	 * descriptionCN:C13通道数浓度
	 */
	double channelConcentration_C13;
	/**
	 * nameCN:C14通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C14<br>
	 * descriptionCN:C14通道数浓度
	 */
	double channelConcentration_C14;
	/**
	 * nameCN:C15通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C15<br>
	 * descriptionCN:C15通道数浓度
	 */
	double channelConcentration_C15;
	/**
	 * nameCN:C16通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C16<br>
	 * descriptionCN:C16通道数浓度
	 */
	double channelConcentration_C16;
	/**
	 * nameCN:C17通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C17<br>
	 * descriptionCN:C17通道数浓度
	 */
	double channelConcentration_C17;
	/**
	 * nameCN:C18通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C18<br>
	 * descriptionCN:C18通道数浓度
	 */
	double channelConcentration_C18;
	/**
	 * nameCN:C19通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C19<br>
	 * descriptionCN:C19通道数浓度
	 */
	double channelConcentration_C19;
	/**
	 * nameCN:C20通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C20<br>
	 * descriptionCN:C20通道数浓度
	 */
	double channelConcentration_C20;
	/**
	 * nameCN:C21通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C21<br>
	 * descriptionCN:C21通道数浓度
	 */
	double channelConcentration_C21;
	/**
	 * nameCN:C22通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C22<br>
	 * descriptionCN:C22通道数浓度
	 */
	double channelConcentration_C22;
	/**
	 * nameCN:C23通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C23<br>
	 * descriptionCN:C23通道数浓度
	 */
	double channelConcentration_C23;
	/**
	 * nameCN:C24通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C24<br>
	 * descriptionCN:C24通道数浓度
	 */
	double channelConcentration_C24;
	/**
	 * nameCN:C25通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C25<br>
	 * descriptionCN:C25通道数浓度
	 */
	double channelConcentration_C25;
	/**
	 * nameCN:C26通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C26<br>
	 * descriptionCN:C26通道数浓度
	 */
	double channelConcentration_C26;
	/**
	 * nameCN:C27通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C27<br>
	 * descriptionCN:C27通道数浓度
	 */
	double channelConcentration_C27;
	/**
	 * nameCN:C28通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C28<br>
	 * descriptionCN:C28通道数浓度
	 */
	double channelConcentration_C28;
	/**
	 * nameCN:C29通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C29<br>
	 * descriptionCN:C29通道数浓度
	 */
	double channelConcentration_C29;
	/**
	 * nameCN:C30通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C30<br>
	 * descriptionCN:C30通道数浓度
	 */
	double channelConcentration_C30;
	/**
	 * nameCN:C31通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C31<br>
	 * descriptionCN:C31通道数浓度
	 */
	double channelConcentration_C31;
	/**
	 * nameCN:C32通道数浓度<br>
	 * unit: 个<br>
	 * BUFR FXY:V15023_C32<br>
	 * descriptionCN:C32通道数浓度
	 */
	double channelConcentration_C32;
	public String getStationNumberChina() {
		return stationNumberChina;
	}
	public void setStationNumberChina(String stationNumberChina) {
		this.stationNumberChina = stationNumberChina;
	}
	public double getItemCode() {
		return itemCode;
	}
	public void setItemCode(double itemCode) {
		this.itemCode = itemCode;
	}
	public Date getObservationTime() {
		return observationTime;
	}
	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}
	public double getStoragePlace() {
		return storagePlace;
	}
	public void setStoragePlace(double storagePlace) {
		this.storagePlace = storagePlace;
	}
	public double getWeightFactor() {
		return weightFactor;
	}
	public void setWeightFactor(double weightFactor) {
		this.weightFactor = weightFactor;
	}
	public double getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(double errorCode) {
		this.errorCode = errorCode;
	}
	public double getBatteryVoltageCode() {
		return batteryVoltageCode;
	}
	public void setBatteryVoltageCode(double batteryVoltageCode) {
		this.batteryVoltageCode = batteryVoltageCode;
	}
	public double getValveCurrent() {
		return valveCurrent;
	}
	public void setValveCurrent(double valveCurrent) {
		this.valveCurrent = valveCurrent;
	}
	public double getCorrectCount() {
		return correctCount;
	}
	public void setCorrectCount(double correctCount) {
		this.correctCount = correctCount;
	}
	public double getPressureCount() {
		return pressureCount;
	}
	public void setPressureCount(double pressureCount) {
		this.pressureCount = pressureCount;
	}
	public double getBackUp1() {
		return backUp1;
	}
	public void setBackUp1(double backUp1) {
		this.backUp1 = backUp1;
	}
	public double getHumidityCount() {
		return humidityCount;
	}
	public void setHumidityCount(double humidityCount) {
		this.humidityCount = humidityCount;
	}
	public double getTemperatureCount() {
		return temperatureCount;
	}
	public void setTemperatureCount(double temperatureCount) {
		this.temperatureCount = temperatureCount;
	}
	public double getTimeInterval() {
		return timeInterval;
	}
	public void setTimeInterval(double timeInterval) {
		this.timeInterval = timeInterval;
	}
	public double getWindSpeedMeteringFactor() {
		return windSpeedMeteringFactor;
	}
	public void setWindSpeedMeteringFactor(double windSpeedMeteringFactor) {
		this.windSpeedMeteringFactor = windSpeedMeteringFactor;
	}
	public double getWindDirectionMeteringFactor() {
		return windDirectionMeteringFactor;
	}
	public void setWindDirectionMeteringFactor(double windDirectionMeteringFactor) {
		this.windDirectionMeteringFactor = windDirectionMeteringFactor;
	}
	public double getPrecipitationMeteringFactor() {
		return precipitationMeteringFactor;
	}
	public void setPrecipitationMeteringFactor(double precipitationMeteringFactor) {
		this.precipitationMeteringFactor = precipitationMeteringFactor;
	}
	public double getTemperatureSlopeCorrect() {
		return temperatureSlopeCorrect;
	}
	public void setTemperatureSlopeCorrect(double temperatureSlopeCorrect) {
		this.temperatureSlopeCorrect = temperatureSlopeCorrect;
	}
	public double getHumiditySlopeCorrect() {
		return humiditySlopeCorrect;
	}
	public void setHumiditySlopeCorrect(double humiditySlopeCorrect) {
		this.humiditySlopeCorrect = humiditySlopeCorrect;
	}
	public double getPressureSlopeCorrect() {
		return pressureSlopeCorrect;
	}
	public void setPressureSlopeCorrect(double pressureSlopeCorrect) {
		this.pressureSlopeCorrect = pressureSlopeCorrect;
	}
	public double getTemperatureBiasCorrect() {
		return temperatureBiasCorrect;
	}
	public void setTemperatureBiasCorrect(double temperatureBiasCorrect) {
		this.temperatureBiasCorrect = temperatureBiasCorrect;
	}
	public double getHumidityBiasCorrect() {
		return humidityBiasCorrect;
	}
	public void setHumidityBiasCorrect(double humidityBiasCorrect) {
		this.humidityBiasCorrect = humidityBiasCorrect;
	}
	public double getPressureBiasCorrect() {
		return pressureBiasCorrect;
	}
	public void setPressureBiasCorrect(double pressureBiasCorrect) {
		this.pressureBiasCorrect = pressureBiasCorrect;
	}
	public double getWindSpeedSensitivity() {
		return windSpeedSensitivity;
	}
	public void setWindSpeedSensitivity(double windSpeedSensitivity) {
		this.windSpeedSensitivity = windSpeedSensitivity;
	}
	public double getWindDirectionDip() {
		return windDirectionDip;
	}
	public void setWindDirectionDip(double windDirectionDip) {
		this.windDirectionDip = windDirectionDip;
	}
	public double getPrecipitationSensorCorrectFactor() {
		return precipitationSensorCorrectFactor;
	}
	public void setPrecipitationSensorCorrectFactor(double precipitationSensorCorrectFactor) {
		this.precipitationSensorCorrectFactor = precipitationSensorCorrectFactor;
	}
	public double getAirPressure() {
		return airPressure;
	}
	public void setAirPressure(double airPressure) {
		this.airPressure = airPressure;
	}
	public double getBackUp2() {
		return backUp2;
	}
	public void setBackUp2(double backUp2) {
		this.backUp2 = backUp2;
	}
	public double getHumidity() {
		return humidity;
	}
	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public double getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}
	public double getWindDirection() {
		return windDirection;
	}
	public void setWindDirection(double windDirection) {
		this.windDirection = windDirection;
	}
	public double getPrecipitation() {
		return precipitation;
	}
	public void setPrecipitation(double precipitation) {
		this.precipitation = precipitation;
	}
	public double getChannelConcentration_C1() {
		return channelConcentration_C1;
	}
	public void setChannelConcentration_C1(double channelConcentration_C1) {
		this.channelConcentration_C1 = channelConcentration_C1;
	}
	public double getChannelConcentration_C2() {
		return channelConcentration_C2;
	}
	public void setChannelConcentration_C2(double channelConcentration_C2) {
		this.channelConcentration_C2 = channelConcentration_C2;
	}
	public double getChannelConcentration_C3() {
		return channelConcentration_C3;
	}
	public void setChannelConcentration_C3(double channelConcentration_C3) {
		this.channelConcentration_C3 = channelConcentration_C3;
	}
	public double getChannelConcentration_C4() {
		return channelConcentration_C4;
	}
	public void setChannelConcentration_C4(double channelConcentration_C4) {
		this.channelConcentration_C4 = channelConcentration_C4;
	}
	public double getChannelConcentration_C5() {
		return channelConcentration_C5;
	}
	public void setChannelConcentration_C5(double channelConcentration_C5) {
		this.channelConcentration_C5 = channelConcentration_C5;
	}
	public double getChannelConcentration_C6() {
		return channelConcentration_C6;
	}
	public void setChannelConcentration_C6(double channelConcentration_C6) {
		this.channelConcentration_C6 = channelConcentration_C6;
	}
	public double getChannelConcentration_C7() {
		return channelConcentration_C7;
	}
	public void setChannelConcentration_C7(double channelConcentration_C7) {
		this.channelConcentration_C7 = channelConcentration_C7;
	}
	public double getChannelConcentration_C8() {
		return channelConcentration_C8;
	}
	public void setChannelConcentration_C8(double channelConcentration_C8) {
		this.channelConcentration_C8 = channelConcentration_C8;
	}
	public double getChannelConcentration_C9() {
		return channelConcentration_C9;
	}
	public void setChannelConcentration_C9(double channelConcentration_C9) {
		this.channelConcentration_C9 = channelConcentration_C9;
	}
	public double getChannelConcentration_C10() {
		return channelConcentration_C10;
	}
	public void setChannelConcentration_C10(double channelConcentration_C10) {
		this.channelConcentration_C10 = channelConcentration_C10;
	}
	public double getChannelConcentration_C11() {
		return channelConcentration_C11;
	}
	public void setChannelConcentration_C11(double channelConcentration_C11) {
		this.channelConcentration_C11 = channelConcentration_C11;
	}
	public double getChannelConcentration_C12() {
		return channelConcentration_C12;
	}
	public void setChannelConcentration_C12(double channelConcentration_C12) {
		this.channelConcentration_C12 = channelConcentration_C12;
	}
	public double getChannelConcentration_C13() {
		return channelConcentration_C13;
	}
	public void setChannelConcentration_C13(double channelConcentration_C13) {
		this.channelConcentration_C13 = channelConcentration_C13;
	}
	public double getChannelConcentration_C14() {
		return channelConcentration_C14;
	}
	public void setChannelConcentration_C14(double channelConcentration_C14) {
		this.channelConcentration_C14 = channelConcentration_C14;
	}
	public double getChannelConcentration_C15() {
		return channelConcentration_C15;
	}
	public void setChannelConcentration_C15(double channelConcentration_C15) {
		this.channelConcentration_C15 = channelConcentration_C15;
	}
	public double getChannelConcentration_C16() {
		return channelConcentration_C16;
	}
	public void setChannelConcentration_C16(double channelConcentration_C16) {
		this.channelConcentration_C16 = channelConcentration_C16;
	}
	public double getChannelConcentration_C17() {
		return channelConcentration_C17;
	}
	public void setChannelConcentration_C17(double channelConcentration_C17) {
		this.channelConcentration_C17 = channelConcentration_C17;
	}
	public double getChannelConcentration_C18() {
		return channelConcentration_C18;
	}
	public void setChannelConcentration_C18(double channelConcentration_C18) {
		this.channelConcentration_C18 = channelConcentration_C18;
	}
	public double getChannelConcentration_C19() {
		return channelConcentration_C19;
	}
	public void setChannelConcentration_C19(double channelConcentration_C19) {
		this.channelConcentration_C19 = channelConcentration_C19;
	}
	public double getChannelConcentration_C20() {
		return channelConcentration_C20;
	}
	public void setChannelConcentration_C20(double channelConcentration_C20) {
		this.channelConcentration_C20 = channelConcentration_C20;
	}
	public double getChannelConcentration_C21() {
		return channelConcentration_C21;
	}
	public void setChannelConcentration_C21(double channelConcentration_C21) {
		this.channelConcentration_C21 = channelConcentration_C21;
	}
	public double getChannelConcentration_C22() {
		return channelConcentration_C22;
	}
	public void setChannelConcentration_C22(double channelConcentration_C22) {
		this.channelConcentration_C22 = channelConcentration_C22;
	}
	public double getChannelConcentration_C23() {
		return channelConcentration_C23;
	}
	public void setChannelConcentration_C23(double channelConcentration_C23) {
		this.channelConcentration_C23 = channelConcentration_C23;
	}
	public double getChannelConcentration_C24() {
		return channelConcentration_C24;
	}
	public void setChannelConcentration_C24(double channelConcentration_C24) {
		this.channelConcentration_C24 = channelConcentration_C24;
	}
	public double getChannelConcentration_C25() {
		return channelConcentration_C25;
	}
	public void setChannelConcentration_C25(double channelConcentration_C25) {
		this.channelConcentration_C25 = channelConcentration_C25;
	}
	public double getChannelConcentration_C26() {
		return channelConcentration_C26;
	}
	public void setChannelConcentration_C26(double channelConcentration_C26) {
		this.channelConcentration_C26 = channelConcentration_C26;
	}
	public double getChannelConcentration_C27() {
		return channelConcentration_C27;
	}
	public void setChannelConcentration_C27(double channelConcentration_C27) {
		this.channelConcentration_C27 = channelConcentration_C27;
	}
	public double getChannelConcentration_C28() {
		return channelConcentration_C28;
	}
	public void setChannelConcentration_C28(double channelConcentration_C28) {
		this.channelConcentration_C28 = channelConcentration_C28;
	}
	public double getChannelConcentration_C29() {
		return channelConcentration_C29;
	}
	public void setChannelConcentration_C29(double channelConcentration_C29) {
		this.channelConcentration_C29 = channelConcentration_C29;
	}
	public double getChannelConcentration_C30() {
		return channelConcentration_C30;
	}
	public void setChannelConcentration_C30(double channelConcentration_C30) {
		this.channelConcentration_C30 = channelConcentration_C30;
	}
	public double getChannelConcentration_C31() {
		return channelConcentration_C31;
	}
	public void setChannelConcentration_C31(double channelConcentration_C31) {
		this.channelConcentration_C31 = channelConcentration_C31;
	}
	public double getChannelConcentration_C32() {
		return channelConcentration_C32;
	}
	public void setChannelConcentration_C32(double channelConcentration_C32) {
		this.channelConcentration_C32 = channelConcentration_C32;
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
	
	
	
}
	
	