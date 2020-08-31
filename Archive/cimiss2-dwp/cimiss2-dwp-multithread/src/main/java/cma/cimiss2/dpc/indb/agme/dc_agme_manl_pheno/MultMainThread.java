package cma.cimiss2.dpc.indb.agme.dc_agme_manl_pheno;


import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.cimiss2.dwp.tools.config.CTSCodeMap;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.TimingMapInfo;

import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.agme.DISubThread;
import cma.cimiss2.dpc.indb.common.LoopFolderThread;

/**
 * All rights Reserved, Designed By www.cma.gov.cn
 * @Title:  MultMainThread.java   
 * @Package cma.cimiss2.dpc.indb.agme.pheno   
 * @Description:    TODO(农业气象自然物候要素数据	大数据平台	E.0003.0003.R001)   
 * 	农业气象自然物侯要素—木本植物物候期
	农业气象自然物侯要素—草本植物物候期
	农业气象自然物侯要素—气象水文现象
	农业气象自然物侯要素—动物物候期
 * @author: zuoqiang wu    
 * @date:   2019年1月16日 下午7:49:37   
 * @version V1.0 
 * @Copyright: 2019 www.cma.gov.cn Inc. All rights reserved. 
 * 注意：本内容仅限于国家气象信息中心内部传阅，禁止外泄以及用于其他的商业目
 */
public class MultMainThread {
    static BlockingQueue<StatDi> diQueues = new LinkedBlockingQueue<StatDi>();
    private static int threadCount = 1;
    // 声明一个缓存队列，存放轮询到的文件路径
    static BlockingQueue<String> files = new LinkedBlockingQueue<String>();
    public static ArrayList<CTSCodeMap> ctsCodeMaps = new ArrayList<>();
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
			//File file = new File("config/dpc_agme_pheno_manl.properties");
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
	    	String ctS = StartConfig.ctsCode();
			String sodSs[] = StartConfig.sodCodes();
			String reportSods[] = StartConfig.reportSodCodes();
			String valuetables[] = StartConfig.valueTables();
			String reportTable = StartConfig.reportTable();
			for(int i = 0; i < valuetables.length; i ++){
				CTSCodeMap codeMap = new CTSCodeMap();
				codeMap.setCts_code(ctS);
				codeMap.setSod_code(sodSs[i]);
				codeMap.setReport_sod_code(reportSods[i]);
				codeMap.setValue_table_name(valuetables[i]);
				codeMap.setReport_table_name(reportTable);
				ctsCodeMaps.add(codeMap);
			}
			
	        // 启动固定线程池
	        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
	        // 消息中间件
	        if(StartConfig.fileLoop() == 0) {
		        for (int i = 0; i < threadCount; i++) {
		            executor.execute(new MultSubThread(diQueues,ctsCodeMaps));
		        }
	        }
	        else if (StartConfig.fileLoop() == 1) {  // 目录轮询
	        	for (int i = 0; i < threadCount; i++) {
		            executor.execute(new FileLoopThread(files, diQueues,ctsCodeMaps));
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
