package cma.cimiss2.dpc.decoder.tools.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <br>
 * @Title:  StringUtil.java
 * @Package cma.cimiss2.dpc.decoder.tools.utils
 * @Description: 字符串工具类
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月29日 下午1:34:34   wangzunpeng    Initial creation.
 * </pre>
 * 
 * @author wangzunpeng
 */
public class StringUtil {

	/**
	 * @Fields RANDOM_STR : 唯一串
	 */
	private static final String RANDOM_STR = "TMP9*60533%9GT1FEAF^42368R2(16A)0B00F20A438";
	
	/**
	 * @Fields DOUBLE_QUOTATION_ARBITRARILY_REG : 两端带有双引号中间是任意字符的正则表达式
	 */
	public static final String DOUBLE_QUOTATION_ARBITRARILY_REG = "\".*?\"";
	
	/**
	 * 
	 * @Title: 字符串分割
	 * @Description: 以regex分割字符串，但当中有些字符串是一整体，此整体中存在和分割符一样的字符
	 * @param str 需要被分割的字符串
	 * @param regex 分割符
	 * @param constansRegex 字符串中存在分割符时，但此串字符是一个整体，此整体的正则表达式
	 * @return String[] 分割之后的字符数组
	 * @throws:
	 */
	public static String[] split(String str, String regex, String constansRegex) {
		Matcher m = Pattern.compile(constansRegex).matcher(str);
		long num = 9487236510L;
		Map<String, String> tmpMap = new HashMap<String, String>();
		while (m.find()) {
			String group = m.group(0);
			String tmp_num = (RANDOM_STR + num).replaceAll(regex, "");	//唯一字符串,防止此替换串中存在regex的字符，将其替换为空
			str = str.replace(group, tmp_num);
			tmpMap.put(tmp_num, group);
			num++;
		}
		String[] strs = str.split(regex);
		for (int i = 0; i < strs.length; i++) {
			String s = strs[i];
			String string = tmpMap.get(s);
			if (string != null) {
				strs[i] = string;
			}
		}
		return strs;
	}
	
	/**
	 * @Title: main
	 * @Description: TODO 测试
	 * @param args 
	 * @return void
	 * @throws: 
	 */
	public static void main(String[] args) {
		String str = "a,b,c,\"d,e\",\"f,g\",789,1,2,\"3,6,4\",5,你好";
		long sd = System.currentTimeMillis();
		
		String[] ss = split(str,",", DOUBLE_QUOTATION_ARBITRARILY_REG);
		long ed = System.currentTimeMillis();
		System.out.println(ed - sd + "ms");
		for (String s : ss) {
			System.out.println(s);
			
		}
	}
}
