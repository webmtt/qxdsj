package cma.cimiss2.dpc.indb.core.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;

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
	public final Logger logger = LoggerFactory.getLogger(LoadPropertiesFile.class);
	private String dbPropertiesFile="config/db.properties";
	private String configPropertiesFile="config/config.properties";
	private static LoadPropertiesFile instance;
	private Properties globalProperties;
	private Properties configProperties;
//	private Properties configTimeRange;
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
			logger.error("配置文件："+dbPropertiesFile+"不存在\n");
		} catch (IOException e) {
			logger.error("读取配置文件："+dbPropertiesFile+"错误\n");
		}
    	
    	configProperties = new Properties();
    	try {
    		configProperties.load(new FileInputStream(configPropertiesFile));
			
		} catch (FileNotFoundException e) {
			logger.error("配置文件："+configPropertiesFile+"不存在\n");
		} catch (IOException e) {
			logger.error("读取配置文件："+configPropertiesFile+"错误\n");
		}
    	
	}
	
	/**
	 * @Title: getInstance 
	 * @Description: TODO(单例模式，获取实体对象) 
	 * @param config_path
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

	
	
	

}
