package cma.cimiss2.dpc.indb.radi.dc_radi_chn_mut_myer_tab;

import cma.cimiss2.dpc.decoder.fileDecode.util.FileHadleUtil;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.radi.dc_radi_chn_mut_mmon_tab.ChnMutMmonTabSubThread;
import cma.cimiss2.dpc.indb.utils.FileDataTimeUtil;
import org.apache.commons.io.FileUtils;
import org.cimiss2.dwp.tools.config.CTSCodeMap;
import org.cimiss2.dwp.tools.config.StartConfig;

import java.io.File;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author ：YCK
 * @date ：Created in 2019/10/29 0029 13:20
 * @description：中国气象辐射基本要素累年年值数据集(v2.0)数据表
 * @modified By：
 * @version: 1.0$
 */
public class ChnMutMyerTabMainThread {
//    private static String configPropertiesFile = "D:\\M.0011.0001.S001_config.properties";
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
//                excutor.execute(new ChnMutMyerTabSubThread(topic));
//            }
//            Long t2 = System.currentTimeMillis();
//            System.out.println("All is finished! 耗时：" + (t2 - t1));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
public static void start1(){
        BlockingQueue<StatDi> diQueues = new LinkedBlockingQueue<StatDi>();
        int threadCount = 1;   // 线程池的大小
        ArrayList<CTSCodeMap> ctsCodeMaps = new ArrayList<>();
        threadCount = StartConfig.getThreadCount();
        String ctS = StartConfig.ctsCode("R012_cts_code");
        String sodSs[] = StartConfig.sodCodes("R012_sod_code");
        String reportSods[] = StartConfig.reportSodCodes();
        String valuetables[] = StartConfig.valueTables("R012_value_table_name");
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
                executor.execute(new ChnMutMyerTabSubThread(diQueues));
            }
        }
    }
    public static void main(String[] args) throws Exception {
        start();
    }
    private static BlockingQueue<StatDi> diQueues = new LinkedBlockingQueue<StatDi>();
    private static ChnMutMyerTabSubThread chnMutMyerTabSubThread=  new ChnMutMyerTabSubThread(diQueues);
    private static String path=StartConfig.ctsCode("R012_ChnMulMyer_file_path");
    private static int taskTime=Integer.parseInt(StartConfig.ctsCode("R012_ChnMulMyer_task_time"));

    public static void start(){
        //初始化文件时间
        FileDataTimeUtil.loadFileDataProperties();
        new Timer("ChnMulMyer" ).schedule(new TimerTask() {
            @Override
            public void run() {
                List<String> list=new ArrayList<>();
                String lastTime = FileDataTimeUtil.getProperties().get("R012_ChnMulMyer_lastTime").toString().trim();
                FileHadleUtil.getFilePath(path, list,lastTime);
                StringBuffer loggerBuffer=null;
                long maxFileTime=0;
                for (int i = 0; i < list.size(); i++) {
                    String filePath=list.get(i);
                    loggerBuffer=new StringBuffer();
                    chnMutMyerTabSubThread.processMsg(filePath, new Date(), loggerBuffer);
                    File file=new File(filePath);
                    System.out.println(file.getName()+"解码入库完成！");
                    //读取解码文件完成，删除文件
//                    FileUtils.deleteQuietly(file);
                    long fileLastTime=file.lastModified();
                    if(maxFileTime<fileLastTime){
                        maxFileTime=file.lastModified();
                    }
                }
                if(maxFileTime!=0) {
                    //更新文件时间
                    FileDataTimeUtil.updateFileData("R012_ChnMulMyer_lastTime", maxFileTime + "");
                }
            }
        },0,taskTime);
    }
}
