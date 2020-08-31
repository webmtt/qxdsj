package cma.cimiss2.dpc.indb.bufrsate.insat;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.cimiss2.dwp.tools.config.StartConfig;

import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.common.BufrConfig;
import cma.cimiss2.dpc.indb.common.LoopFolderThread2;

import cma.cimiss2.dpc.indb.DISubThread_insat;

/**
 * @Title:  InsatMainThread.java   
 * @Package cma.cimiss2.dpc.indb.bufrsate.insat   
 * @Description:    TODO(卫星云导风资料数据（BUFR格式）	大数据平台)   
 * @author: liyamin   
 * @date:   2019年1月17日 上午11:42  
 * @version V1.0 
 * 注意：本内容仅限于国家气象信息中心内部传阅，禁止外泄以及用于其他的商业目
 */
public class InsatMainThread {
	static {
		BufrConfig.init();
	}
	
	static BlockingQueue<StatDi> diQueues = new LinkedBlockingQueue<StatDi>();
    private static int threadCount = 1;   // 线程池的大小
    // 声明一个缓存队列，存放轮询到的文件路径
    static BlockingQueue<String> files = new LinkedBlockingQueue<String>();
    static long loopEndTime=0;
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
			// 启动固定线程池
			ExecutorService executor = Executors.newFixedThreadPool(threadCount);
			  // 消息中间件
	     	if(StartConfig.fileLoop() == 0) {
				for (int i = 0; i < threadCount; i++) {
					executor.execute(new SubThread(cts_code, diQueues)); //可以处理的资料:云导风K.0536.0001.R001/K.0537.0001.R001/K.0362.0001.R001/K.0466.0001.R001/K.0505.0001.R001/K.0340.0001.R001/K.0341.0001.R001/K.0342.0001.R001        
				}
	     	}
	     	else if(StartConfig.fileLoop() == 1){
	     		for(int i = 0; i < threadCount; i ++){
	     			executor.execute(new FileLoopThread(cts_code, files,diQueues));
	     		}
	     		ExecutorService service1 = Executors.newFixedThreadPool(1);
		        service1.execute(new LoopFolderThread2(files));
		        
		        if(StartConfig.getLoopQuitFlag()==true){
		        	executor.shutdown();
			        service1.shutdown();
			        loopEndTime=new Date().getTime();
		        }
	     	}
	     	// 启动固定线程池
	        ExecutorService service = Executors.newCachedThreadPool();
	        service.execute(new DISubThread_insat(diQueues,loopEndTime));
	        if(StartConfig.fileLoop() == 1){
	        	service.shutdown();
	        }

    	}
	}


}
