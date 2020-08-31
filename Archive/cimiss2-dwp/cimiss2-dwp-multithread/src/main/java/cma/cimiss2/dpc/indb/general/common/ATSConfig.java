package cma.cimiss2.dpc.indb.general.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.indb.general.vo.AtsTableInfo;
import cma.cimiss2.dpc.indb.general.vo.PathInfo;

public class ATSConfig {
	public static final Logger logger = LoggerFactory.getLogger("loggerInfo");
	private Map<String, AtsTableInfo> atsTableInfos;
	private static ATSConfig atsConfig = null;
	private ATSConfig() {
		atsTableInfos = new HashMap<>();
		SAXReader saxReader = new SAXReader();
		try {
			Document document = saxReader.read(new File("config/ATSConfig.xml"));
			 // 获取根元素
	        Element root = document.getRootElement();
	        // 获取所有子元素
	        @SuppressWarnings("unchecked")
			List<Element> childList = root.elements();
	        for (int i = 0; i < childList.size(); i++) {
	        	
	        	if(childList.get(i).getName().equalsIgnoreCase("dataType")){
	        		AtsTableInfo atsTableInfo = new AtsTableInfo();
	        		Element dataId = childList.get(i).element("dataId");
	        		
	        		atsTableInfo.setDataId(getPathInfo(dataId.getStringValue()));
	        		
	        		Element prodCode = childList.get(i).element("prodCode");
	        		atsTableInfo.setProdCode(getPathInfo(prodCode.getStringValue()));
	        		
	        		Element dataTime = childList.get(i).element("dataTime");
	        		atsTableInfo.setDataTime(getPathInfo(dataTime.getStringValue()));
	        		
	        		Element station = childList.get(i).element("station");
	        		atsTableInfo.setStation(getPathInfo(station.getStringValue()));
	        		
	        		Element productMethod = childList.get(i).element("productMethod");
	        		atsTableInfo.setProductMethod(getPathInfo(productMethod.getStringValue()));
	        		
	        		Element productCenter = childList.get(i).element("productCenter");
	        		atsTableInfo.setProductCenter(getPathInfo(productCenter.getStringValue()));
	        		
	        		Element productDescription = childList.get(i).element("productDescription");
	        		atsTableInfo.setProductDescription(getPathInfo(productDescription.getStringValue()));
	        		
	        		Element attributes = childList.get(i).element("attributes");
	        		
	        		if(attributes != null) {
	        			List<PathInfo> attrsPathinfos = new ArrayList<PathInfo>();
	        			@SuppressWarnings("unchecked")
						List<Element> attrs = attributes.elements();
	        			for (int j = 0; j < attrs.size(); j++) {
	        				attrsPathinfos.add(getPathInfo(attrs.get(i).getStringValue()));
						}
	        			
	        			atsTableInfo.setAttrs(attrsPathinfos);
	        		}
	        		atsTableInfos.put(childList.get(i).attributeValue("name"), atsTableInfo);
	        	}
			}
			
		} catch (DocumentException e) {
			e.printStackTrace();
			logger.error("\n 配置文件不存在"+ e.getMessage());
		}
	}
	
	/**
	 * 统计字符或者字符在目标字符串中出现的次数
	 * @param source  目标字符串
	 * @param sub  被检测字符串
	 * @return   出现的次数
	 */
	private int getCharCount(String source, String sub) {
		int count = 0;
		int length = source.length() - sub.length();
		for (int i = 0; i < length; i++) {
			String soucesBak = source.substring(i, i+sub.length());
			int index = soucesBak.indexOf(sub);
			if (index != -1) {
				count ++ ;
			}
		}
		return count;
	}
	private PathInfo getPathInfo(String msg) {
		if(msg.startsWith("#")) {
			return new PathInfo(0, msg.replace("#", ""));
		} else if (msg.startsWith("[") && msg.endsWith("]") && !msg.contains("YYYYDDD")) {
			if(getCharCount(msg, "[") == 1) {
				if(msg.contains(":")) {
					msg = msg.replace("[", "").replace("]", "");
					String []itemString = msg.split(":");
					return new PathInfo(2, null, Integer.parseInt(itemString[0].trim()), 
							Integer.parseInt(itemString[1].trim()), 
							Integer.parseInt(itemString[1].trim()) + Integer.parseInt(itemString[2].trim()));
				}else {
					msg = msg.replace("[", "");
					msg = msg.replace("]", "");
					return new PathInfo(1, null, Integer.parseInt(msg.trim()));
				}
			}else {
				return new PathInfo(3, msg, 0, 0, 0);
			}
			
		}else {
			return new PathInfo(4, msg, 0, 0, 0);
		}
	}
	
	public static ATSConfig getInstance(){
		if(atsConfig == null){
			atsConfig = new ATSConfig();
		}
		return atsConfig;
	}
	
	
	
	public Map<String, AtsTableInfo> getAtsTableInfos() {
		return atsTableInfos;
	}
	
	public static void main(String[] args) {
		ATSConfig.getInstance().getAtsTableInfos();
	}

}
