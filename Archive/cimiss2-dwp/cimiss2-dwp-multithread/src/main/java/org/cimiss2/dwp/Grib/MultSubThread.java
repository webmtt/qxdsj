package org.cimiss2.dwp.Grib;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import org.cimiss2.dwp.tools.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import cma.cimiss2.dpc.decoder.grib.CassandraDbOperation;
import cma.cimiss2.dpc.decoder.grib.CassandraM4DbOperation;
import cma.cimiss2.dpc.decoder.grib.DataAttr;
import cma.cimiss2.dpc.decoder.grib.GribConfig;
import cma.cimiss2.dpc.decoder.grib.GribDecode;
import cma.cimiss2.dpc.decoder.grib.GribM4Config;
import cma.cimiss2.dpc.decoder.grib.Grib_Struct_Data;
import cma.cimiss2.dpc.decoder.grib.di_ei.SendDiProcess;
import cma.cimiss2.dpc.decoder.grib.di_ei.SendEiProcess;

import com.rabbitmq.client.AMQP.BasicProperties;
import cma.cimiss2.dpc.decoder.grib.GribConfigAreaMapping;

/**
 * <br>
 * MultSubThread 数值模式数据解码入库线程
 * 
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 12/18/2017   zhengbo    Initial creation.
 * </pre>
 * 
 * @author zhengbo
 *
 * 
 */
public class MultSubThread implements Runnable, Callable<List<String>>{
	HashMap<String, Integer> retryMap;
	//public static final Logger LOGGER = LoggerFactory.getLogger(MultSubThread.class);	
	protected static final Logger LOGGER = LoggerFactory.getLogger("gribLoggerInfo");
	protected static final Logger LOGGER_E = LoggerFactory.getLogger("gribErrorInfo");
	public static final Logger messagelogger = LoggerFactory.getLogger("messageInfo"); //消息日志
	public static final String url = "";
	public static File fileR = null;	
	private static int read_messge_num = 0; //累计读取消息条数
	
	//cassandra入库操作类
	private CassandraDbOperation cassadraOper = null;
	
	//M4cassandra入库操作类
	private CassandraM4DbOperation m4cassadraOper = null;
	
	//资料类别标识
	private String data_indentify = "";
	
	private BlockingQueue<String> files;
	private String cts_code = "";
	
	public MultSubThread(String data_indentify) 
	{
		this.data_indentify = data_indentify;
		
		cassadraOper = new CassandraDbOperation();
		m4cassadraOper = new CassandraM4DbOperation();		
		
	}
	
	//归档接口构造函数
	public MultSubThread(BlockingQueue<String> files, String cts_code)
	{
		this.files = files;
		this.cts_code = cts_code;		
		
		cassadraOper = new CassandraDbOperation();
		m4cassadraOper = new CassandraM4DbOperation();		
		
		LOGGER.info("cts_code:" + cts_code);
		//读取data_indentify
		try 
		{
			DataAttr data_attributes = (DataAttr) GribConfig.get_map_data_description().get(cts_code);
			this.data_indentify = data_attributes.getdescription();
		}
		catch (Exception e) {
			e.printStackTrace();
			String data = "读取data_indentify失败," + GribConfig.getSysTime() + "," ;
			LOGGER_E.error(data);
			String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
			GribConfig.write_log_to_file(error_log_file, data);
		}
	}
	
	public void init()
	{
		if(cassadraOper==null)
		{
			cassadraOper = new CassandraDbOperation();
		}
		
		if(m4cassadraOper==null)
		{
			m4cassadraOper = new CassandraM4DbOperation();
		}
	}

	@Override
	public void run() {
		// 读取rabbitmq 配置文件
		//String rabit_mq_path="config/grib_rabitmq_" + data_indentify + ".xml";
		//LOGGER.info("rabitmq config path:"+rabit_mq_path);
		
		RabbitMQConfig  rabbitMQConfig = new RabbitMQConfig();		
		
		try {
			ConnectionFactory factory = new ConnectionFactory();
			// 获取rabbitMQ连接信息
            factory.setHost(rabbitMQConfig.getHost());
            LOGGER.info("rabitmq Host:"+rabbitMQConfig.getHost());
            factory.setUsername(rabbitMQConfig.getUser());
            //LOGGER.info("rabitmq User:"+rabbitMQConfig.getUser());
            factory.setPassword(rabbitMQConfig.getPassword());
            factory.setPort(rabbitMQConfig.getPort());
            //LOGGER.info("rabitmq Port:"+rabbitMQConfig.getPort());
            factory.setAutomaticRecoveryEnabled(true); //设置网络异常重连
            
            factory.setNetworkRecoveryInterval(10000); //每10秒尝试重连一次            
		    
		    // 创建RabbitMQ连接
            Connection connection = factory.newConnection();  
            final Channel channel = connection.createChannel();
            channel.basicQos(1);
            
            LOGGER.info("rabitmq QueueName:"+rabbitMQConfig.getQueueName());
            channel.queueDeclare(rabbitMQConfig.getQueueName(), true, false, false, null);
            
            // 定义消费者
            Consumer consumer = new DefaultConsumer(channel) {
            	@Override
            	public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
            			byte[] body){            		
            		try
            		{            			
	            		Date recv_time = new Date();
	            		String message = new String(body, "UTF-8");
	            		read_messge_num++;
	            		LOGGER.info(data_indentify + "入Cassandra累计读取消息数(message num):" + read_messge_num);
						//获取消息体
	            		//Log4jUtil.getLog4jUtil().info("message : " + message);
	            		LOGGER.info("message : " + message);
	            		messagelogger.info("message:" + message + "\n");
	            		// 消息处理          
	            		//String filename = "测试数据/日本_高分_原始多场/W_NAFP_C_RJTD_20171119000000_GSM_GPV_RGL_GLL0P5DEG_L-PALL_FD0003_GRIB2.BIN";
	            		//String filename = "测试数据/德国_高分_单场/NAFP_EDZW_1_FTM-78-GLB-SOTM-250X250-106-0-0-002-999998-2017111700-0.GRB";
	            		//String filename = "test_data/edzw/NAFP_EDZW_1_FTM-78-GLB-SOTM-250X250-106-0-0-002-999998-2017111700-0.GRB";
	            		//message = "F.0013.0001.R001:" + filename;            		        		
	            		
	            		//处理此消息文件，解码，入Cassandra库
	            		//LOGGER.info("处理消息，解码入Cassandra库");
	            		//int sucess = proess_msg(message, recv_time);	
	            		//int sucess = 1;
	            		//LOGGER.info("sucess:"+sucess);
	            		
	            		//拆分，入索引表   
	            		ProcessMessageToIndexDbThread processMessageThread = new ProcessMessageToIndexDbThread(message, recv_time,channel,envelope,data_indentify,cassadraOper,m4cassadraOper);
	            		//threadPool.execute(processMessageThread); //启动单独线程处理
	            		//int success_num = processMessageThread.proess_msg(message, recv_time); //不启动单独线程，在同一个线程中串行处理
	            		//返回：解码成功场数_入Cassandra场数_入索引表场数_文件读取错误或消息体错误
	            		String ret_str = processMessageThread.proess_msg(message, recv_time); //不启动单独线程，在同一个线程中串行处理
	            		//LOGGER.info("success_num:"+success_num);
	            		LOGGER.info("ret_str:"+ret_str);
	            		String[] ret_str_array = ret_str.split("_");
	            		int decodeSum = Integer.parseInt(ret_str_array[0]);
	            		int cassandraSum = Integer.parseInt(ret_str_array[1]);
	            		int indexSum = Integer.parseInt(ret_str_array[2]);
	            		int fileOrMessageError = Integer.parseInt(ret_str_array[3]);
	            		
	            		//LOGGER.info("sucess:"+sucess);
	            		//删除消息条件：
	        			//1)如果消息体有误或文件读取失败（不存在）
	            		//2)或解码场数为空
	            		//3)或入索引库场数正常（不管入Cassandra，因为不稳定）	  
	            		if(fileOrMessageError==-1||decodeSum==0||(indexSum==decodeSum))
	            		//if(success_num==-1||(sucess==1&&success_num>=1)) //入Cassandra和索引库同时返回成功
	            		{  
	            			LOGGER.info("be able to delete message");
	            			channel.basicAck(envelope.getDeliveryTag(), false);
	            			LOGGER.info("删除消息(delete message):" + message);
	            		}
	            		else //否则，将消息重新放回队列，等待下次处理
	            		{
	            			channel.basicNack(envelope.getDeliveryTag(), false, true);
	            		}
	            		
	           		
	            		//休眠3秒
	            		/*
	            		try {
	            			 LOGGER.info("sleep 3 second...3...");
							Thread.sleep(3000); 
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
						*/
            		}
            		catch(IOException e)
            		{
            			String data = data_indentify + ":处理消息出现异常,Exception:" + e.getMessage()  + GribConfig.getSysTime() + "," ;
            			LOGGER_E.error(data);
            			
            			//使用单独的线程池来入Rest EI
            			if(GribConfig.getRestfulEi().equalsIgnoreCase("1"))
            			{
            				SendEiProcess sendEiProcess = SendEiProcess.getInstance();
            				String KIndex = "RabbitMQ中间件处理消息异常，" + rabbitMQConfig.getHost() + "，" + rabbitMQConfig.getPort() + "，" + e.getMessage();
            				sendEiProcess.process_ei("OP_DPC_A_4_35_02", KIndex, "", "");
            			}
            			
            			String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
            			GribConfig.write_log_to_file(error_log_file, data);
            		}
            	}            	
            };
            
            channel.basicConsume(rabbitMQConfig.getQueueName(), false, consumer);
            
		} catch (Exception e) {
			e.printStackTrace();
			String data = data_indentify + ":rabbit MQ创建连接失败(construct failed),Exception:" + e.getMessage()  + GribConfig.getSysTime() + "," ;
			LOGGER_E.error(data);
			
			//使用单独的线程池来入Rest EI
			if(GribConfig.getRestfulEi().equalsIgnoreCase("1"))
			{
				SendEiProcess sendEiProcess = SendEiProcess.getInstance();
				String KIndex = "RabbitMQ中间件连接异常，" + rabbitMQConfig.getHost() + "，" + rabbitMQConfig.getPort() + "，" + e.getMessage();
				sendEiProcess.process_ei("OP_DPC_A_4_35_02", KIndex, "", "");
			}
			
			String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
			GribConfig.write_log_to_file(error_log_file, data);
		}
		
		
		//test_grib();
		
		/*
		for(int i=1;i<2;i++)
		{
			LOGGER.info("i======" + i);
			//test_grib_thread();
		}
		*/
		
		LOGGER.info("end###");		
		
		//return;
	}
	
	/**
	 * 处理消息文件，进行解码和入库
	 * 
	 * @param message
	 *         消息文件（含路径）
	 * @param recv_time
	 *         消息接收时间 
	 * @return 入库是否全部数据都成功
	 *         1：数据全部入库成功，0：入库有记录失败，-1：消息体有错
	 */
	//处理消息
	//返回  
	//-1：消息体有错; 0：入库有记录失败; 1：入库全部成功
	protected int proess_msg(String message, Date recv_time)
	{
		int sucess = 0; //入库是否成功
		
		int index = message.indexOf(":");
		String message_array[] = message.split(":");
		
		if(message_array.length>=2)
		{
			String data_id = message.substring(0, 16); //四级编码			
			String file_path = message.substring(17); //带路径的数据:/CIMISS2/data/F/F.0006.0001.R001/201712/2017121816/Z_NAFP_C_BABJ_20171218120000_P_NWPC-GRAPES-GFS-HNEHE-00000.grib2

			
			LOGGER.info("message:" + file_path + "," + data_id);		
			
			
			DataAttr data_attributes = (DataAttr) GribConfig.get_map_data_description().get(data_id);
			
			//如果是集合预报，不走这路入Cassandra
			if(data_attributes.getIsEnsemble())
			{
				sucess = 1;
				LOGGER.info("集合预报(IsEnsemble)：" + message + "，不直接入Cassandra，返回（return）.");
				return sucess;
			}
						
			String grib_version = data_attributes.getgribversion();
			if(grib_version.compareToIgnoreCase("1")==0) //grib1版本
			{
				GribDecode grib_decode = new GribDecode();
				Map<String,List<Grib_Struct_Data>> map_grib_decode_result = new HashMap<String,List<Grib_Struct_Data>>();
				
				//解码入库grib1数据
				map_grib_decode_result.clear();	
				map_grib_decode_result = grib_decode.read_grib1_data(file_path,data_id,recv_time);
				LOGGER.info("map_grib_decode_result.grib1.size:"+map_grib_decode_result.size());				
				
				sucess = cassadraOper.into_cassandra_map(map_grib_decode_result);
				
				map_grib_decode_result.clear();
				map_grib_decode_result = null;
				
			}
			else if(grib_version.compareToIgnoreCase("2")==0) //grib2版本
			{
				GribDecode grib_decode = new GribDecode();
				Map<String,List<Grib_Struct_Data>> map_grib_decode_result = new HashMap<String,List<Grib_Struct_Data>>();
				
				//解码入库grib2数据
				map_grib_decode_result.clear();	
				map_grib_decode_result = grib_decode.read_grib2_data(file_path,data_id,recv_time);
				LOGGER.info("map_grib_decode_result.grib2.size:"+map_grib_decode_result.size());
				
				sucess = cassadraOper.into_cassandra_map(map_grib_decode_result);				
				
				map_grib_decode_result.clear();
				map_grib_decode_result = null;
			}
			else if(grib_version.compareToIgnoreCase("12")==0) //同时包含grib1和grib2版本
			{
				GribDecode grib_decode = new GribDecode();				
				
				//解码入库grib1数据
				Map<String,List<Grib_Struct_Data>> map_grib_decode_result1 = new HashMap<String,List<Grib_Struct_Data>>();
				map_grib_decode_result1.clear();	
				map_grib_decode_result1 = grib_decode.read_grib1_data(file_path,data_id,recv_time);
				LOGGER.info("map_grib_decode_result.grib1.size:"+map_grib_decode_result1.size());	
				
				int sucess1 = cassadraOper.into_cassandra_map(map_grib_decode_result1);				
				
				//解码入库grib2数据
				Map<String,List<Grib_Struct_Data>> map_grib_decode_result2 = new HashMap<String,List<Grib_Struct_Data>>();
				map_grib_decode_result2.clear();	
				map_grib_decode_result2 = grib_decode.read_grib2_data(file_path,data_id,recv_time);
				LOGGER.info("map_grib_decode_result.grib2.size:"+map_grib_decode_result2.size());				
					
				int sucess2 = cassadraOper.into_cassandra_map(map_grib_decode_result2);						
				
				//如果grib1和grib2都全部入库成功，则返回：1
				if(sucess1==1&&sucess2==1)
				{
					sucess = 1;
				}
				else //否则，返回：0
				{
					sucess = 0;
				}
				
				map_grib_decode_result1.clear();
				map_grib_decode_result1 = null;
				
				map_grib_decode_result2.clear();
				map_grib_decode_result2 = null;
			}
			
			
		}
		else //消息体有误
		{
			sucess = -1;
			String data = "消息体有错："+message + "," + GribConfig.getSysTime();
			LOGGER_E.error(data);
			String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
			GribConfig.write_log_to_file(error_log_file, data);
		}
		
		return sucess;
		
	}	
	
	//关闭cassandra连接
	public void  close_cassa()
	{
		if(cassadraOper!=null)
		{
			cassadraOper.close();	
		}
		cassadraOper = null;
	}
	
	protected void finalize() throws Throwable
	{
		close_cassa();
		LOGGER.info("线程销毁(thread finalize)...");
        // 递归调用超类中的finalize方法
        super.finalize(); 
	}	
	
	
	//测试数据-用线程
	private void test_grib_thread()
	{
		//String filename = "测试数据/德国_单场/NAFP_EDZW_1_FTM-78-GLB-SOTM-250X250-106-0-0-002-999998-2017111700-0.GRB";
		String filename = "测试数据/日本_高分_原始多场/W_NAFP_C_RJTD_20171206060000_GSM_GPV_RRA2_GLL0P5DEG_LP10_FD0000_GRIB2.BIN";
		String message = "F.0011.0002.R001:" + filename;
		Date recv_time = new Date();
		Channel channel  = null;
		Envelope envelope = null;
		ProcessMessageToIndexDbThread processMessageThread = new ProcessMessageToIndexDbThread(message, recv_time,channel,envelope,data_indentify,cassadraOper,m4cassadraOper);
		//threadPool.execute(processMessageThread);
	}

	@Override
	public List<String> call() throws Exception {
		// TODO Auto-generated method stub
		List<String> listFiles = new ArrayList<>();
		while(this.files.size() > 0) {
			String filename = this.files.poll();
			File file = new File(filename);
			if(file != null && file.exists() && file.isFile()) {
				String message = this.cts_code+":"+filename;
				
				//拆分，入索引表   
				Date recv_time = new Date();
        		ProcessMessageToIndexDbThread processMessageThread = new ProcessMessageToIndexDbThread(message, recv_time,null,null,data_indentify,cassadraOper,m4cassadraOper);
        		//threadPool.execute(processMessageThread); //启动单独线程处理
        		//int success_num = processMessageThread.proess_msg(message,  recv_time); //不启动单独线程，在同一个线程中串行处理
        		String ret_str = processMessageThread.proess_msg(message, recv_time); //不启动单独线程，在同一个线程中串行处理
        		//LOGGER.info("success_num:"+success_num);
        		LOGGER.info("ret_str:"+ret_str);
        		//LOGGER.info("success_num:"+success_num);
				
				listFiles.add(filename);
			}else {
				LOGGER_E.error("filename :" + filename +" not find ");
			}
			
		}
		
		return listFiles;
		
	}
}
