package cma.cimiss2.dpc.indb.cspi;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.utils.Bytes;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class CspiTest {
	

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
		js.put("datetype", "cspi");
		js.put("datatime", "20170901000016");
		js.put("data", "D:/logs/cspi/Z_SEVP_C_BABJ_20190121120456_P_CSPI-SFER-ESCP-ACHN-LNO-P9-201901212000-01206.MIC");
		String topic = "M.0035.0002.S001";
		String str = JSONObject.toJSONString(js,SerializerFeature.WriteMapNullValue);  
		ProducerRecord<String, Bytes> record = new ProducerRecord<>(topic,topic, Bytes.wrap(str.getBytes()));
		producer.send(record);
		producer.close();

	}
}
