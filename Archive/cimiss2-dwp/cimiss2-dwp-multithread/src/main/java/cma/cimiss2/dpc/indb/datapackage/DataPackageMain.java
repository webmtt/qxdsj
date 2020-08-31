package cma.cimiss2.dpc.indb.datapackage;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cimiss2.dwp.tools.config.StartConfig;

/**
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre
 * (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * <b>Description:</b><br>
 * 
 * @author wuzuoqiang
 * @version 1.0
 * @Note <b>ProjectName:</b> cimiss2-dwp-multithread <br>
 *       <b>PackageName:</b> cma.cimiss2.dpc.indb.datapackage <br>
 *       <b>ClassName: 归档回取数据主流程</b> DataPackageMain <br>
 *       <b>Date:</b> 2019年5月8日 下午3:17:09
 */
public class DataPackageMain {
	
	/**
	 * 归档数据处理的主函数
	 * @param args
	 */
	public static void main(String[] args) {
		String regxString = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$";
		// 编译正则表达式
	    Pattern pattern = Pattern.compile(regxString);
	    Matcher matcher = pattern.matcher("2091-05-22 01:00:00");
		System.out.println(matcher.matches());
		if(args.length < 1) {
    		System.out.println("请输出程序启动端口号！");
    		return;
    	}else {
			try {
				StartConfig.runport = Integer.parseInt(args[0].trim());
			} catch (Exception e) {
				System.out.println("第一个参数为程序端口号，请输入正确的端口号");
				e.printStackTrace();
				return;
			}
			if(args.length <2) {
				System.out.println("第二个参数为配置文件路径，请输入");
				return;
			}
			//File file = new File("config/agme_asmm_config.properties");
			File file = new File(args[1].trim()); 
			if(!file.exists() || !file.isFile()) {
				System.out.println("config file not fond :" +file.getPath());
				return;
			}
			StartConfig.setConfigFile(file.getPath());
			
			DataPackageServer.start();
    	}
		
	}

}
