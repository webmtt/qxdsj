package cma.cimiss2.dpc.unstruct.common;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageUtil {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static String CTS_CODE_REGEX = "[A-Z]\\.\\d{4}\\.\\d{4}.R\\d{3}";
	public static boolean messageCheck(byte [] body) {
		try {
			String message = new String(body, "UTF-8");
			// 验证消息体的基本结构，前16个字符为CTS四级编码
			if(message.length() > 16 && message.indexOf(":") == 16) {
				
				// CTS四级编码格式检查
				String ctsCode = message.substring(0, 16);
				Pattern pattern = Pattern.compile(CTS_CODE_REGEX);
				Matcher matcher = pattern.matcher(ctsCode);
				if (matcher.matches()) {
					File file = new File(message.substring(17));
					if (file.exists()) {
						// 文件名格式检查，
						return true;
					}else {
						infoLogger.error("file not exists ： " + message );
						return false;
					}
					
				}else {
					infoLogger.error("cts code error ： " + message );
					return false;
				}
								
			}else {
				infoLogger.error("message error ： " + message + "\r not find ':' in index=16 or message length <= 16");
				return false;
			}
			
			
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			infoLogger.error("MessageUtil.MessageUtil error " + e);
			return false;
		}
		
	}
	
	
	public static void main(String[] args) {
		String messageString = "A.0001.0002.R001:/data/cccc.txt";
		
		System.out.println(messageString.indexOf(":"));
		
		Pattern pattern = Pattern.compile(CTS_CODE_REGEX);
		Matcher matcher = pattern.matcher(messageString.substring(0, 16));
		System.out.println(matcher.matches());
	}

}
