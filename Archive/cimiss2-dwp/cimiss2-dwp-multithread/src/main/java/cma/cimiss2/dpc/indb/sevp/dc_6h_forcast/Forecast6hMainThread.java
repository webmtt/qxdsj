package cma.cimiss2.dpc.indb.sevp.dc_6h_forcast;

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
import cma.cimiss2.dpc.indb.sevp.DISubThread;
import cma.cimiss2.dpc.indb.common.LoopFolderThread;
/**
 * 
 * <br>
 * @Title:  Forecast6hMainThread.java
 * @Package cma.cimiss2.dpc.indb.sevp.forecast6h
 * @Description:    TODO(大城市逐6小时精细化气象资料主线程)
 *          大城市逐6小时精细化气象要素指导预报产品	大数据平台	M.0032.0001.R001
	大城市逐6小时精细化气象要素预报产品－各省的订正预报产品	大数据平台	M.0032.0002.R001
	大城市逐6小时精细化气象要素预报产品－全国共享预报产品	大数据平台	M.0032.0003.R001
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月8日 上午10:17:03   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */
public class Forecast6hMainThread {
    // 声明一个缓存队列
    static BlockingQueue<StatDi> diQueues = new LinkedBlockingQueue<StatDi>();
    private static int threadCount = 1;
    // 声明一个缓存队列，存放轮询到的文件路径
    static BlockingQueue<String> files = new LinkedBlockingQueue<String>();
    /**
     * 
     * @Title: main   
     * @Description: TODO(主线程入口函数)   
     * @param: @param args
     * @return: void      
     * @throws
     * 
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
 			//File file = new File("config/sevp_fi6h_config.properties");
 			File file = new File(args[1].trim()); 
			if(!file.exists() || !file.isFile()) {
				System.out.println("config file not fond :" +file.getPath());
				return;
			}
			StartConfig.setConfigFile(file.getPath());
	    	threadCount = StartConfig.getThreadCount();
	    	  // 资料的CTS CODE
	    	String cts_code = "";
 			if(args.length == 3){
 				cts_code = args[2].trim();
 				System.out.println("第三个参数为资料CTS编码: " + cts_code);
 			}
 			else{
 				cts_code = StartConfig.ctsCodes()[0];
 			}
 			
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
		            executor.execute(new Forecast6hMultSubThread(diQueues));
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
