package org.cimiss2.dwp.tools.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.commons.lang3.ArrayUtils;

/**
 * <br>
 * @Title:  NumberUtil.java
 * @Package org.cimiss2.dwp.tools.utils
 * @Description: 数字工具类
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年1月2日 上午10:11:27   wangzunpeng    Initial creation.
 * </pre>
 * 
 * @author wangzunpeng
 */
public class NumberUtil {

	/**
	 * @Title: addZeroForNum 
	 * @Description: TODO(补0方法) 
	 * @param num
	 * @param strLength
	 * @return int
	 * @throws:
	 */
	public static int addZeroForNum(int num, int strLength) {
		String str = String.valueOf(num);
	    int strLen = str.length();
	    if (strLen < strLength) {
	        while (strLen < strLength) {
	            StringBuffer sb = new StringBuffer();

	            sb.append(str).append("0");//右补0
	            str = sb.toString();
	            strLen = str.length();
	        }
	    }
	 
	    return Integer.parseInt(str);
	}
	/**
	 * @Title: 数字转换成指定长度
	 * @Description: 数字转换指定长度，高位补0，如果是负数第一位为1，如果为正数，第一位为0，符号算在指定长度内
	 * @param number 需要转换的数字
	 * @param length 转换后的长度
	 * @return String 转换指定长度后的数字
	 * @throws: 
	 */
	public static String Num2DesignatedLenWithMark(long number, int length) {
		StringBuffer sb = new StringBuffer();
		if(number < 0) {
			sb.append(1);
		}else {
			sb.append(0);
		}
		for(int i = 0; i < length - 1; i ++) {
			sb.append(0);
		}
		DecimalFormat df = new DecimalFormat(sb.toString());
		return df.format(Math.abs(number));
	}
	/**
	 * @Title: 数字转换成指定长度
	 * @Description: 数字转换指定长度，不带符号转换
	 * @param number 需要转换的数字
	 * @param length 转换后的长度
	 * @return String 转换指定长度后的数字
	 * @throws: 
	 */
	public static String Num2DesignatedLen(long number, int length) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < length; i ++) {
			sb.append(0);
		}
		DecimalFormat df = new DecimalFormat(sb.toString());
		return df.format(number);
	}
	
	/**
	 * @Fields NINE_ARRAYS : 用于格式化为6个9的缺测值{99,999,9999,99999}
	 */
	private static final double[] NINE_ARRAYS = {99,999,9999,99999};
//	private static final double[] NINE_DOUBLE = {99999.9,9999.99,999.999,99.9999,9.99999};
	/**
	 * 
	 * @Title: FormatNumOrNine
	 * @Description: 将double数字转换为BigDecimal，如果number为{99,999,9999,99999}，将其转换为6位9缺测值
	 * @param number double格式的数值
	 * @return BigDecimal 转换后的数值
	 * @throws:
	 */
	public static BigDecimal FormatNumOrNine(double number) {
		boolean contains = ArrayUtils.contains(NINE_ARRAYS, number);
		if(contains) {
			DecimalFormat df = new DecimalFormat("000000");
			String ff = df.format(number);
			String sixRes = ff.replace("0", "9");
			return new BigDecimal(sixRes);
		}else {
			return new BigDecimal(String.valueOf(number));
		}
	}
	/**
	 * 
	 * @Title: FormatNumOrNineOnLatLon
	 * @Description: 将double数字转换为BigDecimal，如果number为{99,999,9999,99999}，将其转换为6位9缺测值
	 * @param number double格式的数值
	 * @return BigDecimal 转换后的数值
	 * @throws:
	 */
	public static BigDecimal FormatNumOrNineOnLatLon(double number) {
		boolean contains = ArrayUtils.contains(NINE_ARRAYS, number);
		if(contains) {
			DecimalFormat df = new DecimalFormat("000000");
			String ff = df.format(number);
			String sixRes = ff.replace("0", "9");
			return new BigDecimal(sixRes);
		}else {
			return new BigDecimal(number).setScale(4, BigDecimal.ROUND_HALF_UP);
		}
	}
	
	public static void main(String[] args) {
		System.out.println(NumberUtil.FormatNumOrNine(99999.900));
	}
}
