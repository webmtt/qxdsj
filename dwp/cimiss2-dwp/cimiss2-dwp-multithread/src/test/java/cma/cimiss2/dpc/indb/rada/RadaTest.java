package cma.cimiss2.dpc.indb.rada;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class RadaTest {

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
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        //生产者核心类
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
//        String topic = "J.0010.0001.R001";// 
//        String topic = "J.0010.0001.R003";// 雷达单仰角
//        String topic = "J.0010.0001.R004";// cut
//        String topic = "J.0010.0001.R005";// vcp
//        String topic = "J.0010.0001.R006";// 南郊雷达所有仰角
//        String topic = "J.0010.0001.S090";// 城市院的京津冀雷达拼图
//        String topic = "J.0010.0001.S006";//逐月ERSST
//        String topic = "J.0010.0001.S008";//怀来车载x波段雷达数据
        String topic = "J.0010.0001.S009";//x波段雷达数据
//        String topic = "J.0010.0001.S010";//信息中心的京津冀雷达拼图
        try {
        	// 消息的value
//            String messageValue = "D:/Z_RADR_I_Z9352_20181029193200_O_DOR_CB_CAP.bin";
//            String messageValue = "F:/dongao/data/Z_RADR_I_Z9010_20190521061200_O_DOR-CUT_SA_CAP_5_1.bin";//雷达单仰角
//            String messageValue = "F:/dongao/data/Z_RADR_I_Z9010_20181030000000_O_DOR_SA_CAP.bin";// 南郊雷达所有仰角
//            String messageValue = "F:/dongao/data/Z_RADR_I_Z9010_20190520000000_O_DOR-CUT_SA_CAP_5_1_FMT.bin";//cut
//            String messageValue = "F:/dongao/data/Z_RADR_I_Z9220_20190520000000_O_DOR_SA_CAP_FMT.bin";//vcp
//            String messageValue = "F:/dongao/data/20190420_161205_bjanc_mergedDbz.nc";//城市院的京津冀雷达拼图
//            String messageValue = "F:/dongao/data/sst.mnmean.nc";//逐月ERSST
//            String messageValue = "F:/dongao/data/VTB20181030135613.011.zip";//怀来车载x波段雷达数据
            String messageValue = "F:/dongao/data/BJXCP.20180919.160900.AR2.bz2";//x波段雷达数据
//            String messageValue = "F:/dongao/data/Z_OTHE_RADAMOSAIC_20190521073000.bin";//信息中心的京津冀雷达拼图
            if (isAsync) {
                //异步发送消息
                // 第一个参数是ProducerRecord类型的对象，封装了目标Topic、消息的key、消息的value
                // 第二个参数是一个CallBack对象，当生产者接收到Kafka发来的ACK确
                // 认消息的时候，会调用此CallBack对象的onCompletion()方法，实现 回调功能
                ProducerRecord<String, String> record = new ProducerRecord<>(topic, messageValue, messageValue);
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
