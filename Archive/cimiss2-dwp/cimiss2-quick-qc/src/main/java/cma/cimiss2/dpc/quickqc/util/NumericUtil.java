package cma.cimiss2.dpc.quickqc.util;


public class NumericUtil {
	
	/**
	 *  //判断字符串是否为数字
	 * @param str 传输字符串
	 * @return  true 和 false
	 */
	public static boolean isNumeric(String str) {
		if (str != null && !"".equals(str.trim())){
			return str.matches("^[0-9]*$");
		}
	    else{
			return false;
		}
	}

	
}
