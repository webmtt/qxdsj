package cma.cimiss2.dpc.indb.storm.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.hitec.bufr.bean.EntityBean;
import com.hitec.bufr.bean.XmlBean;
import com.hitec.bufr.util.StringUtil;
import com.hitec.bufr.util.XMLUtil;

public class BufrConfig {

	private BufrConfig() {

	}

	
//	private static Map<String, Map<String, Map<String, EntityBean>>> cMap = null;
	private static Map<String, Map<String, XmlBean>> cMap = new HashMap<>();;
	private static Map<String, List<String>> tableMap = new HashMap<>();;
	public static void init() {
		
		TableConfigIni tableIni = TableConfigIni.getIni();
		Map<String, Properties> map = tableIni.get();
		Iterator<Entry<String, Properties>> tableIt = map.entrySet().iterator();
		while (tableIt.hasNext()) {
			Entry<String, Properties> tableNext = tableIt.next();
			String configKey = tableNext.getKey();
			Properties configValue = tableNext.getValue();
			
//			Map<String, Map<String, EntityBean>> configMap = new HashMap<String, Map<String, EntityBean>>();
			Map<String, XmlBean> configMap = new HashMap<String, XmlBean>();
			List<String> tableList = new ArrayList<String>();
			
			configValue.forEach((tableName, configFile) -> {
//				Map<String, EntityBean> entityMap = XMLUtil.readDataInfo(StringUtil.getConfigPath() + "bufr/" + configFile);
				XmlBean xb = XMLUtil.readDataInfos(StringUtil.getConfigPath() + "bufr/" + configFile);
//				System.out.println(" \n\n"+StringUtil.getConfigPath() + "bufr/" + configFile);
				String tn = tableName.toString();
//				configMap.put(tn, entityMap);
				configMap.put(tn, xb);
				tableList.add(tn);

			});
			
			cMap.put(configKey, configMap);
			
			Collections.sort(tableList); // 升序
			Collections.reverse(tableList); // 倒序
			
			tableMap.put(configKey, tableList);
		}
		
	}

//	public static Map<String, Map<String, EntityBean>> get(String configSection){
	public static Map<String, XmlBean> get(String configSection){
		if(cMap == null || cMap.size() <= 0 || tableMap == null || tableMap.size() <=0) {
			init();
		}
		return cMap.get(configSection);
	}
	
//	public static Map<String, Map<String, Map<String, EntityBean>>> getConfigMap(){
	public static Map<String, Map<String, XmlBean>> getConfigMap(){
		return cMap;
	}
	
	public static Map<String, List<String>> getTableMap(){
		return tableMap;
	}
	
//	public static Map<String, EntityBean> get(String tabName) {
//		if (configMap == null || configMap.size() <= 0 || tableList == null || tableList.size() <= 0) {
//			init();
//		}
//		return configMap.get(tabName);
//	}
//
//	public static Map<String, Map<String, EntityBean>> getConfigMap() {
//		return configMap;
//	}
//
//	public static List<String> getTableList() {
//		if (tableList == null || tableList.size() <= 0) {
//			init();
//		}
//		return tableList;
//	}
//	public static void main(String[] args){
//    	String tableSection = "SURF_WEA_CBF_MUL_HOR_TAB";
//    	Map<String, XmlBean> configMap = BufrConfig.get(tableSection);
//    	System.out.println(configMap);
//    }
}
