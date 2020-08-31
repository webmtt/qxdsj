package org.cimiss2.dwp.tools.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil2;

public class LoadPropertiesFile2 {

//	public static final Logger logger = LoggerFactory.getLogger("loggerInfo");
	public static String dbPropertiesFile="config/db.properties";
	public static String configPropertiesFile="config/dpc_cawn_bufr_anion.properties";
	public static String dpc_global_config = "config/dpc_global_config.properties";
	private static LoadPropertiesFile2 instance;
	private Properties globalProperties;
	private Properties configProperties;
	/**
	 * @Title:  LoadPropertiesFile   
	 * @Description:    TODO(单例模式，私有构造方法)   
	 * @param:  @param config_path  
	 * @throws:
	 */
	private LoadPropertiesFile2() {
		TimeCheckUtil2.timeCheckUtil = TimeCheckUtil2.getInstance();
		globalProperties = new Properties();
    	try {
    		globalProperties.load(new FileInputStream(dbPropertiesFile));	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
//			logger.error("配置文件："+dbPropertiesFile+"不存在\n");
		} catch (IOException e) {
			e.printStackTrace();
//			logger.error("读取配置文件："+dbPropertiesFile+"错误\n");
		}
    	
    	configProperties = new Properties();
    	try {
    		configProperties.load(new FileInputStream(dpc_global_config));
    		configProperties.load(new FileInputStream(configPropertiesFile));
    		if(configProperties.containsKey("D_DATETIME_AFTER_DAY") && configProperties.containsKey("D_DATETIME_BEFORE_DAY")) {
    			TimeCheckUtil2.timeCheckUtil.setAfter_day(Integer.parseInt(configProperties.getProperty("D_DATETIME_AFTER_DAY", "3").trim()));
        		TimeCheckUtil2.timeCheckUtil.setBefore_day(Integer.parseInt(configProperties.getProperty("D_DATETIME_BEFORE_DAY", "3").trim()));
    		}else if (configProperties.containsKey("D_DATETIME_AFTER_DAY")) {
    			TimeCheckUtil2.timeCheckUtil.setAfter_day(Integer.parseInt(configProperties.getProperty("D_DATETIME_AFTER_DAY", "3").trim()));
    			TimeCheckUtil2.timeCheckUtil.setBefore_day(1);
			}else if (configProperties.containsKey("D_DATETIME_BEFORE_DAY")) {
				TimeCheckUtil2.timeCheckUtil.setBefore_day(Integer.parseInt(configProperties.getProperty("D_DATETIME_BEFORE_DAY", "3").trim()));
				TimeCheckUtil2.timeCheckUtil.setAfter_day(1);
			}else {
    			TimeCheckUtil2.timeCheckUtil.setAfter_day(1);
        		TimeCheckUtil2.timeCheckUtil.setBefore_day(1);
			}
    		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
//			logger.error("配置文件："+configPropertiesFile+"不存在\n");
		} catch (IOException e) {
			e.printStackTrace();
//			logger.error("读取配置文件："+configPropertiesFile+"错误\n");
		}
	}
	
	/**
	 * @Title: getInstance 
	 * @Description: TODO(单例模式，获取实体对象) 
	 * @param 无
	 * @return LoadPropertiesFile
	 * @throws:
	 */
	public static LoadPropertiesFile2 getInstance(){
        if(instance==null){
            instance=new LoadPropertiesFile2();
        }
        return instance;
    }

	public Properties getGlobalProperties() {
		return globalProperties;
	}

	public Properties getConfigProperties() {
		return configProperties;
	}

	public static void main(String[] args) {
//		LoadPropertiesFile.configPropertiesFile = "kkkk";
		LoadPropertiesFile2.getInstance().getConfigProperties();
		
		System.out.println(TimeCheckUtil2.checkTime(new Date()));
	}

}
