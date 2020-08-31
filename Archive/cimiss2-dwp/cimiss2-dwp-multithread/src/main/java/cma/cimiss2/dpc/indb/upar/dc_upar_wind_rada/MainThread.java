package cma.cimiss2.dpc.indb.upar.dc_upar_wind_rada;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.QCUtil;

import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.common.LoopFolderThread;
import cma.cimiss2.dpc.indb.upar.DISubThread;

/**
 * ************************************
 * @ClassName: MainThread
 * @Auther: dangjinhu
 * @Date: 2019/3/21 08:34
 * @Description: 风廓线雷达资料解析--ascii--进程启动
 * @Copyright: All rights reserver.
 * ************************************
 */

public class MainThread {

    static BlockingDeque<StatDi> diQueues = new LinkedBlockingDeque<>();
    // 声明一个缓存队列，存放轮询到的文件路径
    private static BlockingQueue<String> files = new LinkedBlockingQueue<String>();
    private static int threadCount = 1;

    static {
        QCUtil.init();
    }

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
                System.out.println("第二个参数为配置文件，请输入");
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
            // timed task,初始化站点信息
            ScheduledExecutorService task = Executors.newSingleThreadScheduledExecutor();
            task.scheduleAtFixedRate(QCUtil::init, 0, 1, TimeUnit.DAYS);
            // 解码入库线程
            ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                    threadCount,
                    10,
                    10,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(20),
                    new ThreadPoolExecutor.DiscardPolicy());
            if (StartConfig.fileLoop() == 0) {
                // 线程数量
                for (int i = 0; i < threadCount; i++) {
                    threadPool.execute(new SubThread(diQueues));
                }
            } else if (StartConfig.fileLoop() == 1) {
                // 目录轮询
                for (int i = 0; i < threadCount; i++) {
                    threadPool.execute(new FileLoopThread(files, diQueues));
                }
                ExecutorService service = Executors.newFixedThreadPool(1);
                service.execute(new LoopFolderThread(files));
            }
            // DISubThread
            ExecutorService service = Executors.newFixedThreadPool(1);
            service.execute(new DISubThread(diQueues));

        }
    }
}
