package cma.cimiss2.dpc.indb.detection.day;


import cma.cimiss2.dpc.indb.core.tools.ConfigurationManager;
import cma.cimiss2.dpc.indb.core.tools.LogUtil;


import org.nutz.ioc.Ioc;

import java.sql.SQLException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 质控后地面气象要素日值资料_一体化
 * 端口 ：4011
 * Created by xzh on 2017/11/01.
 */
public class radi_day {
    static Ioc ioc;
    static String dataSource;

    public static void main(String[] args) throws SQLException, InterruptedException {
        if (args == null || args.length != 2) {
            System.out.println( "args is null,args[0]=<port>,args[1]=<properties> ");
            System.exit(1);
        }
        try {
            ConfigurationManager.loadPro("db.properties",args[1]);
        }catch (Exception e){
            System.out.println("properties error!"+e.getCause());
            System.exit(1);
        }

        System.setProperty("process_log", ConfigurationManager.getString("process_log")
                .replaceAll("\\{name}","mpr_pre")
                .replaceAll("\\{port}",args[0]));
        System.setProperty("message_log", ConfigurationManager.getString("message_log")
                .replaceAll("\\{name}","mpr_pre")
                .replaceAll("\\{port}",args[0]));


        LogUtil.info("loggerInfo", "降水检测的预报与实况配对数据" + new Date());
        System.out.println(ConfigurationManager.getString("RabbitMQ.host"));
        System.out.println("配置文件："+args[1]);
        radi_day dayValMainThread = new radi_day();
        
        //定时执行任务
    /*    Timer timer = new Timer();
        timer.schedule(new MyTask(dayValMainThread),0,600000);*/
        
        //执行一次
        dayValMainThread.run();

    }

    public void run() {
        int count = ConfigurationManager.getInteger("MessageThreadCount");
        ExecutorService executor = Executors.newFixedThreadPool(count);
        if  ("1".equals(ConfigurationManager.getString("fileloop"))) {
            executor.execute(new FilePolling(ConfigurationManager.getString("src"), "PRE_F.0052.0001.S001:"));
        }
        for (int i = 0; i < count; i++) { 
                executor.execute(new DayValueMysqlSubThread());
        }

        executor.shutdown();
    }
}

/*class MyTask extends TimerTask{

	private radi_day dayValMainThread;
	
    public MyTask(radi_day dayValMainThread) { 
    	this.dayValMainThread = dayValMainThread;
	}

	@Override
	public void run() {

		System.out.println(System.currentTimeMillis()/1000 + "---");
		dayValMainThread.run();
	}
}
*/