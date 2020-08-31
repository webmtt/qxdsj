package cma.cimiss2.dpc.indb.rada;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import cma.cimiss2.dpc.indb.sate.TopicAndFilename;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class PUPTest {

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
        try {
        	// 消息的value
//        	List<TopicAndFilename> list = traverseFolder2("F:/dongao/data/PUP");
//            for (TopicAndFilename topicAndFilename : list) {
        	String topic = "J.0003.0001.S001";
        	String filePath = "/htht/tools/test/data/Z_RADR_I_Z9010_20181128235400_P_DOR_SA_CR_10X10_230_NUL.010.bin";
                Map<String, Object> js = new LinkedHashMap<>();
        		js.put("TypeTag", 1);
        		js.put("TYPE", topic);
        		js.put("IIIII", "Z9010");
        		js.put("CCCC", null);
        		js.put("Model", "SA");
        		js.put("Catalog", "CAP");
        		js.put("DPI", null);
        		js.put("Range", null);
        		js.put("Elevation", null);
        		js.put("ID", 9010);
        		js.put("STime", "20190610120000");
        		js.put("FileName", "Z_RADR_I_Z9010_20181128235400_P_DOR_SA_CR_10X10_230_NUL.010.bin");
        		js.put("NasPath", filePath);
        		js.put("Format", "bin");
        		js.put("FileSize", "696");
        		js.put("MD5", " 3b00b1249bbe3551");
        		String str = JSONObject.toJSONString(js,SerializerFeature.WriteMapNullValue);
                if (isAsync) {
                    //异步发送消息
                    ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, str.getBytes("utf-8"));
                    producer.send(record);
                } else {
                    //同步发送消息
                    System.out.println("kafka");
                }
//			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			producer.close();
		}
    }
	
	/**
	 * 获取四级编码和文件名
	 * @param path
	 * @return
	 */
	private static List<TopicAndFilename> traverseFolder2(String path) {

        File dir = new File(path);
        List<TopicAndFilename> list = new ArrayList<TopicAndFilename>();
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (null == files || files.length == 0) {
                System.out.println("文件夹是空的!");
                return list;
            } else {
                for (File file : files) {
	               	TopicAndFilename tf = new TopicAndFilename();
	               	String filePath = file.getAbsolutePath();
	                String filename = file.getName();
	                if(filename.contains("NUL")){
	                	tf.setTopic("J.0003.0002.S001");
	                } else {
	               	 	tf.setTopic("J.0003.0001.S001");
	                }
	                tf.setFilename(filename);
	                tf.setFilePath(filePath);
	                list.add(tf);
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
		return list;
    }
}