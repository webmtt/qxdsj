package cma.cimiss2.dpc.indb.sate;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class FY2GTest {

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
//        String topic = "K.0480.0001.S001";
//        String topic = "K.0472.0001.S001";
        String topic = "K.0493.0001.S001";
//        String topic = "K.0488.0001.S001";
        try {
        	// 消息的value
//            String messageValue = "D:/testData/Z_SATE_C_BAWX_20180831163501_P_FY2G_PRE_001_OTG_20180831_1600.AWX";
//            String filename = "Z_SATE_C_BAWX_20180831163501_P_FY2G_PRE_001_OTG_20180831_1600.AWX";
//            String messageValue = "D:/testData/Z_SATE_C_BAWX_20180831161831_P_FY2G_SEC_IR1_LCN_20180831_1600.AWX";
//            String filename = "Z_SATE_C_BAWX_20180831161831_P_FY2G_SEC_IR1_LCN_20180831_1600.AWX";
//            String messageValue = "D:/testData/Z_SATE_C_BAWX_20180831163636_P_FY2G_LST_MLT_OTG_20180831_1600.AWX";
            String filename = "Z_SATE_C_BAWX_20180831163636_P_FY2G_LST_MLT_OTG_20180831_1600.AWX";
            String messageValue = "/htht/tools/test/data/Z_SATE_C_BAWX_20180831163636_P_FY2G_LST_MLT_OTG_20180831_1600.AWX";
//            String filename = "Z_SATE_C_BAWX_20180831164937_P_FY2G_HPF_500_OTG_20180831_1600.AWX";
//            List<TopicAndFilename> list = traverseFolder2("D:/testData/FY-2G");
//            for (TopicAndFilename topicAndFilename : list) {
//            	Map<String, String> map = new HashMap<String, String>();
//                map.put("filePath", topicAndFilename.getFilePath());
//                map.put("filename", topicAndFilename.getFilename());
//                map.put("sodCode", topicAndFilename.getTopic());
//                String js = JSONObject.toJSONString(map);
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
                if (isAsync) {
                    //异步发送消息
                    ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, str.getBytes());
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
                     if(filename.contains("SEC")){
                    	 if(filename.contains("LCN")){
                    		 tf.setTopic("K.0472.0001.S001");
                    	 } else {
                    		 tf.setTopic("K.0473.0001.S001");
                    	 }
                     } else if (filename.contains("HPF")) {
                    	 tf.setTopic("K.0488.0001.S001");
                     } else if (filename.contains("PRE")) {
                    	 tf.setTopic("K.0480.0001.S001");
                     } else {
                    	 tf.setTopic("K.0493.0001.S001");
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