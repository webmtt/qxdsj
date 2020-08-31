package cma.cimiss2.dpc.indb.storm.bufr.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class RabbitMQConfig {
	/**
	 * 对列名
	 */
	private static String queueName;
	/**
	 * 用户名
	 */
	private static String user;
	/**
	 * 密码
	 */
	private static String password;
	/**
	 * 端口号
	 */
	private static int port;
	/**
	 * 主机名
	 */
	private static String host;
	private static int consumersCount;
	
	/*
	 * 入库类型
	 */
	private static int dataBaseType;
	/**
	 * mq配置文件路径
	 */
	private String propertiesPath = System.getProperty("user.dir") + "/config/bufr/rabbitmq.properties";

	public RabbitMQConfig(String propertiesPath) {
		this.propertiesPath = propertiesPath;

	}

	public RabbitMQConfig() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(propertiesPath));
			setQueueName(prop.getProperty("RabbitMQ.queueName"));
			setHost(prop.getProperty("RabbitMQ.host"));
			setUser(prop.getProperty("RabbitMQ.user"));
			setPassword(prop.getProperty("RabbitMQ.passWord"));
			setPort(Integer.parseInt(prop.getProperty("RabbitMQ.port", "5672")));
			setConsumersCount(Integer.parseInt(prop.getProperty("RabbitMQ.consumersCount", "1")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String getQueueName() {
		return queueName;
	}

	public static void setQueueName(String queueName) {
		RabbitMQConfig.queueName = queueName;
	}

	public static String getUser() {
		return user;
	}

	public static void setUser(String user) {
		RabbitMQConfig.user = user;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		RabbitMQConfig.password = password;
	}

	public static int getPort() {
		return port;
	}

	public static void setPort(int port) {
		RabbitMQConfig.port = port;
	}

	public static String getHost() {
		return host;
	}

	public static void setHost(String host) {
		RabbitMQConfig.host = host;
	}

	public static int getConsumersCount() {
		return consumersCount;
	}

	public static void setConsumersCount(int consumersCount) {
		RabbitMQConfig.consumersCount = consumersCount;
	}

	public String getPropertiesPath() {
		return propertiesPath;
	}

	public void setPropertiesPath(String propertiesPath) {
		this.propertiesPath = propertiesPath;
	}

	public static void main(String[] args) {
		new RabbitMQConfig();
		System.out.println(getHost() + getPassword() + getPort() + getQueueName() + getUser());
	}

}
