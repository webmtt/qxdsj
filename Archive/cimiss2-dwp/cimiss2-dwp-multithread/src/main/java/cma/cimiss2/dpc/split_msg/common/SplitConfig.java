package cma.cimiss2.dpc.split_msg.common;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.split_msg.bean.SplitFileName;
import cma.cimiss2.dpc.split_msg.bean.SplitMsg;

/**
* *******************************************************************************************<br>
* <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
* *******************************************************************************************<br>
* <b>Description: 加载消息拆分的配置策略文件</b><br> 
* @author wuzuoqiang 
* @version 1.0
* @Note 
* <b>ProjectName:</b> cimiss2-dwp-split-msg
* <br><b>PackageName:</b> org.cimiss2.dwp.split.msg.common
* <br><b>ClassName:</b> SplitConfig
* <br><b>Date:</b> 2019年9月16日 下午5:59:42
 */
public class SplitConfig {
	private final static Logger logger = LoggerFactory.getLogger(SplitConfig.class);
	private Map<String, SplitMsg> splitMsgMap;
	
	private static class SplitConfigHelper{
		private static final SplitConfig INSTANCE_SPLIT_CONFIG = new SplitConfig();
	}
	
	/**
	 * 在单例模式的构造函数中加载配置文件
	 */
	private SplitConfig() {
		splitMsgMap = new HashMap<String, SplitMsg>();
		SAXReader saxReader = new SAXReader();
		Document document;
		try {
			document = saxReader.read(new File("config/split_message.xml"));
			 // 获取根元素
	        Element root = document.getRootElement();
	        @SuppressWarnings("unchecked")
			List<Element> childList = root.elements();
	        
        	for (Element element : childList) {
        		System.out.println(element.getName());
        		SplitMsg splitMsg = new SplitMsg();
        		splitMsg.setCts_code(element.attributeValue("cts_code").trim());
        		splitMsg.setSplit_expression(element.attributeValue("split_expression").trim());
				@SuppressWarnings("unchecked")
				List<Element> subElements = element.elements();
				for (int j = 0; j < subElements.size(); j++) {
					SplitFileName splitFileName = new SplitFileName();
					@SuppressWarnings("unchecked")
					List<Element> subElements2 = subElements.get(j).elements();
					for (Element ele : subElements2) {
						if(ele.getName().trim().equalsIgnoreCase("sod_code")) {
							splitFileName.setSod_code(ele.getStringValue().trim());
						}else if (ele.getName().trim().equalsIgnoreCase("split_index")) {
							splitFileName.setSplit_index(ele.getStringValue().trim());
						}else if (ele.getName().trim().equalsIgnoreCase("split_content")) {
							splitFileName.setSplit_content(ele.getStringValue().trim());
						}
					}
					
					splitMsg.put(splitFileName);
				}
				
				splitMsgMap.put(splitMsg.getCts_code(), splitMsg);
			}
			
		} catch (DocumentException e) {
			logger.error("读取配置文件错误    config/split_message.xml" + e.getMessage());
			e.printStackTrace();
		}
		
		
	}
	
	public static SplitConfig getSplitConfig() {
		return SplitConfigHelper.INSTANCE_SPLIT_CONFIG;
	}
	
	public Map<String, SplitMsg> getSplitMsgMap() {
		return splitMsgMap;
	}

	public void setSplitMsgMap(Map<String, SplitMsg> splitMsgMap) {
		this.splitMsgMap = splitMsgMap;
	}

	public static void main(String[] args) {
		System.out.println(SplitConfig.getSplitConfig().getSplitMsgMap().keySet());
	}

}
