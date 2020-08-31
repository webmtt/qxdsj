package cma.cimiss2.dpc.indb.upar.dc_upar_flsh;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.cimiss2.dwp.tools.config.StartConfig;

import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.upar.DISubThread;
import cma.cimiss2.dpc.indb.common.LoopFolderThread;


/**
 * -------------------------------------------------------------------------------
 * <br>
 * @Title:  LDPMainThread.java   
 * @Package cma.cimiss2.dpc.indb.upar.light_locat   
 * @Description:(ADTD系统雷电定位数据文件)
 * @资料四级编码：     B.0006.0004.R001
 * @入库表名：             upar_adtd_chn_tab
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月26日 下午5:48:32   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *---------------------------------------------------------------------------------
 */
public class LDPMainThread {
    private static BlockingQueue<StatDi> diQueues = new LinkedBlockingQueue<>();
    private static int threadCount = 1;   // 线程池的大小
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
 			//File file = new File("config/upar_flsh_config.properties");
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
	        // 发送DI线程
	        ExecutorService service = Executors.newFixedThreadPool(1);
	        service.execute(new DISubThread(diQueues));
    	}
    }
}
