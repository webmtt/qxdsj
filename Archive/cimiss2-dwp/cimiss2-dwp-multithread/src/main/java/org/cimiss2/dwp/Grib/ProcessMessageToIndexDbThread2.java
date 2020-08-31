package org.cimiss2.dwp.Grib;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cimiss2.dwp.tools.config.StartConfig;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;

import cma.cimiss2.dpc.decoder.grib.DataAttr;
import cma.cimiss2.dpc.decoder.grib.GribConfig;
import cma.cimiss2.dpc.decoder.grib.GribConfigAreaMapping;
import cma.cimiss2.dpc.decoder.grib.GribDecode;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessMessageToIndexDbThread2
{
	protected static final Logger LOGGER = LoggerFactory.getLogger("gribLoggerInfo");
	protected static final Logger LOGGER_E = LoggerFactory.getLogger("gribErrorInfo");
	private String dir;
	private String data_id;

	private static int read_messge_num = 0; //累计读取消息条数
	
	static int count_num = 0; //统计目录下处理了多少个文件
	static int count_num_sucess = 0; //统计成功处理了多少个文件
	
	public ProcessMessageToIndexDbThread2(String dir, String data_id)
	{
		this.dir = dir;
		this.data_id = data_id;		
		
		GribConfig.readConfig(); //读取模式资料配置
		
		GribConfigAreaMapping.readConfig(); //读取模式区域映射配置
	}
	
	public ProcessMessageToIndexDbThread2()
	{
		GribConfig.readConfig(); //读取模式资料配置
		GribConfigAreaMapping.readConfig(); //读取模式区域映射配置
	}
		
	public void processs(String dir,String data_id)
	{
		process_dir(dir,data_id);
		
		LOGGER.info("completed, count_num=" + count_num);
		LOGGER.info("completed, count_num_sucess=" + count_num_sucess);	
		
		LOGGER_E.error("completed, count_num=" + count_num);
		LOGGER_E.error("completed, count_num_sucess=" + count_num_sucess);	
	}

	/*递归处理一个目录*/
	public void process_dir(String dir,String data_id)
	{
		File filedir = new File(dir);	
		
		//如果是目录
		if (filedir.isDirectory())
		{
			String[] filelist = filedir.list();
			for (int i = 0; i < filelist.length; i++)
			{
				String path_name = dir + "/" + filelist[i];
				
				File filelist_dir = new File(path_name);				
				if(filelist_dir.isDirectory()) //如果是目录
				{
					process_dir(path_name,data_id); //递归处理这个目录
				}
				else //如果是文件
				{	
					
					try
					{						
						Date recv_time = new Date();
						process_into_db(path_name,data_id,recv_time); //读取并入库
						
					}
					catch(Exception e)
					{
				        e.printStackTrace();
				        LOGGER_E.error(e.getMessage()+",process_dir中,"+getSysTime());
				        
				    }	
					finally
					{
						LOGGER.info("处理:" + path_name + "完成," + getSysTime());
					}
				}
			}
		}
		else //如果是文件
		{
			LOGGER_E.error("初始参数是文件，应该是个目录");
		}
	}
	
	/*对一个数据文件进行读取并入库*/	
	//返回：0：失败  1：成功
	public int process_into_db(String path_name,String data_id, Date recv_time)
	{
		count_num++;
		
		LOGGER.info("begin to process:");
		LOGGER.info("path_name:" + path_name);
		LOGGER.info("file_name:" + data_id);
		LOGGER.info("recv_time:" + recv_time);
		
		//入索引表   		
		String ret_str = proess_data(path_name,data_id, recv_time);
		LOGGER.info("ret_str:"+ret_str);
		
		String[] ret_str_array = ret_str.split("_");
		int decodeSum = Integer.parseInt(ret_str_array[0]);
		int cassandraSum = Integer.parseInt(ret_str_array[1]);
		int indexSum = Integer.parseInt(ret_str_array[2]);
		
		if(decodeSum==indexSum)
		{
			LOGGER.info("decodeSum==indexSum,成功," + path_name);
			count_num_sucess++; //处理成功文件数加1
			return 1;
		}
		else
		{
			LOGGER_E.error("decodeSum!=indexSum,失败" + path_name);
			return 0;
		}	
		
	}
	
	/**
	 * 获得系统时间
	 * 
	 * @param
	 */
	public String getSysTime()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String currentSysTime = dateFormat.format(date);
		return currentSysTime;
	}
	
	//处理数据文件
	//file_path:带路径的数据:/CIMISS2/data/F/F.0006.0001.R001/201712/2017121816/Z_NAFP_C_BABJ_20171218120000_P_NWPC-GRAPES-GFS-HNEHE-00000.grib2
	//data_id:四级编码
	//返回：  解码成功场数_入Cassandra场数_入索引表场数_文件读取错误
	public String proess_data(String file_path,String data_id, Date recv_time)
	{		
		LOGGER.info("data:" + file_path + "," + data_id);			
			
		GribDecode grib_decode = new GribDecode();
		
		DataAttr data_attributes = (DataAttr) GribConfig.get_map_data_description().get(data_id);
		String field_organize = data_attributes.getfieldorganize();
					
		String ret_str = "";
		if("common".compareToIgnoreCase(field_organize)==0) //将原始多场文件拆分成单场文件：GRIB...7777...GRIB...7777
		{				
			//倒数第2个参数是null，不入Cassandra
			//最后1个参数是false，不入单场信息索引表
			//返回：解码成功场数_入Cassandra场数_入索引表场数_文件读取错误
			ret_str = grib_decode.split_grib_record_use_S0Lengh(file_path, data_id, recv_time,null,null,false,false); 			
		}
		else if("other".compareToIgnoreCase(field_organize)==0) //将原始多场文件拆分成单场文件：循环嵌套（如日本高分）
		{
			//倒数第2个参数是null，不入Cassandra
			//最后1个参数是false，不入单场信息索引表
			//返回：解码成功场数_入Cassandra场数_入索引表场数_文件读取错误
			ret_str = grib_decode.split_grib_record_other(file_path, data_id, recv_time,null,null,false,false);
		}	
		
		return ret_str;
			
	}
}