package cma.cimiss2.dpc.indb.framework.rmq.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.ConnectionFactory;

/**
* *******************************************************************************************<br>
* <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
* *******************************************************************************************<br>
* <b>Description:</b><br> 
* @author wuzuoqiang 
* @version 1.0
* @Note
* <b>ProjectName:</b> cimiss2-dwp-multithread
* <br><b>PackageName:</b> cma.cimiss2.dpc.indb.framework.rmq.common
* <br><b>ClassName:</b> RabbitMQConf
* <br><b>Date:</b> 2020年4月5日 上午10:13:43
 */
public class RabbitMQConf {
	
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static ConnectionFactory connectionFactory;
	
	private static RabbitMQConf rabbitMQConf = null;
	private Properties properties;
	private RabbitMQConf(String config) {
		try {
			properties = new Properties();
			properties.load(new FileInputStream(new File("config/dpc_global_config.properties")));
			properties.load(new FileInputStream(new File(config)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			infoLogger.info("RabbitMQConf properties.load ", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			infoLogger.info("RabbitMQConf properties.load ", e);
		}
		
		connectionFactory = new ConnectionFactory();
		// 获取rabbitMQ连接信息
		connectionFactory.setHost(properties.getProperty("RabbitMQ.host").trim());
		connectionFactory.setUsername(properties.getProperty("RabbitMQ.user").trim());
		connectionFactory.setPassword(properties.getProperty("RabbitMQ.passWord").trim());
		connectionFactory.setPort(Integer.parseInt(properties.getProperty("RabbitMQ.port").trim()));
		// 设置网络异常自动连接恢复
		connectionFactory.setAutomaticRecoveryEnabled(true);
		 // 每10秒尝试重试连接一次
		connectionFactory.setNetworkRecoveryInterval(1000);
        // 设置不重新声明交换器，队列等信息。
		connectionFactory.setTopologyRecoveryEnabled(false);
	}
	
	public static RabbitMQConf getRabbitMQConf(String config) {
		if(rabbitMQConf == null ) {
			rabbitMQConf = new RabbitMQConf(config);
		}
		return rabbitMQConf;
	}
	public  ConnectionFactory  getConnectionFactory() {
		return connectionFactory;
	}

}
