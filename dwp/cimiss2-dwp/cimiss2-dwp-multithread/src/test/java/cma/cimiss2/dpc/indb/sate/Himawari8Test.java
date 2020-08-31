package cma.cimiss2.dpc.indb.sate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Himawari8Test {

	public static void main(String[] args) {
        //消息发送模式：同步或异步
        boolean isAsync = args.length == 0
                || !args[0].trim().equalsIgnoreCase("sync");
        Properties properties = new Properties();
        //Kafka服务端的主机名和端口号
        properties.put("bootstrap.servers", "10.224.47.203:9092");
        //客户的ID
        properties.put("client.id", "ProducerDemo");
     // StringSerializer用来将String对象序列化成字节数组
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // StringSerializer用来将String对象序列化成字节数组
        properties.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        //生产者核心类
        KafkaProducer<String, byte[]> producer = new KafkaProducer<>(properties);
        String topic = "K.0505.0002.S001";
        try {
        	// 消息的value
            String messageValue = "/htht/tools/test/data/HS_H08_20190329_0000_B08_FLDK_R05_S0110.DAT";
            String filename = "HS_H08_20190329_0000_B08_FLDK_R05_S0110.DAT";
            Map<String, Object> js = new LinkedHashMap<>();
    		js.put("TypeTag", 1);
    		js.put("TYPE", topic);
    		js.put("ProjType", null);
    		js.put("STime", "20190610120000");
    		js.put("DataType", null);
    		js.put("InTime", null);
    		js.put("OTime", null);
    		js.put("FileName", filename);
    		js.put("NasPath", messageValue);
    		js.put("Format", "DAT");
    		js.put("FileSize", "696");
    		js.put("MD5", " 3b00b1249bbe3551");
    		String str = JSONObject.toJSONString(js,SerializerFeature.WriteMapNullValue);
//            Map<String, String> map = new HashMap<String, String>();
//            map.put("filePath", messageValue);
//            map.put("filename", filename);
//            String js = JSONObject.toJSONString(map);
            if (isAsync) {
                //异步发送消息
                ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, str.getBytes());
                producer.send(record);
            } else {
                //同步发送消息
                System.out.println("kafka");
            }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			producer.close();
		}
    }
}