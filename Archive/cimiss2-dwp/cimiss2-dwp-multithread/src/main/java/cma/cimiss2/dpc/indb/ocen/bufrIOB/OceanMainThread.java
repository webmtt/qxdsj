package cma.cimiss2.dpc.indb.ocen.bufrIOB;

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
import cma.cimiss2.dpc.indb.ocen.DISubThread;
// TODO: Auto-generated Javadoc

/**
 * All rights Reserved, Designed By www.cma.gov.cn
 *
 * @version V1.0
 * @Title:  OceanMainThread.java
 * @Package cma.cimiss2.dpc.indb.ocen.bufr
 * @Description: 海洋测站的地面观测数据（ISS）	大数据平台	A.0001.0021.R001
 * 浮标的海面以下观测报告-TESAC（IOP）（BUFR格式）	大数据平台	C.0001.0012.R001
 * 海面观测报告-TRACKOB（IOR）（BUFR格式）	大数据平台	C.0001.0013.R001
 * 海平面及其以下观测报告-BATHY/TESAC（IOS）（BUFR格式）	大数据平台	C.0001.0014.R001
 * 海洋船舶浮标观测报告（IOB）（BUFR格式）	大数据平台	C.0001.0018.R001   
 * @author: zuoqiang wu
 * @date:   2019年1月17日 上午10:12:34
 * @Copyright: 2019 www.cma.gov.cn Inc. All rights reserved.
 * 注意：本内容仅限于国家气象信息中心内部传阅，禁止外泄以及用于其他的商业目
 */
public class OceanMainThread {
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
 			File file = new File(args[1].trim()); // config/dpc_bufr_global_ocean_iob.properties
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
					executor.execute(new SubThread(cts_code, diQueues)); //可以处理的资料包括4种：C.0001.0012.R001\C.0001.0013.R001\C.0001.0014.R001\C.0001.0018.R001
				}
	     	}
	     	else if(StartConfig.fileLoop() == 1){
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
