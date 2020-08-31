package cma.cimiss2.dpc.indb.surf.dc_surf_chn;

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
import cma.cimiss2.dpc.indb.surf.DISubThread;

// TODO: Auto-generated Javadoc
/**
 * All rights Reserved, Designed By www.cma.gov.cn
 *
 * @version V1.0
 * @Title:  MainThread.java
 * @Package cma.cimiss2.dpc.indb.surf.bufr
 * @Description: 国家站、区域站地面分钟、小时 4种资料解码入库
 * @author: cuihongyuan
 * @date:   2019年9月10日 上午10:11:57
 * @Copyright: 2019 www.cma.gov.cn Inc. All rights reserved.
 * 注意：本内容仅限于国家气象信息中心内部传阅，禁止外泄以及用于其他的商业目
 */
public class MainThread {
	
	static {
		BufrConfig.init();
	}
	
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
 			File file = new File(args[1].trim()); //config/bufr_hor_config.properties
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
	    	TimingMapInfo.RefreshMapInfo();//定时重新加载stationInfo_Config.lua文件
			// 启动固定线程池
			ExecutorService executor = Executors.newFixedThreadPool(threadCount);
			  // 消息中间件
	     	if(StartConfig.fileLoop() == 0) {
				for (int i = 0; i < threadCount; i++) {
					executor.execute(new SubThread(cts_code, diQueues)); //可以处理两种资料：A.0001.0018.R001和A.0001.0021.R001
				}
	     	}
	     	else if(StartConfig.fileLoop() == 1){
	     		// 二进制流格式，没有目录轮询方式
	     		for(int i = 0; i < threadCount; i ++){
	     			executor.execute(new FileLoop(StartConfig.ctsCode(), files, diQueues));
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
