/*
 * @(#)WeekDayUtil.java 2016年1月28日
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.sys.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年1月28日
 */
public class WeekDayUtil {
	/**
	 * 根据日期获得星期
	 */
	public static String getWeekOfDateName(Date date){
		String[] weekDaysName={"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
//		String[] weekDaysCode={"0","1","2","3","4","5","6"};
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		int intWeek=c.get(Calendar.DAY_OF_WEEK)-1;
		return weekDaysName[intWeek];
	}
	public static String getWeekOfDateCode(Date date){
//		String[] weekDaysName={"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
		String[] weekDaysCode={"0","1","2","3","4","5","6"};
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		int intWeek=c.get(Calendar.DAY_OF_WEEK)-1;
		return weekDaysCode[intWeek];
	}
	/**
	 * 获得周一的日期
	 */
	public static String getMonday(Date date){
		SimpleDateFormat sf=new SimpleDateFormat("yyyyMMdd");
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		return sf.format(c.getTime());
	}
	/**
	 * 当前日期前几天或者后几天的日期
	 */
	public  static String afterNDay(int n){
		SimpleDateFormat sf=new SimpleDateFormat("yyyyMMdd");
		Calendar c=Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, n);
		Date date =c.getTime();
		String s=sf.format(date);
		return s;
	}
	/**
	 * 判断两个日期是否在同一周
	 */
	public  static boolean isSameWeek(Date d1,Date d2){
		Calendar c1=Calendar.getInstance();
		Calendar c2=Calendar.getInstance();
		c1.setTime(d1);
		c2.setTime(d2);
		int sunYear=c1.get(Calendar.YEAR)-c2.get(Calendar.YEAR);
		if(0==sunYear){
			if(c1.get(Calendar.WEEK_OF_YEAR)==c2.get(Calendar.WEEK_OF_YEAR)){
				return true;
			}
		}else if(1==sunYear&&11==c2.get(Calendar.MONTH)){
			//如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
			if(c1.get(Calendar.WEEK_OF_YEAR)==c2.get(Calendar.WEEK_OF_YEAR)){
				return true;
			}
			}else if(-1==sunYear&&11==c1.get(Calendar.MONTH)){
				if(c1.get(Calendar.WEEK_OF_YEAR)==c2.get(Calendar.WEEK_OF_YEAR)){
					return true;
			}
		}
		return false;
	}
}
