package org.cimiss2.dwp.tools.utils;
/**
 * 
 * <br>
 * @Title:  EIConfig.java   
 * @Package org.cimiss2.dwp.tools.utils   
 * @Description:    TODO(读取EI配置文件)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月20日 下午4:44:29   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *
 *
 */

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.tools.common.EI;

public class EIConfig {
	public static final Logger logger = LoggerFactory.getLogger(EIConfig.class);
	private static EIConfig eiConfig = null;
	// EI配置对象
	private Map<String, EI> eiMaps = null;
	private EIConfig() {
		SAXReader saxReader = new SAXReader();
		eiMaps = new HashMap<String, EI>();
		try {
			Document document = saxReader.read(new File(ConfigurationManager.getJarSuperPath()+"config"+File.separator+"EIConfig.xml"));
			 // 获取根元素
	        Element root = document.getRootElement();
	        // 获取所有子元素
	        @SuppressWarnings("unchecked")
			List<Element> childList = root.elements();
	        for (int i = 0; i < childList.size(); i++) {
	        	String name = childList.get(i).attribute("id").getStringValue();
				EI ei = new EI();
				// 事件类型
				ei.setEVENT_TYPE(name);
				
				Element systemN = childList.get(i).element("SYSTEM");
				ei.setSYSTEM(systemN.getStringValue());

				Element group_id = childList.get(i).element("GROUP_ID");
				ei.setGROUP_ID(group_id.getStringValue());
				
				Element event_title = childList.get(i).element("EVENT_TITLE");
				ei.setEVENT_TITLE(event_title.getStringValue());
				
				Element event_level = childList.get(i).element("EVENT_LEVEL");
				ei.setEVENT_LEVEL(event_level.getStringValue());
				
				Element col_type = childList.get(i).element("COL_TYPE");
				ei.setCOL_TYPE(col_type.getStringValue());

				Element data_from = childList.get(i).element("DATA_FROM");
				ei.setDATA_FROM(data_from.getStringValue());

				Element event_trag = childList.get(i).element("EVENT_TRAG");
				ei.setEVENT_TRAG(event_trag.getStringValue());

				Element event_control = childList.get(i).element("EVENT_CONTROL");
				ei.setEVENT_CONTROL(event_control.getStringValue());
				
				try {
					ei.setEVENT_EXT2(InetAddress.getLocalHost().getHostAddress());
				} catch (UnknownHostException e) {
					logger.error("获取主机IP信息错误");
					continue;
				}
				if(!eiMaps.containsKey(name)) {
					eiMaps.put(name, ei);
				}
			}
		} catch (DocumentException e) {
			logger.error("EI配置文件不存在：config/EIConfig.xml");
		}
		
	}
	/**
	 * 单例模式，配置文件加载一次
	 * @Title: getEiConfig   
	 * @Description: TODO(这里用一句话描述这个方法的作用)   
	 * @param: @return      
	 * @return: EIConfig      
	 * @throws
	 */
	public static EIConfig getEiConfig() {
		if(eiConfig == null) {
			eiConfig = new EIConfig();
		}
		return eiConfig;
	}
	
	
	
	public Map<String, EI> getEiMaps() {
		return eiMaps;
	}
	public void setEiMaps(Map<String, EI> eiMaps) {
		this.eiMaps = eiMaps;
	}
	
	
	public static void main(String[] args) {
		EI ei = EIConfig.getEiConfig().getEiMaps().get("111");
		System.out.println(ei);
	}
}
