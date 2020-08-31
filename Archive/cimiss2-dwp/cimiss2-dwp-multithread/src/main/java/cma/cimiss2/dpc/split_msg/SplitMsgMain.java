package cma.cimiss2.dpc.split_msg;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.cimiss2.dwp.tools.config.StartConfig;
import org.cmadaas.ats.core.ATSClient;

public class SplitMsgMain {

	public static void main(String[] args) {
//		ATSClient client = new ATSClient("config/");
		
		String filenameString = "W_NAFP_C_ECMF_20200309180049_P_D1D03091200031021001.bz2";
		System.out.println(filenameString.split("[.]")[0]);
		if(args.length < 1) {
    		System.out.println("请输出程序启动端口号！");
    		return;
    	}
		
		try {
			StartConfig.runport = Integer.parseInt(args[0].trim());
		} catch (Exception e) {
			System.out.println("第一个参数为程序端口号，请输入正确的端口号");
			e.printStackTrace();
			return;
		}
		
		if(args.length < 2) {
			System.out.println("第二个参数为配置文件路径，请输入");
			return;
		}
		File file = new File(args[1].trim()); 
		if(!file.exists() || !file.isFile()) {
			System.out.println("config file not fond :" +file.getPath());
			return;
		}
		
		
		// 启动固定线程池
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.execute(new SplitMsgThread(args[1].trim()));
	}

}
