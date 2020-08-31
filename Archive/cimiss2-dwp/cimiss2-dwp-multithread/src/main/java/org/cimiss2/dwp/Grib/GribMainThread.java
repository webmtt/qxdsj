package org.cimiss2.dwp.Grib;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.cimiss2.dwp.tools.config.StartConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.grib.GribConfig;
import cma.cimiss2.dpc.decoder.grib.GribConfigAreaMapping;
import cma.cimiss2.dpc.decoder.grib.GribM4Config;
import cma.cimiss2.dpc.indb.common.LoopFolderThread;
import cma.cimiss2.dpc.indb.general.common.IndexConf;

/**
 * 
 * <br>
 * @Title:  GribMainThread.java   
 * @Package org.cimiss2.dwp.GRIB   
 * @Description:    TODO(数值模式数据处理线程)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月18日 上午00:00:07   zhengbo    Initial creation.
 * </pre>
 * 
 * @author zhengbo
 *
 *
 */
public class GribMainThread {
	public static HashMap<String, Integer> retryMap = new HashMap<String, Integer>();

	// 声明一个缓存队列，存放轮询到的文件路径
    static BlockingQueue<String> files = new LinkedBlockingQueue<String>();
	/**
	 * 
	 * @Title: main   
	 * @Description: TODO(主线程入口函数)   
	 * @param: @param args
	 * @param: @throws SQLException
	 * @param: @throws InterruptedException      
	 * @return: void      
	 * @throws
	 */
	public static void main(String[] args) throws SQLException, InterruptedException {
		
		//如果是scan_dir开头的参数，是数据目录扫描，导入历史数据的方式		
		if(args.length == 3&&args[0].equalsIgnoreCase("scan_dir")) 
		{
			System.out.println("带3个参数，第1个是scan_dir，第2个是数据目录，第3个是四级编码");
			
			String data_dir = args[1];
			String data_id = args[2];
			
			Long t1 = System.currentTimeMillis();
			Date date = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
			String startTime = df.format(date);

			System.out.println("开始运行时间：" + startTime);
			System.out.println("开始运行时间：" + startTime);
			
			//启动单独线程：拆分，入索引表   
			//ExecutorService executor = Executors.newFixedThreadPool(1);
			System.out.println("data_dir:" + data_dir + ",data_id:" + data_id);
			System.out.println("data_dir:" + data_dir + ",data_id:" + data_id);
			ProcessMessageToIndexDbThread2 processMessageThread = new ProcessMessageToIndexDbThread2(data_dir,data_id);
			//threadPool.execute(processMessageThread);
			processMessageThread.processs(data_dir, data_id);
							
			Long t2 = System.currentTimeMillis();
			System.out.println("All is finished! 耗时：" + (t2 - t1));
			System.out.println("All is finished! 耗时：" + (t2 - t1));
			
			return;
		}
		else if(args.length == 3&&args[0].equalsIgnoreCase("scan_field_dir")) //扫描模式场文件目录：scan_field_dir
		{
			System.out.println("带3个参数，第1个是scan_field_dir，第2个是数据目录，第3个是四级编码");
			
			String data_dir = args[1];
			String data_id = args[2];
			
			Long t1 = System.currentTimeMillis();
			Date date = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
			String startTime = df.format(date);

			System.out.println("开始运行时间：" + startTime);
			System.out.println("开始运行时间：" + startTime);
			
			//启动单独线程：拆分，入索引表   
			//ExecutorService executor = Executors.newFixedThreadPool(1);
			System.out.println("data_dir:" + data_dir + ",data_id:" + data_id);
			System.out.println("data_dir:" + data_dir + ",data_id:" + data_id);
			ProcessMessageToIndexDbThread3 processMessageThread = new ProcessMessageToIndexDbThread3(data_dir,data_id);
			//threadPool.execute(processMessageThread);
			processMessageThread.processs(data_dir, data_id);
							
			Long t2 = System.currentTimeMillis();
			System.out.println("All is finished! 耗时：" + (t2 - t1));
			System.out.println("All is finished! 耗时：" + (t2 - t1));
			
			return;
		}
		
		if (args.length != 3) 
		{
			System.out.println("参数不对，应带3个参数，第1个是读取消息的线程数，第2个是资料类别标识，第3个参数是端口号...");
			
			return;
		}
		
		
		
		int count = Integer.parseInt(args[0]); // 设置线程池大小
		String data_identify = args[1];
		int port = Integer.parseInt(args[2]); //运行端口号
		
			
		
		System.out.println("线程数为： " + count);
		
		Long t1 = System.currentTimeMillis();
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
		String startTime = df.format(date);
		
		System.out.println("开始运行时间：" + startTime);
		
		//logger.info("开始运行时间：" + startTime);

		// 配置文件
		String config_filename = "config/" + "grib_" + data_identify + "_config.properties";
		File file = new File(config_filename);
		if (!file.exists() || !file.isFile()) {
			System.out.println("config file not fond :" + file.getPath());
			return;
		}
		System.out.println("file.getPath():" + file.getPath());
		StartConfig.setConfigFile(file.getPath());

		// 启动固定线程池

		System.out.println("线程池大小：" + count);
		
		System.out.println("StartConfig.fileLoop()=" + StartConfig.fileLoop());

		//读取相关配置
		GribConfig.readConfig(); //读取模式资料配置
		
		GribConfigAreaMapping.readConfig();//modify,2019.5.29：模式区域映射配置
		
		GribM4Config.readConfig(); //add,2020.4.19,读取云平台与M4的要素映射		

		ExecutorService executor = Executors.newFixedThreadPool(count);
		if (StartConfig.fileLoop() == 0) {
			System.out.println("启动子线程...");
			for (int i = 0; i < count; i++) {
				executor.execute(new MultSubThread(data_identify));
			}
		}else if (StartConfig.fileLoop() == 1) {  // 目录轮询
			boolean readConfig = IndexConf.ReadConfig("config/index.txt");
			if(!readConfig) {

				System.out.println("加载配置文件index.txt失败");
				System.exit(0);
			}
        	for (int i = 0; i < count; i++) {
	            executor.execute(new FileLoopThread(files, data_identify));
	        }
        	
        	ExecutorService service1 = Executors.newFixedThreadPool(1);
	        service1.execute(new LoopFolderThread(files));
			
		}

		Long t2 = System.currentTimeMillis();
		System.out.println("All is finished! 耗时：" + (t2 - t1));
		
		//启动该端口号
		try 
		{
			System.out.println("占用端口号： " + port);
			ServerSocket server = new ServerSocket(port);
			while(true)
			{
				Socket sock = server.accept();
				System.out.println("进行监听： ");
				//server.bind
				//server.accept();
				Thread.sleep(1000); 
			}
			
		} 
		catch (IOException e) 
		{
			System.out.println(e.getMessage());
			e.printStackTrace();			
			System.exit(1);
			return;
		}	
		
		/*
		while(true) //构造死循环，不退出主线程
		{
			System.out.println("sleep...");
			Thread.sleep(60*60*1000); 
		}
		*/
	}

}
