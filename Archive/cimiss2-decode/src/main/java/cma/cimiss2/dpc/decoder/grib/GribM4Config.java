package cma.cimiss2.dpc.decoder.grib;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.grib.DataAttr;

public class GribM4Config 
{
	protected static final Logger logger = LoggerFactory.getLogger("gribLoggerInfo");
	protected static final Logger logger_e = LoggerFactory.getLogger("gribErrorInfo");
	
	private static SAXBuilder builder = new SAXBuilder();//XML解析器
	private static final String STATIC_ELEMENT_MAPPING_CONFIG = "config/grib_m4Element_mapping.xml"; //M4要素映射配置文件
	private static final String STATIC_ISOLINE_CONFIG = "config/grib_M4_IsolineConfig.xml"; //等值线配置文件
	
	private static Map<String,String> map_element_m4 = new HashMap<String,String>(); //M4要素映射map
	private static Map<String,String> map_isoline_m4 = new HashMap<String,String>(); //M4等值线map
	
	private static Map<String,DataAttr> map_data_Attr = new HashMap<String,DataAttr>();
	
	public GribM4Config()
	{		
	}
	
	//读取M4相关配置
	public static void readConfig()
	{
		readElementMappingConfig(); //读取M4要素的映射配置
		read_IsolineConfig(); //读取等值线配置
	}
	
	//读取M4要素的映射配置，如：GPH,HGT
	private static void readElementMappingConfig() 
	{
		File configFile = null;
		try 
		{
			configFile = new File(STATIC_ELEMENT_MAPPING_CONFIG);
			Document doc = builder.build(configFile);
			Element root = doc.getRootElement();//根元素<root>
			
			//读需要入M4的Cassandra存储的资料（四级编码）配置
			List<Element> datalist = new ArrayList<Element>();
			datalist =  root.getChildren("data");
			map_data_Attr.clear();
			for(int i=0;i<datalist.size();i++)
			{
				String data_id = datalist.get(i).getChildText("data_id");
				String m4Cassandra = datalist.get(i).getChildText("m4Cassandra");
				String m4Area = datalist.get(i).getChildText("m4Area");
				String m4TableName = datalist.get(i).getChildText("m4TableName");
				
				logger.info("data_id="+data_id+",m4Cassandra="+m4Cassandra+",m4Area="+m4Area+",m4TableName="+m4TableName);
				
				DataAttr data_attr = new DataAttr(data_id.trim(),Boolean.parseBoolean(m4Cassandra.trim()),m4Area,m4TableName);
				map_data_Attr.put(data_id.trim(), data_attr);
			}
			
			//读取每种资料的m4element_mapping			
			Element data_element = root.getChild("element");
			List<Element> element_mapping_list = new ArrayList<Element>();
			element_mapping_list =  data_element.getChildren("m4element_mapping");
			map_element_m4.clear();
			for(int i=0;i<element_mapping_list.size();i++)
			{
				String element_mapping_str = element_mapping_list.get(i).getText().trim();
				
				logger.info("element_mapping=" + element_mapping_str);
				
				String[] element_mapping_str_array = element_mapping_str.split(",");
        		String element = element_mapping_str_array[0];	//云平台解码要素			
        		String elementM4 = element_mapping_str_array[1]; //M4存储要素
        		
        		map_element_m4.put(element, elementM4);
			}
		}
		catch ( JDOMException e ) 
		{
			//logger_e.error( "读取静态配置文件" + STATIC_ELEMENT_MAPPING_CONFIG + "错误（格式错误）,文件路径:"+configFile.getAbsolutePath(),e.getMessage());
		} catch ( IOException e ) 
		{
			//logger_e.error( "读取静态配置文件" + STATIC_ELEMENT_MAPPING_CONFIG + "错误（未找到或格式错误）,文件路径:"+configFile.getAbsolutePath(),e.getMessage());
		} catch ( Exception e ) 
		{
			//logger_e.error( "读取静态配置文件出现其他异常，使用系统默认值." +"文件路径:"+configFile.getAbsolutePath(),e.getMessage());
		}	
	}
	
	public static Map<String, String> get_map_element_m4()
	{
		return map_element_m4;
	}
	
	//读取等值线配置
	private static void read_IsolineConfig()
	{
		File configFile = null;
		try 
		{
			configFile = new File(STATIC_ISOLINE_CONFIG);
			Document doc = builder.build(configFile);
			Element root = doc.getRootElement();//根元素<configuration>
			
			//读取每种资料的descriptor
			List<Element> descriptor_list = new ArrayList<Element>();
			descriptor_list =  root.getChildren("descriptor"); 
			map_isoline_m4.clear();
			System.out.println("descriptor_list.size()="+descriptor_list.size());
			for(int i=0;i<descriptor_list.size();i++)
			{
				//读取element属性
				String element_str = descriptor_list.get(i).getAttributeValue("element");				
				
				//读取每一个level子节点
				List<Element> level_list = new ArrayList<Element>();
				level_list = descriptor_list.get(i).getChildren("level"); 
				System.out.println("level_list.size()="+level_list.size());
				for(int j=0;j<level_list.size();j++)
				{
					//<level isolineStartValue="0" isolineEndValue="440" isolineSpace="40">500</level>
					String level_str = level_list.get(j).getText().trim();
					System.out.println("level_str="+level_str);
					String isolineStartValue = level_list.get(j).getAttributeValue("isolineStartValue").trim();
					System.out.println("isolineStartValue="+isolineStartValue);
					String isolineEndValue = level_list.get(j).getAttributeValue("isolineEndValue").trim();
					String isolineSpace = level_list.get(j).getAttributeValue("isolineSpace").trim();
					
					//如：APCP_NULL,0_300_5
					//DEPR_500,0_440_40
					map_isoline_m4.put(element_str+"_"+level_str, isolineStartValue+"_"+isolineEndValue+"_"+isolineSpace);
					logger.info(element_str+"_"+level_str + ":" + isolineStartValue+"_"+isolineEndValue+"_"+isolineSpace);
				}	
			}
		}
		catch ( JDOMException e ) 
		{
			//logger_e.error( "读取静态配置文件" + STATIC_ISOLINE_CONFIG + "错误（格式错误）,文件路径:"+configFile.getAbsolutePath(),e.getMessage());
		} catch ( IOException e ) 
		{
			//logger_e.error( "读取静态配置文件" + STATIC_ISOLINE_CONFIG + "错误（未找到或格式错误）,文件路径:"+configFile.getAbsolutePath(),e.getMessage());
		} catch ( Exception e ) 
		{
			//logger_e.error( "读取静态配置文件出现其他异常，使用系统默认值." +"文件路径:"+configFile.getAbsolutePath(),e.getMessage());
		}	
	}
	
	public static Map<String, DataAttr> get_map_data_description()
	{
		return map_data_Attr;
	}
	
	public static Map<String, String> get_map_isoline_m4()
	{
		return map_isoline_m4;
	}
}


