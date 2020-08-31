package cma.cimiss2.dpc.indb.vdras;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.utils.Bytes;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class VdrasTest {

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
        try {
        	// 消息的value
            String filePath = "/htht/tools/test/data/20190604_010945_vdras_ana_5km.nc";
//        	String filePath = "D:/testData/20190604_010945_vdras_ana_5km.nc";
            File file = new File(filePath);
            Map<String, Object> map = new LinkedHashMap<String, Object>();//用于存储kafka消息key的集合
        	
        	int TypeTag = 1;//是否为四级资料编码：1 是；0否（必填）
        	String TYPE = "F.0052.0001.S001";//TYPETag =1四级资料编码;TYPETag = 0 自定义资料编码（必填）
        	String OTime = "yyyy-MM-dd HH:mm:ss";//资料时间（必填）
        	String InTime = "yyyy-MM-dd HH:mm:ss.SSS";//资料到达时间 YYYY-MM-DD HH:MI:SS.sss
        	String STime = "yyyy-MM-dd HH:mm:ss.SSS";//起报时间
        	String Aging = null;//预报时效
        	String Step = null;//时间间隔
        	String DataType = null;//资料类型
        	String FileName = "20190604_010945_vdras_ana_5km.nc";//资料文件名（必填）
            String NasPath = filePath;//原始文件所在路径（必填）
            String Format = FileName.substring(FileName.lastIndexOf(".")+1);//文件格式
            long FileSize = file.length();//文件大小
            String MD5 = null;//校验码
            
            map.put("TypeTag", TypeTag);
            map.put("TYPE", TYPE);
            map.put("OTime", OTime);
            map.put("InTime", InTime);
            map.put("STime", STime);
            map.put("Aging", Aging);
            map.put("Step", Step);
            map.put("DataType", DataType);
            map.put("FileName", FileName);
            map.put("NasPath", NasPath);
            map.put("Format", Format);
            map.put("FileSize", FileSize);
            map.put("MD5", MD5);
            
            String js = JSONObject.toJSONString(map,SerializerFeature.WriteMapNullValue);//获取json字符串
            byte[] b = js.getBytes();
            if (isAsync) {
                //异步发送消息
                // 第一个参数是ProducerRecord类型的对象，封装了目标Topic、消息的key、消息的value
                // 第二个参数是一个CallBack对象，当生产者接收到Kafka发来的ACK确
                // 认消息的时候，会调用此CallBack对象的onCompletion()方法，实现 回调功能
                ProducerRecord<String, Bytes> record = new ProducerRecord<>(TYPE, Bytes.wrap(b));
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