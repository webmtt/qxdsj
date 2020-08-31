package org.cimiss2.dwp.tools.utils;

import java.util.regex.Pattern;

/**
 * 
 * <br>
 * @Title:  StationCodeUtil.java   
 * @Package org.cimiss2.dwp.tools.utils   
 * @Description:    TODO(字符站号转数字)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月19日 下午12:14:57   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *
 *
 */
public class StationCodeUtil {
	/**
	 * 
	 * @Title: stringToAscii   
	 * @Description: TODO(字符站号转数子)   
	 * @param: @param value
	 * @param: @return      
	 * @return: String  字符站    
	 * @throws
	 */
	public static String stringToAscii(String value){  
	    StringBuffer sbu = new StringBuffer();  
	    String regexString = "^[A-Z0-9]{1}\\d{4}$";
	    Pattern p=Pattern.compile(regexString); 
	    if(p.matcher(value).find()){
	    	for (int i = 0; i < value.length(); i++) {
	    		if(!Character.isDigit(value.charAt(i))){
	    			sbu.append((int)value.charAt(i));
	    		}else {
	    			sbu.append(value.charAt(i));
	    		}
	    	}
	    	return sbu.toString();  
	    }else{
	    	return "999999";
	    }
	}
	

}
