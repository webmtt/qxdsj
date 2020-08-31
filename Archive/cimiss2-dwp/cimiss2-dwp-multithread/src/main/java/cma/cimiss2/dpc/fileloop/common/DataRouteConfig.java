package cma.cimiss2.dpc.fileloop.common;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import cma.cimiss2.dpc.fileloop.mq.QueueBean;

public class DataRouteConfig {
	@SuppressWarnings("unchecked")
	public static Map<String, QueueBean> getConfigMap() {
		Map<String, QueueBean> map = new HashMap<>();
		SAXReader reader = new SAXReader();
        File file = new File("config/DataRouteConf.xml");
        Document document;
		try {
			document = reader.read(file);
			Element root = document.getRootElement();
			List<Node> nodes = root.selectNodes("Server/DataRouteList/DataRoute");
			for (Node node : nodes) {
				map.put(node.selectSingleNode("DataType").getStringValue(),
						new QueueBean(node.selectSingleNode("QueueName").getStringValue(), 
									  node.selectSingleNode("ExchangeName").getStringValue(), 
								      node.selectSingleNode("RoutingKey").getStringValue()));
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        
		return map;
	}
	

}
