package cma.cimiss2.dpc.decoder.tools.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <br>
 * @Title:  DateUtil.java
 * @Package cma.cimiss2.dpc.decoder.tools.utils
 * @Description:    日期时间工具类
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年2月27日 下午2:37:16   wangzunpeng    Initial creation.
 * </pre>
 * 
 * @author wangzunpeng
 */
public class DateUtil {
	public final static String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public final static String DATETIME_FORMAT_YMDHMS = "yyyyMMddHHmmss";
	public final static String DATETIME_FORMAT_YMD = "yyyyMMdd";

	/**
	 * @Title: convertJulianDay2Date
	 * @Description: 序日转换为当前的年月日
	 * @param julianDay 序日，当年的第几天
	 * @return String
	 * @throws: 
	 */
	public static String convertJulianDay2Date(int julianDay) {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		return convertJulianDay2Date(year, julianDay);
	}
	
	/**
	 * @Title: convertJulianDay2Date
	 * @Description: 序日转换为年月日
	 * @param year 年份
	 * @param julianDay 序日，这一年的第几天
	 * @return String
	 * @throws: 
	 */
	public static String convertJulianDay2Date(int year, int julianDay) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, 0, 1, 0, 0, 0);
		cal.add(Calendar.DAY_OF_YEAR, julianDay - 1);
		SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT_YMD);
		return sdf.format(cal.getTime());
	}

	/**
	 * 获取当月最大天数
	 * @param date 年月 yyyyMM
	 * @return
	 */
	public static int getDayMaxOfMonth(String date){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, Integer.parseInt(date.substring(0,4)));
		calendar.set(Calendar.MONTH, Integer.parseInt(date.substring(4,6)) - 1);
		//把日期设置为当月第一天
		calendar.set(Calendar.DATE, 1);
		//日期回滚一天，也就是最后一天
		calendar.roll(Calendar.DATE, -1);
		return calendar.get(Calendar.DATE);
	}

	/**
	 * 字符串转Date
	 * @param format
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date formatDate(String format, String date) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(date);
	}
	
	public static void main(String[] args) {
		String convertJulianDay2Date = convertJulianDay2Date(32);
		System.out.println(convertJulianDay2Date);
	}
}
