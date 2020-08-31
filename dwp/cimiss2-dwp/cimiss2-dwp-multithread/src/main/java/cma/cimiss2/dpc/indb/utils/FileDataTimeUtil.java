package cma.cimiss2.dpc.indb.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * @author yangkq
 * @version 1.0
 * @date 2020/8/14
 */
public class FileDataTimeUtil {
    public static Properties properties=null;
    public static URL url=null;
    public static File file=null;
    public static void loadFileDataProperties(){
         properties = new Properties();
        try{
            url= Thread.currentThread().getContextClassLoader().getResource("fileDataTime.properties");
            if(url!=null){
               file= new File(url.getFile());
            }

            // 使用ClassLoader加载properties配置文件生成对应的输入流
            InputStream in = new FileInputStream(file);
            // 使用properties对象加载输入流
            properties.load(in);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static Properties getProperties(){
        return properties;
    }
    public static void updateFileData(String key,String value){
        try{
            properties.setProperty(key,value);
            FileOutputStream outputStream = new FileOutputStream(file);
            properties.store(outputStream, null);
            loadFileDataProperties();
        }catch (Exception e){
            e.printStackTrace();
        }

}
    public static void main(String[] args) {
        FileDataTimeUtil.loadFileDataProperties();
        FileDataTimeUtil.updateFileData("S001_tim_lastTime","2131231");
        String t=FileDataTimeUtil.getProperties().get("S001_tim_lastTime").toString().trim();
        System.out.println(t);
    }
}
