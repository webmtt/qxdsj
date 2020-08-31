package cma.cimiss2.dpc.split_msg;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import cma.cimiss2.dpc.fileloop.common.FileNameCheck;
import cma.cimiss2.dpc.split_msg.bean.DataRouteBean;
import cma.cimiss2.dpc.split_msg.bean.SplitFileName;
import cma.cimiss2.dpc.split_msg.common.DataRouteConfig;
import cma.cimiss2.dpc.split_msg.common.RMQUtil;
import cma.cimiss2.dpc.split_msg.common.SplitConfig;

public class SplitMsgThread implements Runnable {
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private String configFile = "";
	
	public SplitMsgThread(String cfgFile) {
		this.configFile = cfgFile;
	}
	@Override
	public void run() {
		Connection connection = null;
    	Properties fproperties = new Properties();
          // 使用InPutStream流读取properties文件
    	try {
			fproperties.load(new FileInputStream(new File(this.configFile)));
			RMQUtil.setConfigFile(configFile);
			ConnectionFactory factory = new ConnectionFactory();
	    	factory.setHost(fproperties.getProperty("RECE_RMQ_IP").trim());
	    	factory.setUsername(fproperties.getProperty("RECE_RMQ_USER").trim());
	    	factory.setPassword(fproperties.getProperty("RECE_RMQ_PASSWORD").trim());
	    	factory.setPort(Integer.parseInt(fproperties.getProperty("RECE_RMQ_PORT").trim()));
	    	  // 网络异常自动连接恢复
	        factory.setAutomaticRecoveryEnabled(true);
	        // 每10秒尝试重试连接一次
	        factory.setNetworkRecoveryInterval(10000);
	        // 创建连接
	        connection = factory.newConnection();
	        final Channel channel = connection.createChannel();
	        // 声明消费对列，如果队列不存在，则自动创建
            channel.queueDeclare(fproperties.getProperty("RECE_RMQ_QUEUENAME").trim(), true, false, false, null);
	        channel.basicQos(1);

			Consumer consumer = new DefaultConsumer(channel) {
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					messageLogger.info(message);
					int index = message.indexOf(":");
					if(index < 16) {
						infoLogger.error("int index = message.indexOf(:) index < 16");
					}else {
						try {
							process(message, index, fproperties);
							channel.basicAck(envelope.getDeliveryTag(), false);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							infoLogger.error(""+e);
//							channel.basicAck(envelope.getDeliveryTag(), false);
						}
					}
					
				}

			};
			channel.basicConsume(fproperties.getProperty("RECE_RMQ_QUEUENAME").trim(), false, consumer);
		} catch (IOException e) {
			infoLogger.error("", e);
			e.printStackTrace();
		} catch (TimeoutException e) {
			infoLogger.error("", e);
			e.printStackTrace();
		}

	}
	
	
	 /**
     * 
     * @param message
     * @param index
	 * @throws IOException 
	 * @throws InterruptedException 
     */
    private static void process(String message, int index,Properties properties) throws IOException, InterruptedException {
    	// 获取消息中的四级编码
    	String cts_code = message.substring(0, index);
    	// 判断四级编码是否在配置文件内
    	if(SplitConfig.getSplitConfig().getSplitMsgMap().containsKey(cts_code.trim())) {
    		// 获取消息中文件的绝对路径
    		String srcFilePath = message.substring(index+1, message.length());
    		File file = new File(srcFilePath);
    		// 判断文件存在并且是一个文件
    		if(file.exists() && file.isFile()) {
    			// 获取文件名
    			String filename = file.getName();
    			System.out.println("filename : " +filename);
    			// decompress = 0  表示需要解压
    			if(properties.containsKey("decompress") && properties.getProperty("decompress", "0").equalsIgnoreCase("0")) {
    				String cmd = "bzip2 -d "+file.getPath();
    				Process process = Runtime.getRuntime().exec(cmd);
    				process.waitFor();
    				System.out.println(process.getOutputStream().toString());
    				process.destroy();
//    				String tagFile = CompressUtil.decompressBZ2(file, file.getParent(), false);
    				file = new File(file.getParent() + File.separator + filename.split("[.]")[0]);
    				srcFilePath = file.getAbsolutePath();
    				System.out.println("new file : "+ file.getAbsolutePath());
    				if(file.exists() && file.isFile()) {
    					filename = file.getName();
    				}else {
						infoLogger.error("file ont find :" + file.getAbsolutePath());
					}
    			}
    			if(!SplitConfig.getSplitConfig().getSplitMsgMap().containsKey(cts_code)) {
    				infoLogger.error("NEW CTS CODE NOT FIND IN split_message.xml   " + cts_code);
    				return;
    			}
    			
    			if(SplitConfig.getSplitConfig().getSplitMsgMap().get(cts_code).getSplit_expression().trim().equalsIgnoreCase("rex")) {
    				String code = FileNameCheck.txt2String(new  File("config/filematch.txt"), filename).get("data_type").trim();
    				message = code+":"+srcFilePath;
    				 if(DataRouteConfig.getDataRouteConfig().getDataRouteMaps().containsKey(code)) {
						 DataRouteBean dataRouteBean = DataRouteConfig.getDataRouteConfig().getDataRouteMaps().get(code);
						 System.out.println(message);
						 System.out.println(dataRouteBean.getQueueName()+ "============" +dataRouteBean.getExchangeName()+"============="+ dataRouteBean.getRoutingKey());
						 RMQUtil.send_message(message, dataRouteBean.getQueueName(), dataRouteBean.getExchangeName(), dataRouteBean.getRoutingKey());
						 infoLogger.info("send msg :" +message);
						 return;
					 }else {
						 infoLogger.error("NEW CTS CODE NOT FIND IN split_msg_queue_config.xml   " + code);
					}
    			}
    			
    			String[] filenameItems = filename.split("["+ SplitConfig.getSplitConfig().getSplitMsgMap().get(cts_code).getSplit_expression()+"]");
    			
    			for (SplitFileName splitFileName : SplitConfig.getSplitConfig().getSplitMsgMap().get(cts_code).getSplitFileNames()) {
					String split_index = splitFileName.getSplit_index();
					 while (split_index.contains("{")) {
						 int start_index = split_index.indexOf("{");
						 int end_index = split_index.indexOf("}");
						 String subStr = split_index.substring(start_index+1, end_index);
						 if(subStr.contains(":")) {
							 
						 }else {
							int f_index = Integer.parseInt(subStr.trim());
							split_index = split_index.replace(split_index.substring(start_index-1, end_index+1), filenameItems[f_index].trim());
						 }
					 }
					 
					 if(splitFileName.getSplit_content().equalsIgnoreCase(split_index)) {
						 message = splitFileName.getSod_code()+":"+srcFilePath;
						 if(DataRouteConfig.getDataRouteConfig().getDataRouteMaps().containsKey(splitFileName.getSod_code())) {
							 DataRouteBean dataRouteBean = DataRouteConfig.getDataRouteConfig().getDataRouteMaps().get(splitFileName.getSod_code());
							 System.out.println(message);
							 System.out.println(dataRouteBean.getQueueName()+ "============" +dataRouteBean.getExchangeName()+"============="+ dataRouteBean.getRoutingKey());
							 RMQUtil.send_message(message, dataRouteBean.getQueueName(), dataRouteBean.getExchangeName(), dataRouteBean.getRoutingKey());
							 infoLogger.info("send msg :" +message);
						 }
						 break;
					 }else {
						 
						 if(DataRouteConfig.getDataRouteConfig().getDataRouteMaps().containsKey(cts_code)) {
							 DataRouteBean dataRouteBean = DataRouteConfig.getDataRouteConfig().getDataRouteMaps().get(cts_code);
							 System.out.println(message);
							 System.out.println(dataRouteBean.getQueueName()+ "============" +dataRouteBean.getExchangeName()+"============="+ dataRouteBean.getRoutingKey());
							 RMQUtil.send_message(message, dataRouteBean.getQueueName(), dataRouteBean.getExchangeName(), dataRouteBean.getRoutingKey());
							 infoLogger.info("send msg :" +message);
						 }else {
							infoLogger.error("not find " +message);
						 }
//						 infoLogger.error("splitFileName:" + splitFileName.getSplit_content() +" but split_index: " +split_index);
					}
					 
				}
    			
    		}else {
    			infoLogger.error("target file error :" + srcFilePath);
			}
    		
    	}else {
    		infoLogger.info("not find CTS_CODE " + cts_code + "   message : " +message);
		}
		
	}

}
