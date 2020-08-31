package cma.cimiss2.dpc.fileloop.common;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MakeUpData {
	private final static Logger logger = LoggerFactory.getLogger("messageInfo"); //消息日志
	public static void getDatafromDirToRMQ(Date startDate, Date endDate, String path, List<String> cts_codes) {
//		Connection connection = null;
//		Channel channel = null;
		try {
//			connection = MQUtils.getConnection();
//			channel = connection.createChannel();
			
			File baseFile = new File(path);
			System.out.println(path);
			for (File subFile : baseFile.listFiles()) {
				String regEx = "[A-Z]";
				// 编译正则表达式
			    Pattern pattern = Pattern.compile(regEx);
			    Matcher matcher = pattern.matcher(subFile.getName());
			    if(!matcher.matches()) {
			    	logger.error("该文件夹为异常文件夹：" + subFile.getAbsolutePath());
			    	continue;
			    }else if (subFile.isDirectory()) {
			    	for (File cts_code_file : subFile.listFiles()) {
			    		System.out.println(cts_code_file.getName().trim());
			    		if(cts_codes != null) {
			    			if(cts_codes.contains(cts_code_file.getName().trim())) {
			    				processFile(startDate, endDate, cts_code_file, cts_code_file.getName().trim());
			    			}else {
								logger.info("该四级编码 : " + cts_code_file.getName().trim() +" 不在配置文件内");
							}
			    		}else {
			    			processFile(startDate, endDate, cts_code_file, cts_code_file.getName().trim());
						}
			    	}
				}
			}
			
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void processFile(Date startDate, Date endDate, File file, String cts_code) {
		System.out.println("++++++++++++++++++++++++++++++++++++");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHH");
		Pattern pattern = Pattern.compile("\\d{10}");
		for (File dateFile : file.listFiles()) {
			System.out.println(dateFile.getPath());
			 Matcher matcher = pattern.matcher(dateFile.getName());
			 if(matcher.matches()) {
				 try {
					Date fileDate = simpleDateFormat.parse(dateFile.getName());
					System.out.println(simpleDateFormat.format(fileDate) +"      "+ simpleDateFormat.format(startDate) + "    " + simpleDateFormat.format(endDate));
					if(fileDate.after(endDate) && fileDate.before(startDate)) {
						sendFile(dateFile, cts_code);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
			 }
		}
		
		
//		Connection connection = null;
//		Channel channel = null;
//		try {
//			connection = MQUtils.getConnection();
//			channel = connection.createChannel();
//			
//		}catch (Exception e) {
//			// TODO: handle exception
//		}
	}
	
	
	private static void sendFile(File dateFile, String cts_code) {
		for (File  file : dateFile.listFiles()) {
			System.out.println(cts_code + ":" +file.getPath());
			
		}
		
	}

	public static void main(String[] args) {
		
	   
	}
}
