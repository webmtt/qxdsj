package cma.cimiss2.dpc.indb.surf.dc_surf_reg;


import cma.cimiss2.dpc.indb.core.tools.ConfigurationManager;
import cma.cimiss2.dpc.indb.core.tools.LogUtil;
import cma.cimiss2.dpc.indb.surf.FilePolling;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: 区域站文件轮询主类
 * @Aouthor: xzh
 * @create: 2018-05-21 11:46
 */
public class dpc_surf_reg {


    public static void main(String[] args) {
        if (args == null || args.length != 2) {
            System.out.println("args is null,args[0]=<port>,args[1]=<properties> ");
            System.exit(1);
        }
		try {
			ConfigurationManager.loadPros("db.properties", "dpc_global_config.properties", args[1]);
		} catch (Exception e) {
			System.out.println("properties error!" + e.getCause());
			System.exit(1);
		}

        System.setProperty("process_log", ConfigurationManager.getString("process_log")
                .replaceAll("\\{name}","reg")
                .replaceAll("\\{port}",args[0]));
        System.setProperty("message_log", ConfigurationManager.getString("message_log")
                .replaceAll("\\{name}","reg")
                .replaceAll("\\{port}",args[0]));

        LogUtil.info("loggerInfo", "区域站资料开始运行" + new Date());
        System.out.println(ConfigurationManager.getString("RabbitMQ.host"));
        System.out.println("配置文件："+args[1]);
        dpc_surf_reg regMainThread = new dpc_surf_reg();
        
        regMainThread.run();

    }
    public void run() {
		int count = ConfigurationManager.getInteger("MessageThreadCount");
		String cts_code = ConfigurationManager.getString("cts_code");
		String dataSource = ConfigurationManager.getString("dataBaseType");
		ExecutorService executor = Executors.newFixedThreadPool(count);
		if ("1".equals(ConfigurationManager.getString("fileloop"))) {
			System.out.println("Start Loop File...............");
			System.out.println("Folder: "  + ConfigurationManager.getString("src"));
			System.out.println("Data Type: " + cts_code);
			executor.execute(new FilePolling(ConfigurationManager.getString("src"), cts_code + ":"));
		}
		for (int i = 0; i < count; i++) {
			switch (dataSource) {
			case "2":
				executor.execute(new REGMysqlSubThread());
				break;
			case "1":
				executor.execute(new REGMysqlSubThread());
				break;
			case "0":
				executor.execute(new REGMysqlSubThread());
				break;
			default:
				LogUtil.error("loggerInfo", "缓冲库类型选择失败");
				return;
			}
		}

		executor.shutdown();
    	
    	
    	/*int count = ConfigurationManager.getInteger("thread-num");
        String src = ConfigurationManager.getString("src");
        count = count > 1 ? count : count + 1;
        ExecutorService executor = Executors.newFixedThreadPool(count);

        executor.execute(new FilePolling(src, "A.0001.0028.R001:"));
        for (int i = 0; i < count; i++) {
            executor.execute(new FileREGSubThread());
        }
        executor.shutdown();*/
    }
}
