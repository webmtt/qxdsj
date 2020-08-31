package cma.cimiss2.dpc.indb.meofis;

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
import com.alibaba.fastjson.serializer.SerializerFeature;

public class MeofisTest {
	

	public static void main(String[] args) {
		
		Properties properties = new Properties();
        //Kafka服务端的主机名和端口号
        properties.put("bootstrap.servers", "10.224.47.203:9092");
        //客户的ID
        properties.put("client.id", "ProducerDemo");
     // StringSerializer用来将String对象序列化成字节数组
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // StringSerializer用来将String对象序列化成字节数组
        properties.put("value.serializer", "org.apache.kafka.common.serialization.BytesSerializer");
        KafkaProducer<String, Bytes> producer = new KafkaProducer<>(properties);
		// 发送发字节数组
		JSONObject js = new JSONObject(true);
		js.put("datetype", "meofis");
		js.put("datatime", "20170901000016");
		js.put("data", "D:/logs/meofis/SEVP_NMC_RFFC_SCMOC_EME_ACHN_L88_P9_20170909000016812.TXT");
		String topic = "M.0002.0005.S002";
		String str = JSONObject.toJSONString(js,SerializerFeature.WriteMapNullValue);  
		ProducerRecord<String, Bytes> record = new ProducerRecord<>(topic,topic, Bytes.wrap(str.getBytes()));
		producer.send(record);
		producer.close();

	}
}
