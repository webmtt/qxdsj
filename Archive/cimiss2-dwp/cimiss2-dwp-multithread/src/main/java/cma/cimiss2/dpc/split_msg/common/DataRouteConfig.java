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

import cma.cimiss2.dpc.split_msg.bean.DataRouteBean;

public class DataRouteConfig {
	private final static Logger logger = LoggerFactory.getLogger(DataRouteConfig.class);
	private Map<String, DataRouteBean> dataRouteMaps = new HashMap<String, DataRouteBean>();
	private static class DataRouteConfigHelper {
		private static final DataRouteConfig DATA_ROUTE_CONFIG = new DataRouteConfig();
	}

	public DataRouteConfig() {
		SAXReader saxReader = new SAXReader();
		Document document;
		try {
			document = saxReader.read(new File("config/split_msg_queue_config.xml"));
			// 获取根元素
			Element root = document.getRootElement();
			@SuppressWarnings("unchecked")
			List<Element> childList = root.elements();
			for (Element element : childList) {
				@SuppressWarnings("unchecked")
				List<Element> subElements = element.elements();
				DataRouteBean dataRouteBean = new DataRouteBean();
				for (Element ele : subElements) {
					if(ele.getName().equalsIgnoreCase("DataType")) {
						dataRouteBean.setDataType(ele.getStringValue().trim());
					}else if (ele.getName().equalsIgnoreCase("ExchangeName")) {
						dataRouteBean.setExchangeName(ele.getStringValue().trim());
					}else if (ele.getName().equalsIgnoreCase("ExchangeType")) {
						dataRouteBean.setExchangeType(ele.getStringValue().trim());
					}else if (ele.getName().equalsIgnoreCase("RoutingKey")) {
						dataRouteBean.setRoutingKey(ele.getStringValue().trim());
					}else if (ele.getName().equalsIgnoreCase("QueueName")) {
						dataRouteBean.setQueueName(ele.getStringValue().trim());
					}
				}
				
				dataRouteMaps.put(dataRouteBean.getDataType(), dataRouteBean);
			}
		} catch (DocumentException e) {
			logger.error("加载配置文件config/DataRouteConf.xml 错误！" + e.getMessage());
			e.printStackTrace();
		}

	}
	
	public static DataRouteConfig getDataRouteConfig() {
		return DataRouteConfigHelper.DATA_ROUTE_CONFIG;
	}
	
	
	
	public Map<String, DataRouteBean> getDataRouteMaps() {
		return dataRouteMaps;
	}

	public void setDataRouteMaps(Map<String, DataRouteBean> dataRouteMaps) {
		this.dataRouteMaps = dataRouteMaps;
	}

	public static void main(String[] args) {
		System.out.println(DataRouteConfig.getDataRouteConfig().getDataRouteMaps().keySet() );
	}
}
