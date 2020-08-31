package cma.cimiss2.dpc.indb.core.tools;

import cma.cimiss2.dpc.decoder.tools.common.EI;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 */
public class EIConfig {
    public static final Logger logger = LoggerFactory.getLogger(EIConfig.class);
    private static EIConfig eiConfig = null;
    // EI配置对象
    private Map<String, EI> eiMaps = null;

    /**
     * @param path 配置文件路径，config上一层文件夹,多线程不需要传，赋值“”
     */
    private EIConfig(String path) {
        SAXReader saxReader = new SAXReader();
        eiMaps = new HashMap<String, EI>();
        String jarWholePath = "";
        try {
            if ("".equals(path)) {
                jarWholePath = ConfigurationManager.getConfigPath()+ "EIConfig.xml";
            } else {
                jarWholePath = path + "EIConfig.xml";
            }
            Document document = saxReader.read(jarWholePath);

            // 获取根元素
            Element root = document.getRootElement();
            // 获取所有子元素
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
                if (!eiMaps.containsKey(name)) {
                    eiMaps.put(name, ei);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

            logger.error("EI配置文件不存在：config/EIConfig.xml");
        }

    }

    /**
     * 单例模式，配置文件加载一次
     *
     * @param path 配置文件路径，config上一层文件夹，最后加/，多线程不需要传，赋值“”
     * @return
     */
    public static EIConfig getEiConfig(String path) {
        if (eiConfig == null) {
            eiConfig = new EIConfig(path);
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
        String path = "";
        Map<String, EI> eiMaps = EIConfig.getEiConfig(path).getEiMaps();
        for (String key : eiMaps.keySet()) {
            System.out.println(key);
        }
    }
}
