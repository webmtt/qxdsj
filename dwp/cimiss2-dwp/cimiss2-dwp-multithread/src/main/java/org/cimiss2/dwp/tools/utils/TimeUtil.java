package org.cimiss2.dwp.tools.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * <br>
 * @Title:  TimeUtil.java   
 * @Package org.cimiss2.dwp.RADAR.tool   
 * @Description:    TODO(时间工具类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月15日 下午7:27:46   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *
 *
 */
public class TimeUtil {

	/**
	 * 缺省的日期显示格式： yyyy-MM-dd
	 */
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * 缺省的日期时间显示格式：yyyy-MM-dd HH:mm:ss
	 */
	public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String DATE_FMT_YMD = "yyyyMMdd";
	public static final String DATE_FMT_YMDH = "yyyyMMddHH";
	public static final String DATE_FMT_YMDHM = "yyyyMMddHHmm";
	public static final String DATE_FMT_YMDHMS = "yyyyMMddHHmmss";
	
	/**
	 * 
	 * @Title: getSysTime   
	 * @Description: TODO(获取当前系统时间)   
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	public static String getSysTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String currentSysTime = dateFormat.format(date);
		return currentSysTime;
	}

	/**
	 * 功能：获取系统时间
	 * @param strDateFormat 时间类格式：yyyy-MM-dd HH:mm:ss.SSS / yyyyMMdd /yyyyMMdd HH:mm:ss
	 * @return ：返回指定格式的系统时间字符串
	 */
	public static String getSysTime(String strDateFormat) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(strDateFormat);
		Date date = new Date();
		String currentSysTime = dateFormat.format(date);
		return currentSysTime;
	}

	/**
	 * 获取制定格式的日期字符串
	 * 时间类格式：yyyy-MM-dd HH:mm:ss.SSS / yyyyMMdd /yyyyMMdd HH:mm:ss
	 * @param date 时间
	 * @param strDateFormat 时间类格式：yyyy-MM-dd HH:mm:ss.SSS / yyyyMMdd /yyyyMMdd HH:mm:ss
	 * @return
	 */
	public static String date2String(Date date, String strDateFormat) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(strDateFormat);
		String strTime = dateFormat.format(date);
		return strTime;
	}
	
	

	/**
	 * 字符串转为日期
	 * @param strDate 日期字符串
	 * @param strDateFormat 日期格式，时间类格式：yyyy-MM-dd HH:mm:ss.SSS / yyyyMMdd /yyyyMMdd HH:mm:ss
	 * @return
	 */
	public static Date String2Date(String strDate, String strDateFormat) {
		Date date = null;
		try {
			date = new SimpleDateFormat(strDateFormat).parse(strDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 获取某一个日期的年份
	 * 
	 * @param d
	 * @return
	 */
	public static int getYear(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return c.get(Calendar.YEAR);
	}

	/**
	 * 获取某一个日期的月份
	 * 
	 * @param d
	 * @return
	 */
	public static int getMonth(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return c.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取某一个月的日
	 * 
	 * @param d
	 * @return
	 */
	public static int getDayOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 根据日期确定星期几:1-星期日，2-星期一.....s
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int mydate = c.get(Calendar.DAY_OF_WEEK);
		return mydate;
	}

	/**
	 * 获取某一天的小时
	 * 
	 * @param d
	 * @return
	 */
	public static int getHourOfDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取某一日期的小时，1到12小时
	 * 
	 * @param d
	 * @return
	 */
	public static int getHour(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.HOUR);
	}

	/**
	 * 获取某一个日期的分钟
	 * 
	 * @param d
	 * @return
	 */
	public static int getMinute(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MINUTE);
	}

	/**
	 * 获取某一个日期的秒
	 * 
	 * @param d
	 * @return
	 */
	public static int getSecond(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.SECOND);
	}
	public static  String[] getYmdhm(String date){
		int size=5;
		String []datea=new String[size];
		datea[0]=date.substring(0,4);
		datea[1]=date.substring(4,6);
		datea[2]=date.substring(6,8);
		datea[3]=date.substring(8,10);
		datea[4]=date.substring(10,12);
		
		return datea;
	}
	public static  String[] getYmdhms(String date){
		int size=6;
		String []datea=new String[size];
		datea[0]=date.substring(0,4);
		datea[1]=date.substring(4,6);
		datea[2]=date.substring(6,8);
		datea[3]=date.substring(8,10);
		datea[4]=date.substring(10,12);
		datea[5]=date.substring(12,14);
		return datea;
	}
}
