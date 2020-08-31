package cma.cimiss2.dpc.decoder.check.util;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.tools.utils.TimeUtil;

public class TimeCheckUtil {
	public static final Logger logger = LoggerFactory.getLogger("loggerInfo"); // 消息处理日志

	private static int before_day = 72;
	private static int after_day = 72;

	public static int getBefore_day() {
		return before_day;
	}

	public static void setBefore_day(int before_day) {
		TimeCheckUtil.before_day = before_day;
	}

	public static int getAfter_day() {
		return after_day;
	}

	public static void setAfter_day(int after_day) {
		TimeCheckUtil.after_day = after_day;
	}

	/**
	   *  资料时间范围检查
	 * @param dateTime 资料时间
	 * @return 如果在给定时间范围内 返回 true
	 */
	public static boolean checkTime(Date dateTime) {

		// 当前时间 + TimeCheckUtil.before_day 小时
		Calendar beforeCalendar = Calendar.getInstance();
		beforeCalendar.add(Calendar.HOUR_OF_DAY, TimeCheckUtil.before_day);
		// 当前时间 - TimeCheckUtil.after_day 小时
		Calendar afterCalendar = Calendar.getInstance();
		afterCalendar.add(Calendar.HOUR_OF_DAY, -TimeCheckUtil.after_day);

//		System.out
//				.println("Time Range: 开始时间     " + TimeUtil.date2String(beforeCalendar.getTime(), "yyyy-MM-dd HH:mm:ss")
//						+ "\t 结束时间    " + TimeUtil.date2String(afterCalendar.getTime(), "yyyy-MM-dd HH:mm:ss")
//						+ "\t 资料时间    " + TimeUtil.date2String(dateTime, "yyyy-MM-dd HH:mm:ss"));

		if (dateTime.after(afterCalendar.getTime()) && dateTime.before(beforeCalendar.getTime())) {
			return true;
		} else {
			logger.error("Time Range: " + TimeUtil.date2String(beforeCalendar.getTime(), "yyyy-MM-dd HH:mm:ss") + "~ \t"
					+ TimeUtil.date2String(afterCalendar.getTime(), "yyyy-MM-dd HH:mm:ss") + "but nowdate : "
					+ TimeUtil.date2String(dateTime, "yyyy-MM-dd HH:mm:ss"));
			return false;
		}

	}

}
