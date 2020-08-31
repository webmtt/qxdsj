package org.cimiss2.dwp.Grib;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
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

public class ProcessMessageToIndexDbThread3
{
	protected static final Logger LOGGER = LoggerFactory.getLogger("gribLoggerInfo");
	protected static final Logger LOGGER_E = LoggerFactory.getLogger("gribErrorInfo");
	private String dir;
	private String data_id;

	private static int read_messge_num = 0; //累计读取消息条数
	
	static int count_num = 0; //统计目录下处理了多少个文件
	static int count_num_sucess = 0; //统计成功处理了多少个文件
	
	public ProcessMessageToIndexDbThread3(String dir, String data_id)
	{
		this.dir = dir;
		this.data_id = data_id;		
		
		GribConfig.readConfig(); //读取模式资料配置
		
		GribConfigAreaMapping.readConfig(); //读取模式区域映射配置
	}
	
	public ProcessMessageToIndexDbThread3()
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
	
	/*对一个单场数据文件进行读取并入库*/	
	//返回：0：失败  1：成功
	public int process_into_db(String path_name,String data_id, Date recv_time)
	{
		//判断这个文件是不是以数字作为后缀，如：.1  .2   .178
		///CMADAAS/DATA/NAFP/field_file/F.0013.0001.S001/20200410/A_HUXG10EDZW100000_C_BABJ_20200410040930_42431.bin.1
		String suffix = path_name.substring(path_name.lastIndexOf(".")+1);
		LOGGER.info("suffix:" + suffix);
		if(!isAllNumber(suffix))
		{
			LOGGER_E.error("该文件不以数字结尾："+path_name);
			return 0;
		}
		
		count_num++; //文件数加1
		
		LOGGER.info("begin to process:");
		LOGGER.info("path_name:" + path_name);
		LOGGER.info("file_name:" + data_id);
		LOGGER.info("recv_time:" + recv_time);
		
		//入索引表   
		//返回：0：入库失败; 大于1：入库成功
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
	
	//处理数据文件
	//file_path:带路径的数据:/CMADAAS/DATA/NAFP/field_file/F.0013.0001.S001/20200410/A_HUXG10EDZW100000_C_BABJ_20200410040930_42431.bin.1
	//data_id:四级编码
	//返回  
	//返回：解码成功场数_入Cassandra场数_入索引表场数
	public String proess_data(String file_path,String data_id, Date recv_time)
	{
		int sucess_num = 0; //入库是否成功
		
		LOGGER.info("data:" + file_path + "," + data_id);	
		
		int version = getGribVersion(file_path);
		LOGGER.info("version:"+version);
			
		GribDecode grib_decode = new GribDecode();
		
		//返回：解码成功场数_入Cassandra场数_入索引表场数
		String ret_str = grib_decode.process_field_file(file_path, file_path, version+"", data_id, recv_time, null, null, false, false);
		
		return ret_str;
			
	}
	
	//判断一个字符串是否都是数字
	//true：都是数字；false：并非都是数字
	private boolean isAllNumber(String str)
	{
		for (int i = 0; i < str.length(); i++) 
		{
			//System.out.println(str.charAt(i));
			if (!Character.isDigit(str.charAt(i))) 
			{
				return false;
			}
		}
		
		return true;
	}
	
	//读取该场文件的grib版本号
	//返回：1或2：正常；0：失败
	private int getGribVersion(String file_path)
	{
		DataInputStream dataInStream = null;  //读数据的输入流
		int ret_version = 0;
		try 
	    { 
			dataInStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file_path))); 
	    		    	 	    	 
	    	long byte_count = 0; //记录读了多少个字节
	    	//先定位到开头的“GRIB”
	    	int begin_a = dataInStream.read(); //记录四个开头的字符
	    	int begin_b = dataInStream.read();
	    	int begin_c = dataInStream.read();
	    	int begin_d = dataInStream.read();
	    	byte_count = byte_count + 4;
	    	
	    	while (begin_a!=-1&&begin_b!=-1&&begin_c!=-1&&begin_d!=-1)  //如果读到结尾，跳出
	    	 { 
	    		if(begin_a==0x47&&begin_b==0x52&&begin_c==0x49&&begin_d==0x42) //读到“GRIB”
	    		{
	    			int byte_5 = dataInStream.read(); //第5个字节
	    			 int byte_6 = dataInStream.read();
	    			 int byte_7 = dataInStream.read();
	    			 int version = dataInStream.read(); //版本号
	    			 ret_version = version;
	    			 
	    			 break;
	    		}
	    		
	    		 //往下读一个字节
	    		 begin_a = begin_b;
	    		 begin_b = begin_c;
	    		 begin_c = begin_d;
	    		 begin_d = dataInStream.read();	   
	    		 byte_count++;	    		 
	    		 
	    	 }
	     }
		catch (IOException ex1) 
	     { 
	    	
	    	 //ex1.printStackTrace(); 
	    	 String log_data = "读取文件失败，filename=" + file_path + "," + getSysTime() + ",Exception:" + ex1.getMessage();
	    	 LOGGER_E.error(log_data);
	    	 String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
	    	 GribConfig.write_log_to_file(error_log_file, log_data);
	     } 
	     finally
	     { 
	    	 try 
	    	 { 	    		 
	    		 if(dataInStream!=null)
	    		 {	
	    			 dataInStream.close(); 
	    		 }	 
	    	 } 
	    	 catch (IOException ex2) 
	    	 { 
	    		 ex2.printStackTrace(); 
	    	 } 
	     }
		
		return ret_version;
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
}