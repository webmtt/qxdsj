package cma.cimiss2.dpc.quickqc.bean;

public class SunRiseSetTime {
	/**
	 * 日出时间
	 */
	private double sunRiseTime;
	/**
	 * 日落时间
	 */
	private double sunSetTime;
	/**
	 * 太阳高度角最大时间
	 */
	private double TimeOfMaxSolaAltitudeAngle;
	
	
	public double getSunRiseTime() {
		return sunRiseTime;
	}
	public void setSunRiseTime(double sunRiseTime) {
		this.sunRiseTime = sunRiseTime;
	}
	public double getSunSetTime() {
		return sunSetTime;
	}
	public void setSunSetTime(double sunSetTime) {
		this.sunSetTime = sunSetTime;
	}
	public double getTimeOfMaxSolaAltitudeAngle() {
		return TimeOfMaxSolaAltitudeAngle;
	}
	public void setTimeOfMaxSolaAltitudeAngle(double timeOfMaxSolaAltitudeAngle) {
		TimeOfMaxSolaAltitudeAngle = timeOfMaxSolaAltitudeAngle;
	}
	
	
}
