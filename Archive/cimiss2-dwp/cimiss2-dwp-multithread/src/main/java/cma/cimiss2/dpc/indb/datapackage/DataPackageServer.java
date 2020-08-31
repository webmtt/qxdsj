package cma.cimiss2.dpc.indb.datapackage;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;
import org.cimiss2.dwp.tools.config.RabbitMQConfig;
import org.nutz.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import cma.cimiss2.dpc.decoder.tools.common.FileDi;
import cma.cimiss2.dpc.decoder.tools.common.SateFileDI;
import cma.cimiss2.dpc.indb.common.FileUtil;
import cma.cimiss2.dpc.indb.datapackage.bean.PackageMessageInfo;
import cma.cimiss2.dpc.indb.datapackage.bean.ReponseInfo;
import cma.cimiss2.dpc.indb.datapackage.bean.RequestInfo;
import cma.cimiss2.dpc.indb.datapackage.bean.RetriveStateInfo;
import cma.cimiss2.dpc.indb.datapackage.util.JaxbObjectAndXmlUtil;
import cma.cimiss2.dpc.indb.datapackage.util.MsgSendUtil;
import cma.cimiss2.dpc.indb.general.common.LoadTableConfig;

public class DataPackageServer {
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// 默认线程吃的大小
	private static int threadCount = 5;
	public static void start() {
		RabbitMQConfig rabbitMQConfig = new RabbitMQConfig();

		ConnectionFactory factory = new ConnectionFactory();
		// 获取rabbitMQ连接信息
		factory.setHost(rabbitMQConfig.getHost());
		factory.setUsername(rabbitMQConfig.getUser());
		factory.setPassword(rabbitMQConfig.getPassword());
		factory.setPort(rabbitMQConfig.getPort());
		infoLogger.info("RabbitMQ Connection Info:"+rabbitMQConfig.getHost()+"@"+rabbitMQConfig.getPort()+"@"+rabbitMQConfig.getUser()+"@"+rabbitMQConfig.getPassword()+"@"+rabbitMQConfig.getQueueName());
		try {
			// 创建RabbitMQ连接
			Connection connection = factory.newConnection();
			// 创建通道
			final Channel channel = connection.createChannel();
			// 声明消费对列，如果队列不存在，则自动创建
			channel.queueDeclare(rabbitMQConfig.getQueueName(), true, false, false, null);
			// 设置每次取一个消息
			channel.basicQos(1);
			// 定义消费者
			Consumer consumer = new DefaultConsumer(channel) {
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					// 获取消息体的内容
					String msg = new String(body, "UTF-8");
					// 接收到的消息内容反序列化为  PackageMessageInfo  实体类
					System.out.println("msg ------------------------   : " + msg);
					PackageMessageInfo packageMessageInfo = JaxbObjectAndXmlUtil.xml2Object(msg, PackageMessageInfo.class);
					
					
					if(packageMessageInfo != null) {
						ReponseInfo reponseInfo = packageMessageInfo.getReponseInfo();
						RequestInfo requestInfo =  packageMessageInfo.getRequestInfo();
						if(reponseInfo != null && requestInfo != null) {
							// 归档系统返回数据的路径
							String fileDirStr = reponseInfo.getFilesDir();
							if(fileDirStr != null && !fileDirStr.equals("")) {
								File file = new File(fileDirStr);
								if(file != null && file.exists() && file.isDirectory()) {
									processMsg(fileDirStr, requestInfo);
								}else {
									infoLogger.error("data package fileDir not find :" +fileDirStr);
								}
							}else {
								infoLogger.error("data package fileDir error!" + msg);
							}
						}
					}else {
						infoLogger.error("PackageMessageInfo packageMessageInfo = JaxbObjectAndXmlUtil.xml2Object(msg, PackageMessageInfo.class); Error" );
						infoLogger.error(msg);
					}	
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			};

			// 绑定消费者
			channel.basicConsume(rabbitMQConfig.getQueueName(), false, consumer);
		} catch (IOException e) {
			infoLogger.error(e.getMessage());
			e.printStackTrace();
		} catch (TimeoutException e) {
			infoLogger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 处理归档回取的消息
	 * @param fileDirStr  归档数据路径
	 * @param requestInfo 归档数据请求信息
	 */
	private static void processMsg(String fileDirStr, RequestInfo requestInfo) {
		RetriveStateInfo retriveStateInfo = new RetriveStateInfo();
		retriveStateInfo.setLinkStartTime(simpleDateFormat.format(new Date()));
		BlockingQueue<String> files = new LinkedBlockingQueue<String>();
		// 获取文件夹目录中的所有文件名
		FileUtil.getFiles(fileDirStr, files);
		// 启用线池进行数据处理
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(threadCount);
		List<FutureTask<List<String>>> tasks = new ArrayList<FutureTask<List<String>>>();
		for (int i = 0; i < threadCount; i++) {
			FutureTask<List<String>> task = null;
			
			if(LoadTableConfig.getInstance().getTablesMaps().containsKey(requestInfo.getRequestDataType())) {
				// 归档雷达资料处理
				if(requestInfo.getRequestDataType().startsWith("J")) {
					BlockingQueue<FileDi> diQueues = new LinkedBlockingQueue<>();
					task = new FutureTask<>(new cma.cimiss2.dpc.indb.rada.IndexSubThread(diQueues, files, requestInfo.getRequestDataType()));
				// 归档卫星资料处理
				}else if (requestInfo.getRequestDataType().startsWith("K")) {
					BlockingQueue<SateFileDI> diQueues = new LinkedBlockingQueue<>();
					task = new FutureTask<>(new cma.cimiss2.dpc.indb.sate.IndexSubThread(diQueues, files, requestInfo.getRequestDataType()));
				}else {
				// 归档其他资料处理
					BlockingQueue<FileDi> diQueues = new LinkedBlockingQueue<>();
					task = new FutureTask<>(new cma.cimiss2.dpc.indb.general.IndexSubThread(diQueues, files, requestInfo.getRequestDataType()));
				}
			}else {
				task = new FutureTask<>(new org.cimiss2.dwp.Grib.MultSubThread(files, requestInfo.getRequestDataType()));
			}
			
			
			fixedThreadPool.submit(task);
			tasks.add(task);
		}
		int fileCount = 0;
		for (int i = 0; i < tasks.size(); i++) {
			try {
				List<String> filenames = tasks.get(i).get();
				fileCount = fileCount + filenames.size();
			} catch (InterruptedException e) {
				infoLogger.error(e.getMessage());
				e.printStackTrace();
			} catch (ExecutionException e) {
				infoLogger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		
		
		String regxString = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$";
		// 编译正则表达式
	    Pattern pattern = Pattern.compile(regxString);
	    
	    if(pattern.matcher(requestInfo.getRequestStartTime()).matches()) {
	    	try {
				retriveStateInfo.setDataStartTime(simpleDateFormat.format(dateFormat.parse(requestInfo.getRequestStartTime())));
			} catch (ParseException e) {
				infoLogger.error(e.getMessage());
				e.printStackTrace();
			}
	    }else {
	    	retriveStateInfo.setDataStartTime(requestInfo.getRequestStartTime());
		}
	    
	    
	    if(pattern.matcher(requestInfo.getRequestEndTime()).matches()) {
	    	try {
				retriveStateInfo.setDataEndTime(simpleDateFormat.format(dateFormat.parse(requestInfo.getRequestEndTime())));
			} catch (ParseException e) {
				infoLogger.error(e.getMessage());
				e.printStackTrace();
			}
	    }else {
			retriveStateInfo.setDataEndTime(requestInfo.getRequestEndTime());
		}
	    
	    
		
		retriveStateInfo.setDataType(requestInfo.getRequestDataType());
		retriveStateInfo.setRetrieveId(requestInfo.getRequestNo());
		
		retriveStateInfo.setProcLink(2);
		if(fileCount == 0) {
			retriveStateInfo.setProcState(1);
		}else {
			retriveStateInfo.setProcState(0);
		}
		
		retriveStateInfo.setLinkEndTime(simpleDateFormat.format(new Date()));
		
		String info = Json.toJson(retriveStateInfo);
		boolean sendFlag = MsgSendUtil.getMsgSendUtil().sendMessage(info);
		
		infoLogger.info("process retrieveId @ " + requestInfo.getRequestNo() +
						"\n dataType @ " + requestInfo.getRequestDataType() + 
						"\n RequestStartTime @ " + requestInfo.getRequestStartTime() +
						"\n RequestEndTime @ " + requestInfo.getRequestEndTime() +
						"\n File Count : " +fileCount + 
						"\n Send Message : " +sendFlag);
		
		fixedThreadPool.shutdown();
		
	}
	
	

}
