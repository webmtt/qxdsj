package com.rizhi.test;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class PropertiesUtil {



    /**
     * 配置文件转换工具类
     * 获取properties文件中的内容,并返回map
     * @return
     */
    public  Map<String, String> getProperties(String filePath) {
        Map<String, String> map = new HashMap<String, String>();
        InputStream in = null;
        Properties p = new Properties();
        try {
            in = new BufferedInputStream(new FileInputStream(new File(filePath)));
            p.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Set<Entry<Object, Object>> entrySet = p.entrySet();
        for (Entry<Object, Object> entry : entrySet) {
            map.put((String) entry.getKey(), (String) entry.getValue());
        }
        return map;
    }
}