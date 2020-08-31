package cma.cimiss2.dpc.decoder.agme;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

// TODO: Auto-generated Javadoc
/**
 * The Class PropertiesUtil.
 */
public class PropertiesUtil {
	
	/** The properties util. */
	private static PropertiesUtil propertiesUtil = new PropertiesUtil();
	
	/** The properties. */
	private static Properties properties = null;
	
	/** The cts code. */
	public static String cts_code = "";
	
	/**
	 * The Class AgmeType.
	 */
	public class AgmeType{
		
		/** The table name. */
		private String tableName;
		
		/** The sod type. */
		private String sod_type;
		
		/** The cts type. */
		private String cts_type;
		
		/** The dpc type. */
		private String dpc_type;
		
		/**
		 * Instantiates a new agme type.
		 *
		 * @param tableName the table name
		 * @param sod_type the sod type
		 * @param cts_type the cts type
		 * @param dpc_type the dpc type
		 */
		public AgmeType(String tableName, String sod_type, String cts_type, String dpc_type) {
			this.tableName = tableName;
			this.sod_type = sod_type;
			this.cts_type = cts_type;
			this.dpc_type = dpc_type;
		}
		
		/**
		 * Gets the table name.
		 *
		 * @return the table name
		 */
		public String getTableName() {
			return tableName;
		}
		
		/**
		 * Gets the cts type.
		 *
		 * @return the cts type
		 */
		public String getCts_type() {
			return cts_type;
		}
		
		/**
		 * Gets the sod type.
		 *
		 * @return the sod type
		 */
		public String getSod_type() {
			return sod_type;
		}
		
		/**
		 * Gets the dpc type.
		 *
		 * @return the dpc type
		 */
		public String getDpc_type() {
			return dpc_type;
		}
	}
	
	/** The agme data map. */
	public static Map<String, AgmeType> agmeDataMap = new HashMap<String, PropertiesUtil.AgmeType>();

	/**
	 * Sets the config file.
	 *
	 * @param configFile the new config file
	 */
	public static void setConfigFile(String configFile) {
		properties = new Properties();
		try {
			properties.load(new FileInputStream(configFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the agme data map.
	 *
	 * @return the agme data map
	 */
	public static Map<String, AgmeType> getAgmeDataMap(){
		return agmeDataMap;
	}
	
	/**
	 * Sets the config file xml.
	 *
	 * @param cfgXML the new config file xml
	 */
	public static void setConfigFileXml(String cfgXML){
		SAXReader reader = null;
		
		try {
			reader = new SAXReader();
			Document doc = reader.read(cfgXML);
			Element root = doc.getRootElement();
			for(@SuppressWarnings("rawtypes")
			Iterator type = root.elementIterator(); type.hasNext();){
				// CTS_CODE, CROP01, CROP02...
				Element t = (Element)type.next();
				if(t.getName().equalsIgnoreCase("CTS_CODE"))
					cts_code = t.getStringValue();
				else{
					AgmeType agmeType = propertiesUtil.new AgmeType(t.elementText("TABLE_NAME"), t.elementText("SOD_CODE"), cts_code, t.elementText("DPC_CODE"));
					agmeDataMap.put(t.getName(), agmeType);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the CTS code.
	 *
	 * @return the CTS code
	 */
	public static String getCTSCode() {
		if(properties.containsKey("CTS_CODE")) {
			return properties.getProperty("CTS_CODE").trim();
		}else {
			return null;
		}
	}
	
	/**
	 * Gets the table name.
	 *
	 * @return the table name
	 */
	public static String getTableName() {
		if(properties.containsKey("TABLE_NAME")) {
			return properties.getProperty("TABLE_NAME").trim();
		}else {
			return null;
		}
	}
	
	/**
	 * Gets the SOD code.
	 *
	 * @return the SOD code
	 */
	public static String getSODCode() {
		if(properties.containsKey("SOD_CODE")) {
			return properties.getProperty("SOD_CODE").trim();
		}else {
			return null;
		}
	}
	
	/**
	 * Gets the DPC code.
	 *
	 * @return the DPC code
	 */
	public static String getDPCCode() {
		if(properties.containsKey("DPC_CODE")) {
			return properties.getProperty("DPC_CODE").trim();
		}else {
			return null;
		}
	}
	
	/**
	 * Gets the tt.
	 *
	 * @return the tt
	 */
	public static String getTT() {
		if(properties.containsKey("V_TT")) {
			return properties.getProperty("V_TT").trim();
		}else {
			return null;
		}
	}
	
	/**
	 * Gets the cccc.
	 *
	 * @return the cccc
	 */
	public static String getCCCC() {
		if(properties.containsKey("C_CCCC")) {
			return properties.getProperty("C_CCCC").trim();
		}else {
			return null;
		}
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
//		PropertiesUtil.setConfigFile("config/Agme_2222_setups.properties");
//		System.out.println(PropertiesUtil.getDPCCode());
		PropertiesUtil.setConfigFileXml("config/Agme_2222_setups.xml");
		Map<String, AgmeType> map = PropertiesUtil.getAgmeDataMap();
		System.out.println(map.size());
		System.out.println(map.get("CROP01").getTableName());
	}
}