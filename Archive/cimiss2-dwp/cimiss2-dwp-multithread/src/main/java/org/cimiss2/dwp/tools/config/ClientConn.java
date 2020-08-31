package org.cimiss2.dwp.tools.config;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import com.obs.services.model.AccessControlList;
import com.obs.services.model.ObjectMetadata;
import com.obs.services.model.PutObjectRequest;
import com.obs.services.model.PutObjectResult;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>

 *
 * <p>
 * notes:
 * <ul>
 *   <li> 非结构化数据入库连接实例
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2019年12月13日 下午2:15:45   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class ClientConn {
	Logger logger = Logger.getLogger("msgproc");
	public static String obsPropertiesFile = "config/obs.properties";
	private Properties configProperties;
	
	/**
	 * 存储服务器地址
	 */
	private static String endPoint = "10.40.18.27" ;
	/**
	 * HTTPS 请求对应的端口
	 */
	private static int httpsPort = 443;
	/**
	 * 接入证书
	 */
	private static String ak = ""; 
	/**
	 * 安全证书
	 */
	private static String sk = ""; 
	/**
	 * 要上传到的桶
	 */
	private String bucketName = "";
	/**
	 * 对象名称
	 */
	private String objectKey = "";
	/**
	 * 类对象
	 */
	private static ClientConn clientConn = null;
	
	/**
	 * @Title: loadObsProperties   
	 * @Description: 加载obs.ini     
	 * @throws：
	 */
	private void loadObsProperties(){
		configProperties = new Properties();
		try {
    		configProperties.load(new FileInputStream(obsPropertiesFile));
    		logger.info("Load " + obsPropertiesFile + " OK!");
		} catch (FileNotFoundException e) {
			logger.error("Config file " + obsPropertiesFile + " not exist.\n");
		} catch (IOException e) {
			logger.error("Load file " + obsPropertiesFile + " error.\n");
		}
	}
	
	/**
	 * @Title:  ClientConn   
	 * @Description: 成员变量赋值 
	 * @param:    
	 * @throws
	 */
	private ClientConn(){
		if(configProperties == null){
			loadObsProperties();
		}
		try{
			Properties pps = configProperties;
			endPoint = pps.getProperty("endPoint").trim();
			httpsPort = Integer.parseInt(pps.getProperty("httpsPort").trim());
			ak = pps.getProperty("ak").trim();
			sk = pps.getProperty("sk").trim();
			bucketName = pps.getProperty("bucketName").trim();
			objectKey = pps.getProperty("objectKey").trim();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * @Title: savePics   
	 * @Description: 上传文件  
	 * @param bytes
	 * @param simpleFileName 不带后缀的相对文件路径+文件名
	 * @throws：
	 */
	public static String savePics(byte []bytes, String simpleFileName){
		
		getConfig();
		ObsConfiguration config = new ObsConfiguration();   // 配置文件
		config.setEndPoint(clientConn.getEndPoint());
		config.setHttpsOnly(true);
		config.setEndpointHttpsPort(clientConn.getHttpsPort());
		config.setDisableDnsBucket(true);
		config.setSignatString("v4");//V4 鉴权
		
		// 实例化 ObsClient 服务
		ObsClient obsClient = new ObsClient(clientConn.getAK(), clientConn.getSK(), config);
		System.out.println(obsClient);
		String https = putObject(obsClient, bytes, simpleFileName);
		
		try {
			obsClient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return https;
	}
	
	/**
	 * @Title: putObject   
	 * @Description: 上传文件
	 * @param obsClient
	 * @param bytes
	 * @return String      
	 * @throws：
	 */
	public static String putObject(ObsClient obsClient, byte []bytes, String simpleFileName) {
		try {
			ObjectMetadata metadata = new ObjectMetadata();// 设置上传对象的元数据
			metadata.setContentLength((long)bytes.length);// 设置头信息中的文件长度
			metadata.setContentType("image/jpeg");// 设置上传的文件类型
			String bucketName = clientConn.getBucketName();// 要上传到的桶
			String objectKey = clientConn.getObjectKey() + simpleFileName;// 对象名称
			
			PutObjectRequest request = new PutObjectRequest();
			request.setBucketName(bucketName);
			InputStream sbs = new ByteArrayInputStream(bytes); 
			request.setInput(sbs);
			request.setMetadata(metadata);
			request.setObjectKey(objectKey);
			PutObjectResult result = obsClient.putObject(request);
			// 设置访问权限 
			obsClient.setObjectAcl(bucketName, objectKey, AccessControlList.REST_CANNED_PUBLIC_READ, null);
			System.out.println("Object etag: " + result.getObjectUrl()); // 输出返回结果
			
			return result.getObjectUrl();
			
		} catch (Exception e1) {
			e1.printStackTrace();
			return "";
		}
	}
	
	/**
	 * @Title: getConfig   
	 * @Description: 单例模式，加载配置
	 * @return ClientConn      
	 * @throws：
	 */
	public static ClientConn getConfig(){
		if(clientConn == null){
			clientConn = new ClientConn();
		}
		return clientConn;
	}
	
	public String getObjectKey(){
		return objectKey;
	}
	
	public String getEndPoint(){
		return endPoint;
	}
	
	public int getHttpsPort(){
		return httpsPort;
	}

	public String getAK(){
		return ak;
	}
	
	public String getSK(){
		return sk;
	}
	
	public String getBucketName(){
		return bucketName;
	}
	
}
