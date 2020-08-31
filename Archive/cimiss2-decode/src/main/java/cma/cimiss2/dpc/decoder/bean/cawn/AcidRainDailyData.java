package cma.cimiss2.dpc.decoder.bean.cawn;

import java.util.Date;

/**
 * 
 * -------------------------------------------------------------------------------
 * <br>
 * @Title:  AcidRainDailyData.java   
 * @Package cma.cimiss2.dpc.decoder.bean.cawn   
 * @Description:    TODO(大气成分酸雨日数据解码实体类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年2月9日 上午11:33:03   maxiyue    Initial creation.
 * </pre>
 * 
 * @author maxiyue
 *---------------------------------------------------------------------------------
 */
public class AcidRainDailyData {
	/**
	 * NO: 1.1  <br>
	 * nameCN: 区站号 <br>
	 * unit: <br>
	 * BUFR FXY: V01301 <br>
	 * descriptionCN: 5位数字或第1位为字母，第2-5位为数字
	 */
	String stationNumberChina;
	
	/**
	 * NO: 1.2 <br>
	 * nameCN: 纬度 <br>
	 * unit: 度 <br>
	 * BUFR FXY: V05001 <br>
	 * descriptionCN:
	 */
	Double latitude;
	/**
	 * NO: 1.3  <br>
	 * nameCN: 经度 <br>
	 * unit: 度 <br>
	 * BUFR FXY: V06001 <br>
	 * descriptionCN:
	 */
	Double longitude;
	/**
	 * NO: 1.4  <br>
	 * nameCN: 观测场海拔高度 <br>
	 * unit: 1m <br>
	 * BUFR FXY: V07001 <br>
	 * descriptionCN: 若低于海平面，为负值
	 */
	Double heightOfSationGroundAboveMeanSeaLevel;
	/**
	 * NO: 1.5  <br>
	 * nameCN: 观测方式 <br>
	 * unit: <br>
	 * BUFR FXY: V01405 <br>
	 * descriptionCN: 
	 */
	Double ObservationMethod;
	/**
	 * NO: 1.6  <br>
	 * nameCN: 观测时间 <br>
	 * unit: 日期<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 观测时间。
	 */
	Date observationTime;
	/**
	 * NO: 1.7  <br>
	 * nameCN: 降水开始时间 <br>
	 * unit: <br>
	 * BUFR FXY: V04307 <br>
	 * descriptionCN: 格式：日时分
	 */
	Double Precipitation_StartTime;
	/**
	 * NO: 1.8  <br>
	 * nameCN: 降水结束时间 <br>
	 * unit: <br>
	 * BUFR FXY: V04308 <br>
	 * descriptionCN: 格式：日时分
	 */
	Double Precipitation_EndTime;
	/**
	 * NO: 1.9  <br>
	 * nameCN: 酸雨观测降水量<br>
	 * unit: <br>
	 * BUFR FXY: V13011 <br>
	 * descriptionCN: 
	 */
	Double AcidRainObservedPrecipitation;
	/**
	 * NO: 2.0  <br>
	 * nameCN: 初测时样品温度<br>
	 * unit: <br>
	 * BUFR FXY: V12501_01 <br>
	 * descriptionCN: 
	 */
	Double InitialSurveySampleTemperature;
	/**
	 * NO: 2.1  <br>
	 * nameCN:初测pH值第1次读数<br>
	 * unit: <br>
	 * BUFR FXY:V15532_01_1 <br>
	 * descriptionCN: 
	 */
	Double InitialSurvey_PH_1;
	/**
	 * NO: 2.2  <br>
	 * nameCN:初测pH值第2次读数<br>
	 * unit: <br>
	 * BUFR FXY:V15532_01_2 <br>
	 * descriptionCN: 
	 */
	Double InitialSurvey_PH_2;
	/**
	 * NO: 2.3 <br>
	 * nameCN:初测pH值第3次读数<br>
	 * unit: <br>
	 * BUFR FXY:V15532_01_3<br>
	 * descriptionCN: 
	 */
	Double InitialSurvey_PH_3;
	/**
	 * NO: 2.4<br>
	 * nameCN:初测平均pH值<br>
	 * unit: <br>
	 * BUFR FXY:V15532_01_701<br>
	 * descriptionCN: 
	 */
	Double InitialSurvey_Average_PH;
	/**
	 * NO: 2.5  <br>
	 * nameCN:初测K值第1次读数<br>
	 * unit: <br>
	 * BUFR FXY:V13371_01_1 <br>
	 * descriptionCN: 
	 */
	Double InitialSurvey_K_1;
	/**
	 * NO: 2.6  <br>
	 * nameCN:初测K值第2次读数<br>
	 * unit: <br>
	 * BUFR FXY:V13371_01_2 <br>
	 * descriptionCN: 
	 */
	Double InitialSurvey_K_2;
	/**
	 * NO: 2.7  <br>
	 * nameCN:初测K值第3次读数<br>
	 * unit: <br>
	 * BUFR FXY:V13371_01_3 <br>
	 * descriptionCN: 
	 */
	Double InitialSurvey_K_3;
	/**
	 * NO: 2.8  <br>
	 * nameCN:初测25℃时平均K值<br>
	 * unit: <br>
	 * BUFR FXY:V13371_01_701<br>
	 * descriptionCN: 
	 */
	Double InitialSurvey25_Average_K;
	/**
	 * NO: 2.9 <br>
	 * nameCN:复测时样品温度<br>
	 * unit: <br>
	 * BUFR FXY:V12501_02<br>
	 * descriptionCN: 
	 */
	Double RetestSurveySampleTemperature;
	/**
	 * NO: 3.0 <br>
	 * nameCN:复测pH值第1次读数<br>
	 * unit: <br>
	 * BUFR FXY:V15532_02_1<br>
	 * descriptionCN: 
	 */
	Double RetestSurvey_PH_1;
	/**
	 * NO: 3.1 <br>
	 * nameCN:复测pH值第2次读数<br>
	 * unit: <br>
	 * BUFR FXY:V15532_02_2<br>
	 * descriptionCN: 
	 */
	Double RetestSurvey_PH_2;
	/**
	 * NO: 3.2 <br>
	 * nameCN:复测pH值第3次读数<br>
	 * unit: <br>
	 * BUFR FXY:V15532_02_3<br>
	 * descriptionCN: 
	 */
	Double RetestSurvey_PH_3;
	/**
	 * NO: 3.3<br>
	 * nameCN:复测平均pH值<br>
	 * unit: <br>
	 * BUFR FXY:V15532_02_701<br>
	 * descriptionCN: 
	 */
	Double RetestSurvey_Average_PH;
	/**
	 * NO: 3.4 <br>
	 * nameCN:复测k值第1次读数<br>
	 * unit: <br>
	 * BUFR FXY:V13371_02_1<br>
	 * descriptionCN: 
	 */
	Double RetestSurvey_K_1;
	/**
	 * NO: 3.5 <br>
	 * nameCN:复测k值第2次读数<br>
	 * unit: <br>
	 * BUFR FXY:V13371_02_2<br>
	 * descriptionCN: 
	 */
	Double RetestSurvey_K_2;
	/**
	 * NO: 3.6 <br>
	 * nameCN:复测k值第3次读数<br>
	 * unit: <br>
	 * BUFR FXY:V13371_02_3<br>
	 * descriptionCN: 
	 */
	Double RetestSurvey_K_3;
	/**
	 * NO: 3.7 <br>
	 * nameCN:复测25℃时平均K值<br>
	 * unit: <br>
	 * BUFR FXY:V13371_02_701<br>
	 * descriptionCN: 
	 */
	Double RetestSurvey25_Average_K;
	/**
	 * NO: 3.8 <br>
	 * nameCN:14时风向<br>
	 * unit: <br>
	 * BUFR FXY:V11001_14<br>
	 * descriptionCN: 
	 */
	Double WindDirection_14;
	/**
	 * NO: 3.9 <br>
	 * nameCN:14时风速<br>
	 * unit: <br>
	 * BUFR FXY:V11002_14<br>
	 * descriptionCN: 
	 */
	Double WindSpeed_14;
	/**
	 * NO: 4.0 <br>
	 * nameCN:20时风向<br>
	 * unit: <br>
	 * BUFR FXY:V11001_20<br>
	 * descriptionCN: 
	 */
	Double WindDirection_20;
	/**
	 * NO: 4.1 <br>
	 * nameCN:20时风速<br>
	 * unit: <br>
	 * BUFR FXY:V11002_20<br>
	 * descriptionCN: 
	 */
	Double WindSpeed_20;
	/**
	 * NO: 4.2<br>
	 * nameCN:02时风向<br>
	 * unit: <br>
	 * BUFR FXY:V11001_02<br>
	 * descriptionCN: 
	 */
	Double WindDirection_02;
	/**
	 * NO: 4.3<br>
	 * nameCN:02时风速<br>
	 * unit: <br>
	 * BUFR FXY:V11002_02<br>
	 * descriptionCN: 
	 */
	Double WindSpeed_02;
	/**
	 * NO: 4.4 <br>
	 * nameCN:08时风向<br>
	 * unit: <br>
	 * BUFR FXY:V11001_08<br>
	 * descriptionCN: 
	 */
	Double WindDirection_08;
	/**
	 * NO: 4.5 <br>
	 * nameCN:08时风速<br>
	 * unit: <br>
	 * BUFR FXY:V11002_08<br>
	 * descriptionCN: 
	 */
	Double WindSpeed_08;
	/**
	 * NO: 4.6 <br>
	 * nameCN:降水期间天气现象1<br>
	 * unit: <br>
	 * BUFR FXY:V20003_1<br>
	 * descriptionCN: 
	 */
	Double PrecipitationPeriodWeatherPhenomenon_1 ;
	/**
	 * NO: 4.7 <br>
	 * nameCN:降水期间天气现象2<br>
	 * unit: <br>
	 * BUFR FXY:V20003_2<br>
	 * descriptionCN: 
	 */
	Double PrecipitationPeriodWeatherPhenomenon_2 ;
	/**
	 * NO: 4.8 <br>
	 * nameCN:降水期间天气现象3<br>
	 * unit: <br>
	 * BUFR FXY:V20003_3<br>
	 * descriptionCN: 
	 */
	Double PrecipitationPeriodWeatherPhenomenon_3;
	/**
	 * NO: 4.9 <br>
	 * nameCN:降水期间天气现象4<br>
	 * unit: <br>
	 * BUFR FXY:V20003_4<br>
	 * descriptionCN: 
	 */
	Double PrecipitationPeriodWeatherPhenomenon_4 ;
	/**
	 * NO: 5.0 <br>
	 * nameCN:复测指示码<br>
	 * unit: <br>
	 * BUFR FXY:V02452<br>
	 * descriptionCN: 
	 */
	Double RepeatSurveyIndicatorCode ;
	/**
	 * NO: 5.1 <br>
	 * nameCN:K值测量是否使用温度补偿功能指示码<br>
	 * unit: <br>
	 * BUFR FXY:V02450<br>
	 * descriptionCN: 
	 */
	Double TemperatureCompensation_K_IndicatorCode ;
	/**
	 * NO: 5.2 <br>
	 * nameCN:因故延迟样品测量指示码<br>
	 * unit: <br>
	 * BUFR FXY:V02451<br>
	 * descriptionCN: 
	 */
	Double DelaySampleIndicatorCode;
	/**
	 * NO: 5.3 <br>
	 * nameCN:降水样品异常状况<br>
	 * unit: <br>
	 * BUFR FXY:V13372<br>
	 * descriptionCN: 
	 */
	Double PrecipitationSampleException;
	
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
	public Double getObservationMethod() {
		return ObservationMethod;
	}
	public void setObservationMethod(Double observationMethod) {
		ObservationMethod = observationMethod;
	}
	public Date getObservationTime() {
		return observationTime;
	}
	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}
	public Double getPrecipitation_StartTime() {
		return Precipitation_StartTime;
	}
	public void setPrecipitation_StartTime(Double precipitation_StartTime) {
		Precipitation_StartTime = precipitation_StartTime;
	}
	public Double getPrecipitation_EndTime() {
		return Precipitation_EndTime;
	}
	public void setPrecipitation_EndTime(Double precipitation_EndTime) {
		Precipitation_EndTime = precipitation_EndTime;
	}
	public Double getAcidRainObservedPrecipitation() {
		return AcidRainObservedPrecipitation;
	}
	public void setAcidRainObservedPrecipitation(Double acidRainObservedPrecipitation) {
		AcidRainObservedPrecipitation = acidRainObservedPrecipitation;
	}
	public Double getInitialSurveySampleTemperature() {
		return InitialSurveySampleTemperature;
	}
	public void setInitialSurveySampleTemperature(Double initialSurveySampleTemperature) {
		InitialSurveySampleTemperature = initialSurveySampleTemperature;
	}
	public Double getInitialSurvey_PH_1() {
		return InitialSurvey_PH_1;
	}
	public void setInitialSurvey_PH_1(Double initialSurvey_PH_1) {
		InitialSurvey_PH_1 = initialSurvey_PH_1;
	}
	public Double getInitialSurvey_PH_2() {
		return InitialSurvey_PH_2;
	}
	public void setInitialSurvey_PH_2(Double initialSurvey_PH_2) {
		InitialSurvey_PH_2 = initialSurvey_PH_2;
	}
	public Double getInitialSurvey_PH_3() {
		return InitialSurvey_PH_3;
	}
	public void setInitialSurvey_PH_3(Double initialSurvey_PH_3) {
		InitialSurvey_PH_3 = initialSurvey_PH_3;
	}
	public Double getInitialSurvey_Average_PH() {
		return InitialSurvey_Average_PH;
	}
	public void setInitialSurvey_Average_PH(Double initialSurvey_Average_PH) {
		InitialSurvey_Average_PH = initialSurvey_Average_PH;
	}
	public Double getInitialSurvey_K_1() {
		return InitialSurvey_K_1;
	}
	public void setInitialSurvey_K_1(Double initialSurvey_K_1) {
		InitialSurvey_K_1 = initialSurvey_K_1;
	}
	public Double getInitialSurvey_K_2() {
		return InitialSurvey_K_2;
	}
	public void setInitialSurvey_K_2(Double initialSurvey_K_2) {
		InitialSurvey_K_2 = initialSurvey_K_2;
	}
	public Double getInitialSurvey_K_3() {
		return InitialSurvey_K_3;
	}
	public void setInitialSurvey_K_3(Double initialSurvey_K_3) {
		InitialSurvey_K_3 = initialSurvey_K_3;
	}
	public Double getInitialSurvey25_Average_K() {
		return InitialSurvey25_Average_K;
	}
	public void setInitialSurvey25_Average_K(Double initialSurvey25_Average_K) {
		InitialSurvey25_Average_K = initialSurvey25_Average_K;
	}
	public Double getRetestSurveySampleTemperature() {
		return RetestSurveySampleTemperature;
	}
	public void setRetestSurveySampleTemperature(Double retestSurveySampleTemperature) {
		RetestSurveySampleTemperature = retestSurveySampleTemperature;
	}
	public Double getRetestSurvey_PH_1() {
		return RetestSurvey_PH_1;
	}
	public void setRetestSurvey_PH_1(Double retestSurvey_PH_1) {
		RetestSurvey_PH_1 = retestSurvey_PH_1;
	}
	public Double getRetestSurvey_PH_2() {
		return RetestSurvey_PH_2;
	}
	public void setRetestSurvey_PH_2(Double retestSurvey_PH_2) {
		RetestSurvey_PH_2 = retestSurvey_PH_2;
	}
	public Double getRetestSurvey_PH_3() {
		return RetestSurvey_PH_3;
	}
	public void setRetestSurvey_PH_3(Double retestSurvey_PH_3) {
		RetestSurvey_PH_3 = retestSurvey_PH_3;
	}
	public Double getRetestSurvey_Average_PH() {
		return RetestSurvey_Average_PH;
	}
	public void setRetestSurvey_Average_PH(Double retestSurvey_Average_PH) {
		RetestSurvey_Average_PH = retestSurvey_Average_PH;
	}
	public Double getRetestSurvey_K_1() {
		return RetestSurvey_K_1;
	}
	public void setRetestSurvey_K_1(Double retestSurvey_K_1) {
		RetestSurvey_K_1 = retestSurvey_K_1;
	}
	public Double getRetestSurvey_K_2() {
		return RetestSurvey_K_2;
	}
	public void setRetestSurvey_K_2(Double retestSurvey_K_2) {
		RetestSurvey_K_2 = retestSurvey_K_2;
	}
	public Double getRetestSurvey_K_3() {
		return RetestSurvey_K_3;
	}
	public void setRetestSurvey_K_3(Double retestSurvey_K_3) {
		RetestSurvey_K_3 = retestSurvey_K_3;
	}
	public Double getRetestSurvey25_Average_K() {
		return RetestSurvey25_Average_K;
	}
	public void setRetestSurvey25_Average_K(Double retestSurvey25_Average_K) {
		RetestSurvey25_Average_K = retestSurvey25_Average_K;
	}
	public Double getWindDirection_14() {
		return WindDirection_14;
	}
	public void setWindDirection_14(Double windDirection_14) {
		WindDirection_14 = windDirection_14;
	}
	public Double getWindSpeed_14() {
		return WindSpeed_14;
	}
	public void setWindSpeed_14(Double windSpeed_14) {
		WindSpeed_14 = windSpeed_14;
	}
	public Double getWindDirection_20() {
		return WindDirection_20;
	}
	public void setWindDirection_20(Double windDirection_20) {
		WindDirection_20 = windDirection_20;
	}
	public Double getWindSpeed_20() {
		return WindSpeed_20;
	}
	public void setWindSpeed_20(Double windSpeed_20) {
		WindSpeed_20 = windSpeed_20;
	}
	public Double getWindDirection_02() {
		return WindDirection_02;
	}
	public void setWindDirection_02(Double windDirection_02) {
		WindDirection_02 = windDirection_02;
	}
	public Double getWindSpeed_02() {
		return WindSpeed_02;
	}
	public void setWindSpeed_02(Double windSpeed_02) {
		WindSpeed_02 = windSpeed_02;
	}
	public Double getWindDirection_08() {
		return WindDirection_08;
	}
	public void setWindDirection_08(Double windDirection_08) {
		WindDirection_08 = windDirection_08;
	}
	public Double getWindSpeed_08() {
		return WindSpeed_08;
	}
	public void setWindSpeed_08(Double windSpeed_08) {
		WindSpeed_08 = windSpeed_08;
	}
	public Double getPrecipitationPeriodWeatherPhenomenon_1() {
		return PrecipitationPeriodWeatherPhenomenon_1;
	}
	public void setPrecipitationPeriodWeatherPhenomenon_1(Double precipitationPeriodWeatherPhenomenon_1) {
		PrecipitationPeriodWeatherPhenomenon_1 = precipitationPeriodWeatherPhenomenon_1;
	}
	public Double getPrecipitationPeriodWeatherPhenomenon_2() {
		return PrecipitationPeriodWeatherPhenomenon_2;
	}
	public void setPrecipitationPeriodWeatherPhenomenon_2(Double precipitationPeriodWeatherPhenomenon_2) {
		PrecipitationPeriodWeatherPhenomenon_2 = precipitationPeriodWeatherPhenomenon_2;
	}
	public Double getPrecipitationPeriodWeatherPhenomenon_3() {
		return PrecipitationPeriodWeatherPhenomenon_3;
	}
	public void setPrecipitationPeriodWeatherPhenomenon_3(Double precipitationPeriodWeatherPhenomenon_3) {
		PrecipitationPeriodWeatherPhenomenon_3 = precipitationPeriodWeatherPhenomenon_3;
	}
	public Double getPrecipitationPeriodWeatherPhenomenon_4() {
		return PrecipitationPeriodWeatherPhenomenon_4;
	}
	public void setPrecipitationPeriodWeatherPhenomenon_4(Double precipitationPeriodWeatherPhenomenon_4) {
		PrecipitationPeriodWeatherPhenomenon_4 = precipitationPeriodWeatherPhenomenon_4;
	}
	public Double getRepeatSurveyIndicatorCode() {
		return RepeatSurveyIndicatorCode;
	}
	public void setRepeatSurveyIndicatorCode(Double repeatSurveyIndicatorCode) {
		RepeatSurveyIndicatorCode = repeatSurveyIndicatorCode;
	}
	public Double getTemperatureCompensation_K_IndicatorCode() {
		return TemperatureCompensation_K_IndicatorCode;
	}
	public void setTemperatureCompensation_K_IndicatorCode(Double temperatureCompensation_K_IndicatorCode) {
		TemperatureCompensation_K_IndicatorCode = temperatureCompensation_K_IndicatorCode;
	}
	public Double getDelaySampleIndicatorCode() {
		return DelaySampleIndicatorCode;
	}
	public void setDelaySampleIndicatorCode(Double delaySampleIndicatorCode) {
		DelaySampleIndicatorCode = delaySampleIndicatorCode;
	}
	public Double getPrecipitationSampleException() {
		return PrecipitationSampleException;
	}
	public void setPrecipitationSampleException(Double precipitationSampleException) {
		PrecipitationSampleException = precipitationSampleException;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
