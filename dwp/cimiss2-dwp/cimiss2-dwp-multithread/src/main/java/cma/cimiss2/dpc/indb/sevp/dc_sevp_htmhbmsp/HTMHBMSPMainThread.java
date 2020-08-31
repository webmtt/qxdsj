package cma.cimiss2.dpc.indb.sevp.dc_sevp_htmhbmsp;

import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import org.cimiss2.dwp.tools.config.CTSCodeMap;
import org.cimiss2.dwp.tools.config.StartConfig;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author ywj
 * @title: HTMHBMSPMainThread
 * @projectName demo2
 * @description: 非结构化数据解码入库
 * Highway Traffic Meteorological Hourly Basic Monitoring Service Products
 * @date 2019/7/11 17:29
 */
public class HTMHBMSPMainThread {

//    private static String configPropertiesFile = "config/dc_sevp_htmhbmsp_config.properties";
//
//    public static void main(String[] args) {
//        /*if (args.length > 1) {
//            configPropertiesFile = args[0];
//        } else {
//            System.out.println("请输入配置文件参数！");
//            return;
//        }*/
//        File file = new File(configPropertiesFile);
//        if (!file.exists() || !file.isFile()) {
//            System.out.println("not find " + configPropertiesFile);
//            return;
//        }
//        StartConfig.setConfigFile(configPropertiesFile);
//        Long t1 = System.currentTimeMillis();
//        Date date = new Date();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
//        String startTime = df.format(date);
//        System.out.println("开始运行时间：" + startTime);
//
//
//        int count = StartConfig.getThreadCount();//默认线程数为1
//        ExecutorService executor = Executors.newFixedThreadPool(count);
//        for (int i = 0; i < count; i++) {
//            executor.execute(new HTMHBMSPSubThread());
//        }
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Long t2 = System.currentTimeMillis();
//        System.out.println("All is finished! 耗时：" + (t2 - t1));
//    }
static BlockingQueue<StatDi> diQueues = new LinkedBlockingQueue<StatDi>();
    private static int threadCount = 1;   // 线程池的大小
    public static ArrayList<CTSCodeMap> ctsCodeMaps = new ArrayList<>();
    // 声明一个缓存队列，存放轮询到的文件路径
    static BlockingQueue<String> files = new LinkedBlockingQueue<String>();

    /**
     * create by zxj
     * updateDate 2019-07-25
     */
    public static void main(String[] args) {
        /*if(args.length < 1) {
            System.out.println("请输出程序启动端口号！");
            return;
        }else {
            try {
                StartConfig.runport = Integer.parseInt(args[0].trim());
            } catch (Exception e) {
                System.out.println("第一个参数为程序端口号，请输入正确的端口号");
                e.printStackTrace();
                return;
            }
            if(args.length <2) {
                System.out.println("第二个参数为配置文件路径，请输入");
                return;
            }
            //File file = new File("config/upar_sing_config.properties");
            File file = new File(args[1].trim());
            if(!file.exists() || !file.isFile()) {
                System.out.println("config file not fond :" +file.getPath());
                return;
            }
            StartConfig.setConfigFile(file.getPath());
            threadCount = StartConfig.getThreadCount();*/
            /*try {
                ServerSocket server = new ServerSocket(StartConfig.runport);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                return;
            }*/
        threadCount = StartConfig.getThreadCount();
        String ctS = StartConfig.ctsCode("S024_cts_code");
        String sodSs[] = StartConfig.sodCodes("S024_sod_code");
        String reportSods[] = StartConfig.reportSodCodes();
        String valuetables[] = StartConfig.valueTables("S024_value_table_name");
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
                executor.execute(new HTMHBMSPSubThread());
            }
        }
    }
}
