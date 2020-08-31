package cma.cimiss2.dpc.fileloop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.cimiss2.dwp.tools.config.StartConfig;
import cma.cimiss2.dpc.fileloop.common.FileLoopThread;
import cma.cimiss2.dpc.fileloop.common.QuartzManager;
import cma.cimiss2.dpc.fileloop.di.DISubThread;

public class MainApp {
//	private final static Logger logger = LoggerFactory.getLogger(MainApp.class);
	public static void main(String[] args) {
		
//		final Logger logger = Logger.getLogger(MainApp.class);
//		File file = new File("/space/dpc/work/input");
//		String regEx = "[A-Z]";
//		// 编译正则表达式
//		Pattern pattern = Pattern.compile(regEx);
//		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);
//		for (File subFile : file.listFiles()) {
//			Matcher matcher = pattern.matcher(subFile.getName());
//			if (!matcher.matches()) {
//				logger.error("该文件夹为异常文件夹：" + subFile.getAbsolutePath());
//				continue;
//			}else {
//				logger.info(" start loop dir : " + subFile.getPath());
//				System.out.println(subFile.getPath());
//				LoopThread thread = new LoopThread(subFile.getPath());
//				fixedThreadPool.submit(thread);
//			}
//		}
//		File file = new File("D:\\cmadass\\code\\dwp\\cimiss2-dwp\\cimiss2-dwp-multithread\\target\\dependency");
//		for (File ff : file.listFiles()) {
//			System.out.println(ff.getName());
//		}
//		FileLoopThread fileLoopThread = new FileLoopThread();
//		try {
//			fileLoopThread.execute(null);
//		} catch (JobExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		StartConfig.isProperties = false;
		StartConfig.cts_code = "FileLoop";
		QuartzManager.addJob("FileLoopThread", "FileLoopThread", "FileLoopThread", "FileLoopThread", FileLoopThread.class, "0 */30 * * * ?");
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
		fixedThreadPool.execute(new DISubThread());
//		logger.info("QuartzManager.addJob(\"FileLoopThread\", \"FileLoopThread\", \"FileLoopThread\", \"FileLoopThread\", FileLoopThread.class, \"0 */30 * * * ?\");");
		System.out.println("QuartzManager.addJob(\"FileLoopThread\", \"FileLoopThread\", \"FileLoopThread\", \"FileLoopThread\", FileLoopThread.class, \"0 */30 * * * ?\");");
//		Calendar calendar = Calendar.getInstance();
//		calendar.set(Calendar.DAY_OF_MONTH, 11);
//		calendar.set(Calendar.HOUR_OF_DAY, 0);
//		calendar.set(Calendar.MINUTE, 0);
//		calendar.set(Calendar.SECOND, 0);
//		System.out.println(calendar.getTime());
//		
//		Calendar calendar1 = Calendar.getInstance();
//		calendar1.set(Calendar.DAY_OF_MONTH, 12);
//		calendar1.set(Calendar.HOUR_OF_DAY, 12);
//		calendar1.set(Calendar.MINUTE, 0);
//		calendar1.set(Calendar.SECOND, 0);
//		MakeUpData.getDatafromDirToRMQ(calendar.getTime(), calendar1.getTime(), "/space/dpc/work/data", null);
	
		
	}

}
