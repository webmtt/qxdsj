package cma.cimiss2.dpc.decoder.bean.upar;

public class UparMinute {
	/**
	 * NO: 1  <br>
	 * nameCN: 采样相对时间 <br>
	 * unit: 分钟<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 计算时间相对于施放时间差，单位为分钟，从0开始编发
	 */
	private Double relativeTime;
	/**
	 * NO: 2  <br>
	 * nameCN:温度 <br>
	 * unit: 度<br>
	 * BUFR FXY: <br>温度计算值，单位为度，其中1位符号位，2位整数，1位小数
	 */
	private Double Temperature;
	/**
	 * NO: 3  <br>
	 * nameCN: 气压  <br>
	 * unit: 百帕<br>
	 * BUFR FXY: <br>气压计算值，单位为百帕，其中4位整数，1位小数
	 */
	private Double pressure;
	/**
	 * NO: 4  <br>
	 * nameCN: 相对湿度  <br>
	 * unit: <br>
	 * BUFR FXY: <br>采样时相对湿度值，3位整数
	 */
	private int RelativeHumidity;
	/**
	 * NO: 5  <br>
	 * nameCN: 风向  <br>
	 * unit: 度 <br>
	 * BUFR FXY: <br>风向，单位为度，取值范围0-360，用3位整数表示当风向为0度时，用360表示
	 */
	private int windDir;
	/**
	 * NO:6  <br>
	 * nameCN: 风速  <br>
	 * unit: 米/秒 <br>
	 * BUFR FXY: <br>风速，单位为米/秒
	 */
	private double windSpeed;
	/**
	 * NO: 7 <br>
	 * nameCN: 高度  <br>
	 * unit: 势米 <br>
	 * BUFR FXY: <br>探空位势高度，单位位势米，5位整数，
	 */
	private int height;
	/**
	 * NO: 8  <br>
	 * nameCN: 经度偏差  <br>
	 * unit: 度 <br>
	 * BUFR FXY: <br>经度-测站经度，以度为单位，其中1位符号位，1位整数，3位小数
	 */
	private double longitudeDev;
	/**
	 * NO: 9  <br>
	 * nameCN: 纬度偏差  <br>
	 * unit: 度 <br>
	 * BUFR FXY: <br>纬度-测站纬度，以度为单位，其中1位符号位，1位整数，3位小数
	 */
	private double latitudeeDev;
	public Double getRelativeTime() {
		return relativeTime;
	}
	public void setRelativeTime(Double relativeTime) {
		this.relativeTime = relativeTime;
	}
	public Double getTemperature() {
		return Temperature;
	}
	public void setTemperature(Double temperature) {
		Temperature = temperature;
	}
	public Double getPressure() {
		return pressure;
	}
	public void setPressure(Double pressure) {
		this.pressure = pressure;
	}
	public int getRelativeHumidity() {
		return RelativeHumidity;
	}
	public void setRelativeHumidity(int relativeHumidity) {
		RelativeHumidity = relativeHumidity;
	}
	public int getWindDir() {
		return windDir;
	}
	public void setWindDir(int windDir) {
		this.windDir = windDir;
	}
	public double getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public double getLongitudeDev() {
		return longitudeDev;
	}
	public void setLongitudeDev(double longitudeDev) {
		this.longitudeDev = longitudeDev;
	}
	public double getLatitudeeDev() {
		return latitudeeDev;
	}
	public void setLatitudeeDev(double latitudeeDev) {
		this.latitudeeDev = latitudeeDev;
	}
	
	
}
