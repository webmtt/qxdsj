package org.cimiss2.dwp.tools.config;


import java.util.Properties;

import org.cimiss2.dwp.tools.utils.LoadPropertiesFile;
/**
 * -------------------------------------------------------------------------------
 * <br>
 * @Title:  StartConfig.java   
 * @Package org.cimiss2.dwp.tools.config   
 * @Description:    TODO(程序启动是加载)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月10日 上午11:11:10   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *---------------------------------------------------------------------------------
 */
public class StartConfig {
	// 是否发送DI
	private static boolean isSendDi = true;
	
	private static boolean isSendEi = true;
	private static int threadCount = 1;
	private static int diThreadCount =1; 
	private static int databaseType = 1;
	private static int fileloop = 0;
	public static int runport = 0;
	
	public static boolean isWriteFile  = false;
	public static String writePath = "";
	
	public static long maxFileSize = 10240000;//缺省最大处理文件10MB
	
	public static void setConfigFile(String configFile) {
 		LoadPropertiesFile.configPropertiesFile = configFile;
		LoadPropertiesFile.getInstance().ReloadConfig();
		 
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
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("message_log")) {
			String path = config.getProperty("message_log").trim();
			path = path.replace("{port}", runport + "");
			return path;
		}
		return "../logs/%d{yyyy-MM-dd}/dpc_message_"+runport+"_%d{yyyy-MM-dd}.log";
	}
	
	public static String logProcessPath() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("process_log")) {
			String path = config.getProperty("process_log").trim();
			path = path.replace("{port}", runport + "");
			return path;
		}
		return "../logs/%d{yyyy-MM-dd}/dpc_process_\"+runport+\"_%d{yyyy-MM-dd}.log";
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
	public static String ctsCode(String code) {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey(code)) {
			String cts_code = config.getProperty(code).trim();
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
	
	public static String valueTable(String table) {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey(table)) {
			String value_table_name = config.getProperty(table).trim();
			return value_table_name;
		}else {
			return null;
		}
	}
	public static String queueName(String table) {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey(table)) {
			String value_table_name = config.getProperty(table).trim();
			return value_table_name;
		}else {
			return null;
		}
	}
	public static void main(String args[]){
		System.out.println(StartConfig.queueName("R004_RabbitMQ.queueName"));
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
	public static String[] sodCodes(String code) {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey(code)) {
			String sod_code[] = config.getProperty(code).trim().split(",");
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
	public static String[] valueTables(String table) {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey(table)) {
			String value_table_name[] = config.getProperty(table).trim().split(",");
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
	public static String sodCode(String code) {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey(code)) {
			String sod_code = config.getProperty(code).trim();
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
			if(isWriteFileStr == "1")
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
				return Integer.parseInt(maxSize);
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
	
	// get kafka topics
	public static String[] kafkaTopics() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if(config.containsKey("kafka.topics")) {
			String kaftps[] = config.getProperty("kafka.topics").trim().split(",");
			for(int i = 0; i < kaftps.length; i ++)
				kaftps[i] = kaftps[i].trim();
			return kaftps;
		}else {
			return null;
		}
	}
	
	// get nafp.SliceX
	public static int nafpSliceX() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if (config.containsKey("nafp.slicex")) {
			int slicex = Integer.parseInt(config.getProperty("nafp.slicex").trim(), 10);
			return slicex;
		} else {
			return 100;
		}
	}

	// get nafp.SliceY
	public static int nafpSliceY() {
		Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
		if (config.containsKey("nafp.slicey")) {
			int slicex = Integer.parseInt(config.getProperty("nafp.slicey").trim(), 10);
			return slicex;
		} else {
			return 100;
		}
	}
	
}
