package cma.cimiss2.dpc.decoder.surf;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 *  All Rights reserved by National Meteorological Information centre (NMIC)<br>
 * Main class of decode the normalfloor per hour data. <br>
 * 质控后区域站雨量数据版主帮助类
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 11/22/2017   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *
 *           
 */
public class DecodeUtil {
	/**
	 * 判断一个字符串是否为数字
	 * @param num_str 字符串
	 * @return true和false
	 *
	 */
	public static boolean is_number(final String num_str){
		//^[0-9]*[1-9][0-9]*$
		String regex = "^[0-9]*[0-9][0-9]*$";
		return Pattern.matches(regex, num_str);
	}
	
	/**
	 * 度分秒 转为 度
	 * @param latlon_str
	 * @return
	 */
	public static double to_latlon(String latlon_str){
		if(is_number(latlon_str)){
			float value = Float.parseFloat(latlon_str.substring(0, latlon_str.length()-4)) + 
					      Float.parseFloat(latlon_str.substring(latlon_str.length()-4, latlon_str.length()-2))/60 + 
					      Float.parseFloat(latlon_str.substring(latlon_str.length()-2, latlon_str.length()))/3600;
			BigDecimal bg = new BigDecimal(value);

			return bg.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
		}else {
			return Double.NaN;
		}
	}

}
