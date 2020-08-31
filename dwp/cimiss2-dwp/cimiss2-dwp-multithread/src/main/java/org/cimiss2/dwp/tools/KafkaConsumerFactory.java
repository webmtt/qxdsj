package org.cimiss2.dwp.tools;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.utils.Bytes;

public class KafkaConsumerFactory {
	private static Properties properties = null;
	private static KafkaConsumerFactory consumerFactory = null;
	
	static {
		if(properties == null) {
			//properties = LoadPropertiesFile.getInstance().getGlobalProperties();
			properties = new Properties();
			
			InputStream is = null;
			String strAddr = "10.224.47.203:9092,10.224.47.204:9092,10.224.47.205:9092";
			String strGroup = "dwp";
			try {
				is = KafkaConsumerFactory.class.getClassLoader().getResourceAsStream("kafkaconfig.properties");				 
				properties.load(is);			 

			} catch (Exception ex) {
				properties.put("bootstrap.servers", strAddr);
				properties.put("group.id", strGroup);
				
				//properties.put("bootstrap.servers", "10.224.47.203:9092");
				//properties.put("group.id", "ecmf");
				properties.put("enable.auto.commit", "true");
				properties.put("auto.commit.interval.ms", "1000");
				properties.put("auto.offset.reset", "earliest");
				properties.put("session.timeout.ms", "30000");
				properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
				properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
			} 
			
		}
	}
	
	/**
	 * @Title: getInstance 
	 * @Description: TODO(单例模式，返回唯一实例) 
	 * @return 
	 *         : KafkaConsumerFactory 对象
	 * @throws：
	 */
	public static synchronized KafkaConsumerFactory getInstance() {
		if(null == consumerFactory) {
			consumerFactory = new KafkaConsumerFactory();
		}
		return consumerFactory;
	}
	
	public KafkaConsumer<String, Bytes> getConsumer(String topic){		 
		KafkaConsumer<String, Bytes> kafkaConsumer = new KafkaConsumer<>(properties);
		if( null!=topic && topic.length()>0) {		 
			kafkaConsumer.subscribe(Arrays.asList(topic));
		}
		return kafkaConsumer;
	}
	
	public KafkaConsumer<String, String> getConsumer_(String topic){		 
		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
		if( null!=topic && topic.length()>0) {		 
			kafkaConsumer.subscribe(Arrays.asList(topic));
		}
		return kafkaConsumer;
	}
	
	public KafkaConsumer<String, Bytes> getConsumer(String[] topics){	 
		KafkaConsumer<String, Bytes> kafkaConsumer = new KafkaConsumer<>(properties);
		if( null != topics  ) {			 
				kafkaConsumer.subscribe(Arrays.asList(topics));			 		
		}
		 
		return kafkaConsumer;
	}
	
}
