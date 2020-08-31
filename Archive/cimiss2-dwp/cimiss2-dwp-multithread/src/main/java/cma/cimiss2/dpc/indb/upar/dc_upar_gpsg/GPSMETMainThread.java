package cma.cimiss2.dpc.indb.upar.dc_upar_gpsg;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.cimiss2.dwp.tools.config.StartConfig;

import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.upar.DISubThread;
import cma.cimiss2.dpc.indb.common.LoopFolderThread;
/**
 * 
 * <br>
 * @Title:  GPSMETlMainThread.java   
 * @Package cma.cimiss2.dpc.indb.upar.gps_met   
 * @Description:(GPS\MET水汽数据处理主线程)
 * GPS/MET水汽数据产品	大数据平台	B.0005.0005.R001
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年1月22日 下午2:17:19   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 *
 *
 */
public class GPSMETMainThread {
	private static int threadCount = 1;
    static BlockingQueue<StatDi> diQueues = new LinkedBlockingQueue<StatDi>();
    // 声明一个缓存队列，存放轮询到的文件路径
    static BlockingQueue<String> files = new LinkedBlockingQueue<String>();
    /**
     * 
     * @Title: main   
     * @Description: (主线程入口函数)   
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
 			//File file = new File("config/upar_gpsg_config.properties");
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
		            executor.execute(new GPSMETMultSubThread(diQueues));
		        }
	        }
	        else if (StartConfig.fileLoop() == 1) {  // 目录轮询
	     		for (int i = 0; i < threadCount; i++) {
	        		executor.execute(new FileLoopThread(files, diQueues));
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
