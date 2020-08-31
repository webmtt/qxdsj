package cma.cimiss2.dpc.indb.sevp.dc_fine_forcast;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.cimiss2.dwp.tools.config.*;
import org.cimiss2.dwp.tools.utils.TimingMapInfo;

import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.common.LoopFolderThread;
import cma.cimiss2.dpc.indb.sevp.DISubThread;
/**
 * 
 * <br>
 * @Title:  fineforecastMainThread.java
 * @Package cma.cimiss2.dpc.indb.sevp.fineforecast
 * @Description:    TODO(全国城镇精细化预报资料主线程)
 * 全国城镇精细化预报指导预报产品	大数据平台	M.0012.0001.R001
         全国城镇精细化预报产品－各省的订正预报产品	大数据平台	M.0012.0002.R001
         全国城镇精细化预报产品－全国共享预报产品	大数据平台	M.0012.0003.R001
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月4日 下午10:25:09   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */

public class FineForecastMainThread {
	private static int threadCount = 1;   // 线程池的大小
    static BlockingQueue<StatDi> diQueues = new LinkedBlockingQueue<StatDi>();
    // 声明一个缓存队列，存放轮询到的文件路径
    static BlockingQueue<String> files = new LinkedBlockingQueue<String>();
    /**
     * 
     * @Title: main   
     * @Description: TODO(主线程入口函数)   
     * @param: @param args
     * @return: void      
     * @throws
     */
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
 			
 			//File file = new File("config/sevp_fine_config.properties");
 			File file = new File(args[1].trim()); 
			if(!file.exists() || !file.isFile()) {
				System.out.println("config file not fond :" +file.getPath());
				return;
			}
			StartConfig.setConfigFile(file.getPath());
	    	threadCount = StartConfig.getThreadCount();
	    	try {
				ServerSocket server = new ServerSocket(StartConfig.runport);
			} catch (IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				return;
			}
	    	TimingMapInfo.RefreshMapInfo();//定时重新加载StationInfo_Config.lua文件
	    	
	    	  // 资料的CTS CODE
	    	String cts_code = "";
 			if(args.length == 3){
 				cts_code = args[2].trim();
 				System.out.println("第三个参数为资料CTS编码: " + cts_code);
 			}
 			else{
 				cts_code = StartConfig.ctsCodes()[0];
 			}
 			
	    	// 启动固定线程池
	        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
	        // 消息中间件
	     	if(StartConfig.fileLoop() == 0) {
		        for (int i = 0; i < threadCount; i++) {
		            executor.execute(new FineForecastMultSubThread(diQueues));
		        }
	     	}
	     	else if (StartConfig.fileLoop() == 1) {  // 目录轮询
	     		for (int i = 0; i < threadCount; i++) {
	        		executor.execute(new FileLoopThread(files, diQueues, cts_code));
		        }
	        	ExecutorService service1 = Executors.newFixedThreadPool(1);
		        service1.execute(new LoopFolderThread(files));
	     	}
	     	 // 启动固定线程池
	        ExecutorService service = Executors.newFixedThreadPool(1);
	        service.execute(new DISubThread(diQueues));
     	}
    }
}
