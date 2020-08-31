package cma.cimiss2.dpc.indb.xml.common_xml;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import org.cimiss2.dwp.tools.config.DiEiConfig;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.slf4j.LoggerFactory;
import com.hitec.bufr.util.StringUtil;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.xml.XmlDecodeFile;
import cma.cimiss2.dpc.indb.common.LoopFolderThread;
import cma.cimiss2.dpc.indb.sevp.DISubThread;

/**
 * ************************************
 * 
 * @ClassName: MainThread
 * @Auther: liuwenxia
 * @Date: 2019/4/1 14:18
 * @Description: 全球海表观测定时值数据集--进程启动
 * @Copyright: All rights reserver. ************************************
 */

public class MainThread {

    static BlockingDeque<StatDi> diQueues = new LinkedBlockingDeque<>();
    // 声明一个缓存队列，存放轮询到的文件路径
    private static BlockingQueue<String> files = new LinkedBlockingQueue<String>();
    private static int threadCount = 1;
    //private static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");

    public static void main(String[] args) {
        System.out.println(args[0]);
        System.out.println(args[1]);
        System.out.println(args[2]);
        System.out.println(args[3]);
        if (args.length < 3) {
            System.out.println("请输入端口号 + 配置文件名 + 入库配置xml文件名 + DiEi配置文件");
        } else {
            try {
                StartConfig.runport = Integer.parseInt(args[0].trim());
            } catch (Exception e) {
                System.out.println("第一个参数为程序端口号，请输入正确的端口号");
                e.printStackTrace();
                return;
            }
            File file = new File(StringUtil.getConfigPath() + args[1].trim()); // common_xml/surf/common_xml_config.properties   
            if (!file.exists() || !file.isFile()) {
                System.out.println("配置文件不存在 :" + file.getPath());
                return;
            }
            StartConfig.setConfigFile(file.getPath());
            // 配置入库xml提取
            String xmlfile = StringUtil.getConfigPath() + args[2].trim(); // common_xml/surf/OCEN_SHB_GLB_FTM_REAL.xml
            File xmlFile = new File(xmlfile);
            if (!xmlFile.exists() || !xmlFile.isFile()) {
                System.out.println("入库配置xml文件不存在 :" + xmlFile.getPath());
                return;
            }

            // xml文件读取
            boolean xmlDecodeResult = XmlDecodeFile.XmlFileDecode(LoggerFactory.getLogger("loggerInfo"), xmlfile);
            if (xmlDecodeResult == false || XmlDecodeFile.xmlDecoder == null
                    || XmlDecodeFile.xmlDecoder.size() == 0) {
                System.out.println("入库配置xml文件解析失败 :" + xmlFile.getPath());
                return;
            }


            // DIEI配置文件提取
            String DIFIfile = StringUtil.getConfigPath() + args[3].trim();// common_xml/surf/di_ei_config.properties
            File DIFIFile = new File(DIFIfile);
            if (!DIFIFile.exists() || !DIFIFile.isFile()) {
                System.out.println("入库配置xml文件不存在 :" + DIFIFile.getPath());
                return;
            }
            // DiEi配置文件读取
            new DiEiConfig(DIFIfile);
            if (DiEiConfig.parseResult == false) {
                System.out.println("DiEi配置文件解析失败 :" + DiEiConfig.propertiesFile);
                return;
            }

            
            threadCount = StartConfig.getThreadCount();
            try {
                @SuppressWarnings({"unused", "resource"})
                ServerSocket server = new ServerSocket(StartConfig.runport);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return;
            }
            //        new ThreadPoolExecutor(threadCount, 10, 10, TimeUnit.SECONDS,
            //                new ArrayBlockingQueue<>(20), new ThreadPoolExecutor.DiscardPolicy());
            ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);
            // 消息队列
            if (StartConfig.fileLoop() == 0) {
                for (int i = 0; i < threadCount; i++) {
                    threadPool.execute(new SubThread(diQueues));
                }
            } else if (StartConfig.fileLoop() == 1) {
                // 目录轮询
                for (int i = 0; i < threadCount; i++) {
                    threadPool.execute(new FileLoopThread(files, diQueues));
                }
                // 读取本地存见放入files队列中
                ExecutorService service = Executors.newFixedThreadPool(1);
                service.execute(new LoopFolderThread(files));
            }
            // DISubThread
            ExecutorService service = Executors.newFixedThreadPool(1);
            service.execute(new DISubThread(diQueues));
        }
    }
}
