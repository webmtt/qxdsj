package cma.cimiss2.dpc.decoder.tools;

/**
 * 
 * <br>
 * @Title:  ElementValUtil.java   
 * @Package cma.cimiss2.dpc.decoder.tools   
 * @Description:    TODO(数值数据的处理)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月25日 下午3:10:08   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 *
 *
 */
public class ElementValUtil {
	/**
	 * 
	 * @Title: ToBeValid   
	 * @Description: TODO(浮点数值数据 的处理 , factor < 1.0)   
	 * @param: @param str
	 * @param: @param factor
	 * @param: @return      
	 * @return: double      
	 * @throws
	 */
	public static double ToBeValidDouble(String str, double factor) {
		int len = str.length();
		int cnt = 0;
		for (int i = 0; i < len; i++) {
			if (str.charAt(i) == '9')
				cnt++;
		}
		if (cnt == len)
			return 999999;
		else
			try{
				return Double.parseDouble(str) * factor;
			}catch (Exception e) {
				return 999999;
			}
	}

	/**
	 * 
	 * @Title: ToBeValidDouble   
	 * @Description: TODO(数值数据的处理 , factor = 1.0)   
	 * @param: @param str
	 * @param: @return      
	 * @return: double      
	 * @throws
	 */
	public static double ToBeValidDouble(String str) {
		int len = str.length();
		int cnt = 0;
		for (int i = 0; i < len; i++) {
			if (str.charAt(i) == '9')
				cnt++;
		}
		if (cnt == len)
			return 999999;
		else{
			try{
				return Double.parseDouble(str);
			}catch (Exception e) {
				return 999999;
			}
		}
	}
	/**
	 * 
	 * @Title: ToBeValidDouble   
	 * @Description: TODO(数值数据的处理)   
	 * @param: @param str
	 * @param: @return      
	 * @return: double      
	 * @throws
	 */
	public static double ToBeValidDouble2(String str) {
			try{
				return Double.parseDouble(str);
			}catch (Exception e) {
				return 999999;
			}
	}
	/**
	 * @Title: CheckValidValue2Double
	 * @Description: 校验值并转换为double,如果被校验的值value等于缺测值，直接返回默认缺测值999999，如果missValue为null则判断value是否全为9，如果全是9返回999999，否则将value转换为double返回
	 * @param value 需校验的值
	 * @param missValue 缺测值(报文文件中规定的)
	 * @return double 返回转换的后的值
	 * @throws: 
	 */
	public static double CheckValidValue2Double(String value, Double missValue) {
		
		if(missValue != null && value != null && !"".equals(value)) {
			try{
				double parseValue = Double.parseDouble(value);
				if ((missValue < 0 && parseValue <= missValue) || (missValue > 0 && parseValue >= missValue)) {
					return 999999;
				} else {
					return parseValue;
				}
			}catch (Exception e) {
				return 999999;
			}
		}else {
			if(value != null) {
				return ToBeValidDouble(value);
			}else {
				return 999999;
			}
		}
	}
	
	public static double CheckValidValue2Double(double value, double missValue) {
		if(value == missValue){
			return 999999;
		}else {
			return value;
		}
		
	}

	/**
	 * 
	 * @Title: ToBeValidInt   
	 * @Description: TODO(整数数值数据 整数的处理 )   
	 * @param: @param str
	 * @param: @return      
	 * @return: int      
	 * @throws:
	 */
	public static int ToBeValidInt(String str) {
		int len = str.length();
		int cnt = 0;
		for (int i = 0; i < len; i++) {
			if (str.charAt(i) == '9')
				cnt++;
		}
		if (cnt == len)
			return 999999;
		else
			return Integer.parseInt(str);
	}

	/**
	 * 纬度转换成度分秒的形式，前两位度不变，分除以60，秒除以3600
	 * 
	 */

	public static Double getlatitude(String arg1) {
		Double latitude = Double.valueOf(arg1.substring(0, 2)) + Double.valueOf(arg1.substring(2, 4)) / 60 + Double.valueOf(arg1.substring(4, 6)) / 3600;

		return latitude;
	}

	/**
	 * 经度转换成度分秒的形式，前三位度不变，分除以60，秒除以3600
	 * 
	 */
	public static Double getLongitude(String arg1) {
		int idx = arg1.indexOf(".");
		if(idx != -1 && arg1.substring(0, idx).length() == 6){
			arg1 = "0" + arg1;
		}
		Double longitude = Double.valueOf(arg1.substring(0, 3)) + Double.valueOf(arg1.substring(3, 5)) / 60 + Double.valueOf(arg1.substring(5, 7)) / 3600;

		return longitude;
	}
	
	public static void main(String[] args) {
		System.out.println(CheckValidValue2Double(99.0, 99));
	}

}
