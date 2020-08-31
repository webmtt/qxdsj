package cma.cimiss2.dpc.indb.rada;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class ShortTest {
	
	public static void main(String[] args) {
		
		
		String topic = "M.0058.0005.S002";//短期天气公报
//		String filePath = "F:\\dongao\\data\\MSP2_BJ-MO_ MDWB_ME_LNO_BJ_201710230548_00000-24012.XML";
		String filePath = "/htht/tools/test/data/MSP2_BJ-MO_ MDWB_ME_LNO_BJ_201710230548_00000-24012.XML";
		Map<String, Object> js = new LinkedHashMap<>();
		js.put("TypeTag", 1);
		js.put("TYPE", topic);
		js.put("IIIII", "Z9010");
		js.put("CCCC", null);
		js.put("OTime", null);
		js.put("InTime", null);
		js.put("STime", "20190610120000");
		js.put("FileType", null);
		js.put("DataType", null);
		js.put("FileName", "20190420_161205_bjanc_mergedDbz.nc");
		js.put("BBB", null);
		js.put("PQC", 9010);
		js.put("NasPath", filePath);
		js.put("Format", "bz2");
		js.put("FileSize", "696");
		js.put("MD5", " 3b00b1249bbe3551");
		js.put("Lenth", 121);
		String str = JSONObject.toJSONString(js,SerializerFeature.WriteMapNullValue);
		
		//消息发送模式：同步或异步
        boolean isAsync = args.length == 0 || !args[0].trim().equalsIgnoreCase("sync");

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
        try {
        	// 消息的value
            if (isAsync) {
                //异步发送消息
                // 第一个参数是ProducerRecord类型的对象，封装了目标Topic、消息的key、消息的value
                // 第二个参数是一个CallBack对象，当生产者接收到Kafka发来的ACK确
                // 认消息的时候，会调用此CallBack对象的onCompletion()方法，实现 回调功能
                ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, str.getBytes());
                producer.send(record);
            } else {
                //同步发送消息
                //KafkaProducer.send()方法的返回值类型是Future<RecordMetadata>
                //这里通过Future.get()方法，阻塞当前线程，等待Kafka服务端的ACK响应
                System.out.println("kafka");
            }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			producer.close();
		}
		
		
		
	}
}
