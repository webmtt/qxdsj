package cma.cimiss2.dpc.indb.cawn.dc_cawn_aap;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.TimingMapInfo;

import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.cawn.DISubThread;
import cma.cimiss2.dpc.indb.common.LoopFolderThread;

/**
 * 
 * <br>
 * 
 * @Title: AAPMainThread.java
 * @Package cma.cimiss2.dpc.indb.cawn.aap
 * @Description: TODO(黑碳气溶胶数据数据处理主线程)
 * 大气成分	大气成分黑碳资料	大数据平台	G.0002.0001.R001
 *               <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年6月1日 上午9:44:23   cuihongyuan    Initial creation.
 *               </pre>
 * 
 * @author cuihongyuan
 *
 *
 */
public class AAPMainThread {
	static BlockingQueue<StatDi> diQueues = new LinkedBlockingQueue<StatDi>();
	  // 声明一个缓存队列，存放轮询到的文件路径
    static BlockingQueue<String> files = new LinkedBlockingQueue<String>();
    // 默认处理线程池的大小
    private static int threadCount = 1;   // 线程池的大小
	public static void main(String[] args) throws SQLException, InterruptedException {
		if (args.length < 1) {
			System.out.println("请输出程序启动端口号！");
			return;
		} else {
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
			//File file = new File("config/cawn_bc_config.properties");
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
			ExecutorService executor = Executors.newFixedThreadPool(threadCount);
			if(StartConfig.fileLoop() == 0) { // 资料处理方式：消息队列
				for (int i = 0; i < threadCount; i++) { // threadCount：启动线程数量
					executor.execute(new AAPMultSubThread(diQueues)); // 消息队列方式解码入库线程, diQueues：DI队列
				}
			}
			else if (StartConfig.fileLoop() == 1) {  // 资料处理方式：目录轮询
	        	for (int i = 0; i < threadCount; i++) {
		            executor.execute(new FileLoopThread(files, diQueues));//目录轮询方式解码入库线程,diQueues：DI队列
		        }
	        	ExecutorService service1 = Executors.newFixedThreadPool(1); 
		        service1.execute(new LoopFolderThread(files));  //轮询目录,存放轮询到的文件路径到缓存队列files
			}
			// 启动固定线程池
			ExecutorService service = Executors.newFixedThreadPool(1); // 发送DI的线程数量1
			service.execute(new DISubThread(diQueues)); // 发送DI的线程
		}
		
	}
}
