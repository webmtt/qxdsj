package cma.cimiss2.dpc.indb.upar.dc_bufr_airplane_report;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.TimingMapInfo;

import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.common.BufrConfig;
import cma.cimiss2.dpc.indb.common.LoopFolderThread;
import cma.cimiss2.dpc.indb.upar.DISubThread;
/**
 * All rights Reserved, Designed By www.cma.gov.cn
 * @Title:  UparArdMainThread.java   
 * @Package cma.cimiss2.dpc.indb.upar.bufr_ard1   
 * @Description:    TODO(飞机高空探测报告(IUA) （BUFR格式）	大数据平台	B.0001.0020.R001)   
 * @author: zuoqiang wu    
 * @date:   2019年1月17日 上午10:10:25   
 * @version V1.0 
 * @Copyright: 2019 www.cma.gov.cn Inc. All rights reserved. 
 * 注意：本内容仅限于国家气象信息中心内部传阅，禁止外泄以及用于其他的商业目
 */
public class UparArdMainThread {
	static {
		BufrConfig.init();
	}
	
	static BlockingQueue<StatDi> diQueues = new LinkedBlockingQueue<StatDi>();
    private static int threadCount = 1;   // 线程池的大小
    // 声明一个缓存队列，存放轮询到的文件路径
    static BlockingQueue<String> files = new LinkedBlockingQueue<String>();
	
	public static void main(String[] args) {
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
 			File file = new File(args[1].trim()); //config/bufr/C.0001.0014.R001_config.properties
			if(!file.exists() || !file.isFile()) {
				System.out.println("config file not fond :" +file.getPath());
				return;
			}
			StartConfig.setConfigFile(file.getPath());
	    	threadCount = StartConfig.getThreadCount();
	    	String cts_code = StartConfig.ctsCode();
	    	try {
				ServerSocket server = new ServerSocket(StartConfig.runport);
			} catch (IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				return;
			}
	    	TimingMapInfo.RefreshMapInfo();//定时重新加载StationInfo_Config.lua文件
	    	
			// 启动固定线程池
			ExecutorService executor = Executors.newFixedThreadPool(threadCount);
			  // 消息中间件
	     	if(StartConfig.fileLoop() == 0) {
				for (int i = 0; i < threadCount; i++) {
					executor.execute(new SubThread(cts_code, diQueues)); //可以处理的资料:B.0001.0020.R001飞机高空探测报告   
				}
	     	}
	     	else if(StartConfig.fileLoop() == 1){
	     		for(int i = 0; i < threadCount; i ++){
	     			executor.execute(new FileLoopThread(cts_code, files,diQueues)); 
	     		}
	     		ExecutorService service1 = Executors.newFixedThreadPool(1);
		        service1.execute(new LoopFolderThread(files));
	     	}
	     	// 启动固定线程池
	        ExecutorService service = Executors.newCachedThreadPool();
	        service.execute(new DISubThread(diQueues));
	        
    	}
	}


}
