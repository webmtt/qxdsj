package cma.cimiss2.dpc.indb.balloon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.utils.Bytes;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class BalloonTest {

	public static void main(String[] args) {
//        //消息发送模式：同步或异步
//        boolean isAsync = args.length == 0
//                || !args[0].trim().equalsIgnoreCase("sync");
//
//        Properties properties = new Properties();
//        //Kafka服务端的主机名和端口号
//        properties.put("bootstrap.servers", "10.224.47.203:9092");
//        //客户的ID
//        properties.put("client.id", "ProducerDemo");
//     // StringSerializer用来将String对象序列化成字节数组
//        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        // StringSerializer用来将String对象序列化成字节数组
//        properties.put("value.serializer", "org.apache.kafka.common.serialization.BytesSerializer");
//
//        //生产者核心类
//        KafkaProducer<String, Bytes> producer = new KafkaProducer<>(properties);
//        String topic = "B.0010.0001";
//        BufferedReader bufr = null;
//        try {
//        	// 消息的value
//            String filePath = "D:/testData/Z_UPAR_I_54511_20190517231532_O_TEMP-L.txt";
//            File file = new File(filePath);
//            String filename = file.getName();
//            Map<String, String> map = new HashMap<String, String>();
//            map.put("filename", filename);
//            String js = JSONObject.toJSONString(map);
//            String line = null;
//            StringBuffer sb = new StringBuffer();
//            bufr = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
//            while((line = bufr.readLine())!=null){
//            	sb.append(line+"\r\n");
//            }
//            byte[] b = sb.toString().getBytes();
//            
//            if (isAsync) {
//                //异步发送消息
//                // 第一个参数是ProducerRecord类型的对象，封装了目标Topic、消息的key、消息的value
//                // 第二个参数是一个CallBack对象，当生产者接收到Kafka发来的ACK确
//                // 认消息的时候，会调用此CallBack对象的onCompletion()方法，实现 回调功能
//                ProducerRecord<String, Bytes> record = new ProducerRecord<>(topic, js, Bytes.wrap(b));
//                producer.send(record);
//            } else {
//                //同步发送消息
//                //KafkaProducer.send()方法的返回值类型是Future<RecordMetadata>
//                //这里通过Future.get()方法，阻塞当前线程，等待Kafka服务端的ACK响应
//                System.out.println("kafka");
//            }
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			producer.close();
//			try {
//				bufr.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//        
//    }
		
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
//     String topic = "A.0043.0002.S001";
     BufferedReader bufr = null;
     try {
     	String filePath = "D:/testData/Z_UPAR_I_54511_20190517231532_O_TEMP-L.txt";//原始文件所在路径（必填）
     	File file = new File(filePath);
     	String line = null;
         StringBuffer sb = new StringBuffer();
         bufr = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
         while((line = bufr.readLine())!=null){
         	sb.append(line+"\r\n");
         }
         byte[] buf = sb.toString().getBytes();
     	// kafka消息key(模拟)
     	Map<String, Object> map = new LinkedHashMap<String, Object>();//用于存储kafka消息key的集合
     	
     	int TypeTag = 1;//是否为四级资料编码：1 是；0否（必填）
     	String TYPE = "B.0010.0001";//TYPETag =1四级资料编码;TYPETag = 0 自定义资料编码（必填）
     	String IIIII = null;//台站编号：单一台站填写台站编号，多个或者无台站信息填null
     	String CCCC = null;//编报中心：台站上级单位，如果没有填null;
     	String OTime = "yyyy-MM-dd HH:mm:ss";//资料时间（必填）
     	String InTime = "yyyy-MM-dd HH:mm:ss.SSS";//资料接入时间（必填）
     	String STime = "yyyy-MM-dd HH:mm:ss.SSS";//接口发送时间（必填）
     	String FileType = "O";//O为观测类；R为状态类
     	String DataType = null;//AWS-自动气象站地面气象要素资料；AWS-SS_DAY日照观日值测资料；AWS-PRF：降水观测资料; AWS_DAY;日值观测表；不确定或没有填
     	String FileName = "Z_UPAR_I_54511_20190517231532_O_TEMP-L.txt";//资料文件名（必填）
     	String BBB = null;//更正标识eg：CCX。数据更正标识，可选标志，CC为固定代码；x取值为A～X；如果资料不是更正报
     	int PQC = 0;//质量控制标识 =1 已质控 =0 未质控
         String NasPath = filePath;//原始文件所在路径（必填）
         String Format = "TXT";//文件格式
         long FileSize = 944746;//文件大小
         String MD5 = null;//校验码
         int length = 1;//数据块长度，有必要时填写具体数值
         byte[] Data = buf;
         
         map.put("TypeTag", TypeTag);
         map.put("TYPE", TYPE);
         map.put("IIIII", IIIII);
         map.put("CCCC", CCCC);
         map.put("OTime", OTime);
         map.put("InTime", InTime);
         map.put("STime", STime);
         map.put("FileType", FileType);
         map.put("DataType", DataType);
         map.put("FileName", FileName);
         map.put("BBB", BBB);
         map.put("PQC", PQC);
         map.put("NasPath", NasPath);
         map.put("Format", Format);
         map.put("FileSize", FileSize);
         map.put("MD5", MD5);
         map.put("length", length);
//         map.put("Data", Data);
         
         
         String js = JSONObject.toJSONString(map,SerializerFeature.WriteMapNullValue);//获取json字符串
         
         ProducerRecord<String,byte[]> record = new ProducerRecord<String, byte[]>(TYPE,js,Data);
         producer.send(record);
         
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			producer.close();
			try {
				bufr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
