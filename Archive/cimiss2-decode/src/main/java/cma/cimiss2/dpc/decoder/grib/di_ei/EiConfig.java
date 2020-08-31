package cma.cimiss2.dpc.decoder.grib.di_ei;

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

public class EiConfig 
{
	protected static final Logger logger = LoggerFactory.getLogger("gribLoggerInfo");
	protected static final Logger logger_e = LoggerFactory.getLogger("gribErrorInfo");
	
	public static SAXBuilder builder = new SAXBuilder();//XML解析器
	public static final String STATIC_SYSTEM_CONFIG_FILENAME = "config/grib_EI.xml";
	
	private static String local_ip = ""; //本地ip地址，用于发EI
	
	private static Map<String,EiAttr> map_EI_Attr = new HashMap<String,EiAttr>();
	
	public EiConfig()
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
			
			local_ip = root.getChild( "local_ip" ).getValue(); //读取path_NAS_file
			
			//读取每种资料的description
			List<Element> datalist = new ArrayList<Element>();
			datalist =  root.getChildren("EI");
			map_EI_Attr.clear();
			for(int i=0;i<datalist.size();i++)
			{
				String sequence = datalist.get(i).getChildText("sequence");
				String SYSTEM = datalist.get(i).getChildText("SYSTEM");
				String GROUP_ID = datalist.get(i).getChildText("GROUP_ID");
				String MSG_TYPE = datalist.get(i).getChildText("MSG_TYPE");
				String COL_TYPE = datalist.get(i).getChildText("COL_TYPE");
				String DATA_FROM = datalist.get(i).getChildText("DATA_FROM");
				String EVENT_TYPE = datalist.get(i).getChildText("EVENT_TYPE");
				String EVENT_LEVEL = datalist.get(i).getChildText("EVENT_LEVEL");
				String EVENT_TITLE = datalist.get(i).getChildText("EVENT_TITLE");
				String KObject = datalist.get(i).getChildText("KObject");
				String KEvent = datalist.get(i).getChildText("KEvent");
				String KResult = datalist.get(i).getChildText("KResult");
				String KIndex = datalist.get(i).getChildText("KIndex");
				String KComment = datalist.get(i).getChildText("KComment");
				String EVENT_SUGGEST = datalist.get(i).getChildText("EVENT_SUGGEST");
				String EVENT_CONTROL = datalist.get(i).getChildText("EVENT_CONTROL");
				String EVENT_TRAG = datalist.get(i).getChildText("EVENT_TRAG");
				String EVENT_EXT2 = datalist.get(i).getChildText("EVENT_EXT2");
				
				String send = datalist.get(i).getChildText("send").trim();
				
				logger.info("sequence="+sequence+",SYSTEM="+SYSTEM+",GROUP_ID="+GROUP_ID+",MSG_TYPE="+MSG_TYPE+",COL_TYPE="+COL_TYPE+",DATA_FROM="+DATA_FROM
						+",EVENT_TYPE="+EVENT_TYPE+",EVENT_LEVEL="+EVENT_LEVEL+",EVENT_TITLE="+EVENT_TITLE+",KObject="+KObject+",KEvent="+KEvent+",KResult="+KResult
						+",KIndex="+KIndex+",KComment="+KComment+",EVENT_SUGGEST="+EVENT_SUGGEST+",EVENT_CONTROL="+EVENT_CONTROL+",EVENT_TRAG="+EVENT_TRAG+",EVENT_EXT2="+EVENT_EXT2);
			
				EiAttr eiAttr = new EiAttr(sequence,SYSTEM,GROUP_ID,MSG_TYPE,COL_TYPE,DATA_FROM
						,EVENT_TYPE,EVENT_LEVEL,EVENT_TITLE,KObject,KEvent,KResult
						,KIndex,KComment,EVENT_SUGGEST,EVENT_CONTROL,EVENT_TRAG,EVENT_EXT2
						,send);
				
				map_EI_Attr.put(EVENT_TYPE.trim(),eiAttr);
				
			}
		}
		catch ( JDOMException e ) 
		{
			logger_e.error( "读取静态配置文件" + STATIC_SYSTEM_CONFIG_FILENAME + "错误（格式错误）,文件路径:"+configFile.getAbsolutePath(),e);
		} catch ( IOException e ) 
		{
			logger_e.error( "读取静态配置文件" + STATIC_SYSTEM_CONFIG_FILENAME + "错误（未找到或格式错误）,文件路径:"+configFile.getAbsolutePath(),e);
		} catch ( Exception e ) 
		{
			logger.warn( "读取静态配置文件出现其他异常，使用系统默认值." +"文件路径:"+configFile.getAbsolutePath(),e);
		}	
	}	
	
	public static String getlocal_ip()
	{
		return local_ip;
	}
	
	public static Map<String, EiAttr> get_map_data_description()
	{
		return map_EI_Attr;
	}
}
