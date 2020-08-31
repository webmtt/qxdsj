package cma.cimiss2.dpc.decoder.bean.upar;

import java.util.Date;
import java.util.List;

public class UparLBand {
	 /**
	  * VERSION
	  * 
	  */
	private String version;
	/**
	* 文件版本号
    * 操作软件版本号，其中2位整数，2位小数
    */
	private String fileVersion;	
	/**
	 * 区站号
	 * 5位数字或第1位为字母，第2-5位为数字
	 */
	private String stationNumberChina;
	
	/**
	 * 经度
	 * 测站的经度，以度为单位，其中第1位为符号位，东经取正，西经取负，3位整数，4位小数
	 */
	private Double longitude;
	
	/**
	 * 纬度
	 * 测站的纬度，以度为单位，其中第1位为符号位，北纬取正，南纬取负，2位整数，4位小数
	 */
	private Double latitude;
	
	/**
	 * 观测场拔海高度
	 * 观测场拔海高度，以米为单位，其中第1位为符号位，4位整数，1位小数
	 */
	private Double heightOfSationGroundAboveMeanSeaLevel; 
	/**
	 * 探测系统型号
	 * 探测系统型号代码，代码不能出现空格
	 */
	private String typeOfDetectionSystem;
	
	/**
	 * 探测系统天线高度
	 * 探测系统天线距水银槽的高度，以米为单位，2位整数，1位小数
	 */
	private Double antennaHeight;
	
	/**
	 * 探空仪型号
	 * 探空仪型号代码，代码说明表参见附件1，代码不能出现空格
	 */
	private String radiosondeModel;
	
	/**
	 * 仪器编号
	 * 探空仪的编号
	 */
	private String InstrumentNumber;
	
	/**
	 * 施放计数
	 * 本月内探测仪施放累计数
	 */
	private Integer castCount;
	
	/**
	 * 球重量
	 * 单位克
	 * 携带探空仪的施放球重量，单位为克
	 */
	private Double ballWeight;
	/**
	 * 附加物重量
	 * 单位克
	 * 
	 */
	private Double additiveWeight;
	
	/**
	 * 总举力
	 * 单位克
	 * 
	 */
	private Double totalForce;
	
	/**
	 * 净举力
	 * 单位克
	 * 文件第3行空格分割取数组第8个，需要string类型转换Integer类型
	 */
	private Double netLifting;
	
	/**
	 * 平均升速
	 * 单位(米/分钟)
	 * 
	 */
	private Double averageSpeedOfLift;

	/**
	 * 温度基测值
	 * 单位度
	 * 温度基测值，单位为度，其中1位符号位，2位整数，1位小数
	 */
	private Double baseTemperatureValue;
	
	/**
	 * 温度仪器值
	 * 单位度
	 * 温度仪器值，单位为度，其中1位符号位，2位整数，1位小数
	 */
	private Double instrumentTemperatureValue;
	
	/**
	 * 温度偏差
	 * 单位度
	 * 计算方法：温度基测值-温度仪器值
	 * 温度偏差（计算方法：温度基测值-温度仪器值），单位为度，其中1位符号位，1位整数，1位小数
	 */
	private Double temperatureDeviation;
	
	/**
	 * 气压基测值
	 * 单位百帕
	 * 气压基测值，单位为百帕，其中4位整数，1位小数
	 */
	private Double basePressureValue;
	
	/**
	 * 气压仪器值
	 * 单位百帕
	 * 气压仪器值，单位为百帕，其中4位整数，1位小数
	 */
	private Double instrumentPressureValue;
	
	/**
	 * 气压偏差
	 * 单位百帕
	 * 计算方法：气压基测值-气压仪器值
	 * 气压偏差（计算方法：气压基测值-气压仪器值），单位为百帕，其中1位符号位，1位整数，1位小数
	 */
	private Double pressureDeviation;
	
	/**
	 * 相对湿度基测值
	 * 相对湿度基测值，3位整数
	 */
	private Integer baseRelativeHumidityValue;
	
	/**
	 * 相对湿度仪器值
	 * 相对湿度仪器值，3位整数
	 */
	private Integer instrumentRelativeHumidityValue;
	
	/**
	 * 相对湿度偏差
	 * 计算方法：湿度基测值-湿度仪器值
	 * 相对湿度偏差（计算方法：湿度基测值-湿度仪器值），其中1位符号位，1位整数
	 */
	private Integer relativeHumidityDeviation;
	
	/**
	 * 仪器检测结论
	 * 1：合格 0：不合格
	 * 仪器检测结论用1或0表示，其中1表示合格，0表示不合格
	 */
	private Integer instrumentTestConclusion;
	/**
	 * 施放时间（世界时）
	 * 时间采用世界时，其中4位年，2位月，2位日，2位时，2位分，2位秒
	 */
	private Date castingUtcTime;
	
	/**
	 * 施放时间（地方时）
	 * 时间采用地方时，其中4位年，2位月，2位日，2位时，2位分，2位秒
	 */
	private Date castingLocalTime;
	
	/**
	 * 探空终止时间（世界时）
	 * 时间采用世界时，其中4位年，2位月，2位日，2位时，2位分，2位秒
	 */
	private Date soundingTerminationUtcTime;
	
	/**
	 * 测风终止时间（世界时）
	 * 时间采用世界时，其中4位年，2位月，2位日，2位时，2位分，2位秒
	 */
	private Date anemometryTerminationUtcTime;
	
	/**
	 * 探空终止原因
	 *探空终止原因的编码，编码参见附件1
	 */
	private Integer reasonForSoundingTermination;
	
	/**
	 * 测风终止原因 
	 *探空终止原因的编码，编码参见附件1
	 */
	private Integer reasonForAnemometryTermination;
	
	/**
	 * 探空终止高度 
	 * 探空观测终止高度，单位为米
	 */
	private Double soundingTerminationHeight;
	
	/**
	 * 测风终止高度 
	 * 测风观测终止高度，单位为米
	 */
	private Double AnemometryTerminationHeight;
	
	/**
	 * 太阳高度角 
	 * 单位度
	 * 施放瞬间太阳高度角，单位为度，其中1位符号位，3位整数，2位小数
	 */
	private Double solarElevationAngle;
	
	/**
	 * 施放瞬间本站地面温度
	 * 单位度
	 * 施放瞬间本站地面温度值，单位为度，其中1位符号位，2位整数，1位小数
	 */
	private Double instantGroundTemperature;
	
	/**
	 * 施放瞬间本站地面气压
	 * 单位百帕
	 * 施放瞬间本站地面气压值，单位为百帕，其中4位整数，1位小数
	 */
	private Double instantGroundPressure;
  	
	/**
	 * 施放瞬间本站地面相对湿度
	 * 单位百帕
	 * 施放瞬间本站地面相对湿度值，用3位整数表示
	 */
	private Integer instantGroundRelativeHumidity; 
	
	/**
	 * 施放瞬间本站地面风向
	 * 单位度 0-360，静风时风向0；当风向0时，用360表示
	 * 施放瞬间本站地面风向，单位为度，取值范围0-360，用3位整数表示  静风时，风向用0表示   当风向为0度时，用360表示

	 */
	private Integer instantGroundWindDirection;
	
	/**
	 * 施放瞬间本站地面风速
	 * 单位（米/秒）
	 * 施放瞬间本站地面风速，单位为米/秒，其中3位整数，1位小数
	 */
	private Double instantGroundWindSpeed;
	
	/**
	 * 施放瞬间能见度
	 * 单位公里
	 * 施放瞬间能见度，单位为公里，其中2位整数，1位小数
	 */
	private Double instantVisibility;
	
	/**
	 * 施放瞬间本站云属1
	 * 施放瞬间本站云属1的编码，编码参见附件
	 */
	private Integer instantCloudGenus1;
	
	/**
	 * 施放瞬间本站云属2
	 * 施放瞬间本站云属2的编码，编码参见附件1
	 */
	private Integer instantCloudGenus2;
	
	/**
	 * 施放瞬间本站云属3
	 * 施放瞬间本站云属3的编码，编码参见附件1
	 */
	private Integer instantCloudGenus3;
	
	/**
	 * 施放瞬间本站低云量
	 * 单位成 0-10
	 * 单位为成，取值0-10
	 */
	private Integer instantLowCloudCover;
		
	/**
	 * 施放瞬间本站总云量
	 * 单位成 0-10
	 * 单位为成，取值0-10
	 */
	private Integer instantTotalCloudCover;
	
	/**
	 * 施放瞬间天气现象1
	 * 施放瞬间天气现象1的编码，编码参见附件1
	 */
	private Integer instantWeatherPhenomenon1;
	
	/**
	 * 施放瞬间天气现象2
	 *施放瞬间天气现象2的编码，编码参见附件1
	 */
	private Integer instantWeatherPhenomenon2;
	
	/**
	 * 施放瞬间天气现象3
	 * 施放瞬间天气现象3的编码，编码参见附件1
	 */
	private Integer instantWeatherPhenomenon3;
	
	/**
	 * 施放点方位角
	 * 单位度
	 * 施放点方位角，单位为度，取值范围0-360，其中3位整数，2位小数
	 */
	private Double castPointAzimuth;
	
	/**
	 * 施放点仰角
	 * 单位度
	 * 施放点仰角, 单位为度，取值范围-6-90，其中1位符号位，2位整数，2位小数
	 */
	private Double castPointElevation;
	
	/**
	 * 施放点距离
	 * 单位米
	 * 探测仪器与探测系统天线之间的直线距离，单位米，用３位整数，2位小数表示
	 */
	private Double castPointDistance;
	/**
	 * 更正标志
	 */
	private String correctSign= "000";
	
	/**
	 * 秒级数据对应的实体
	 */
	private List<UparSecond> uparsecond;
	/**
	 * 分级数据对应的实体
	 */
	private List<UparMinute> uparminute;
	public String getVersion() {
		return version;
	}
	public String getFileVersion() {
		return fileVersion;
	}
	public String getStationNumberChina() {
		return stationNumberChina;
	}
	public Double getLongitude() {
		return longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public Double getHeightOfSationGroundAboveMeanSeaLevel() {
		return heightOfSationGroundAboveMeanSeaLevel;
	}
	public String getTypeOfDetectionSystem() {
		return typeOfDetectionSystem;
	}
	public Double getAntennaHeight() {
		return antennaHeight;
	}
	public String getRadiosondeModel() {
		return radiosondeModel;
	}
	public String getInstrumentNumber() {
		return InstrumentNumber;
	}
	public Integer getCastCount() {
		return castCount;
	}
	public Double getBallWeight() {
		return ballWeight;
	}
	public Double getAdditiveWeight() {
		return additiveWeight;
	}
	public Double getTotalForce() {
		return totalForce;
	}
	public Double getNetLifting() {
		return netLifting;
	}
	public Double getAverageSpeedOfLift() {
		return averageSpeedOfLift;
	}
	public Double getBaseTemperatureValue() {
		return baseTemperatureValue;
	}
	public Double getInstrumentTemperatureValue() {
		return instrumentTemperatureValue;
	}
	public Double getTemperatureDeviation() {
		return temperatureDeviation;
	}
	public Double getBasePressureValue() {
		return basePressureValue;
	}
	public Double getInstrumentPressureValue() {
		return instrumentPressureValue;
	}
	public Double getPressureDeviation() {
		return pressureDeviation;
	}
	public Integer getBaseRelativeHumidityValue() {
		return baseRelativeHumidityValue;
	}
	public Integer getInstrumentRelativeHumidityValue() {
		return instrumentRelativeHumidityValue;
	}
	public Integer getRelativeHumidityDeviation() {
		return relativeHumidityDeviation;
	}
	public Integer getInstrumentTestConclusion() {
		return instrumentTestConclusion;
	}
	public Date getCastingUtcTime() {
		return castingUtcTime;
	}
	public Date getCastingLocalTime() {
		return castingLocalTime;
	}
	public Date getSoundingTerminationUtcTime() {
		return soundingTerminationUtcTime;
	}
	public Date getAnemometryTerminationUtcTime() {
		return anemometryTerminationUtcTime;
	}
	public Integer getReasonForSoundingTermination() {
		return reasonForSoundingTermination;
	}
	public Integer getReasonForAnemometryTermination() {
		return reasonForAnemometryTermination;
	}
	public Double getSoundingTerminationHeight() {
		return soundingTerminationHeight;
	}
	public Double getAnemometryTerminationHeight() {
		return AnemometryTerminationHeight;
	}
	public Double getSolarElevationAngle() {
		return solarElevationAngle;
	}
	public Double getInstantGroundTemperature() {
		return instantGroundTemperature;
	}
	public Double getInstantGroundPressure() {
		return instantGroundPressure;
	}
	public Integer getInstantGroundRelativeHumidity() {
		return instantGroundRelativeHumidity;
	}
	public Integer getInstantGroundWindDirection() {
		return instantGroundWindDirection;
	}
	public Double getInstantGroundWindSpeed() {
		return instantGroundWindSpeed;
	}
	public Double getInstantVisibility() {
		return instantVisibility;
	}
	public Integer getInstantCloudGenus1() {
		return instantCloudGenus1;
	}
	public Integer getInstantCloudGenus2() {
		return instantCloudGenus2;
	}
	public Integer getInstantCloudGenus3() {
		return instantCloudGenus3;
	}
	public Integer getInstantLowCloudCover() {
		return instantLowCloudCover;
	}
	public Integer getInstantTotalCloudCover() {
		return instantTotalCloudCover;
	}
	public Integer getInstantWeatherPhenomenon1() {
		return instantWeatherPhenomenon1;
	}
	public Integer getInstantWeatherPhenomenon2() {
		return instantWeatherPhenomenon2;
	}
	public Integer getInstantWeatherPhenomenon3() {
		return instantWeatherPhenomenon3;
	}
	public Double getCastPointAzimuth() {
		return castPointAzimuth;
	}
	public Double getCastPointElevation() {
		return castPointElevation;
	}
	public Double getCastPointDistance() {
		return castPointDistance;
	}
	public String getCorrectSign() {
		return correctSign;
	}
	public List<UparSecond> getUparsecond() {
		return uparsecond;
	}
	public List<UparMinute> getUparminute() {
		return uparminute;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public void setFileVersion(String fileVersion) {
		this.fileVersion = fileVersion;
	}
	public void setStationNumberChina(String stationNumberChina) {
		this.stationNumberChina = stationNumberChina;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public void setHeightOfSationGroundAboveMeanSeaLevel(Double heightOfSationGroundAboveMeanSeaLevel) {
		this.heightOfSationGroundAboveMeanSeaLevel = heightOfSationGroundAboveMeanSeaLevel;
	}
	public void setTypeOfDetectionSystem(String typeOfDetectionSystem) {
		this.typeOfDetectionSystem = typeOfDetectionSystem;
	}
	public void setAntennaHeight(Double antennaHeight) {
		this.antennaHeight = antennaHeight;
	}
	public void setRadiosondeModel(String radiosondeModel) {
		this.radiosondeModel = radiosondeModel;
	}
	public void setInstrumentNumber(String instrumentNumber) {
		InstrumentNumber = instrumentNumber;
	}
	public void setCastCount(Integer castCount) {
		this.castCount = castCount;
	}
	public void setBallWeight(Double ballWeight) {
		this.ballWeight = ballWeight;
	}
	public void setAdditiveWeight(Double additiveWeight) {
		this.additiveWeight = additiveWeight;
	}
	public void setTotalForce(Double totalForce) {
		this.totalForce = totalForce;
	}
	public void setNetLifting(Double netLifting) {
		this.netLifting = netLifting;
	}
	public void setAverageSpeedOfLift(Double averageSpeedOfLift) {
		this.averageSpeedOfLift = averageSpeedOfLift;
	}
	public void setBaseTemperatureValue(Double baseTemperatureValue) {
		this.baseTemperatureValue = baseTemperatureValue;
	}
	public void setInstrumentTemperatureValue(Double instrumentTemperatureValue) {
		this.instrumentTemperatureValue = instrumentTemperatureValue;
	}
	public void setTemperatureDeviation(Double temperatureDeviation) {
		this.temperatureDeviation = temperatureDeviation;
	}
	public void setBasePressureValue(Double basePressureValue) {
		this.basePressureValue = basePressureValue;
	}
	public void setInstrumentPressureValue(Double instrumentPressureValue) {
		this.instrumentPressureValue = instrumentPressureValue;
	}
	public void setPressureDeviation(Double pressureDeviation) {
		this.pressureDeviation = pressureDeviation;
	}
	public void setBaseRelativeHumidityValue(Integer baseRelativeHumidityValue) {
		this.baseRelativeHumidityValue = baseRelativeHumidityValue;
	}
	public void setInstrumentRelativeHumidityValue(Integer instrumentRelativeHumidityValue) {
		this.instrumentRelativeHumidityValue = instrumentRelativeHumidityValue;
	}
	public void setRelativeHumidityDeviation(Integer relativeHumidityDeviation) {
		this.relativeHumidityDeviation = relativeHumidityDeviation;
	}
	public void setInstrumentTestConclusion(Integer instrumentTestConclusion) {
		this.instrumentTestConclusion = instrumentTestConclusion;
	}
	public void setCastingUtcTime(Date castingUtcTime) {
		this.castingUtcTime = castingUtcTime;
	}
	public void setCastingLocalTime(Date castingLocalTime) {
		this.castingLocalTime = castingLocalTime;
	}
	public void setSoundingTerminationUtcTime(Date soundingTerminationUtcTime) {
		this.soundingTerminationUtcTime = soundingTerminationUtcTime;
	}
	public void setAnemometryTerminationUtcTime(Date anemometryTerminationUtcTime) {
		this.anemometryTerminationUtcTime = anemometryTerminationUtcTime;
	}
	public void setReasonForSoundingTermination(Integer reasonForSoundingTermination) {
		this.reasonForSoundingTermination = reasonForSoundingTermination;
	}
	public void setReasonForAnemometryTermination(Integer reasonForAnemometryTermination) {
		this.reasonForAnemometryTermination = reasonForAnemometryTermination;
	}
	public void setSoundingTerminationHeight(Double soundingTerminationHeight) {
		this.soundingTerminationHeight = soundingTerminationHeight;
	}
	public void setAnemometryTerminationHeight(Double anemometryTerminationHeight) {
		AnemometryTerminationHeight = anemometryTerminationHeight;
	}
	public void setSolarElevationAngle(Double solarElevationAngle) {
		this.solarElevationAngle = solarElevationAngle;
	}
	public void setInstantGroundTemperature(Double instantGroundTemperature) {
		this.instantGroundTemperature = instantGroundTemperature;
	}
	public void setInstantGroundPressure(Double instantGroundPressure) {
		this.instantGroundPressure = instantGroundPressure;
	}
	public void setInstantGroundRelativeHumidity(Integer instantGroundRelativeHumidity) {
		this.instantGroundRelativeHumidity = instantGroundRelativeHumidity;
	}
	public void setInstantGroundWindDirection(Integer instantGroundWindDirection) {
		this.instantGroundWindDirection = instantGroundWindDirection;
	}
	public void setInstantGroundWindSpeed(Double instantGroundWindSpeed) {
		this.instantGroundWindSpeed = instantGroundWindSpeed;
	}
	public void setInstantVisibility(Double instantVisibility) {
		this.instantVisibility = instantVisibility;
	}
	public void setInstantCloudGenus1(Integer instantCloudGenus1) {
		this.instantCloudGenus1 = instantCloudGenus1;
	}
	public void setInstantCloudGenus2(Integer instantCloudGenus2) {
		this.instantCloudGenus2 = instantCloudGenus2;
	}
	public void setInstantCloudGenus3(Integer instantCloudGenus3) {
		this.instantCloudGenus3 = instantCloudGenus3;
	}
	public void setInstantLowCloudCover(Integer instantLowCloudCover) {
		this.instantLowCloudCover = instantLowCloudCover;
	}
	public void setInstantTotalCloudCover(Integer instantTotalCloudCover) {
		this.instantTotalCloudCover = instantTotalCloudCover;
	}
	public void setInstantWeatherPhenomenon1(Integer instantWeatherPhenomenon1) {
		this.instantWeatherPhenomenon1 = instantWeatherPhenomenon1;
	}
	public void setInstantWeatherPhenomenon2(Integer instantWeatherPhenomenon2) {
		this.instantWeatherPhenomenon2 = instantWeatherPhenomenon2;
	}
	public void setInstantWeatherPhenomenon3(Integer instantWeatherPhenomenon3) {
		this.instantWeatherPhenomenon3 = instantWeatherPhenomenon3;
	}
	public void setCastPointAzimuth(Double castPointAzimuth) {
		this.castPointAzimuth = castPointAzimuth;
	}
	public void setCastPointElevation(Double castPointElevation) {
		this.castPointElevation = castPointElevation;
	}
	public void setCastPointDistance(Double castPointDistance) {
		this.castPointDistance = castPointDistance;
	}
	public void setCorrectSign(String correctSign) {
		this.correctSign = correctSign;
	}
	public void setUparsecond(List<UparSecond> uparsecond) {
		this.uparsecond = uparsecond;
	}
	public void setUparminute(List<UparMinute> uparminute) {
		this.uparminute = uparminute;
	}
	
	
}
