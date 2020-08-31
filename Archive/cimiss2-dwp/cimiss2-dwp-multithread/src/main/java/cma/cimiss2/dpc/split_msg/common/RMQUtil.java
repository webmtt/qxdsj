package cma.cimiss2.dpc.split_msg.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RMQUtil {
	private final static Logger logger = LoggerFactory.getLogger(RMQUtil.class);
	
	Channel channel = null;
	
	private static String configFile;
	
	



	public static void setConfigFile(String configFile) {
		RMQUtil.configFile = configFile;
	}


	private static Connection getConnection() {
		Connection connection = null;
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(new File(configFile)));
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(properties.getProperty("SEND_RMQ_IP").trim());
			factory.setUsername(properties.getProperty("SEND_RMQ_USER").trim());
			factory.setPassword(properties.getProperty("SEND_RMQ_PASSWORD").trim());
			factory.setPort(Integer.parseInt(properties.getProperty("SEND_RMQ_PORT").trim()));
			factory.setVirtualHost("/");

			// 网络异常自动连接恢复
			factory.setAutomaticRecoveryEnabled(true);
			// 每10秒尝试重试连接一次
			factory.setNetworkRecoveryInterval(10000);
			// 创建连接
			connection = factory.newConnection();
			return connection;
		} catch (IOException | TimeoutException e1) {
			e1.printStackTrace();
			return null;
		}
	}

	
	public static void send_message(String message, String queueName, String exchangeName, String routtingKey) {
		
		Connection connection = getConnection();
		if(connection != null) {
			Channel channel = null;
			try {
				channel = connection.createChannel();
				channel.queueDeclare(queueName, true, false, false, null);
				channel.queueBind(queueName, exchangeName, routtingKey);
				channel.basicPublish(exchangeName, routtingKey, null, message.getBytes());
				logger.info( "success "+exchangeName+" "+routtingKey+"  "+message.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				if(channel != null) {
					try {
						channel.close();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (TimeoutException e) {
						e.printStackTrace();
					}
				}
			}
			
			try {
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
