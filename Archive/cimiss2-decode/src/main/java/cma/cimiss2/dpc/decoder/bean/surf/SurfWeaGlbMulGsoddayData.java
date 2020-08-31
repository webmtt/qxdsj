package cma.cimiss2.dpc.decoder.bean.surf;

/**
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	NCDC-GSOD全球地面日值资料实体类
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《NCDC-GSOD全球地面日值资料表》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月27日 上午11:08:44   dengyongliang    Initial creation.
 * </pre>
 * 
 * @author dengyongliang
 * @version 0.0.1
 */
public class SurfWeaGlbMulGsoddayData {
	  /**
     * NO: 0.1  <br>
     * nameCN: 原始STN站号 <br>
     * unit: <br>
     * BUFR FXY: V_STN <br>
     * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
     */
	String  stationNumberLocation;
	  /**
     * NO: 0.2  <br>
     * nameCN: 原始WBAN站号 <br>
     * unit:  <br>
     * BUFR FXY: V_WBAN <br>
     * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
     */
	String weatherBureauAirForceNavy;
	  /**
     * NO: 0.3  <br>
     * nameCN: 年/月/日 <br>
     * unit: <br>
     * BUFR FXY:V04001\V04002\V04003 <br>
     * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
     */
	String yearMoDa;
	
	public String getYearMoDa() {
		return yearMoDa;
	}
	public void setYearMoDa(String yearMoDa) {
		this.yearMoDa = yearMoDa;
	}
	/**
     * NO: 1.1  <br>
     * nameCN: 日平均气温<br>
     * unit: 摄氏度  <br>
     * BUFR FXY: V12001_701 <br>
     * descriptionCN: <br>
     * decode rule: 华氏转化为摄氏度，（取值－32）÷1.8；缺省 9999.9，解析为999999 <br>
     * field rule: 直接赋值
     */
	double dailyAvgTemperature = 999999;
	  /**
     * NO: 1.2  <br>
     * nameCN: 日气温记录数<br>
     * unit: <br>
     * BUFR FXY: V12001_040 <br>
     * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
     */
	int dailyAvgTemperatureCount = 999999;
	 /**
     * NO: 1.3  <br>
     * nameCN:  日平均露点温度/DEWP<br>
     * unit: 摄氏度 <br>
     * BUFR FXY:V12003 <br>
     * descriptionCN: <br>
     * decode rule:华氏转化为摄氏度，（取值－32）÷1.8；缺省 9999.9，解析为999999<br>
     * field rule:直接赋值
     */
	double dewPointTemperature = 999999;
	/**
     * NO: 1.4  <br>
     * nameCN:  日露点温度记录数<br>
     * unit: <br>
     * BUFR FXY:V12003_040 <br>
     * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
     */
	int  dewPointTemperatureCount = 999999;
	/**
     * NO: 1.5  <br>
     * nameCN:  日平均海平面气压<br>
     * unit: 百帕 <br>
     * BUFR FXY: V10051_701 <br>
     * descriptionCN: <br>
     * decode rule:直接取值，缺省 9999.9，解析为999999<br>
     * field rule:直接赋值
     */
	double seaLevelPressure = 999999;
	/**
     * NO: 1.6  <br>
     * nameCN:  日海平面气压记录数<br>
     * unit: <br>
     * BUFR FXY: V10051_040 <br>
     * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
     */
	int  seaLevelPressureCount = 999999;
	/**
     * NO: 1.7  <br>
     * nameCN:  日平均本站气压<br>
     * unit: 百帕  <br>
     * BUFR FXY: V10004_701 <br>
     * descriptionCN:  <br>
     * decode rule:直接取值，缺省 9999.9，解析为999999<br>
     * field rule:直接赋值
     */
	double stationPressure = 999999;
	/**
     * NO: 1.8  <br>
     * nameCN:  日本站气压记录数<br>
     * unit: <br>
     * BUFR FXY:V10004_040 <br>
     * descriptionCN:  <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
     */
	int stationPressureCount = 999999;
	/**
     * NO: 1.9  <br>
     * nameCN:  日平均能见度(VISIB)<br>
     * unit: 英里 <br>
     * BUFR FXY:V20001_701 <br>
     * descriptionCN:  <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
     */
	double visibility = 999999;
	/**
     * NO: 1.10  <br>
     * nameCN:  日能见度记录数<br>
     * unit:  <br>
     * BUFR FXY: V20001_040<br>
     * descriptionCN:  <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
     */
	int visibilityCount = 999999;
	/**
     * NO: 1.11  <br>
     * nameCN:  日平均风速(WDSP)<br>
     * unit: m/s <br>
     * BUFR FXY:V11291 <br>
     * descriptionCN:  <br>
     * decode rule: 海里/时转化为米/秒，取值 *1852/3600<br>
     * field rule:直接赋值
     */
	double windSpeed = 999999;
	/**
     * NO: 1.12  <br>
     * nameCN:  日风速记录数<br>
     * unit: <br>
     * BUFR FXY: V11291_040 <br>
     * descriptionCN:  <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
     */
	int  windSpeedCount = 999999;
	/**
     * NO: 1.13  <br>
     * nameCN:  日最大持续风速(MXSPD)<br>
     * unit: <br>
     * BUFR FXY: V11320 <br>
     * descriptionCN: 海里/时转化为米/秒，取值 *1852/3600<br>
     * field rule:直接赋值
     */
	double  maxSustainedWindSpeed  = 999999;
	/**
     * NO: 1.14  <br>
     * nameCN:  日最大阵风风速(GUST)<br>
     * unit: <br>
     * BUFR FXY: V11041 <br>
     * descriptionCN: <br>
     * descriptionCN: 海里/时转化为米/秒，取值 *1852/3600<br>
     * field rule:直接赋值
     */
	double maxWindGust = 999999;
	/**
     * NO: 1.15  <br>
     * nameCN:   日最高气温(MAX)<br>
     * unit: 摄氏度 <br>
     * BUFR FXY: V12011 <br>
     * descriptionCN: <br>
     * decode rule:华氏转化为摄氏度，（取值－32）÷1.8；缺省 9999.9，解析为999999<br>
     * field rule:直接赋值
     */
	Double maxTemperature = (double) 999999;
	/**
     * NO: 1.16 <br>
     * nameCN:  日最高气温取值标志<br>
     * unit: 代码表 <br>
     * BUFR FXY: V12011_MARK <br>
     * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
     */
	String maxFlag = "";
	/**
     * NO: 1.17  <br>
     * nameCN:  日最低气温<br>
     * unit: 摄氏度  <br>
     * BUFR FXY: V12012 <br>
     * descriptionCN: <br>
     * decode rule:华氏转化为摄氏度，（取值－32）÷1.8；缺省 9999.9，解析为999999<br>
     * field rule:直接赋值
     */
	Double minTemperature = (double) 999999;
	/**
     * NO: 1.18  <br>
     * nameCN:  最低气温标志<br>
     * unit: <br>
     * BUFR FXY: V12012_MARK <br>
     * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
     */
	String minFlag = "";
	/**
     * NO: 1.19  <br>
     * nameCN:  日降水量(PRCP)<br>
     * unit: 毫米 <br>
     * BUFR FXY:V13023 <br>
     * descriptionCN: <br>
     * decode rule: 英寸转化为毫米，取值*25.4<br>
     * field rule:直接赋值
     */
	double precipitationDaily = 999999;
	/**
     * NO: 1.20  <br>
     * nameCN:  日降水量(PRCP)标志<br>
     * unit: 代码表<br>
     * BUFR FXY:V13023_MARK <br>
     * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
     */
	String precipitationDailyFlag = "";
	/**
     * NO: 1.21  <br>
     * nameCN:  日(总)雪深(SNDP )<br>
     * unit: <br>
     * BUFR FXY: V13013 <br>
     * descriptionCN: <br>
     * decode rule: 英寸转化为毫米，取值*25.4<br>
     * field rule:直接赋值
     */
	double snowDepth = 999999;
	
	/**
     * NO: 1.22  <br>
     * nameCN: 标示<br>
     * unit: <br>
     * BUFR FXY: 无对应字段 <br>
     * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:
     */
	int FRSHTT = 999999;
	/**
     * NO: 1.23  <br>
     * nameCN:  雾标示<br>
     * unit: <br>
     * BUFR FXY: V20302_042 <br>
     * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
     */
	int fog = 999999;
	/**
     * NO: 1.24  <br>
     * nameCN:  雨标示<br>
     * unit: <br>
     * BUFR FXY:V20302_060 <br>
     * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
     */
	int rain = 999999;
	/**
     * NO: 1.25  <br>
     * nameCN:  雪<br>
     * unit: <br>
     * BUFR FXY: V20302_070 <br>
     * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
     */
	int snow = 999999;
	/**
     * NO: 1.26  <br>
     * nameCN: 冰雹标示<br>
     * unit: <br>
     * BUFR FXY: V20302_089 <br>
     * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
     */
	int hail = 999999;
	/**
     * NO: 1.27  <br>
     * nameCN:  雷标示<br>
     * unit: <br>
     * BUFR FXY:V20302_017 <br>
     * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
     */
	int thunder = 999999;
	/**
     * NO: 1.28  <br>
     * nameCN:  龙卷风标示<br>
     * unit: <br>
     * BUFR FXY: V20302_019 <br>
     * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
     */
	int tornado = 999999;
	
	public String getStationNumberLocation() {
		return stationNumberLocation;
	}
	public void setStationNumberLocation(String stationNumberLocation) {
		this.stationNumberLocation = stationNumberLocation;
	}
	public String getWeatherBureauAirForceNavy() {
		return weatherBureauAirForceNavy;
	}
	public void setWeatherBureauAirForceNavy(String weatherBureauAirForceNavy) {
		this.weatherBureauAirForceNavy = weatherBureauAirForceNavy;
	}
	public double getDailyAvgTemperature() {
		return dailyAvgTemperature;
	}
	public void setDailyAvgTemperature(double dailyAvgTemperature) {
		this.dailyAvgTemperature = dailyAvgTemperature;
	}
	public int getDailyAvgTemperatureCount() {
		return dailyAvgTemperatureCount;
	}
	public void setDailyAvgTemperatureCount(int dailyAvgTemperatureCount) {
		this.dailyAvgTemperatureCount = dailyAvgTemperatureCount;
	}
	public double getDewPointTemperature() {
		return dewPointTemperature;
	}
	public void setDewPointTemperature(double dewPointTemperature) {
		this.dewPointTemperature = dewPointTemperature;
	}
	public int getDewPointTemperatureCount() {
		return dewPointTemperatureCount;
	}
	public void setDewPointTemperatureCount(int dewPointTemperatureCount) {
		this.dewPointTemperatureCount = dewPointTemperatureCount;
	}
	public double getSeaLevelPressure() {
		return seaLevelPressure;
	}
	public void setSeaLevelPressure(double seaLevelPressure) {
		this.seaLevelPressure = seaLevelPressure;
	}
	public int getSeaLevelPressureCount() {
		return seaLevelPressureCount;
	}
	public void setSeaLevelPressureCount(int seaLevelPressureCount) {
		this.seaLevelPressureCount = seaLevelPressureCount;
	}
	public double getStationPressure() {
		return stationPressure;
	}
	public void setStationPressure(double stationPressure) {
		this.stationPressure = stationPressure;
	}
	public int getStationPressureCount() {
		return stationPressureCount;
	}
	public void setStationPressureCount(int stationPressureCount) {
		this.stationPressureCount = stationPressureCount;
	}
	public double getVisibility() {
		return visibility;
	}
	public void setVisibility(double visibility) {
		this.visibility = visibility;
	}
	public int getVisibilityCount() {
		return visibilityCount;
	}
	public void setVisibilityCount(int visibilityCount) {
		this.visibilityCount = visibilityCount;
	}
	public double getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}
	public int getWindSpeedCount() {
		return windSpeedCount;
	}
	public void setWindSpeedCount(int windSpeedCount) {
		this.windSpeedCount = windSpeedCount;
	}
	public double getMaxSustainedWindSpeed() {
		return maxSustainedWindSpeed;
	}
	public void setMaxSustainedWindSpeed(double maxSustainedWindSpeed) {
		this.maxSustainedWindSpeed = maxSustainedWindSpeed;
	}
	public double getMaxWindGust() {
		return maxWindGust;
	}
	public void setMaxWindGust(double maxWindGust) {
		this.maxWindGust = maxWindGust;
	}
	public Double getMaxTemperature() {
		return maxTemperature;
	}
	public void setMaxTemperature(Double maxTemperature) {
		this.maxTemperature = maxTemperature;
	}
	public String getMaxFlag() {
		return maxFlag;
	}
	public void setMaxFlag(String maxFlag) {
		this.maxFlag = maxFlag;
	}
	public Double getMinTemperature() {
		return minTemperature;
	}
	public void setMinTemperature(Double minTemperature) {
		this.minTemperature = minTemperature;
	}
	public String getMinFlag() {
		return minFlag;
	}
	public void setMinFlag(String minFlag) {
		this.minFlag = minFlag;
	}
	public double getPrecipitationDaily() {
		return precipitationDaily;
	}
	public void setPrecipitationDaily(double precipitationDaily) {
		this.precipitationDaily = precipitationDaily;
	}
	public String getPrecipitationDailyFlag() {
		return precipitationDailyFlag;
	}
	public void setPrecipitationDailyFlag(String precipitationDailyFlag) {
		this.precipitationDailyFlag = precipitationDailyFlag;
	}
	public double getSnowDepth() {
		return snowDepth;
	}
	public void setSnowDepth(double snowDepth) {
		this.snowDepth = snowDepth;
	}
	public int getFRSHTT() {
		return FRSHTT;
	}
	public void setFRSHTT(int fRSHTT) {
		FRSHTT = fRSHTT;
	}
	public int getFog() {
		return fog;
	}
	public void setFog(int fog) {
		this.fog = fog;
	}
	public int getRain() {
		return rain;
	}
	public void setRain(int rain) {
		this.rain = rain;
	}
	public int getSnow() {
		return snow;
	}
	public void setSnow(int snow) {
		this.snow = snow;
	}
	public int getHail() {
		return hail;
	}
	public void setHail(int hail) {
		this.hail = hail;
	}
	public int getThunder() {
		return thunder;
	}
	public void setThunder(int thunder) {
		this.thunder = thunder;
	}
	public int getTornado() {
		return tornado;
	}
	public void setTornado(int tornado) {
		this.tornado = tornado;
	}
	@Override
	public String toString() {
		return "SurfWeaGlbMulGsoddayData [stationNumberLocation=" + stationNumberLocation
				+ ", weatherBureauAirForceNavy=" + weatherBureauAirForceNavy + ", yearMoDa=" + yearMoDa
				+ ", dailyAvgTemperature=" + dailyAvgTemperature + ", dailyAvgTemperatureCount="
				+ dailyAvgTemperatureCount + ", dewPointTemperature=" + dewPointTemperature
				+ ", dewPointTemperatureCount=" + dewPointTemperatureCount + ", seaLevelPressure=" + seaLevelPressure
				+ ", seaLevelPressureCount=" + seaLevelPressureCount + ", stationPressure=" + stationPressure
				+ ", stationPressureCount=" + stationPressureCount + ", visibility=" + visibility + ", visibilityCount="
				+ visibilityCount + ", windSpeed=" + windSpeed + ", windSpeedCount=" + windSpeedCount
				+ ", maxSustainedWindSpeed=" + maxSustainedWindSpeed + ", maxWindGust=" + maxWindGust
				+ ", maxTemperature=" + maxTemperature + ", maxFlag=" + maxFlag + ", minTemperature=" + minTemperature
				+ ", minFlag=" + minFlag + ", precipitationDaily=" + precipitationDaily + ", precipitationDailyFlag="
				+ precipitationDailyFlag + ", snowDepth=" + snowDepth + ", FRSHTT=" + FRSHTT + ", fog=" + fog
				+ ", rain=" + rain + ", snow=" + snow + ", hail=" + hail + ", thunder=" + thunder + ", tornado="
				+ tornado + "]";
	}
	
	
	
	
	
}
