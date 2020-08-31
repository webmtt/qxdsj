package cma.cimiss2.dpc.indb.atmosphere;

import cma.cimiss2.dpc.decoder.tools.common.FileDi;
import cma.cimiss2.dpc.indb.numeric.NUMERICSubThread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * <br>
 * @Title:  RADAMainThread.java   
 * @Package org.cimiss2.dwp.RADAR   
 * @Description:    TODO(大气成分资料)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月14日 上午8:54:14   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *
 *
 */
public class ATMOSPHEREMainThread {
	static BlockingQueue<FileDi> diQueues = new LinkedBlockingQueue<FileDi>();
    public static void main(String[] args) throws Exception {
        start();
    }

    public static void start() throws InterruptedException {
//		int port = 9999;
//		if(args.length < 2) {
//			System.out.println("Please at least two parameters");
//			return;
//		}
//		try {
//			port = Integer.parseInt(args[0]);
//		} catch (Exception e) {
//			System.out.println("The first parameter must be the port number");
//			return;
//		}
//
//
//		String strConfig = "config/" + args[1]; //传递配置文件参数
//
//		File file = new File(strConfig);
//		if(!file.exists() || !file.isFile()) {
//			System.out.println("not find " + strConfig);
//			return;
//		}
//
//		try {
//			ServerSocket server = new ServerSocket(port);
////			server.accept();
//		} catch (IOException e) {
//			e.printStackTrace();
//
//			return;
//		}
//		StartConfig.setConfigFile(strConfig);
//		StartConfig.runport = port;
//		Long t1 = System.currentTimeMillis();
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
        String startTime = df.format(date);
        System.out.println("开始运行时间：" + startTime);

        //启动入文件索引库子线程
//        int count = StartConfig.getThreadCount();
        int count = 1;
        ExecutorService executor = Executors.newFixedThreadPool(count);
        for (int i = 0; i < count; i++) {
            executor.execute(new ATMOSPHERESubThread(diQueues));
        }
        //启动入库ATS子线程
//        ExecutorService executor1 = Executors.newFixedThreadPool(count);
//        for (int i = 0; i < count; i++) {
//        	executor1.execute(new ATSSubThread());
//        }
//        //DI线程启动
//        if (StartConfig.isSendDi()) {
//            count = StartConfig.getDiThreadCount();
//            ExecutorService executor2 = Executors.newFixedThreadPool(count);
//            for (int i = 0; i < count; i++) {
//            	executor2.execute(new DISubThread(diQueues));
//            }
//		}

//        Thread.sleep(1000);
//        Long t2 = System.currentTimeMillis();
//        System.out.println("All is finished! 耗时：" + (t2 - t1));
    }
}
