package cma.cimiss2.dpc.decoder.grib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GribConfig
{
	//private static final Logger logger = LoggerFactory.getLogger(GribConfig.class);
	protected static final Logger logger = LoggerFactory.getLogger("gribLoggerInfo");
	protected static final Logger logger_e = LoggerFactory.getLogger("gribErrorInfo");
	
	public static SAXBuilder builder = new SAXBuilder();//XML解析器
	public static final String STATIC_SYSTEM_CONFIG_FILENAME = "config/cimiss2_dwp_decode_grib_decode.xml";
	
	//写NAS文件的路径，从xml中读取
	//完整路径：Path_NAS_file/F.0010/20170802/EC_TEM_2017080216_GLB.grib
	//单场数据路径：Path_NAS_file/F.0010/20170802/Field_file/W_NAFP_C_ECMF_20170802061826_P_C1D11190000112218001.1
	private static String Path_NAS_file = ""; //写NAS大文件的路径  
	private static String log_file_path = ""; //写日志的路径
	private static String send_restful_di = ""; //是否发送rest的di
	private static int dataBaseType = 0; //数据库类型 =1阿里云DRDS数据库， =0万里开源数据库
	private static String SEND = ""; //DI发送：资料去向，目标数据库的业务标识名称
	private static String SEND_PHYS = ""; //DI发送： 物理数据库名称
	private static String SEND2 = ""; //DI发送：资料去向，实时数据库的业务标识名称 
	private static String SEND_PHYS2 = ""; //DI发送： 实时数据物理库名称
	private static String DATA_FLOW = ""; //DI发送： BDMAIN:大数据平台主流程； BDBAK ：大数据平台备份流程
	private static String targetURL = ""; //批量发送接口url
	private static String targetURL_single = ""; //单条发送接口url
	
	private static Map<String,DataAttr> map_data_Attr = new HashMap<String,DataAttr>();
	
	private static String send_restful_ei = ""; //是否发送rest的ei
	
	public GribConfig()
	{		
	}
	
	public static void readConfig() 
	{
		File configFile = null;
		try 
		{
			configFile = new File(STATIC_SYSTEM_CONFIG_FILENAME);
			Document doc = builder.build(configFile);
			Element root = doc.getRootElement();//根元素<root>
			
			Path_NAS_file = root.getChild( "path_NAS_file" ).getValue(); //读取path_NAS_file
			
			log_file_path = root.getChild( "log_file_path" ).getValue(); //读取log_file_path			
			
			send_restful_di = root.getChild( "rest_di" ).getValue().trim(); //读取是否发送rest_di
			logger.info("send_restful_di:" + send_restful_di);	
			
			send_restful_ei = root.getChild( "rest_ei" ).getValue().trim(); //读取是否发送rest_ei
			logger.info("send_restful_ei:" + send_restful_ei);
			
			dataBaseType = Integer.parseInt(root.getChild( "dataBaseType" ).getValue().trim()); //读取dataBaseType
			logger.info("dataBaseType:" + dataBaseType);
			
			SEND = root.getChild( "SEND" ).getValue().trim(); //读取DI发送：SEND字段
			logger.info("SEND:" + SEND);
			
			SEND_PHYS = root.getChild( "SEND_PHYS" ).getValue().trim(); //读取DI发送：SEND_PHYS字段
			logger.info("SEND_PHYS:" + SEND_PHYS);
			
			SEND2 = root.getChild( "SEND2" ).getValue().trim(); //读取DI发送：SEND2字段
			logger.info("SEND2:" + SEND2);
			
			SEND_PHYS2 = root.getChild( "SEND_PHYS2" ).getValue().trim(); //读取DI发送：SEND_PHYS2字段
			logger.info("SEND_PHYS2:" + SEND_PHYS2);
			
			DATA_FLOW = root.getChild( "DATA_FLOW" ).getValue().trim(); //读取DI发送：DATA_FLOW字段
			logger.info("DATA_FLOW:" + DATA_FLOW);
			
			targetURL = root.getChild( "targetURL" ).getValue().trim(); //读取DI批量发送接口url：targetURL字段
			logger.info("targetURL:" + targetURL);
			
			targetURL_single = root.getChild( "targetURL_single" ).getValue().trim(); //读取DI批量发送接口url：targetURL_single字段
			logger.info("targetURL_single:" + targetURL_single);
			
			//读取每种资料的description
			List<Element> datalist = new ArrayList<Element>();
			datalist =  root.getChildren("data");
			map_data_Attr.clear();
			for(int i=0;i<datalist.size();i++)
			{
				String data_id = datalist.get(i).getChildText("data_id");
				String description = datalist.get(i).getChildText("description");
				String precision = datalist.get(i).getChildText("precision");
				String grib_version = datalist.get(i).getChildText("grib_version");
				String field_organize  = datalist.get(i).getChildText("field_organize");
				String data_dir = datalist.get(i).getChildText("data_dir");
				
				//时间字段在原始文件名的第几段，如：Z_NAFP_C_BABJ_20180725060000_P_CNPC-T639-GMFS-HNEHE-02100.grib2
				int time_index_of_filename = Integer.parseInt(datalist.get(i).getChildText("time_index_of_filename").trim());
				
				String isEnsemble = datalist.get(i).getChildText("isEnsemble"); //读取isEnsemble
				if(StringUtils.isBlank(isEnsemble)) {
					isEnsemble = "false";
				}		
				
				String index_type_c = datalist.get(i).getChildText("index_type"); //读取index_type
				int index_type = 0;
				if(index_type_c!=null)
				{
					index_type = Integer.parseInt(index_type_c);
				}
				
				String filename_match = datalist.get(i).getChildText("filename_match"); //读取filename_match		
				if(filename_match==null)
				{
					filename_match = "";
				}	
				
				String table_id = datalist.get(i).getChildText("table_id"); //读取table_id
				if(table_id==null) 
				{
					table_id = description; //表名标识默认是description
				}
				
				
				
				System.out.println("data_id="+data_id+",description="+description+",precision="+precision+",grib_version="+grib_version
						+",field_organize=" + field_organize + ",index_type=" + index_type + ",table_id:" + table_id);
				logger.info("data_id="+data_id+",description="+description+",precision="+precision+",grib_version="+grib_version
						+",field_organize=" + field_organize + ",index_type=" + index_type 
						+ ",time_index_of_filename=" + time_index_of_filename
						+",isEnsemble="+isEnsemble+",data_dir="+data_dir + ",table_id:" + table_id);
				
				DataAttr data_attr = new DataAttr(data_id.trim(),description.trim(),precision.trim(),grib_version.trim(),field_organize.trim(),
						time_index_of_filename,Boolean.parseBoolean(isEnsemble.trim()),data_dir, index_type,filename_match,
						table_id);
				
				map_data_Attr.put(data_id.trim(), data_attr);
			}
		}
		catch ( JDOMException e ) 
		{
			logger_e.error( "读取静态配置文件" + STATIC_SYSTEM_CONFIG_FILENAME + "错误（格式错误）,文件路径:"+configFile.getAbsolutePath(),e.getMessage());
		} catch ( IOException e ) 
		{
			logger_e.error( "读取静态配置文件" + STATIC_SYSTEM_CONFIG_FILENAME + "错误（未找到或格式错误）,文件路径:"+configFile.getAbsolutePath(),e.getMessage());
		} catch ( Exception e ) 
		{
			logger.warn( "读取静态配置文件出现其他异常，使用系统默认值." +"文件路径:"+configFile.getAbsolutePath(),e.getMessage());
		}	
	}
	
	public static String getPathNASFile()
	{
		return Path_NAS_file;
	}
	
	public static String getLogFilePath()
	{
		return log_file_path;
	}
		
	public static String getRestfulDi()
	{
		return send_restful_di;
	}
	
	public static String getRestfulEi()
	{
		return send_restful_ei;
	}
	
	public static int getdataBaseType()
	{
		return dataBaseType;
	}
	
	public static String getSEND()
	{
		return SEND;
	}
	
	public static String getSEND_PHYS()
	{
		return SEND_PHYS;
	}
	
	public static String getSEND2()
	{
		return SEND2;
	}
	
	public static String getSEND_PHYS2()
	{
		return SEND_PHYS2;
	}
	
	public static String getDATA_FLOW()
	{
		return DATA_FLOW;
	}
	
	public static String gettargetURL()
	{
		return targetURL;
	}
	
	public static String gettargetURL_single()
	{
		return targetURL_single;
	}
	
	public static Map<String, DataAttr> get_map_data_description()
	{
		return map_data_Attr;
	}
	
	//写日志到文件
	//参数：logFileName：带路径的日志文件
	//data：要写的字符串，添加在日志后面
	public static void write_log_to_file(String logFileName,String data)
	{
		OutputStreamWriter osw = null;
		try 
		{
			try {
				//checkFileExist();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			File file = new File(logFileName);
			osw = new OutputStreamWriter(new FileOutputStream(file, true),"UTF8");
			//写文件			
			osw.write(data+"\n");
			osw.flush();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			 e.printStackTrace();				
		} 
		finally 
		{
			try {
				if (osw != null) {
					osw.close();
					osw = null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
		}
	}
	
	/**
	 * 获得系统时间
	 * 
	 * @param
	 */
	public static String getSysTime()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String currentSysTime = dateFormat.format(date);
		return currentSysTime;
	}
	
	/**
	 * 获得系统时间
	 * 
	 * @param
	 */
	public static String getSysTime2()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String currentSysTime = dateFormat.format(date);
		return currentSysTime;
	}
	
	/**
	 * 获得系统日期
	 * 
	 * @param
	 */
	public static String getSysDate()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		String currentSysTime = dateFormat.format(date);
		return currentSysTime;
	}
}


