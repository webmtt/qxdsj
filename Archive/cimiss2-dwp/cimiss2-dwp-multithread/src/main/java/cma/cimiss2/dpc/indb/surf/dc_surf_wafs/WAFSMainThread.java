package cma.cimiss2.dpc.indb.surf.dc_surf_wafs;

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
import cma.cimiss2.dpc.indb.agme.DISubThread;
import cma.cimiss2.dpc.indb.common.LoopFolderThread;
// TODO: Auto-generated Javadoc

/**
 * <br>.
 *
 * @author cuihongyuan
 * @Title:  WAFSMainThread.java
 * @Package org.cimiss2.dwp.z_surf_wafs
 * @Description:    TODO(飞机地面天气报解码入库主线程)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月2日 下午4:36:06   cuihongyuan    Initial creation.
 * </pre>
 */
public class WAFSMainThread {
    
    /** The di queues. */
    static BlockingQueue<StatDi> diQueues = new LinkedBlockingQueue<StatDi>();
    
    /** The thread count. */
    private static int threadCount = 1;   // 线程池的大小
    
    /** The files. */
    // 声明一个缓存队列，存放轮询到的文件路径
    static BlockingQueue<String> files = new LinkedBlockingQueue<String>();
 
 /**
  * The main method.
  *
  * @param args the arguments
  * @Title: main
  * @Description: TODO(主线程入口)
  * @param: @param args
  * @return: void
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
 			//File file = new File("config/surf_wafs_config.properties");
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
	    	
	    	ExecutorService executor = Executors.newFixedThreadPool(threadCount);
	    	 // 消息中间件
	        if(StartConfig.fileLoop() == 0) {
		        for (int i = 0; i < threadCount; i++) {
		            executor.execute(new WAFSMultSubThread(diQueues));
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
