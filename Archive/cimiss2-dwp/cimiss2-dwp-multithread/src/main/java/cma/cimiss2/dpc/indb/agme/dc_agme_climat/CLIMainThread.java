package cma.cimiss2.dpc.indb.agme.dc_agme_climat;
import org.cimiss2.dwp.tools.config.StartConfig;

import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.agme.DISubThread;
import cma.cimiss2.dpc.indb.common.LoopFolderThread;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * All rights Reserved, Designed By www.cma.gov.cn
 * @Title:  CLIMainThread.java   
 * @Package cma.cimiss2.dpc.indb.agme.cli   
 * @Description:    TODO(农田小气候数据处理线程  E.0023.0001.R001)   
 * @author: zuoqiang wu    
 * @date:   2019年1月16日 下午7:44:14   
 * @version V1.0 
 * @Copyright: 2019 www.cma.gov.cn Inc. All rights reserved. 
 * 注意：本内容仅限于国家气象信息中心内部传阅，禁止外泄以及用于其他的商业目
 */
public class CLIMainThread {
    // 声明一个缓存队列  存放  di
    static BlockingQueue<StatDi> diQueues = new LinkedBlockingQueue<StatDi>();
    // 声明一个缓存队列，存放轮询到的文件路径
    static BlockingQueue<String> files = new LinkedBlockingQueue<String>();
    // 默认处理线程池的大小
    private static int threadCount = 1;   // 线程池的大小

    /**
     * @Title: main   
     * @Description: TODO(主线程入口函数)   
     * @param: @param args
     * 
     * @return: void      
     * @throws
     */
    public static void main(String[] args) {
    	
    	if(args.length < 1) {
    		System.out.println("请输出程序启动参数！");
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
			
			//File file = new File("config/dpc_agme_climat.properties");
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
	    	
	        // 启动固定线程池
	        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
	        // 消息中间件
	        if(StartConfig.fileLoop() == 0) {
	        	for (int i = 0; i < threadCount; i++) {
		            executor.execute(new MultSubThread(diQueues));
		        }
	        }else if (StartConfig.fileLoop() == 1) {  // 目录轮询
	        	
	        	for (int i = 0; i < threadCount; i++) {
		            executor.execute(new FileLoopThread(files, diQueues));
		        }
	        	
	        	ExecutorService service1 = Executors.newFixedThreadPool(1);
		        service1.execute(new LoopFolderThread(files));
				
			}
	        
	        
	        // 启动固定线程池  用于发送DI
	        ExecutorService service = Executors.newFixedThreadPool(1);
	        service.execute(new DISubThread(diQueues));
    	}

			

    	

        

    }

}
