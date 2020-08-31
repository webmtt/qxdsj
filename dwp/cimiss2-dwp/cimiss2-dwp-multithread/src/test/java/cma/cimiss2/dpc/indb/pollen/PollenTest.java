package cma.cimiss2.dpc.indb.pollen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.utils.Bytes;

import com.alibaba.fastjson.JSONObject;

public class PollenTest {

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
        properties.put("value.serializer", "org.apache.kafka.common.serialization.BytesSerializer");

        //生产者核心类
        KafkaProducer<String, Bytes> producer = new KafkaProducer<>(properties);
        String topic = "G.0024.0001.S001";
        BufferedReader bufr = null;
        try {
        	// 消息的value
            String filePath = "D:/testData/YJHF20180805.txt";
            String filename = "YJHF20180805.txt";
            Map<String, String> map = new HashMap<String, String>();
            map.put("filename", filename);
            String js = JSONObject.toJSONString(map);//文件名作为参数传递
            File file = new File(filePath);
            String line = null;
            StringBuffer sb = new StringBuffer();
            bufr = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            while((line = bufr.readLine())!=null){
            	sb.append(line+"\r\n");
            }
            byte[] b = sb.toString().getBytes();
            
            if (isAsync) {
                //异步发送消息
                // 第一个参数是ProducerRecord类型的对象，封装了目标Topic、消息的key、消息的value
                // 第二个参数是一个CallBack对象，当生产者接收到Kafka发来的ACK确
                // 认消息的时候，会调用此CallBack对象的onCompletion()方法，实现 回调功能
                ProducerRecord<String, Bytes> record = new ProducerRecord<>(topic, js, Bytes.wrap(b));
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
			try {
				bufr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        
    }
}
