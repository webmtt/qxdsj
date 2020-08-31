package org.cimiss2.dwp.Grib;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;

import cma.cimiss2.dpc.decoder.grib.CassandraDbOperation;
import cma.cimiss2.dpc.decoder.grib.CassandraM4DbOperation;
import cma.cimiss2.dpc.decoder.grib.DataAttr;
import cma.cimiss2.dpc.decoder.grib.GribConfig;
import cma.cimiss2.dpc.decoder.grib.GribDecode;
import cma.cimiss2.dpc.decoder.grib.Grib_Struct_Data;
import cma.cimiss2.dpc.decoder.grib.di_ei.SendEiProcess;

import org.cimiss2.dwp.tools.config.StartConfig;
//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessMessageToIndexDbThread
{
	//protected static final Logger LOGGER = LoggerFactory.getLogger(ProcessMessageToIndexDbThread.class);
	protected static final Logger LOGGER = LoggerFactory.getLogger("gribLoggerInfo");
	protected static final Logger LOGGER_E = LoggerFactory.getLogger("gribErrorInfo");
	private String message;
	private Date recv_time;
	private String data_indentify;
	private static int read_messge_num = 0; //累计读取消息条数
	private CassandraDbOperation cassadraOper = null; 
	private CassandraM4DbOperation m4cassadraOper = null; 
	
	public ProcessMessageToIndexDbThread(String message, Date recv_time,Channel channel,Envelope envelope,String data_indentify,
			CassandraDbOperation cassadraOper,CassandraM4DbOperation m4cassadraOper)
	{
		this.message = message;
		this.recv_time = recv_time;
		this.data_indentify = data_indentify;
		this.cassadraOper = cassadraOper;
		this.m4cassadraOper = m4cassadraOper;
	}
	
	public ProcessMessageToIndexDbThread()
	{
		
	}
	
	public void run()
	{	
		LOGGER.info("单独线程处理，拆分后，解码入mysql索引库(individual thread: split and decode to mysql database.)");
		read_messge_num++;
		LOGGER.info(data_indentify + "入索引表累计读取消息数(message num):" + read_messge_num);
		try
		{
			//int sucess_num = proess_msg(message, recv_time);
			String ret_str = proess_msg(message, recv_time);
			LOGGER.info("ret_str:"+ret_str);
			
			/*
			if(sucess_num==-1||sucess_num>=1){
				// 消息确认机制
				//channel.basicAck(envelope.getDeliveryTag(), false);	
				if(StartConfig.fileLoop() == 1) {
					String message_array[] = message.split(":");
					File file = new File(message_array[1]);
					if(file.exists()) {
						file.delete();
						LOGGER.info("历史数据入ots处理完成,删除数据:" + message);
					}
				}
			}
			*/
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}
	
	//处理消息入索引表
	//返回  
	//-1：消息体有错或文件读取失败（不存在）; 0：入库失败; 大于1：入库成功
	//返回：解码成功场数_入Cassandra场数_入索引表场数_文件读取错误或消息体错误
	public String proess_msg(String message, Date recv_time)
	{
		int sucess_num = 0; //入库是否成功
		String ret_str = ""; //返回：解码成功场数_入Cassandra场数_入索引表场数_文件读取错误
		
		String message_array[] = message.split(":");
		
		if(message_array.length>=2)
		{
			String data_id = message_array[0]; //四级编码			
			String file_path = message_array[1]; //带路径的数据:/CIMISS2/data/F/F.0006.0001.R001/201712/2017121816/Z_NAFP_C_BABJ_20171218120000_P_NWPC-GRAPES-GFS-HNEHE-00000.grib2
			LOGGER.info("message:" + file_path + "," + data_id);			
			
			GribDecode grib_decode = new GribDecode();
			
			DataAttr data_attributes = (DataAttr) GribConfig.get_map_data_description().get(data_id);
			int index_type = data_attributes.getindex_type();
			String grib_version = data_attributes.getgribversion();
			String field_organize = data_attributes.getfieldorganize();			
			
			if(index_type==0) //GRIB拆分入库
			{
				if("common".compareToIgnoreCase(field_organize)==0) //将原始多场文件拆分成单场文件：GRIB...7777...GRIB...7777
				{					
					//倒数第3个参数如果是null，不入Cassandra；
					//倒数第2个参数是true，入单场信息索引表
					//最后1参数是true，实时流程发送DI
					//sucess_num = grib_decode.split_grib_record_use_S0Lengh(file_path, data_id, recv_time,cassadraOper,true);	
					//返回：解码成功场数_入Cassandra场数_入索引表场数_文件读取错误
					ret_str = grib_decode.split_grib_record_use_S0Lengh(file_path, data_id, recv_time,cassadraOper,m4cassadraOper,true,true);	
				}
				else if("other".compareToIgnoreCase(field_organize)==0) //将原始多场文件拆分成单场文件：循环嵌套（如日本高分）
				{				
					//倒数第3个参数如果是null，不入Cassandra；
					//倒数第2个参数是true，入单场信息索引表
					//最后1参数是true，实时流程发送DI
					//sucess_num = grib_decode.split_grib_record_other(file_path, data_id, recv_time,cassadraOper,true);
					//返回：解码成功场数_入Cassandra场数_入索引表场数_文件读取错误
					ret_str = grib_decode.split_grib_record_other(file_path, data_id, recv_time,cassadraOper,m4cassadraOper,true,true);
				}
			}
			else if(index_type==1) //解析文件名+入Cassandra(智能网格GRIB、nc产品)
			{
				//解析文件名，并入索引表
				String ret_nwfd = grib_decode.process_nwfd_file(file_path, data_id, recv_time);
				
				String[] ret_nwfd_array = ret_nwfd.split("_");
        		
				int decode_num = Integer.parseInt(ret_nwfd_array[0]); //解码成功数
				int index_num = Integer.parseInt(ret_nwfd_array[1]);; //入库成功数				
				
				//入Cassandra
				int cassandraSum = 0;
				if(grib_version.equalsIgnoreCase("1")) //grib版本1
				{
					Map<String,List<Grib_Struct_Data>> map_grib_decode_result1 = new HashMap<String,List<Grib_Struct_Data>>();
								
					map_grib_decode_result1.clear();	
					map_grib_decode_result1 = grib_decode.read_grib1_data(file_path, data_id, recv_time);
					LOGGER.info("map_grib_decode_result1.size:" +map_grib_decode_result1.size());					
					
					int sucess1 = cassadraOper.into_cassandra_map(map_grib_decode_result1);
					
					LOGGER.info("sucess=" + sucess1);
					
					cassandraSum = sucess1;
					
					ret_str = decode_num + "_" + cassandraSum + "_"+ index_num + "_0"; 
					
				}
				else if(grib_version.equalsIgnoreCase("2")) //grib版本2
				{
					Map<String,List<Grib_Struct_Data>> map_grib_decode_result2 = new HashMap<String,List<Grib_Struct_Data>>();
						
					map_grib_decode_result2.clear();	
					map_grib_decode_result2 = grib_decode.read_grib2_data(file_path, data_id, recv_time);
					LOGGER.info("map_grib_decode_result2.size:" +map_grib_decode_result2.size());				
					
						
					int sucess2 = cassadraOper.into_cassandra_map(map_grib_decode_result2);			
					
					LOGGER.info("sucess=" + sucess2);
					
					cassandraSum = sucess2;
					
					ret_str = decode_num + "_" + cassandraSum + "_"+ index_num + "_0"; 
				}
				else if(grib_version.equalsIgnoreCase("12")) //同时包含grib版本1和//grib版本2
				{
					Map<String,List<Grib_Struct_Data>> map_grib_decode_result1 = new HashMap<String,List<Grib_Struct_Data>>();
					Map<String,List<Grib_Struct_Data>> map_grib_decode_result2 = new HashMap<String,List<Grib_Struct_Data>>();
								
					map_grib_decode_result1.clear();	
					map_grib_decode_result1 = grib_decode.read_grib1_data(file_path, data_id, recv_time);
					LOGGER.info("map_grib_decode_result1.size:" +map_grib_decode_result1.size());
					
					map_grib_decode_result2.clear();	
					map_grib_decode_result2 = grib_decode.read_grib2_data(file_path, data_id, recv_time);
					LOGGER.info("map_grib_decode_result2.size:" +map_grib_decode_result2.size());
					
					
					int sucess1 = cassadraOper.into_cassandra_map(map_grib_decode_result1);	
						
					int sucess2 = cassadraOper.into_cassandra_map(map_grib_decode_result2);			
					
					LOGGER.info("sucess1=" + sucess1);
					LOGGER.info("sucess2=" + sucess2);
					
					if(sucess1==1&&sucess2==1)
					{
						cassandraSum = 1;
					}
					else
					{
						cassandraSum = 0;
					}
					
					ret_str = decode_num + "_" + cassandraSum + "_"+ index_num + "_0"; 
				}
				else if(grib_version.equalsIgnoreCase("0")) //不是grib格式
				{
					LOGGER.info("field_organize:" + field_organize);
				}
			}
			else if(index_type==2) //只解析文件名(智能网格SPCC文件)
			{
				//只解析文件名
				//解析文件名，并入索引表
				String ret_nwfd = grib_decode.process_nwfd_file(file_path, data_id, recv_time);
				
				String[] ret_nwfd_array = ret_nwfd.split("_");
        		
				int decode_num = Integer.parseInt(ret_nwfd_array[0]); //解码成功数
				int index_num = Integer.parseInt(ret_nwfd_array[1]);; //入库成功数	
				
				ret_str = decode_num + "_" + 0 + "_"+ index_num + "_0"; 
			}
			else if(index_type==3) //只入Cassandra，针对已经入了索引表的网格资料
			{
				//入Cassandra
				if(grib_version.equalsIgnoreCase("1")) //grib版本1
				{
					Map<String,List<Grib_Struct_Data>> map_grib_decode_result1 = new HashMap<String,List<Grib_Struct_Data>>();
								
					map_grib_decode_result1.clear();	
					map_grib_decode_result1 = grib_decode.read_grib1_data(file_path, data_id, recv_time);
					LOGGER.info("map_grib_decode_result1.size:" +map_grib_decode_result1.size());					
					
					int sucess1 = cassadraOper.into_cassandra_map(map_grib_decode_result1);
					
					LOGGER.info("sucess=" + sucess1);
					
					int decodeSum = 0;
					if(map_grib_decode_result1.size()>0)
					{
						decodeSum = 1;
					}
					ret_str = decodeSum + "_" + sucess1 + "_1_0";
					
					/*
					if(sucess1==1)
					{
						sucess_num = 1;
					}
					*/
					
				}
				else if(grib_version.equalsIgnoreCase("2")) //grib版本2
				{
					Map<String,List<Grib_Struct_Data>> map_grib_decode_result2 = new HashMap<String,List<Grib_Struct_Data>>();
						
					map_grib_decode_result2.clear();	
					map_grib_decode_result2 = grib_decode.read_grib2_data(file_path, data_id, recv_time);
					LOGGER.info("map_grib_decode_result2.size:" +map_grib_decode_result2.size());				
					
						
					int sucess2 = cassadraOper.into_cassandra_map(map_grib_decode_result2);			
					
					LOGGER.info("sucess=" + sucess2);
					
					int decodeSum = 0;
					if(map_grib_decode_result2.size()>0)
					{
						decodeSum = 1;
					}
					ret_str = decodeSum + "_" + sucess2 + "_1_0";
					
					/*
					if(sucess2==1)
					{
						sucess_num = 1;
					}
					*/
				}
				else if(grib_version.equalsIgnoreCase("12")) //同时包含grib版本1和//grib版本2
				{
					Map<String,List<Grib_Struct_Data>> map_grib_decode_result1 = new HashMap<String,List<Grib_Struct_Data>>();
					Map<String,List<Grib_Struct_Data>> map_grib_decode_result2 = new HashMap<String,List<Grib_Struct_Data>>();
								
					map_grib_decode_result1.clear();	
					map_grib_decode_result1 = grib_decode.read_grib1_data(file_path, data_id, recv_time);
					LOGGER.info("map_grib_decode_result1.size:" +map_grib_decode_result1.size());
					
					map_grib_decode_result2.clear();	
					map_grib_decode_result2 = grib_decode.read_grib2_data(file_path, data_id, recv_time);
					LOGGER.info("map_grib_decode_result2.size:" +map_grib_decode_result2.size());
					
					
					int sucess1 = cassadraOper.into_cassandra_map(map_grib_decode_result1);	
						
					int sucess2 = cassadraOper.into_cassandra_map(map_grib_decode_result2);			
					
					LOGGER.info("sucess1=" + sucess1);
					LOGGER.info("sucess2=" + sucess2);
					
					int decodeSum = 0;
					if(map_grib_decode_result1.size()>0||map_grib_decode_result2.size()>0)
					{
						decodeSum = 1;
					}
					int cassandraSum = 0;
					if(sucess1==1&&sucess2==1)
					{
						cassandraSum = 1;
					}
					ret_str = decodeSum + "_" + cassandraSum + "_1_0";
					
					/*
					if(sucess1==1&&sucess2==1)
					{
						sucess_num = 1;
					}
					*/
				}
				else if(grib_version.equalsIgnoreCase("0")) //不是grib格式
				{
					LOGGER.info("field_organize:" + field_organize);
				}
			}
			
				
				
		}
		else //消息体有误
		{
			//sucess_num = -1;
			ret_str = "0_0_0_-1";
			
			//使用单独的线程池来入Rest EI
			if(GribConfig.getRestfulEi().equalsIgnoreCase("1"))
			{
				SendEiProcess sendEiProcess = SendEiProcess.getInstance();
				String KEvent = "消息格式异常：" + message;
				sendEiProcess.process_ei("OP_DPC_A_4_35_04", KEvent, "", "");
			}
		}
		
		//return sucess_num;
		return ret_str;
	}	
}