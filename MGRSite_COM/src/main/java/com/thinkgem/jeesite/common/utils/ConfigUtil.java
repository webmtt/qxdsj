package com.thinkgem.jeesite.common.utils;

import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigUtil {

	/**
	 * 
	 * <p>功能描述：通过key值获得指定配置文件中某个key对应的值</p>
	 * @param filename
	 * @param key
	 * @return
	 * @author: Etone Yong
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public static String getConfigPropertyValue(String filename,String key){
		ResourceBundle config = ResourceBundle.getBundle(filename);
		return config.getString(key);
	}

	/**
	 * 根据key获取properties属性值
	 * @param key
	 * @return String
	 */
	public static String getString(String key) {
		StringBuffer sbuf = new StringBuffer();
		String string = String.valueOf(HitecPropertyConfigurer.getContextProperty(key)).trim();
		String regex = "(\\$\\{)(\\S)+(\\})";
		Pattern pattern = Pattern.compile(regex, Pattern.CANON_EQ);
		Matcher matcher = pattern.matcher(string);
		sbuf = new StringBuffer();
		while (matcher.find()) {
			//			String group = matcher.group();
			//			System.out.println(group);
			String name = string.substring(matcher.start()+2, matcher.end()-1).trim();
			matcher.appendReplacement(sbuf, getString(name));
		}
		matcher.appendTail(sbuf);
		return sbuf.toString();
	}

	/**
	 * 根据key获取properties属性值
	 * @param key
	 * @return int
	 */
	public static int getInt(String key) {
		return Integer.valueOf(getString(key));
	}

	/**
	 * 根据key获取properties属性值
	 * @param key
	 * @return double
	 */
	public static double getDouble(String key) {
		return Double.valueOf(getString(key));
	}
}
