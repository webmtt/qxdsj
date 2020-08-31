package cma.cimiss2.dpc.decoder.bean.cawn;
/**
 * 
 * <br>
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
 * 
 * @author cuihongyuan
 *
 *
 */

public class ForestFire {
	
	/**
	 * 资料时间，年
	 */
	private int year;
	/**
	 * 月
	 */
	private int month;
	/**
	 * 日
	 */
	private int day;
	/**
	 * 时
	 */
	private int hour;
	/**
	 * 分
	 */
	private int minute;
	/**
	 * 秒
	 */
	private int second;
	/**
	 * 所属省，字符串型
	 */
	private String prov = "";
	/**
	 * 所属地、市
	 */
	private String city = "";
	/**
	 * 所属区、县
	 */
	private String county = "";
	/**
	 * 详细地址
	 */
	private String address = "";
	/**
	 * 火情描述
	 */
	private String fireDetail = "";
	/**
	 * 填报人
	 */
	private String filler = "";
	
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getFireDetail() {
		return fireDetail;
	}
	public void setFireDetail(String fireDetail) {
		this.fireDetail = fireDetail;
	}
	public String getFiller() {
		return filler;
	}
	public void setFiller(String filler) {
		this.filler = filler;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	public int getSecond() {
		return second;
	}
	public void setSecond(int second) {
		this.second = second;
	}
	
}
