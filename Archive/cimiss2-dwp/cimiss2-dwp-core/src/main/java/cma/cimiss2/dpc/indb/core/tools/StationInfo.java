package cma.cimiss2.dpc.indb.core.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Description:
 * @Aouthor: xzh
 * @create: 2017-11-20 17:54
 */
public class StationInfo {

    /**
     * 元数据reload判断标准
     */
    public static long judgeTime = System.currentTimeMillis();

    public static long getJudgeTime() {
        return judgeTime;
    }

    public static void setJudgeTime(long judgeTime) {
        StationInfo.judgeTime = judgeTime;
    }

    /**
     * @param path 区分多线程和storm调用，多线程默认路径为jar包上级目录下config/StationInfo_Config.lua
     *             storm 需要传入配置文件路基，如：/home/dpc/dwp/config/
     * @return map 站点信息集合，其中key=time的value为文件最后修改时间
     */
    public static Map<String, Object> getProMap(String path) {
        Properties properties = new Properties();
        Map<String, Object> proMap = new HashMap<String, Object>();
        System.out.println("=======getProMap===========" + path);
        InputStream resource = null;
        String jarWholePath = "";
        try {
            if ("".equals(path)) {
                jarWholePath = ConfigurationManager.getConfigPath()+ "StationInfo_Config.lua";
            } else {
                jarWholePath = path + "StationInfo_Config.lua";
            }
            resource = new FileInputStream(jarWholePath);
            properties.load(resource);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                proMap = (Map) properties;
                proMap.put("time", new File(jarWholePath).lastModified());//加入文件修改时间
                resource.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(jarWholePath + "chuanru path:--" + path);
        return proMap;
    }



    public static void main(String[] args) {
//        Map<String, Object> proMap = StationInfo.getProMap("");
//        for (String str : proMap.keySet()) {
//            System.out.println(str + "=====" + proMap.get(str));
//        }
//
//        String jarWholePath = getConfigPath();
//        System.out.println(jarWholePath);
//        String jarSuperPath = getJarSuperPath();
//        System.out.println(jarSuperPath);
//        String jarCurrentPath = getJarCurrentPath();
//        System.out.println(jarCurrentPath);
    }
}

