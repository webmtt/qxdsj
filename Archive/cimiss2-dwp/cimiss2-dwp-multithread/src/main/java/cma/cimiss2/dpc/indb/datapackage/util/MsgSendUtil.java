package cma.cimiss2.dpc.indb.datapackage.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MsgSendUtil {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	//创建连接工厂
    private ConnectionFactory factory = new ConnectionFactory();
    //创建一个新的连接
    private Connection connection = null;
    private Channel channel = null;
    static String QUEUE_NAME = "";
    private static MsgSendUtil msgSendUtil = null;
    
    public static MsgSendUtil getMsgSendUtil() {
    	if(msgSendUtil == null) {
    		msgSendUtil = new MsgSendUtil();
    	}
    	
    	return msgSendUtil;
    }
    
    
    private MsgSendUtil() {
    	Properties properties = new Properties();
    	try {
    		properties.load(new FileInputStream("config/data_package_config.properties"));
		} catch (Exception e) {
			infoLogger.error("properties.load(new FileInputStream(\"config/data_package_config.properties\")) error " + e.getMessage());
		}
    	if(properties.containsKey("Send.RabbitMQ.host") && properties.containsKey("Send.RabbitMQ.user")
    			&& properties.containsKey("Send.RabbitMQ.passWord") && properties.containsKey("Send.RabbitMQ.queueName")) {
    		// 获取rabbitMQ连接信息
            factory.setHost(properties.getProperty("Send.RabbitMQ.host").trim());
            factory.setUsername(properties.getProperty("Send.RabbitMQ.user").trim());
            factory.setPassword(properties.getProperty("Send.RabbitMQ.passWord".trim()));
            factory.setPort(Integer.parseInt(properties.getProperty("Send.RabbitMQ.port", "5672").trim()));
            QUEUE_NAME = properties.getProperty("Send.RabbitMQ.queueName").trim();
    	}else {
    		infoLogger.error("config/data_package_config.properties not find Send.RabbitMQ.host or Send.RabbitMQ.user or Send.RabbitMQ.passWord or Send.RabbitMQ.queueName");
		}
    	
    	
        // 网络异常自动连接恢复
        factory.setAutomaticRecoveryEnabled(true);
        // 每10秒尝试重试连接一次
        factory.setNetworkRecoveryInterval(10000);
    }
	
	public boolean sendMessage(String message) {
		
		if(connection == null || !connection.isOpen()) {
			try {
				connection = factory.newConnection();
			} catch (IOException | TimeoutException e) {
				infoLogger.error("connection = factory.newConnection() " +e.getMessage());
				e.printStackTrace();
			}
		}
		
		
		if( channel == null || !channel.isOpen()) {
			//创建一个通道
	        try {
				channel = connection.createChannel();
			} catch (IOException e) {
				infoLogger.error("channel = connection.createChannel() " +e.getMessage());
				e.printStackTrace();
			}
	        //  声明一个队列        
	        try {
				channel.queueDeclare(QUEUE_NAME, true, false, false, null);
			
				//发送消息到队列中
		        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
		        infoLogger.info("channel.basicPublish @ " + QUEUE_NAME + "SUCESS");
		        
			} catch (IOException e) {
				
				infoLogger.error("channel.queueDeclare " +e.getMessage());
				e.printStackTrace();
			}
		}
		
		
		if(channel != null) {
			try {
				channel.close();
				channel = null;
			} catch (IOException | TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(connection != null) {
			try {
				connection.close();
				connection = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return true;
	}
	

}
