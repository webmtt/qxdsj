package cma.cimiss2.dpc.decoder.tools.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @author ：YCK
 * @date ：Created in 2019/11/27 0027 9:34
 * @description：获取配置文件信息
 * @modified By：
 * @version: 1.0$
 */
public class GetPropertiesUtil {
    /**
     * 读取配置文件内容
     * @param condition
     * @return
     * @throws IOException
     */
    public static String getMessage(String condition) throws IOException {
        Properties properties = new Properties();
        // 使用InPutStream流读取properties文件
        BufferedReader bufferedReader = new BufferedReader(new FileReader("E:\\work\\dwp\\cimiss2-dwp\\cimiss2-dwp-multithread\\config\\general_config.properties"));
        properties.load(bufferedReader);
        // 获取key对应的value值
        return properties.getProperty(condition);
    }

    /**
     * 大小写转换
     * @param date
     * @return
     */
    public static String ChangeCase(String date){
        String one = date.substring(0,1);
        String two = date.substring(1,2);
        //判断是否为大写字母
        if( !Character.isUpperCase(date.charAt(0)) && !Character.isUpperCase(date.charAt(1))) {
            one = one.toUpperCase();
        }else if(!Character.isUpperCase(date.charAt(0)) && StringUtils.isNumeric(two)){
            one = one.toUpperCase();
        }
       return one + date.substring(1,date.length());
    }

}
