package cma.cimiss2.dpc.indb.cawn.dc_chn_haz;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 雾霾数据集-霾
 * Created by zxj on 2019/11/08.
 */
public class ChnhazMainThread {

    private static String configPropertiesFile = "D:\\G.9901.0001.R008_config.properties";

    public static void main(String[] args) {

        try {
            File file = new File(configPropertiesFile);
            if(!file.exists() || !file.isFile()) {
                System.out.println("not find " + configPropertiesFile);
                return;
            }
            org.cimiss2.dwp.tools.config.StartConfig.setConfigFile(configPropertiesFile);
            Long t1 = System.currentTimeMillis();
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
            String startTime = df.format(date);
            System.out.println("开始运行时间：" + startTime);
            //启动入库子线程，入库表名：SURF_WEA_CHN_DAY_ALL_NAT_TAB_BJ
            int count = org.cimiss2.dwp.tools.config.StartConfig.getThreadCount();//默认线程数为1
            String topic = org.cimiss2.dwp.tools.config.StartConfig.ctsCode();//获取资料四级编码
            ExecutorService executor = Executors.newFixedThreadPool(count);
            for (int i = 0; i < count; i++) {
                executor.execute(new ChnhazThread(topic));
            }
            Long t2 = System.currentTimeMillis();
            System.out.println("All is finished! 耗时：" + (t2 - t1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
