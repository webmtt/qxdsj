package cma.cimiss2.dpc.indb.radi.dc_surf_xili_gard_tab;

import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import org.cimiss2.dwp.tools.config.CTSCodeMap;
import org.cimiss2.dwp.tools.config.StartConfig;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author ：YCK
 * @date ：Created in 2019/11/7 0007 15:07
 * @description：近地面通量——梯度数据表
 * @modified By：
 * @version: 1.0$
 */
public class FXiliGardTabMainThread {
//    private static String configPropertiesFile = "D:\\X.0011.0001.S001_config.properties";
//
//    /**
//     * create by zxj
//     * updateDate 2019-07-25
//     */
//    public static void main(String[] args) {
//        try {
//            File file = new File(configPropertiesFile);
//            if (!file.exists() || !file.isFile()) {
//                System.out.println("not find " + configPropertiesFile);
//                return;
//            }
//
//            // 加载配置文件
//            StartConfig.setConfigFile(configPropertiesFile);
//            Long t1 = System.currentTimeMillis();
//            Date date = new Date();
//            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
//            String startTime = df.format(date);
//            System.out.println("开始运行时间：" + startTime);
//
//            // 启动入库子线程，入库表名：
//            int count = StartConfig.getThreadCount();//默认线程数为1
//            String topic = StartConfig.ctsCode();//获取资料四级编码
//            ExecutorService excutor = Executors.newFixedThreadPool(count);
//            for (int i = 0; i < count; i++) {
//                excutor.execute(new FXiliGardTabSubThread(topic));
//            }
//            Long t2 = System.currentTimeMillis();
//            System.out.println("All is finished! 耗时：" + (t2 - t1));
//        } catch (Exception e) {
//            e.printStackTrace();a
//        }
//
//    }
public static void start() {
        BlockingQueue<StatDi> diQueues = new LinkedBlockingQueue<StatDi>();
        int threadCount = 1;   // 线程池的大小
        ArrayList<CTSCodeMap> ctsCodeMaps = new ArrayList<>();
        threadCount = StartConfig.getThreadCount();
        String ctS = StartConfig.ctsCode("R009_cts_code");
        String sodSs[] = StartConfig.sodCodes("R009_sod_code");
        String reportSods[] = StartConfig.reportSodCodes();
        String valuetables[] = StartConfig.valueTables("R009_min_value_table_name");
        String reportTable = StartConfig.reportTable();
        for(int i = 0; i < valuetables.length; i ++){
            CTSCodeMap codeMap = new CTSCodeMap();
            codeMap.setCts_code(ctS);
            codeMap.setSod_code(sodSs[i]);
//            codeMap.setReport_sod_code(reportSods[i]);
            codeMap.setValue_table_name(valuetables[i]);
            codeMap.setReport_table_name(reportTable);
            ctsCodeMaps.add(codeMap);
        }
        // 启动固定线程池
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        // 消息中间件
        if(StartConfig.fileLoop() == 0) {
            for (int i = 0; i < threadCount; i++) {
                executor.execute(new FXiliGardTabSubThread(diQueues));
            }
        }
    }
    public static void main(String[] args) throws Exception {
        start();
    }
}
