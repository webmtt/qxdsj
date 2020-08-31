package cma.cimiss2.dpc.decoder.grib;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.grib.DataAttr;

public class GribConfigAreaMapping 
{
	protected static final Logger logger = LoggerFactory.getLogger("gribLoggerInfo");
	protected static final Logger logger_e = LoggerFactory.getLogger("gribErrorInfo");
	
	private static SAXBuilder builder = new SAXBuilder();//XML解析器
	private static final String STATIC_AREA_MAPPING_CONFIG_FILENAME = "config/grib_area_mapping.xml";
	
	private static Map<String,String> map_area_Attr = new HashMap<String,String>();
	
	private static final String STATIC_AREA_SPLIT_CONFIG_FILENAME = "config/grib_area_split.xml";	
	private static Map<String,DataAttr> map_areaSplit_Attr = new HashMap<String,DataAttr>();
	
	
	public GribConfigAreaMapping()
	{		
	}
	
	public static void readConfig()
	{
		readAreaMappingConfig(); //读取模式区域映射配置
		
		readAreaSplitConfig(); //读取模式区域裁剪配置
	}
	
	//读取模式区域映射配置
	public static void readAreaMappingConfig() 
	{
		File configFile = null;
		try 
		{
			configFile = new File(STATIC_AREA_MAPPING_CONFIG_FILENAME);
			Document doc = builder.build(configFile);
			Element root = doc.getRootElement();//根元素<root>
			
			//读取每种资料的description
			List<Element> datalist = new ArrayList<Element>();
			datalist =  root.getChildren("data");
			map_area_Attr.clear();
			for(int i=0;i<datalist.size();i++)
			{
				String area_src = datalist.get(i).getChildText("area_src");
				String area_des = datalist.get(i).getChildText("area_des");
				
				System.out.println("area_src=" + area_src + ",area_des=" + area_des);
				logger.info("area_src=" + area_src + ",area_des=" + area_des);
				
				map_area_Attr.put(area_src, area_des);
			}
		}
		catch ( JDOMException e ) 
		{
			logger_e.error( "读取静态配置文件" + STATIC_AREA_MAPPING_CONFIG_FILENAME + "错误（格式错误）,文件路径:"+configFile.getAbsolutePath(),e);
		} catch ( IOException e ) 
		{
			logger_e.error( "读取静态配置文件" + STATIC_AREA_MAPPING_CONFIG_FILENAME + "错误（未找到或格式错误）,文件路径:"+configFile.getAbsolutePath(),e);
		} catch ( Exception e ) 
		{
			logger_e.error( "读取静态配置文件出现其他异常，使用系统默认值." +"文件路径:"+configFile.getAbsolutePath(),e);
		}	
	}
	
	public static Map<String, String> get_map_data_description()
	{
		return map_area_Attr;
	}
	
	//读取模式区域裁剪配置
	public static void readAreaSplitConfig()
	{
		File configFile = null;
		try 
		{
			configFile = new File(STATIC_AREA_SPLIT_CONFIG_FILENAME);
			Document doc = builder.build(configFile);
			Element root = doc.getRootElement();//根元素<root>
			
			//读取每种资料的description
			List<Element> datalist = new ArrayList<Element>();
			datalist =  root.getChildren("data");
			map_areaSplit_Attr.clear();
			for(int i=0;i<datalist.size();i++)
			{
				String data_id = datalist.get(i).getChildText("data_id");
				String area_src = datalist.get(i).getChildText("area_src"); //格式：GLB
				String area_des = datalist.get(i).getChildText("area_des"); //格式：ANEA
				String Lat_Lon = datalist.get(i).getChildText("Lat_Lon"); //格式：60,-10,60,150（起始纬度，终止纬度，起始经度，终止经度）
				
				logger.info("data_id="+data_id+",area_src="+area_src+",area_des="+area_des+",Lat_Lon="+Lat_Lon);
				
				DataAttr data_attr = new DataAttr(data_id.trim(),area_src,area_des,Lat_Lon);				
				map_areaSplit_Attr.put(data_id.trim(), data_attr);
			}
		}
		catch ( JDOMException e ) 
		{
			logger_e.error( "读取静态配置文件" + STATIC_AREA_SPLIT_CONFIG_FILENAME + "错误（格式错误）,文件路径:"+configFile.getAbsolutePath(),e);
		} catch ( IOException e ) 
		{
			logger_e.error( "读取静态配置文件" + STATIC_AREA_SPLIT_CONFIG_FILENAME + "错误（未找到或格式错误）,文件路径:"+configFile.getAbsolutePath(),e);
		} catch ( Exception e ) 
		{
			logger_e.error( "读取静态配置文件出现其他异常，使用系统默认值." +"文件路径:"+configFile.getAbsolutePath(),e);
		}	
	}

	public static Map<String, DataAttr> get_map_area_split_description()
	{
		return map_areaSplit_Attr;
	}	
}


