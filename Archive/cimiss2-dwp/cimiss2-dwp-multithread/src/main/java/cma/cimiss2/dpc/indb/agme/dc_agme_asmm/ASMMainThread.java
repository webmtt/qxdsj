package cma.cimiss2.dpc.indb.agme.dc_agme_asmm;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
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
 * @Title:  ASMMainThread.java   
 * @Package cma.cimiss2.dpc.indb.agme.asm   
 * @Description:    TODO(自动站土壤水分资料 E.0001.0006.R001)   
 * @author: zuoqiang wu    
 * @date:   2019年1月16日 下午7:42:42   
 * @version V1.0 
 * @Copyright: 2019 www.cma.gov.cn Inc. All rights reserved. 
 * 注意：本内容仅限于国家气象信息中心内部传阅，禁止外泄以及用于其他的商业目
 */
public class ASMMainThread {
    static BlockingQueue<StatDi> diQueues = new LinkedBlockingQueue<StatDi>();
    // 声明一个缓存队列，存放轮询到的文件路径
    static BlockingQueue<String> files = new LinkedBlockingQueue<String>();
    public static ArrayList<CTSCodeMap> ctsCodeMaps = new ArrayList<>();
    private static int threadCount = 1;   // 线程池的大小
    
    /**
     * 
     * @Title: main   
     * @Description: TODO(主线程入口函数)   
     * @param args  第一个参数为 消息处理线程池大小，默认为1
     * 第二个参数为是否发送EI告警信息 0为不发送， 1为发送，默认为1，如果第二个参数需要传入，则第一个参数必须同时传入
     * 第三个参数为是否发送DI日志信息0为不发送， 1为发送，默认为1，若第三个参数需要传入，则第一，二参数必须同时传入    
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
			//File file = new File("config/dpc_agme_asmm.properties");
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
		            executor.execute(new ASMMultSubThread(diQueues,ctsCodeMaps));
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
	        ExecutorService service = Executors.newFixedThreadPool(2);
	        service.execute(new DISubThread(diQueues));
    	}
    }
}
