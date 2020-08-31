package cma.cimiss2.dpc.indb.common;

import cma.cimiss2.dpc.indb.vo.AtsTableInfo;
import cma.cimiss2.dpc.indb.vo.PathInfo;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	

	private PathInfo getPathInfo(String msg) {
		if(msg.startsWith("#")) {
			return new PathInfo(0, msg.replace("#", ""));
		} else if (msg.startsWith("[") && msg.endsWith("]")) {
			if(msg.contains("(") && msg.contains(")")){
				int index1 = msg.indexOf("(");
				int pos = Integer.parseInt(msg.substring(1, index1));
				int index2 = msg.indexOf(")");
				String start_end_str = msg.substring(index1+1, index2);
				String[] ses = start_end_str.trim().split(",");
				return new PathInfo(2, null, pos, Integer.parseInt(ses[0]), Integer.parseInt(ses[1]));
			} else {
				msg = msg.replace("[", "");
				msg = msg.replace("]", "");
				return new PathInfo(1, null, Integer.parseInt(msg.trim()));
			}
		}else {
			return null;
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

}
