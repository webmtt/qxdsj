package cma.cimiss2.dpc.indb.surf.dc_mul_hor;

import org.cimiss2.dwp.tools.config.StartConfig;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * C4600地面多要素数据
 * Created by zxj on 2019/10/28.
 */
public class MulhorMainThread {

    private static String configPropertiesFile = "D:\\解码文件\\NM.0001.0001.S001_config.properties";

    /**
     * create by zxj
     * updateDate 2019-10-28
     */
    public static void main(String[] args) {
        try {
            File file = new File(configPropertiesFile);
            if (!file.exists() || !file.isFile()) {
                System.out.println("not find " + configPropertiesFile);
                return;
            }

            // 加载配置文件
            StartConfig.setConfigFile(configPropertiesFile);
            Long t1 = System.currentTimeMillis();
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
            String startTime = df.format(date);
            System.out.println("开始运行时间：" + startTime);

            // 启动入库子线程，入库表名：
            int count = StartConfig.getThreadCount();//默认线程数为1
            String topic = StartConfig.ctsCode();//获取资料四级编码
            ExecutorService excutor = Executors.newFixedThreadPool(count);
            for (int i = 0; i < count; i++) {
                excutor.execute(new MulhorThread(topic));
            }
            Long t2 = System.currentTimeMillis();
            System.out.println("All is finished! 耗时：" + (t2 - t1));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
