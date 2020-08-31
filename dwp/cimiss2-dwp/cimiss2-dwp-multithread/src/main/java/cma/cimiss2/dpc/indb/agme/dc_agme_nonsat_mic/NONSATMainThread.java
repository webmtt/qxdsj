package cma.cimiss2.dpc.indb.agme.dc_agme_nonsat_mic;


import org.cimiss2.dwp.tools.config.StartConfig;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * All rights Reserved, Designed By www.cma.gov.cn
 * @Title:  NONSATMainThread.java
 * @Package cma.cimiss2.dpc.indb.agme.dc_agme_nonsat_mic
 * @Description:    TODO(非标准格式农田小气候（中环天仪） N.0001.0001.S001)
 * @author: zhaoxiaojun
 * @date:   2019年9月30日 下午2:42:42
 * @version V1.0
 * @Copyright: 2019 www.cma.gov.cn Inc. All rights reserved.
 * 注意：本内容仅限于国家气象信息中心内部传阅，禁止外泄以及用于其他的商业目
 */
public class NONSATMainThread {

    private static String configPropertiesFile = "D:\\N.0001.0001.S001_config.properties";

    /**
     * create by zxj
     * updateDate 2019-07-25
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
                excutor.execute(new NONSATSubThread(topic));
            }
            Long t2 = System.currentTimeMillis();
            System.out.println("All is finished! 耗时：" + (t2 - t1));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
