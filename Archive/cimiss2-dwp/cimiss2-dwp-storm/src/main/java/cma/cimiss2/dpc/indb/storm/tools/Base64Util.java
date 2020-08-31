package cma.cimiss2.dpc.indb.storm.tools;

import java.util.Base64;
/**
* *******************************************************************************************<br>
* <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
* *******************************************************************************************<br>
* <b>Description: Base64 编解码工具类</b><br> 
* @author wuzuoqiang 
* @version 1.0
* @Note 主要实现字符串采用Base方式解码和编码
* <b>ProjectName:</b> cimiss2-dwp-multithread
* <br><b>PackageName:</b> cma.cimiss2.dpc.indb.common
* <br><b>ClassName:</b> Base64Util
* <br><b>Date:</b> 2020年4月26日 下午6:07:10
 */
public class Base64Util {
	/**
	 * Base64 对字符串编码
	 * @param pwd 密码字符串
	 * @return Base64 编码之后的字符串
	 */
	public static String encode(String pwd) {
		return new String(Base64.getEncoder().encode(pwd.getBytes()));
	}
	
	/**
	 * Base64 解码字符串
	 * @param pwd 编码之后的字符串
	 * @return Base64 解码后的字符串
	 */
	private static String decode(String pwd) {
		return new String(Base64.getDecoder().decode(pwd.getBytes()));
	}
	
	/**
	 * @获取真实密码
	 * @param pwd Base64编码后的字符串
	 * @return 返回真实密码
	 */
	public static String getPwd(String pwd) {
		String tempPwd = decode(pwd).trim();
		return tempPwd.substring(0, tempPwd.length()-4);
	}
	
	
	public static void main(String[] args) {
//		System.out.println(Base64Util.encode("xugu@2019!dpc"));
		System.out.println(Base64Util.encode("fztest!dpc"));
		System.out.println(Base64Util.getPwd("Znp0ZXN0IWRwYw=="));
		System.out.println(Base64Util.decode("Znp0ZXN0IWRwYw=="));
	}

}
