package org.cimiss2.dwp.tools.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
/**
 * -------------------------------------------------------------------------------
 * <br>
 * @Title:  LoadPropertiesFile.java   
 * @Package org.cimiss2.dwp.tools.utils   
 * @Description:    TODO(加载Properties属性配置文件)
 * 实例单例模式，保证文件只加载一次
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月14日 下午1:43:33   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *---------------------------------------------------------------------------------
 */
public class LoadPropertiesFile {
//	public static final Logger logger = LoggerFactory.getLogger("loggerInfo");
	public static String dbPropertiesFile="config/db.properties";
	public static String configPropertiesFile="config/dpc_cawn_bufr_anion.properties";
	public static String dpc_global_config = "config/dpc_global_config.properties";
	//集成测试加入
	public static String glb_qc_v_config = "config/glb/columnElement.properties";
	private static LoadPropertiesFile instance;
	private Properties globalProperties;
	private Properties configProperties;
	private Properties glbqcVProperties;
	/**
	 * @Title:  LoadPropertiesFile   
	 * @Description:    TODO(单例模式，私有构造方法)   
	 * @param:  @param config_path  
	 * @throws:
	 */
	private LoadPropertiesFile() {
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
    			TimeCheckUtil.setAfter_day(Integer.parseInt(configProperties.getProperty("D_DATETIME_AFTER_DAY", "3").trim()));
        		TimeCheckUtil.setBefore_day(Integer.parseInt(configProperties.getProperty("D_DATETIME_BEFORE_DAY", "3").trim()));
    		}else if (configProperties.containsKey("D_DATETIME_AFTER_DAY")) {
    			TimeCheckUtil.setAfter_day(Integer.parseInt(configProperties.getProperty("D_DATETIME_AFTER_DAY", "3").trim()));
    			TimeCheckUtil.setBefore_day(1);
			}else if (configProperties.containsKey("D_DATETIME_BEFORE_DAY")) {
				TimeCheckUtil.setBefore_day(Integer.parseInt(configProperties.getProperty("D_DATETIME_BEFORE_DAY", "3").trim()));
				TimeCheckUtil.setAfter_day(1);
			}else {
    			TimeCheckUtil.setAfter_day(1);
        		TimeCheckUtil.setBefore_day(1);
			}
    		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
//			logger.error("配置文件："+configPropertiesFile+"不存在\n");
		} catch (IOException e) {
			e.printStackTrace();
//			logger.error("读取配置文件："+configPropertiesFile+"错误\n");
		}
		//集成测试加入
		glbqcVProperties = new Properties();
		try {
			glbqcVProperties.load(new FileInputStream(glb_qc_v_config));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Title: getInstance 
	 * @Description: TODO(单例模式，获取实体对象) 
	 * @param 无
	 * @return LoadPropertiesFile
	 * @throws:
	 */
	public static LoadPropertiesFile getInstance(){
        if(instance==null){
            instance=new LoadPropertiesFile();
        }
        return instance;
    }

	public Properties getGlobalProperties() {
		return globalProperties;
	}

	public Properties getConfigProperties() {
		return configProperties;
	}

	//集成测试加入
	public Properties getGlbqcVProperties() {
		return glbqcVProperties;
	}


	public static void main(String[] args) {
//		LoadPropertiesFile.configPropertiesFile = "kkkk";
		LoadPropertiesFile.getInstance().getConfigProperties();
		
		System.out.println(TimeCheckUtil.checkTime(new Date()));
	}
}
