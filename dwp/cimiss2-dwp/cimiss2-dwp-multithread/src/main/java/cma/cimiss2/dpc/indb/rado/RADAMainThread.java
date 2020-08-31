package cma.cimiss2.dpc.indb.rado;

import cma.cimiss2.dpc.decoder.tools.common.FileDi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * <br>
 *
 * @author wuzuoqiang
 * @Title: RADAMainThread.java
 * @Package org.cimiss2.dwp.RADAR
 * @Description: TODO(雷达资料)
 *
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月14日 上午8:54:14   wuzuoqiang    Initial creation.
 * </pre>
 */
public class RADAMainThread {
    static BlockingQueue<FileDi> diQueues = new LinkedBlockingQueue<FileDi>();

    public static void main(String[] args) throws Exception {
        start(args);
    }

    public static void start(String[] args) throws InterruptedException {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
        String startTime = df.format(date);
        System.out.println("开始运行时间：" + startTime);

        //启动入文件索引库子线程
//        int count = StartConfig.getThreadCount();
        int count = 1;
        ExecutorService executor = Executors.newFixedThreadPool(count);

//        String ctsCode = "J.0020.0004.R001";
        String ctsCode = args[0];

        for (int i = 0; i < count; i++) {
            executor.execute(new IndexSubThread(diQueues, ctsCode));
        }
    }
}
