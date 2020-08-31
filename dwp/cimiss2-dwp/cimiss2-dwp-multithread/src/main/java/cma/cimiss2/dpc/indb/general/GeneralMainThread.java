package cma.cimiss2.dpc.indb.general;

import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import org.cimiss2.dwp.tools.config.StartConfig;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author ：YCK
 * @date ：Created in 2019/11/27 0027 13:43
 * @description：
 * @modified By：
 * @version: $
 */
public class GeneralMainThread {
    static BlockingQueue<StatDi> diQueues = new LinkedBlockingQueue<StatDi>();
    private static int threadCount = 1;   // 线程池的大小
    // 声明一个缓存队列，存放轮询到的文件路径
    //static BlockingQueue<String> files = new LinkedBlockingQueue<String>();
    /**
     * create by zxj
     * updateDate 2019-07-25
     */
    public static void main(String[] args) {
        // 启动固定线程池
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        // 消息中间件
        if(StartConfig.fileLoop() == 0) {
            for (int i = 0; i < threadCount; i++) {
                executor.execute(new  GeneralSubThread(diQueues));
            }
        }
    }
}
