package cma.cimiss2.dpc.indb.core.tools;

import org.nutz.lang.Files;

import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil2;

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
    public static void loadPros(String... path) {
    	TimeCheckUtil2.timeCheckUtil = TimeCheckUtil2.getInstance();
        String jarWholePath = getConfigPath();
        int length = path.length;
        for (int i = 0; i < length; i++) {
        	System.out.println(jarWholePath + path[i]);
            Properties pro = new Properties();
            InputStream in = Files.findFileAsStream(jarWholePath + path[i]);
//            InputStream in = Files.findFileAsStream(path[i]);
            try {
                pro.load(in);
               
            } catch (IOException e) {
                System.out.println(path[i] + "加载文件错误。。。。。。");
            }
            
            if(pro.containsKey("D_DATETIME_AFTER_DAY") && pro.containsKey("D_DATETIME_BEFORE_DAY")) {
				TimeCheckUtil2.timeCheckUtil.setAfter_day(Integer.parseInt(pro.getProperty("D_DATETIME_AFTER_DAY", "3").trim()));
				TimeCheckUtil2.timeCheckUtil.setBefore_day(Integer.parseInt(pro.getProperty("D_DATETIME_BEFORE_DAY", "3").trim()));
			}else if (pro.containsKey("D_DATETIME_AFTER_DAY")) {
				TimeCheckUtil2.timeCheckUtil.setAfter_day(Integer.parseInt(pro.getProperty("D_DATETIME_AFTER_DAY", "3").trim()));
				TimeCheckUtil2.timeCheckUtil.setBefore_day(1);
			}else if (pro.containsKey("D_DATETIME_BEFORE_DAY")) {
				TimeCheckUtil2.timeCheckUtil.setBefore_day(Integer.parseInt(pro.getProperty("D_DATETIME_BEFORE_DAY", "3").trim()));
				TimeCheckUtil2.timeCheckUtil.setAfter_day(1);
			}else {
				TimeCheckUtil2.timeCheckUtil.setAfter_day(1);
				TimeCheckUtil2.timeCheckUtil.setBefore_day(1);
			}
            System.out.print("======TimeCheckUtil.getAfter_day()======" + TimeCheckUtil2.timeCheckUtil.getAfter_day());
            System.out.print("======TimeCheckUtil.getBefore_day()======" + TimeCheckUtil2.timeCheckUtil.getBefore_day());

            prop.putAll(pro);
        }
    }

    public static void loadPro(String path ,String name) {
    	    TimeCheckUtil2.timeCheckUtil = TimeCheckUtil2.getInstance();
            Properties pro = new Properties();
            InputStream in = Files.findFileAsStream(path+name);
            try {
                pro.load(in);
            } catch (IOException e) {
            	e.printStackTrace();
                System.out.println(path+name + "加载文件错误。。。。。。");

            }
            if(pro.containsKey("D_DATETIME_AFTER_DAY") && pro.containsKey("D_DATETIME_BEFORE_DAY")) {
            	TimeCheckUtil2.timeCheckUtil.setAfter_day(Integer.parseInt(pro.getProperty("D_DATETIME_AFTER_DAY", "3").trim()));
            	TimeCheckUtil2.timeCheckUtil.setBefore_day(Integer.parseInt(pro.getProperty("D_DATETIME_BEFORE_DAY", "3").trim()));
			}else if (pro.containsKey("D_DATETIME_AFTER_DAY")) {
				TimeCheckUtil2.timeCheckUtil.setAfter_day(Integer.parseInt(pro.getProperty("D_DATETIME_AFTER_DAY", "3").trim()));
				TimeCheckUtil2.timeCheckUtil.setBefore_day(1);
			}else if (pro.containsKey("D_DATETIME_BEFORE_DAY")) {
				TimeCheckUtil2.timeCheckUtil.setBefore_day(Integer.parseInt(pro.getProperty("D_DATETIME_BEFORE_DAY", "3").trim()));
				TimeCheckUtil2.timeCheckUtil.setAfter_day(1);
			}else {
				TimeCheckUtil2.timeCheckUtil.setAfter_day(1);
				TimeCheckUtil2.timeCheckUtil.setBefore_day(1);
			}
            System.out.print("======TimeCheckUtil.getAfter_day()======" + TimeCheckUtil2.timeCheckUtil.getAfter_day());
            System.out.print("======TimeCheckUtil.getBefore_day()======" + TimeCheckUtil2.timeCheckUtil.getBefore_day());
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
        System.out.println(jarCurrentPath);
        String jarSuperPath = jarCurrentPath.substring(0, i);
        return jarSuperPath;
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
