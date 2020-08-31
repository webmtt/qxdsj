package org.cimiss2.dwp.tools.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


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
//	public static String dbPropertiesFile=ConfigurationManager.getJarSuperPath()+"config/db.properties";
//	public static String configPropertiesFile="E:\\work\\dwp\\cimiss2-dwp\\cimiss2-dwp-multithread\\src\\main\\java\\cma\\cimiss2\\dpc\\indb\\radi\\dc_surf_xili_windener_tab\\E.3800.0004.S017_config.properties";//+"config/agme_cli_config.properties";
	public static String dbPropertiesFile=ConfigurationManager.getJarSuperPath()+"config"+File.separator+"general_config.properties";
	public static String configPropertiesFile=ConfigurationManager.getJarSuperPath()+"config"+File.separator+"general_config.properties";
	private static LoadPropertiesFile instance;
	private Properties globalProperties;
	private Properties configProperties;
	/**
	 * @Title:  LoadPropertiesFile   
	 * @Description:    TODO(单例模式，私有构造方法)   
	 * @param:  @param config_path  
	 * @throws:
	 */
	public  LoadPropertiesFile() {
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
    		configProperties.load(new FileInputStream(configPropertiesFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
//			logger.error("配置文件："+configPropertiesFile+"不存在\n");
		} catch (IOException e) {
			e.printStackTrace();
//			logger.error("读取配置文件："+configPropertiesFile+"错误\n");
		}
	}
	
	public  LoadPropertiesFile(String configPropertiesPath) {
		configPropertiesFile = configPropertiesPath;
		globalProperties = new Properties();
    	try {
    		String path = ConfigurationManager.getJarSuperPath();
    		globalProperties.load(new FileInputStream(path + dbPropertiesFile));	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
//			logger.error("配置文件："+dbPropertiesFile+"不存在\n");
		} catch (IOException e) {
			e.printStackTrace();
//			logger.error("读取配置文件："+dbPropertiesFile+"错误\n");
		}
    	
    	configProperties = new Properties();
    	try {
    		configProperties.load(new FileInputStream(configPropertiesFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
//			logger.error("配置文件："+configPropertiesFile+"不存在\n");
		} catch (IOException e) {
			e.printStackTrace();
//			logger.error("读取配置文件："+configPropertiesFile+"错误\n");
		}
	}
	
	public void  ReloadConfig() {
		configProperties = new Properties();
    	try {
    		configProperties.load(new FileInputStream(configPropertiesFile));
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
	public static LoadPropertiesFile getInstance(String configPropertiesPath){
        if(instance==null){
            instance=new LoadPropertiesFile(configPropertiesPath);
        }
        return instance;
    }
	
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

	public static void main(String[] args) {
//		LoadPropertiesFile.configPropertiesFile = "kkkk";
		LoadPropertiesFile.getInstance().getConfigProperties();
		System.out.println(ConfigurationManager.getJarSuperPath());
	}
}
