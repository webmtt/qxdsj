package cma.cimiss2.dpc.decoder.agme;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class DecodeUtil.
 */
public class DecodeUtil {
	
	/**
	 * Parses the double.
	 *
	 * @param input_str the input str
	 * @return the double
	 */
	public static double parseDouble(String input_str) {
		if(isNumeric(input_str)) {
			return Double.parseDouble(input_str);
		}else {
			return (double) 999999;
		}
	}
	
	/**
	 * Parses the int.
	 *
	 * @param input_str the input str
	 * @return the int
	 */
	public static int parseInt(String input_str) {
		if(isNumeric(input_str)) {
			return Integer.parseInt(input_str);
		}else {
			return 999999;
		}
	}
	
	/**
	 * Parses the float.
	 *
	 * @param input_str the input str
	 * @return the float
	 */
	public static float parseFloat(String input_str) {
		if(isNumeric(input_str)) {
			return Float.parseFloat(input_str);
		}else {
			return 999999;
		}
	}
	
	/**
	 * 判断字符串是否为数字.
	 *
	 * @param input_str the input str
	 * @return true, if is numeric
	 */
	public static boolean isNumeric(String input_str) {
		
		Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
        String bigStr;
        try {
            bigStr = new BigDecimal(input_str.trim()).toString();
        } catch (Exception e) {
            return false;//异常 说明包含非数字。
        }

        Matcher isNum = pattern.matcher(bigStr); // matcher是全匹配
        if (!isNum.matches()) {
            return false;
        }
        return true;
	}
	
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		System.out.println(DecodeUtil.isNumeric(null));
	}

}
