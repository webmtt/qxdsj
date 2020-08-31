package cma.cimiss2.dpc.decoder.upar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cma.cimiss2.dpc.decoder.agme.Content;
import cma.cimiss2.dpc.decoder.agme.DataType;
import cma.cimiss2.dpc.decoder.agme.DecodeUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class HKFlashParseConfig.
 */
public class HKFlashParseConfig {
	
	/** The parae config. */
	private static HKFlashParseConfig paraeConfig = null;
	
	/** The parse beans. */
	private List<HKFlashParseBean> parseBeans = null;
	
	/** The config. */
	public static String config = "config/parseConfig.xml";
	
	/** The Is check. */
	public static boolean IsCheck = false;
	
	/** The report lenth. */
	public static int reportLenth = 999999;
	
	/**
	 * Gets the parae config.
	 *
	 * @return the parae config
	 */
	public static HKFlashParseConfig getParaeConfig() {
		if(paraeConfig == null) {
			paraeConfig =  new HKFlashParseConfig(config);
		}
		return paraeConfig;
	}
	
	/**
	 * Gets the parses the beans.
	 *
	 * @return the parses the beans
	 */
	public List<HKFlashParseBean> getParseBeans() {
		return parseBeans;
	}

	/**
	 * Instantiates a new HK flash parse config.
	 *
	 * @param config the config
	 */
	private HKFlashParseConfig(String config) {
		parseBeans = new ArrayList<HKFlashParseBean>();
		File configFile = new File(config);
		if(configFile.exists() && configFile.isFile()) {
			SAXReader saxReader = new SAXReader();
			Document document;
			try {
				document = saxReader.read(configFile);
				 // 获取根元素
		        Element root = document.getRootElement();
		        // 获取属性
		        if (root.attribute("IsCheck") != null) {   // 判断报文是否需要长度校验
					if(root.attribute("IsCheck").getStringValue().trim().equalsIgnoreCase("true")) {
						paraeConfig.IsCheck = true;
						// 获取需要校验的长度值
						if(root.attribute("length") != null) {
							reportLenth = DecodeUtil.parseInt(root.attribute("length").getStringValue().trim());
						}
					}
				}
		        
		        // 获取所有的子节点
		        List<Element> childList = root.elements();
		        for (Element element : childList) {
		        	HKFlashParseBean bean = new HKFlashParseBean();
		        	// 获取要素名称
					bean.setElementName(element.getStringValue().trim());
					// 获取要素的数据类型
					bean.setDataType(getDataType(element.attribute("dataType").getStringValue().trim()));
					// 要素取值范围
					bean.setContent(getContent(element.attribute("content").getStringValue().trim()));
					if(element.attribute("index") != null) {
//						bean.setIndex(Integer.parseInt(element.attribute("index").getStringValue().trim()));
						bean.setIndex(element.attribute("index").getStringValue().trim());
					}
					
					if(element.attribute("value") != null) {
						bean.setValue(element.attribute("value").getStringValue().trim());
					}
					
					if(element.attribute("Expression") != null) {
						bean.setIsCalc(true);
						bean.setExpression(element.attribute("Expression").getStringValue().trim());
					}
					if(element.attribute("format") != null){
						bean.setFormat(element.attribute("format").getStringValue().trim());
					}
					if(element.attribute("split") != null){
						bean.setSplit(element.attribute("split").getStringValue().trim());
					}
					parseBeans.add(bean);
				}
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	
	/**
	 * Gets the data type.
	 *
	 * @param dataType the data type
	 * @return the data type
	 */
	private static DataType getDataType(String dataType) {
		if(dataType.equalsIgnoreCase("String")) {
			return DataType.STRING;
		}else if (dataType.equalsIgnoreCase("double")) {
			return DataType.DOUBLE;
		}else if (dataType.equalsIgnoreCase("int")) {
			return DataType.INT;
		}else if (dataType.equalsIgnoreCase("float")) {
			return DataType.FLOAT;
		}else if (dataType.equalsIgnoreCase("datetime")) {
			return DataType.DATETIME;
		}else {
			return DataType.UNDIFINE;
		}
	}
	
	/**
	 * Gets the content.
	 *
	 * @param content the content
	 * @return the content
	 */
	private static Content getContent(String content) {
		if(content.equalsIgnoreCase("default")) {
			return Content.DEFAULT;
		}else if (content.equalsIgnoreCase("report")) {
			return Content.REPORT;
		}else if (content.equalsIgnoreCase("filename")) {
			return Content.FILE_NAME;
		}else if(content.equalsIgnoreCase("filepath")){
			return Content.FILE_PATH;
		}else {
			return Content.UNDIFINE;
		}
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		HKFlashParseConfig.config = "config/Light_3333_TAB.xml";
		HKFlashParseConfig.getParaeConfig();
		System.out.println(HKFlashParseConfig.reportLenth);
	}

}
