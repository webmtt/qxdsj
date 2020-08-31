package cma.cimiss2.dpc.indb.ocen.dc_ocean_hyj;


import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.cimiss2.dwp.tools.config.StartConfig;

import cma.cimiss2.dpc.decoder.agme.ParseConfig;
import cma.cimiss2.dpc.decoder.agme.PropertiesUtil;
import cma.cimiss2.dpc.decoder.ocen.OcenParseConfig;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.ocen.DISubThread;
import cma.cimiss2.dpc.indb.common.LoopFolderThread;
import cma.cimiss2.dpc.indb.ocen.dc_ocean_hyj.FileLoopThread;

/**
 * <br>
 * @Title:  BuoyMainThread.java   
 * @Package cma.cimiss2.dpc.indb.ocen.buoy   
 * @Description:(中国浮标站观测资料入库主函数)
 * 国内浮标站观测资料	大数据平台	C.0001.0005.R001
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年2月13日 下午12:52:37   cuihongyuan    Initial creation.
 * </pre>
 * @author cuihongyuan
 */
public class MainThread {
	private static int threadCount = 1;   // 线程池的大小
    static BlockingQueue<StatDi> diQueues = new LinkedBlockingQueue<StatDi>();
 // 声明一个缓存队列，存放轮询到的文件路径
    static BlockingQueue<String> files = new LinkedBlockingQueue<String>();
    /**
     * 
     * @Title: main   
     * @Description:(主线程入口函数)   
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
 			//File file = new File("config/dpc_ocen_sea_buoy.properties");
			File file = new File(args[1].trim()); 
			if(!file.exists() || !file.isFile()) {
				System.out.println("config file not fond :" +file.getPath());
				return;
			}
			if(args.length < 4){
				System.out.println("输入资料基本信息文件，以及表格配置文件！");
				return;
			}
			String dataProp = args[2].trim();
			String tableProp = args[3].trim();
			
			PropertiesUtil.setConfigFile(dataProp);
			OcenParseConfig.config = tableProp;
			
			StartConfig.setConfigFile(file.getPath());
	    	threadCount = StartConfig.getThreadCount();
	    	try {
				ServerSocket server = new ServerSocket(StartConfig.runport);
			} catch (IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				return;
			}
	        // 启动固定线程池
	        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
	        // 消息中间件
	     	if(StartConfig.fileLoop() == 0) {
		        for (int i = 0; i < threadCount; i++) {
		            executor.execute(new MultSubThread(diQueues));
		        }
	     	}
	    	else if (StartConfig.fileLoop() == 1) {  // 目录轮询
	     		for (int i = 0; i < threadCount; i++) {
	        		executor.execute(new FileLoopThread(files, diQueues));
		        }
	        	ExecutorService service1 = Executors.newFixedThreadPool(1);
		        service1.execute(new LoopFolderThread(files));
	     	}

	        ExecutorService service = Executors.newCachedThreadPool();
	        service.execute(new DISubThread(diQueues));
    	}
    }
}
