package cma.cimiss2.dpc.decoder.bean;
// TODO: Auto-generated Javadoc

/**
 * <br>.
 *
 * @author cuihongyuan
 * @Title:  ForestFire.java
 * @Package cma.cimiss2.dpc.decoder.bean
 * @Description:    TODO(森林火情数据要素实体类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月9日 上午9:55:31   cuihongyuan    Initial creation.
 * </pre>
 */

public class ForestFire {
	
	/** 资料时间，年. */
	private int year;
	
	/** 月. */
	private int month;
	
	/** 日. */
	private int day;
	
	/** 时. */
	private int hour;
	
	/** 分. */
	private int minute;
	
	/** 秒. */
	private int second;
	
	/** 所属省，字符串型. */
	private String prov = "";
	
	/** 所属地、市. */
	private String city = "";
	
	/** 所属区、县. */
	private String county = "";
	
	/** 详细地址. */
	private String address = "";
	
	/** 火情描述. */
	private String fireDetail = "";
	
	/** 填报人. */
	private String filler = "";
	
	/**
	 * Gets the prov.
	 *
	 * @return the prov
	 */
	public String getProv() {
		return prov;
	}
	
	/**
	 * Sets the prov.
	 *
	 * @param prov the new prov
	 */
	public void setProv(String prov) {
		this.prov = prov;
	}
	
	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	
	/**
	 * Sets the city.
	 *
	 * @param city the new city
	 */
	public void setCity(String city) {
		this.city = city;
	}
	
	/**
	 * Gets the county.
	 *
	 * @return the county
	 */
	public String getCounty() {
		return county;
	}
	
	/**
	 * Sets the county.
	 *
	 * @param county the new county
	 */
	public void setCounty(String county) {
		this.county = county;
	}
	
	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	/**
	 * Gets the fire detail.
	 *
	 * @return the fire detail
	 */
	public String getFireDetail() {
		return fireDetail;
	}
	
	/**
	 * Sets the fire detail.
	 *
	 * @param fireDetail the new fire detail
	 */
	public void setFireDetail(String fireDetail) {
		this.fireDetail = fireDetail;
	}
	
	/**
	 * Gets the filler.
	 *
	 * @return the filler
	 */
	public String getFiller() {
		return filler;
	}
	
	/**
	 * Sets the filler.
	 *
	 * @param filler the new filler
	 */
	public void setFiller(String filler) {
		this.filler = filler;
	}
	
	/**
	 * Gets the year.
	 *
	 * @return the year
	 */
	public int getYear() {
		return year;
	}
	
	/**
	 * Sets the year.
	 *
	 * @param year the new year
	 */
	public void setYear(int year) {
		this.year = year;
	}
	
	/**
	 * Gets the month.
	 *
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}
	
	/**
	 * Sets the month.
	 *
	 * @param month the new month
	 */
	public void setMonth(int month) {
		this.month = month;
	}
	
	/**
	 * Gets the day.
	 *
	 * @return the day
	 */
	public int getDay() {
		return day;
	}
	
	/**
	 * Sets the day.
	 *
	 * @param day the new day
	 */
	public void setDay(int day) {
		this.day = day;
	}
	
	/**
	 * Gets the hour.
	 *
	 * @return the hour
	 */
	public int getHour() {
		return hour;
	}
	
	/**
	 * Sets the hour.
	 *
	 * @param hour the new hour
	 */
	public void setHour(int hour) {
		this.hour = hour;
	}
	
	/**
	 * Gets the minute.
	 *
	 * @return the minute
	 */
	public int getMinute() {
		return minute;
	}
	
	/**
	 * Sets the minute.
	 *
	 * @param minute the new minute
	 */
	public void setMinute(int minute) {
		this.minute = minute;
	}
	
	/**
	 * Gets the second.
	 *
	 * @return the second
	 */
	public int getSecond() {
		return second;
	}
	
	/**
	 * Sets the second.
	 *
	 * @param second the new second
	 */
	public void setSecond(int second) {
		this.second = second;
	}
	
}
