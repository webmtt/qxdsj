package cma.cimiss2.dpc.decoder.cawn;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
	
	public static void main(String[] args) {
		String convertJulianDay2Date = convertJulianDay2Date(32);
		System.out.println(convertJulianDay2Date);
	}
}
