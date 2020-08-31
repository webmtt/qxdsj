package cma.cimiss2.dpc.indb.storm.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hitec.bufr.util.StringUtil;

import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil2;
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
	public static String dbPropertiesFile="db.properties";
//	public static String configPropertiesFile="config/agme_cli_config.properties";
	private static LoadPropertiesFile instance;
	private Properties globalProperties;
	private Properties configProperties;
	/**
	 * @Title:  LoadPropertiesFile   
	 * @Description:    TODO(单例模式，私有构造方法)   
	 * @param:  @param config_path  
	 * @throws:
	 */
	private LoadPropertiesFile() {
		globalProperties = new Properties();
    	try {
    		globalProperties.load(new FileInputStream( StringUtil.getConfigPath()  + dbPropertiesFile));	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
//			logger.error("配置文件："+dbPropertiesFile+"不存在\n");
		} catch (IOException e) {
			e.printStackTrace();
//			logger.error("读取配置文件："+dbPropertiesFile+"错误\n");
		}
    	TimeCheckUtil2.timeCheckUtil = TimeCheckUtil2.getInstance();
    	configProperties = new Properties();
    	try {
			configProperties.load(new FileInputStream(StringUtil.getConfigPath() + "dpc_global_config.properties"));
    	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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

	public static void main(String[] args) {
//		LoadPropertiesFile.configPropertiesFile = "kkkk";
		LoadPropertiesFile.getInstance().getConfigProperties();
	}
}
