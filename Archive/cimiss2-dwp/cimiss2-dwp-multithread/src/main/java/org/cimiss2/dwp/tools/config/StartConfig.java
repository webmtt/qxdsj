package org.cimiss2.dwp.tools.config;


import org.cimiss2.dwp.tools.utils.LoadPropertiesFile;

import java.util.Properties;

/**
 * -------------------------------------------------------------------------------
 * <br>
 * @Title: StartConfig.java
 * @Package org.cimiss2.dwp.tools.config
 * @Description: TODO(程序启动是加载)
 *
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月10日 上午11:11:10   wuzuoqiang    Initial creation.
 * </pre>
 * @author wuzuoqiang
 * ---------------------------------------------------------------------------------
 */

public class StartConfig {
	
	// RabbitMQ交换机名称
	private String exChange;
	// RabbitMQ 路由名称
	private String routingKey;
	// RabbitMQ 对列名称
	private String queueName;
	// 是否发送DI
	private static boolean isSendDi = true;
	private static boolean isQC = true;
	private static boolean isSendCimissDi = true;
	public static boolean isProperties = true;
	private static boolean isSendEi = true;
	private static int threadCount = 1;
	private static int diThreadCount =1; 
	private static int atsThreadCount = 1;
	private static int databaseType = 1;
	private static int fileloop = 0;
	public static int runport = 0;
	public static String cts_code ="StartConfig";
	public static boolean isWriteFile  = false;
	public static String writePath = "";	
	public static int dataPattern = 0;
	public static int useBBB = 0;
	public static long maxFileSize = 10240000;//缺省最大处理文件10MB
	public static String configFile;

	public static int ccx_update_flag = 0;
	
	public static void setConfigFile(String configFile) {
		StartConfig.configFile = configFile ;
		LoadPropertiesFile.configPropertiesFile = configFile;
	}
	
	
	
	public static String getExChange() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("RabbitMQ.exChange")) {
			return config.getProperty("RabbitMQ.exChange").trim();
		}else {
			return null;
		}
	}



	public static String getRoutingKey() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("RabbitMQ.routingKey")) {
			return config.getProperty("RabbitMQ.routingKey").trim();
		}else {
			return null;
		}
	}



	public static String getQueueName() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("RabbitMQ.queueName")) {
			return config.getProperty("RabbitMQ.queueName").trim();
		}else {
			return null;
		}
	}



	public static int getAtsThreadCount() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("atsThreadCount")) {
			return Integer.parseInt(config.getProperty("atsThreadCount").trim());
		}else {
			return atsThreadCount;
		}
	}



	/**
	 * 是否生成.tmp文件
	 * @return true为生成.tmp文件，然后重命名
	 */
	public static boolean isFileTmpFlag() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("fileTmpFlag")) {
			if(config.getProperty("fileTmpFlag", "1").trim().equalsIgnoreCase("1")) {
				return true;
			}else {
				return false;
			}
		}else {
			return true;
		}
	}
	
	
	// 选项名称FileDeleteFlag，=1删除，=0，不删除，默认值1
	public static boolean isFileDeleteFlag() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("fileDeleteFlag")) {
			if(config.getProperty("fileDeleteFlag", "1").trim().equalsIgnoreCase("1")) {
				return true;
			}else {
				return false;
			}
		}else {
			return true;
		}
	}




	/**
	 * @Title: isSendDi 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @return boolean
	 * @throws:
	 */
	public static boolean isSendDi() {
		Properties global = LoadPropertiesFile.getInstance().getGlobalProperties();
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		
		if(global.containsKey("di.option") && config.containsKey("di.option")) {
//			System.out.println(global.getProperty("di.option"));
			if(global.getProperty("di.option").trim().equals("1") && config.getProperty("di.option").trim().equals("0")) {
				isSendDi = false;
			}else if (global.getProperty("di.option").trim().equals("0")) {
				isSendDi = false;
			}
		}
		global = null ;
		config = null;
		return isSendDi;
	}
	/**
	 * @Title: isSendDi 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @return boolean
	 * @throws:
	 */
	public static boolean isSendCimissDi() {
		Properties global = LoadPropertiesFile.getInstance().getGlobalProperties();
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		
		if(global.containsKey("cimissdi.option") && config.containsKey("cimissdi.option")) {
//			System.out.println(global.getProperty("di.option"));
			if(global.getProperty("cimissdi.option").trim().equals("1") && config.getProperty("cimissdi.option").trim().equals("0")) {
				isSendCimissDi = false;
			}else if (global.getProperty("cimissdi.option").trim().equals("0")) {
				isSendCimissDi = false;
			}
		}
		global = null ;
		config = null;
		return isSendCimissDi;
	}
	public static boolean isConflict_strategy() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("conflict_strategy")) {
			if(config.getProperty("conflict_strategy").trim().equals("1")) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	
	/**
	 * DI是否写入到日志文件
	 * @return
	 */
	public static boolean isDILog() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("DI.WRITE.LOG")) {
			if(config.getProperty("DI.WRITE.LOG").trim().equals("1")) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	public static boolean isCimissDILog() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("CIMISS.DI.WRITE.LOG")) {
			if(config.getProperty("CIMISS.DI.WRITE.LOG").trim().equals("1")) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	
	/**
	 * @Title: isSendEi 
	 * @Description: TODO(第二个参数为是否发送EI告警信息 0为不发送， 1为发送，默认为1，如果第二个参数需要传入，则第一个参数必须同时传入) 
	 * @return boolean
	 * @throws:
	 */
	public static boolean isSendEi() {
		Properties global = LoadPropertiesFile.getInstance().getGlobalProperties();
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(global.containsKey("ei.option") && config.containsKey("ei.option")) {
			
			if(global.getProperty("ei.option").trim().equals("1") && config.getProperty("ei.option").trim().equals("0")) {
				isSendEi = false;
			}else if (global.getProperty("ei.option").trim().equals("0")) {
				isSendEi = false;
			}
		}
		global = null ;
		config = null;
		return isSendEi;
	}
	
	/**
	 * @Title: getThreadCount 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @return int
	 * @throws:
	 */
	public static int getThreadCount() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("MessageThreadCount")) {
			threadCount = Integer.parseInt(config.getProperty("MessageThreadCount").trim());
		}
		return threadCount;
	}

	/**
	 * @Title: getDiThreadCount 
	 * @Description: 获取DI发送处理线程数
	 * @return int
	 * @throws:
	 */
	public static int getDiThreadCount() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("DIThreadCount")) {
			diThreadCount = Integer.parseInt(config.getProperty("DIThreadCount").trim());
		}
		return diThreadCount;
	}
	
	/**
	 * @Title: getDatabaseType 
	 * @Description: TODO(获取使用数据的类型) 
	 * @return int 返回=1 则是用阿里DRDS数据库，=0则使用万里开源的数据库
	 * @throws:
	 */
	public static int getDatabaseType() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("dataBaseType")) {
			databaseType = Integer.parseInt(config.getProperty("dataBaseType").trim());
		}
		return databaseType;
	}
	
	public static int fileLoop() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("fileloop")) {
			fileloop = Integer.parseInt(config.getProperty("fileloop").trim());
		}
		return fileloop;
	}
	
	public static String logMessagePath() {
		if(isProperties) {
			Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
			if(config.containsKey("message_log")) {
				String path = config.getProperty("message_log").trim();
				path = path.replace("{port}", runport + "");
				return path;
			}
			return "../logs/%d{yyyy-MM-dd}/dpc_msg_split_message_"+runport+"_%d{yyyy-MM-dd}.log";
		}else {
			return "../logs/%d{yyyy-MM-dd}/dpc_message"+cts_code+"_%d{yyyy-MM-dd}.log";
		}
		
	}
	
	public static String logProcessPath() {
		
		if(isProperties) {
			Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
			if(config.containsKey("process_log")) {
				String path = config.getProperty("process_log").trim();
				path = path.replace("{port}", runport + "");
				return path;
			}
			return "../logs/%d{yyyy-MM-dd}/dpc_msg_split_message_"+runport+"_%d{yyyy-MM-dd}.log";
		}else {
			return "../logs/%d{yyyy-MM-dd}/dpc_process"+cts_code+"_%d{yyyy-MM-dd}.log";
		}
		
	}
	
	public static String fileLoopPath() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("fileloopPath")) {
			String path = config.getProperty("fileloopPath").trim();
			return path;
		}else {
			return null;
		}
	}
	
	public static String moveFilePath() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("moveFilePath")) {
			String path = config.getProperty("moveFilePath").trim();
			return path;
		}else {
			return null;
		}
	}
	
	public static String ctsCode() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("cts_code")) {
			String cts_code = config.getProperty("cts_code").trim();
			return cts_code;
		}else {
			return null;
		}
	}
	
	public static String keyTable() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("key_table_name")) {
			String key_table_name = config.getProperty("key_table_name").trim();
			return key_table_name;
		}else {
			return null;
		}
	}
	
	public static String valueTable() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("value_table_name")) {
			String value_table_name = config.getProperty("value_table_name").trim();
			return value_table_name;
		}else {
			return null;
		}
	}
	
	public static String reportTable() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("report_table_name")) {
			String report_table_name = config.getProperty("report_table_name").trim();
			return report_table_name;
		}else {
			return null;
		}
	}
	/**
	 * @Title: ctsCodes   
	 * @Description: TODO(针对城镇(6小时)精细化预报资料，获取ctscode)   
	 * @return String[]      
	 * @throws：
	 */
	public static String[] ctsCodes() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("cts_code")) {
			String cts_code[] = config.getProperty("cts_code").trim().split(",");
			for(int i = 0; i < cts_code.length; i ++)
				cts_code[i] = cts_code[i].trim();
			return cts_code;
		}else {
			return null;
		}
	}
	/**
	 * @Title: ctsCodes   
	 * @Description: TODO(针对城镇(6小时)精细化预报资料,获取sodcode)   
	 * @return String[]      
	 * @throws：
	 */
	public static String[] sodCodes() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("sod_code")) {
			String sod_code[] = config.getProperty("sod_code").trim().split(",");
			for(int i = 0; i < sod_code.length; i ++)
				sod_code[i] = sod_code[i].trim();
			return sod_code;
		}else {
			return null;
		}
	}
	/**
	 * @Title: ctsCodes   
	 * @Description: TODO(针对城镇(6小时)精细化预报资料，获取报文sodcode)   
	 * @return String[]      
	 * @throws：
	 */
	public static String[] reportSodCodes() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("report_sod_code")) {
			String report_sod_code[] = config.getProperty("report_sod_code").trim().split(",");
			for(int i = 0; i < report_sod_code.length; i ++)
				report_sod_code[i] = report_sod_code[i].trim();
			return report_sod_code;
		}else {
			return null;
		}
	}
	/**
	 * @Title: sodCode   
	 * @Description: TODO(从配置文件中取得多个要素表)   
	 * @return String      
	 * @throws：
	 */
	public static String[] valueTables() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("value_table_name")) {
			String value_table_name[] = config.getProperty("value_table_name").trim().split(",");
			for(int i = 0; i < value_table_name.length; i ++)
				value_table_name[i] = value_table_name[i].trim();
			return value_table_name;
		}else {
			return null;
		}
	}
	public static String sodCode() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("sod_code")) {
			String sod_code = config.getProperty("sod_code").trim();
			return sod_code;
		}else {
			return null;
		}
	}
	/**
	 * @Title: sodCode_pre   
	 * @Description: TODO(针对公路交通资料，有分钟降水表和路面基本要素表，分钟降水表sodcode)   
	 * @return String      
	 * @throws：
	 */
	public static String sodCode_pre() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("pre_sod_code")) {
			String sod_code = config.getProperty("pre_sod_code").trim();
			return sod_code;
		}else {
			return null;
		}
	}
	/**
	 * @Title: sodCode_pre   
	 * @Description: TODO(针对公路交通资料，有分钟降水表和路面基本要素表，路面基本要素表sodcode)   
	 * @return String      
	 * @throws：
	 */
	public static String sodCode_mul() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("mul_sod_code")) {
			String sod_code = config.getProperty("mul_sod_code").trim();
			return sod_code;
		}else {
			return null;
		}
	}
	/**
	 * @Title: reportSodCode   
	 * @Description: TODO(获取资料报文的SOD编码)   
	 * @return String      
	 * @throws：
	 */
	public static String reportSodCode() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("report_sod_code")) {
			String report_sod_code = config.getProperty("report_sod_code").trim();
			return report_sod_code;
		}else {
			return null;
		}
	}
	
	
	
	public static void main(String args[]){
		
		 System.out.println(StartConfig.sodCode());
		 
	}

	public static String getDataFlow() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("data_flow")) {
			String data_flow = config.getProperty("data_flow").trim();
			return data_flow;
		}else {
			return null;
		}
	}
	/**
	 * 卫星名称
	 * @return
	 */
	public static String sateName() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("SATE_NAME")) {
			return config.getProperty("SATE_NAME").trim();
		}else {
			System.out.println("没有配置  SATE_NAME...............");
			return "";
		}
	}
	/**
	 * 卫星仪器名称
	 * @return
	 */
	public static String sateDevName() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("SATE_DEV_NAME")) {
			return config.getProperty("SATE_DEV_NAME").trim();
		}else {
			System.out.println("没有配置  SATE_DEV_NAME...............");
			return "";
		}
	}
	/**
	 * 卫星仪器通道名称
	 * @return
	 */
	public static String sateDevChaName() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("SATE_DEVCHA_NAME")) {
			return config.getProperty("SATE_DEVCHA_NAME").trim();
		}else {
			System.out.println("没有配置  SATE_DEVCHA_NAME...............");
			return "";
		}
	}
	/**
	 * 卫星产品标识
	 * @return
	 */
	public static String sateProdName() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("PROD_NAME")) {
			return config.getProperty("PROD_NAME").trim();
		}else {
			System.out.println("没有配置  PROD_NAME...............");
			return "";
		}
	}
	
	public static String writeFilePath() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("writeFilePath")) {
			String filepath = config.getProperty("writeFilePath").trim();
			return filepath;
		}else {
			return writePath;
		}
	}
	public static boolean isWriteFile() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("isWriteFile")) {
			String isWriteFileStr = config.getProperty("isWriteFile").trim();
			if(isWriteFileStr.equals("1"))
				return true;
			else {
				return false;
			}
		}else {
			return isWriteFile;
		}
	}
	public static long maxFileSize(){
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("maxFileSize")) {
			String maxSize = config.getProperty("maxFileSize").trim();
			try{
				return Long.parseLong(maxSize);
			}catch(Exception e){
				return maxFileSize;
			}
		}else {
			return maxFileSize;
		}
	}
	//获取dpc编码
	public static String getDPCCode() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("dpc_code")) {
			String data_flow = config.getProperty("dpc_code").trim();
			return data_flow;
		}else {
			return null;
		}
	}
	
	//获取v_tt
	public static String getvtt() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("v_tt")) {
			String data_flow = config.getProperty("v_tt").trim();
			return data_flow;
		}else {
			return null;
		}
	}
	// 获取v_cccc
	public static String v_cccc() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("V_CCCC")) {
			String v_cccc = config.getProperty("V_CCCC").trim();
			return v_cccc;
		}else {
			return null;
		}
	}
	//获取经、纬度变化范围值
	public static double getLatitudeRange() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("latitudeRange")) {
			String latitudeRange =config.getProperty("latitudeRange").trim();
			try{
				return  Double.parseDouble(latitudeRange);
			}catch(Exception e){
				return 0.1;
			}
		}else {
			return 0.1;
		}
	}
	
	  /**
     * 获取stationInfoPath
     * @return String
     */
    public static String stationInfoPath() {
        Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
        return config.getProperty("stationInfoPath") != null ? config.getProperty("stationInfoPath").trim() : null;
    }
    /**
     * 获取处理的资料属性（处理文件还是二进制流）
     * @return
     */
    public static int getDataPattern() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("dataPattern")) {
			dataPattern = Integer.parseInt(config.getProperty("dataPattern").trim());
		}
		return dataPattern;
	}
    /**
     * 获取是否需要应用报文中的V_BBB更正报（A.0001.0044.R001）
     * @return
     */
    public static int getUseBBB() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("useBBB")) {
			useBBB = Integer.parseInt(config.getProperty("useBBB").trim());
		}
		return useBBB;
	}
    /**
     * 
     * @Title: cxx_update   
     * @Description: 旧Z格式资料是否需要做更正
     * @return int      
     * @throws：
     */
    public static int cxx_update(){
    	Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("ccx_update_flag")) {
			ccx_update_flag = Integer.parseInt(config.getProperty("ccx_update_flag").trim());
		}
		return ccx_update_flag;
    }
	 /**
	  * 获取当目录轮询时，是否需要在轮询完目录文件后停止程序的标志
	  * LoopQuitFlag=1表示轮询完停止程序，LoopQuitFlag=0或者未配置LoopQuitFlag时，表示轮询完不停程序
	  * @return
	  */
 	public static boolean getLoopQuitFlag() {
 		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
 		if(config.containsKey("LoopQuitFlag")) {
 			if(config.getProperty("LoopQuitFlag").trim().equalsIgnoreCase("1")) {
 				return true;
 			}else {
 				return false;
 			}
 		}else {
 			return false;
 		}
 	}
 	 /**
	  * 获取目录轮询完不停止程序时，在轮询完目录的文件后需要sleep的秒数，默认1000毫秒，即1秒
	  * @return
	  */
 	 public static double getFileLoopSleepSecond() {
 		double second=1.0;
 		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
 		if(config.containsKey("FileLoopSleepSecond")) {
 			try{
 				return  Double.parseDouble((config.getProperty("FileLoopSleepSecond").trim()));
 			}catch (Exception e) {
 				return second;
			}
 		}else{
 			return second;
 		}
 	}

	/**
	 * 从配置文件读取是否进行质控
	 * @return
	 */
	public static boolean isQC() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if (config.containsKey("qc.option")) {
			if (config.getProperty("qc.option").trim().equals("1")) {
				isQC = true;
			} else if (config.getProperty("qc.option").trim().equals("0")) {
				isQC = false;
			}
		}
		return isQC;
	}
}
