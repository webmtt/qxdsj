package cma.cimiss2.dpc.indb.cawn.dc_cawn_pm25;


import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.TimingMapInfo;

import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.cawn.DISubThread;
import cma.cimiss2.dpc.indb.common.LoopFolderThread;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
/**
 * 
 * <br>
 * @Title:  PM25MainThread.java
 * @Package cma.cimiss2.dpc.indb.cawn.pm25
 * @Description:(大气成分气溶胶PM25质量浓度（PM25）资料入库主线程)
 * 大气成分	大气成分气溶胶PM25质量浓度（PM25）	大数据平台	G.0002.0007.R001

 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年4月10日 下午2:13:59   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */
public class PM25MainThread {
	private static int threadCount  =1;
    static BlockingQueue<StatDi> diQueues = new LinkedBlockingQueue<StatDi>();
    // 声明一个缓存队列，存放轮询到的文件路径
    static BlockingQueue<String> files = new LinkedBlockingQueue<String>();
    public static void main(String[] args) throws SQLException, InterruptedException {
        
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
 			//File file = new File("config/cawn_pm25_config.properties");
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
	    	TimingMapInfo.RefreshMapInfo();//定时重新加载stationInfo_Config.lua文件
	        // 启动固定线程池
	        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
	        // 消息中间件
	     	if(StartConfig.fileLoop() == 0) {
		        for (int i = 0; i < threadCount; i++) {
		            executor.execute(new PM25MultSubThread(diQueues));
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
	        ExecutorService service = Executors.newFixedThreadPool(1);
	        service.execute(new DISubThread(diQueues));
     	}
    }
}
