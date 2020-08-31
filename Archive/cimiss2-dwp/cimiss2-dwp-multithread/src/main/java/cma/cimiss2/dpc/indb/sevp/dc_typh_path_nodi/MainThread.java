package cma.cimiss2.dpc.indb.sevp.dc_typh_path_nodi;

import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.common.LoopFolderThread;
import cma.cimiss2.dpc.indb.sevp.DISubThread;
import org.cimiss2.dwp.tools.config.StartConfig;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.*;

/**
 * ************************************
 * @ClassName: MainThread
 * @Auther: dangjinhu
 * @Date: 2019/3/11 14:18
 * @Description: 台风路径和台风预测--进程启动
 * 全球台风强度预报资料 M.0004.0003.R001
 * 全球台风强度预报资料 M.0004.0002.R001 
 * @Copyright: All rights reserver.
 * ************************************
 */

public class MainThread {

    static BlockingDeque<StatDi> diQueues = new LinkedBlockingDeque<>();
    // 声明一个缓存队列，存放轮询到的文件路径
    private static BlockingQueue<String> files = new LinkedBlockingQueue<String>();
    private static int threadCount = 1;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("请输入端口号 + 配置文件名");
        } else {
            try {
                StartConfig.runport = Integer.parseInt(args[0].trim());
            } catch (Exception e) {
                System.out.println("第一个参数为程序端口号，请输入正确的端口号");
                e.printStackTrace();
                return;
            }
            if (args.length < 2) {
                System.out.println("第二个参数为配置文件名，请输入");
                return;
            }
            File file = new File(args[1].trim()); //config/bufr/bufr_hor_config.properties
            if (!file.exists() || !file.isFile()) {
                System.out.println("配置文件不存在 :" + file.getPath());
                return;
            }
            StartConfig.setConfigFile(file.getPath());
            threadCount = StartConfig.getThreadCount();
            try {
                ServerSocket server = new ServerSocket(StartConfig.runport);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return;
            }
            ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                    threadCount,
                    10,
                    10,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(20),
                    new ThreadPoolExecutor.DiscardPolicy());
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
