package org.cimiss2.dwp.Grib;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <br>
 * @Title:  GribMainThread.java   
 * @Package org.cimiss2.dwp.GRIB   
 * @Description:    扫目录导模式数据(数值模式数据处理线程)
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
public class GribMainThread2 {

	//private static final Logger logger = LoggerFactory.getLogger(GribMainThread2.class);
	protected static final Logger LOGGER = LoggerFactory.getLogger("gribLoggerInfo");
	protected static final Logger LOGGER_E = LoggerFactory.getLogger("gribErrorInfo");

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
		int count = 1;
		if (args.length != 3) {
			LOGGER.info("参数不对，应带3个参数，第1个是scan_dir，第2个是数据目录，第3个是四级编码");
			System.out.println("参数不对，应带3个参数，第1个是scan_dir，第2个是数据目录，第3个是四级编码");

			//LOGGER_E.error("参数不对，应带2个参数，第1个是读取消息的线程数，第2个是资料类别标识");

			return;
		}
		String data_identify = "";
		String data_dir = "";
		String data_id = "";
		if (args.length == 3) {
			// 
			//count = Integer.parseInt(args[0]);
			data_dir = args[1];
			data_id = args[2];
		}
		Long t1 = System.currentTimeMillis();
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
		String startTime = df.format(date);

		LOGGER.info("开始运行时间：" + startTime);
		System.out.println("开始运行时间：" + startTime);
		//logger.info("开始运行时间：" + startTime);

		
		//启动单独线程：拆分，入索引表   
		//ExecutorService executor = Executors.newFixedThreadPool(1);
		System.out.println("data_dir:" + data_dir + ",data_id:" + data_id);
		LOGGER.info("data_dir:" + data_dir + ",data_id:" + data_id);
		ProcessMessageToIndexDbThread2 processMessageThread = new ProcessMessageToIndexDbThread2(data_dir,data_id);
		//threadPool.execute(processMessageThread);
		processMessageThread.processs(data_dir, data_id);
	
		
		/*
		 * while (true){
		 * 
		 * }
		 */
		Thread.sleep(1000);
				
		Long t2 = System.currentTimeMillis();
		System.out.println("All is finished! 耗时：" + (t2 - t1));

	}
	
	
	

}
