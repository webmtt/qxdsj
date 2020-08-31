package cma.cimiss2.dpc.indb.core.tools;


import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * xm文件解析类
 */
public class StormConfig {
//    public static final Logger logger = LoggerFactory.getLogger(StormConfig.class);
    private static StormConfig config = null;

    private Map<String, Map<String, String>> map = null;

    private StormConfig(String path) {
        SAXReader saxReader = new SAXReader();
        map = new HashMap<String, Map<String, String>>();
        try {
            FileInputStream resource = new FileInputStream(path);
//            InputStream resource = StormConfig.class.getClassLoader().getResourceAsStream(path+"config/BABJConfig.xml");
//			Document document = saxReader.read(new File(path+"config/EIConfig.xml"));
            Document document = saxReader.read(resource);

            // 获取根元素
            Element root = document.getRootElement();
            // 获取所有子元素
            List<Element> childList = root.elements();
            int count = 0;
            for (int i = 0; i < childList.size(); i++) {
                String name = childList.get(i).attribute("id").getStringValue();

                System.out.println("加载配置文件-元素：" + name);
                HashMap<String, String> Emap = new HashMap<>();
                if ("base".equals(name)) {
                    Emap.put("TABLE_HOR", childList.get(i).element("TABLE_HOR").getStringValue());
                    
                    //2019-11-4 chy
                    Emap.put("TABLE_GLB_HOR", childList.get(i).element("TABLE_GLB_HOR").getStringValue());
                    Emap.put("TABLE_WEATHER", childList.get(i).element("TABLE_WEATHER").getStringValue());
                    
                    Emap.put("TABLE_MIN", childList.get(i).element("TABLE_MIN").getStringValue());
                    Emap.put("TABLE_MIN_PRE", childList.get(i).element("TABLE_MIN_PRE").getStringValue());
                    Emap.put("TABLE_CUM_PRE", childList.get(i).element("TABLE_CUM_PRE").getStringValue());
                    Emap.put("TABLE_REP", childList.get(i).element("TABLE_REP").getStringValue());
                    Emap.put("BATCHSIZE", childList.get(i).element("BATCHSIZE").getStringValue());
                    Emap.put("INTERVALTIME", childList.get(i).element("INTERVALTIME").getStringValue());
                    Emap.put("DATASOURCE", childList.get(i).element("DATASOURCE").getStringValue());
                    Emap.put("DATA_FLOW", childList.get(i).element("DATA_FLOW").getStringValue());
                    Emap.put("CTS_CODE", childList.get(i).element("CTS_CODE").getStringValue());
                    Emap.put("HOR_SOD_CODE", childList.get(i).element("HOR_SOD_CODE").getStringValue());
                    
                    // 2019-11-4 chy
                    Emap.put("GLB_HOR_SOD_CODE", childList.get(i).element("GLB_HOR_SOD_CODE").getStringValue());
                    Emap.put("WEATHER_SOD_CODE", childList.get(i).element("WEATHER_SOD_CODE").getStringValue());
                    
                    
                    Emap.put("MUL_SOD_CODE", childList.get(i).element("MUL_SOD_CODE").getStringValue());
                    Emap.put("PRE_SOD_CODE", childList.get(i).element("PRE_SOD_CODE").getStringValue());
                    Emap.put("ACCU_SOD_CODE", childList.get(i).element("ACCU_SOD_CODE").getStringValue());
                    Emap.put("REPORT_SOD_CODE", childList.get(i).element("REPORT_SOD_CODE").getStringValue());
                    map.put(name, Emap);
                } else if ("DIEI".equals(name)) {
                    Emap.put("DI_OPTION", childList.get(i).element("DI_OPTION").getStringValue());
                    Emap.put("EI_OPTION", childList.get(i).element("EI_OPTION").getStringValue());
                    map.put(name, Emap);
                } else if ("storm".equals(name)) {
                    Emap.put("WORKER", childList.get(i).element("WORKER").getStringValue());
                    Emap.put("ACKER", childList.get(i).element("ACKER").getStringValue());
                    map.put(name, Emap);
                } else if ("rabbitmq".equals(name)) {
                    count++;
                    Emap.put("num", count + "");

                    Emap.put("QUEUE", childList.get(i).element("QUEUE").getStringValue());
                    Emap.put("EXCHANGE_NAME", childList.get(i).element("EXCHANGE_NAME").getStringValue());
                    //Emap.put("HOST", childList.get(i).element("HOST").getStringValue());
                    //Emap.put("USER", childList.get(i).element("USER").getStringValue());
                    //Emap.put("PASSWORD", childList.get(i).element("PASSWORD").getStringValue());
                    //Emap.put("PORT",childList.get(i).element("PORT").getStringValue());
                    Emap.put("SPOUT", childList.get(i).element("SPOUT").getStringValue());
                    Emap.put("BOLT", childList.get(i).element("BOLT").getStringValue());
                    Emap.put("DIEIBOLT", childList.get(i).element("DIEIBOLT").getStringValue());
                    Emap.put("MINPREBOLT", childList.get(i).element("MINPREBOLT").getStringValue());
                    
                    map.put(name + String.valueOf(count), Emap);
                    map.put(name, Emap);
                }else if("file".equals(name)){
                    Emap.put("SRC", childList.get(i).element("SRC").getStringValue());
                    Emap.put("TARGET", childList.get(i).element("TARGET").getStringValue());
                    Emap.put("THREAD", childList.get(i).element("THREAD").getStringValue());
                    map.put(name, Emap);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();

//            logger.error("配置文件错误！");
        }

    }

    /**
     * 单例模式，配置文件加载一次
     *
     * @throws
     * @Title: getEiConfig
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param: @return
     * @return: EIConfig
     */
    public static StormConfig getConfig(String path) {
        if (config == null) {
            config = new StormConfig(path);
        }
        return config;
    }


    public Map<String, Map<String, String>> getMaps() {
        return map;
    }

    public void setMaps(Map<String, Map<String, String>> map) {
        this.map = map;
    }


    public static void main(String[] args) {
        Map<String, Map<String, String>> map = StormConfig.getConfig("D:\\BBBBB\\dwp\\dwp -ots\\cimiss2-dwp\\cimiss2-dwp-core\\config\\BABJConfig.xml").getMaps();
        for (String key : map.keySet()) {
            System.out.println(key);
        }
        System.out.println(map.get("file").get("SRC"));
        System.out.println(map.get("file").get("TARGET"));
//        System.out.println(map.get("base").get("CONFIG_PATH"));
//        int num = Integer.valueOf(map.get("rabbitmq").get("num"));
//        System.out.println(num);
//        System.out.println(map.get("rabbitmq2").get("QUEUE"));
//        System.out.println(map.get("base").get("TABLE_MIN"));
////        String s = map.get("mysql").get("BATCHSIZE");
//        int batchSize = Integer.valueOf(map.get("base").get("BATCHSIZE"));
//        System.out.println(batchSize);


    }
}
