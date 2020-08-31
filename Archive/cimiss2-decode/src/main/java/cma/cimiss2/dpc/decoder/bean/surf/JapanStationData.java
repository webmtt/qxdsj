package cma.cimiss2.dpc.decoder.bean.surf;

/**
 * ************************************
 * @ClassName:JaPanStationData
 * @Auther: liuyingshuang
 * @Time:2019年4月6日
 * @Description: 日本站点资料实体类
 * @Copyright: All rights reserver.
 * ************************************
 */
public class JapanStationData {
	
	/*
	 * 省
	 */
	private String cityName;
	/*
	 * 市
	 */
	private String countyName;
	/*
	 * 县
	 */
	private String villageName;
	/**
	 * 台站号
	 */
	private String station;
	/*
	 * 纬度
	 * 
	 */
	private double latitude = 999999.0;
	/*
	 *  经度
	 */
	private double longitude = 999999.0;
	/*
	 * 海拔
	 */
	private double altitude = 999999.0;
	
	private String DataTime;
	public String getDataTime() {
		return DataTime;
	}
	public void setDataTime(String dataTime) {
		DataTime = dataTime;
	}
	/*
	 * 温度
	 */
	private double temperature = 999998.0;
	/*
	 *  降水
	 */
	private double precipitation = 999998.0;
	/*
	 * 风向
	 */
	private String windDirection = "999998";
	/*
	 * 风速
	 */
	private double windSpeed = 999998.0;
	/*
	 * 日照时间
	 */
	private double sunshineDuration = 999998.0;
	/*
	 * 雪深
	 */
	private double SnowDepth = 999998.0;
	/*
	 * 相对湿度
	 */
	private double humidity = 999998.0;
	/*
	 * 气压
	 */
	private double pressure = 999998.0;
	/*
	 * 24小时最低温
	 */
	private double minTEP24 = 999998.0;
	/*
	 * 24小时最低温时间
	 */
	private String minTEP24Time = "999998";
	/*
	 * 24小时最高温
	 */
	private double maxTEP24 = 999998.0;
	/*
	 * 24小时最高温时间
	 */
	private String maxTEP24Time = "999998";
	/*
	 * 24小时最大风速
	 */
	private double maxWindSpeed24 = 999998.0;
	/*
	 * 24小时最大风向
	 */
	private String maxWindDirection24 = "999998";
	/*
	 * 最大风时间
	 */
	private String MaxWind24Time = "999998";
	
	/*
	 * 获取站点所属城市
	 */
	public String getCityName() {
		return cityName;
	}
	/*
	 * 设置站点所属省份
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	/*
	 * 获取站点所属城市
	 */
	public String getCountyName() {
		return countyName;
	}
	/*
	 * 设置站点所属城市
	 */
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	/*
	 * 获取站点所属乡镇
	 */
	public String getVillageName() {
		return villageName;
	}
	/*
	 * 设置站点所属乡镇
	 */
	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}
	/*
	 * 获取经度，保留四位小数，不足补0
	 */
	public String getLatitude() {
		
		return  String.format("%.4f", latitude);
	}
	/*
	 * 设置站点经度
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	/*
	 * 获取纬度，保留四位小数，不足补0
	 */
	public String getLongitude() {
		return String.format("%.4f", longitude);
	}
	/*
	 * 设置纬度
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	/*
	 * 获取海拔高度，保留两位小数
	 */
	public String getAltitude() {
		return String.format("%.2f", altitude);
	}
	/*
	 * 设置海拔高度
	 */
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
	/*
	 * 获取温度数据
	 */
	public String getTemperature() {
		return String.format("%.2f", temperature);
	}
	/*
	 * 设置温度数据
	 */
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	/*
	 * 获取降水数据
	 */
	public String getPrecipitation() {
		return String.format("%.2f", precipitation);
	}
	/*
	 * 设置降水数据
	 */
	public void setPrecipitation(double precipitation) {
		this.precipitation = precipitation;
	}
	/*
	 * 获取风向
	 */
	public String getWindDirection() {
		return windDirection;
	}
	/*
	 * 设置风向
	 */
	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}
	/*
	 * 获取风速
	 */
	public String getWindSpeed() {
		return String.format("%.2f", windSpeed);
	}
	/*
	 * 设置风速
	 */
	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}
	/*
	 * 获取日照时间
	 */
	public String getSunshineDuration() {
		return String.format("%.2f", sunshineDuration);
	}
	/*
	 * 设置日照时间
	 */
	public void setSunshineDuration(double sunshineDuration) {
		this.sunshineDuration = sunshineDuration;
	}
	/*
	 * 获取雪深
	 */
	public String getSnowDepth() {
		return String.format("%.2f", SnowDepth);
	}
	/*
	 * 设置雪深
	 */
	public void setSnowDepth(double snowDepth) {
		SnowDepth = snowDepth;
	}
	/*
	 * 获取相对湿度
	 */
	public String getHumidity() {
		return String.format("%.2f", humidity);
	}
	/*
	 * 设置相对湿度
	 */
	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}
	/*
	 * 获取大气压
	 */
	public String getPressure() {
		return String.format("%.2f", pressure);
	}
	/*
	 * 设置大气压
	 */
	public void setPressure(double pressure) {
		this.pressure = pressure;
	}
	/*
	 * 获取24小时最低温
	 */
	public String getMinTEP24() {
		return String.format("%.2f", minTEP24);
	}
	/*
	 * 获取24小时最低温
	 */
	public void setMinTEP24(double minTEP24) {
		this.minTEP24 = minTEP24;
	}
	/*
	 * 获取24小时最低温时间
	 */
	public String getMinTEP24Time() {
		return minTEP24Time;
	}
	/*
	 * 设置24小时最低温时间
	 */
	public void setMinTEP24Time(String minTEP24Time) {
		this.minTEP24Time = minTEP24Time;
	}
	/*
	 * 获取24小时最高温
	 */
	public String getMaxTEP24() {
		return String.format("%.2f", maxTEP24);
	}
	/*
	 * 设置24小时最高温
	 */
	public void setMaxTEP24(double maxTEP24) {
		this.maxTEP24 = maxTEP24;
	}
	/*
	 * 获取24小时最高温时间
	 */
	public String getMaxTEP24Time() {
		return maxTEP24Time;
	}
	/*
	 * 设置24小时最高温时间
	 */
	public void setMaxTEP24Time(String maxTEP24Time) {
		this.maxTEP24Time = maxTEP24Time;
	}
	/*
	 * 获取24小时最大风速
	 */
	public String getMaxWindSpeed24() {
		return String.format("%.2f", maxWindSpeed24);
	}
	/*
	 * 设置24小时最大风速
	 */
	public void setMaxWindSpeed24(double maxWindSpeed24) {
		this.maxWindSpeed24 = maxWindSpeed24;
	}
	/*
	 * 获取24小时最大风向
	 */
	public String getMaxWindDirection24() {
		return maxWindDirection24;
	}
	/*
	 * 设置24小时最大风向
	 */
	public void setMaxWindDirection24(String maxWindDirection24) {
		this.maxWindDirection24 = maxWindDirection24;
	}
	/*
	 * 获取24小时最大风向时间
	 */
	public String getMaxWind24Time() {
		return MaxWind24Time;
	}
	/*
	 * 设置24小时最大风向时间
	 */
	public void setMaxWind24Time(String maxWind24Time) {
		MaxWind24Time = maxWind24Time;
	}
	@Override
	public String toString() {
		return "JapanStationData [cityName=" + cityName + ", countyName=" + countyName + ", villageName=" + villageName
				+ ", latitude=" + latitude + ", longitude=" + longitude + ", altitude=" + altitude + ", DataTime="
				+ DataTime + ", temperature=" + temperature + ", precipitation=" + precipitation + ", windDirection="
				+ windDirection + ", windSpeed=" + windSpeed + ", sunshineDuration=" + sunshineDuration + ", SnowDepth="
				+ SnowDepth + ", humidity=" + humidity + ", pressure=" + pressure + ", minTEP24=" + minTEP24
				+ ", minTEP24Time=" + minTEP24Time + ", maxTEP24=" + maxTEP24 + ", maxTEP24Time=" + maxTEP24Time
				+ ", maxWindSpeed24=" + maxWindSpeed24 + ", maxWindDirection24=" + maxWindDirection24
				+ ", MaxWind24Time=" + MaxWind24Time + "]";
	}
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	
	
	
}
