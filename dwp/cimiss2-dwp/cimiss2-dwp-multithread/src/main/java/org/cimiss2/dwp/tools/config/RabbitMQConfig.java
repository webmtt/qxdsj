package org.cimiss2.dwp.tools.config;
import java.util.Properties;

import org.cimiss2.dwp.tools.utils.LoadPropertiesFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br>
 * 加载RabbitMQ配置文件
 * 返回结果集：RabbitMQ配置信息
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 12/8/2017   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *
 * 
 */
public class RabbitMQConfig {
	public static final Logger logger = LoggerFactory.getLogger(RabbitMQConfig.class);
	/**
	 * 对列名
	 */
	private String queueName;
	/**
	 * 用户名
	 */
	private String user;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 端口号
	 */
	private int port;
	/**
	 * 主机名
	 */
	private String host;
	
	public RabbitMQConfig(String path) {
		Properties pps = LoadPropertiesFile.getInstance().getConfigProperties();
		setQueueName(pps.getProperty("RabbitMQ.queueName"));
		setHost(pps.getProperty("RabbitMQ.host"));
		setUser(pps.getProperty("RabbitMQ.user"));
		setPassword(pps.getProperty("RabbitMQ.passWord"));
		setPort(Integer.parseInt(pps.getProperty("RabbitMQ.port", "5672")));
	}

	public RabbitMQConfig() {
		Properties pps = LoadPropertiesFile.getInstance().getConfigProperties();
		setQueueName(pps.getProperty("RabbitMQ.queueName"));
		setHost(pps.getProperty("RabbitMQ.host"));
		setUser(pps.getProperty("RabbitMQ.user"));
		setPassword(pps.getProperty("RabbitMQ.passWord"));
		setPort(Integer.parseInt(pps.getProperty("RabbitMQ.port", "5672")));
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
	
	
	public static void main(String[] args) {
		RabbitMQConfig config = new RabbitMQConfig();
		System.out.println(config.getHost()+config.getPassword()+config.getPort()+config.getQueueName()+config.getUser());
	}

}
