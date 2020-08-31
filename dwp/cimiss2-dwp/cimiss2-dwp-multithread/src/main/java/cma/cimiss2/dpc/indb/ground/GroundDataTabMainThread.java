package cma.cimiss2.dpc.indb.ground;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.radi.RadiDigChnMulTab;
import cma.cimiss2.dpc.decoder.fileDecode.A0File.decode.A0Decoding;
import cma.cimiss2.dpc.decoder.fileDecode.AFile.decode.ADecoding;
import cma.cimiss2.dpc.decoder.fileDecode.Interface.IFileDecoding;
import cma.cimiss2.dpc.decoder.fileDecode.common.bean.*;
import cma.cimiss2.dpc.decoder.fileDecode.util.FileHadleUtil;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.ground.service.InsertDB;
import cma.cimiss2.dpc.indb.radi.cd_radi_bmb_dat_tab.BmbDatTabSubThread;
import cma.cimiss2.dpc.indb.utils.FileDataTimeUtil;
import org.apache.commons.io.FileUtils;
import org.cimiss2.dwp.tools.config.StartConfig;
import sun.applet.Main;

import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A0文件解码入库启动线程
 * @author yang.kq
 * @version 1.0
 * @date 2020/3/19 9:31
 */
public class GroundDataTabMainThread {

//    public static void start() {
//        BlockingQueue<StatDi> diQueues = new LinkedBlockingQueue<>();
//        int threadCount = 1;   // 线程池的大小
//        threadCount = StartConfig.getThreadCount();
//        // 启动固定线程池
//        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
//        // 消息中间件
//        if(StartConfig.fileLoop() == 0) {
//            for(int i=0;i<threadCount;i++) {
//                executor.execute(new GroundDataTabSubThread(diQueues));
//            }
//        }
//    }
    public  static String path=StartConfig.ctsCode("ground_file_path");
    public  static int taskTime=Integer.parseInt(StartConfig.ctsCode("ground_task_time"));

    private static BlockingQueue<StatDi> diQueues = new LinkedBlockingQueue<StatDi>();
    private static GroundDataTabSubThread groundDataTabSubThread=  new GroundDataTabSubThread(diQueues);
    public static void start(){
        //初始化文件时间
        FileDataTimeUtil.loadFileDataProperties();
        new Timer("GroundData" ).schedule(new TimerTask() {

            @Override
            public void run() {
                ParseResult<String> decodingInfo = new ParseResult<>(false);
                IFileDecoding decoding = null;
                Map<String, Object> resultMap = null;
                String lastTime =FileDataTimeUtil.getProperties().get("ground_file_lastTime").toString().trim();
                InsertDB db = null;
                List<String> list = new ArrayList<>();
                FileHadleUtil.getFilePath(path, list, lastTime);
                String filePath = null;
                StringBuffer loggerBuffer = null;
                long maxFileTime = 0;
                for (int i = 0; i < list.size(); i++) {
                    filePath = list.get(i);
                    loggerBuffer = new StringBuffer();
                    groundDataTabSubThread.processMsg(filePath, new Date(), loggerBuffer);
                    File file = new File(filePath);
                    //读取解码文件完成，删除文件
//                    FileUtils.deleteQuietly(file);
                    long fileLastTime = file.lastModified();
                    if (maxFileTime < fileLastTime) {
                        maxFileTime = file.lastModified();
                    }
                }
                if (maxFileTime != 0) {
                    //更新文件时间
                    FileDataTimeUtil.updateFileData("ground_file_lastTime", maxFileTime + "");
                }
            }
        }, 0,taskTime);
    }

    public static void main(String[] args) {
            start();
    }
}
