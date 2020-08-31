package org.cimiss2.dwp.tools.utils;

import org.nutz.lang.Files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * properties文件加载类
 */
public class ConfigurationManager {

    private static Properties prop = new Properties();

    public static void main(String[] args) {
//        ConfigurationManager.loadPro("D:\\config\\dayLight.properties", "D:\\config\\db.properties");
        System.out.println("----" + ConfigurationManager.getInteger("rdb.driverClassName") + "----");
    }

    /**
     * 加载配置文件  path为文件路径，可以接受多个文件，后面的文件优先级高于前面的文件，相同key值，保留后传文件的value
     *
     * @param path
     */
    public static void loadPro(String... path) {
        String jarWholePath = getConfigPath();
        int length = path.length;
        for (int i = 0; i < length; i++) {
            Properties pro = new Properties();
            InputStream in = Files.findFileAsStream(jarWholePath + path[i]);
            try {
                pro.load(in);
            } catch (IOException e) {
                System.out.println(path[i] + "加载文件错误。。。。。。");
            }
            prop.putAll(pro);
        }
    }

    public static void loadPro(String path ,String name) {

            Properties pro = new Properties();
            InputStream in = Files.findFileAsStream(path+name);
            try {
                pro.load(in);
            } catch (IOException e) {
                System.out.println(path+name + "加载文件错误。。。。。。");

            }
            prop.putAll(pro);

    }


    /**
     * 获取指定key对应的value
     *
     * @param key
     * @return value
     */
    public static String getString(String key) {
        if (prop == null || prop.size() == 0) {
            return null;
        }
        String property = prop.getProperty(key);
        if (property != null) {
            property = property.trim();
        }
        return property;
    }

    /**
     * 获取整数类型的配置项
     *
     * @param key
     * @return value
     */
    public static Integer getInteger(String key) {
        if (prop == null || prop.size() == 0) {
            return 0;
        }
        String value = null;
        try {
            value = getString(key);
            if (value==null){
                return 0;
            }
            return Integer.valueOf(value);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("getInteger=" + key + "=错误========" + value);
        }
        return 0;
    }

    /**
     * 获取布尔类型的配置项
     *
     * @param key
     * @return value
     */
    public static Boolean getBoolean(String key) {
        if (prop == null || prop.size() == 0) {
            return false;
        }
        String value;
        try {
            value = getString(key);
            return Boolean.valueOf(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取Long类型的配置项
     *
     * @param key
     * @return
     */
    public static Long getLong(String key) {
        if (prop == null || prop.size() == 0) {
            return 0L;
        }
        String value;
        try {
            value = getString(key);
            if (value==null){
                return 0L;
            }
            return Long.valueOf(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * 获取List类型的配置项
     *
     * @param key
     * @return
     */
    public static List<String> getList(String key) {
        if (prop == null || prop.size() == 0) {
            return null;
        }
        String value = getString(key);
        String[] array = value.split(",");
        List<String> abcList = new ArrayList<String>();
        for (String str : array) {
            abcList.add(str);
        }
        try {
            return abcList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取配置文件路径，要求jar包上层目录中config文件夹下 home/dpc/dwp-ali/config/
     *
     * @return
     */
    public static String getConfigPath() {
        return getJarSuperPath() + "config/";

    }

    /**
     * 获取jar包上级路径 home/dpc/dwp-ali/
     *
     * @return
     */
    public static String getJarSuperPath() {
        String jarCurrentPath = getJarCurrentPath();
        jarCurrentPath = jarCurrentPath.replace("\\", "/");
        int i = jarCurrentPath.lastIndexOf("/") + 1;
        String jarSuperPath = jarCurrentPath.substring(0, i);
        return jarSuperPath;
    }
    /**
     * 
    * @Function: ConfigurationManager.java
    * @Description: 本地生产公用获取jar包上级目录方法
    *
    * @param:描述1描述
    * @return：返回结果描述
    * @throws：异常描述
    *
    *
     */
    public static String getSuperPath() {
    	String path = System.getProperty("user.dir");
    	path = path.replace("\\", "/");
    	int i = path.lastIndexOf("/") + 1;
    	String superpath = path.substring(0, i);
    	return superpath;
    }
    /**
     * 获取jar包当前路径 home/dpc/dwp-ali/bin
     *
     * @return
     */
    public static String getJarCurrentPath() {
        String jarWholePath = "";
        try {
            jarWholePath = StationInfo.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            try {
                jarWholePath = java.net.URLDecoder.decode(jarWholePath, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                System.out.println(e.toString());
            }
            jarWholePath = new File(jarWholePath).getParentFile().getAbsolutePath();///  home/dpc/dwp-ali/bin
            jarWholePath = jarWholePath.replace("\\", "/");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jarWholePath;
    }

}
