package cma.cimiss2.dpc.indb.surf.dc_surf_suns;


import cma.cimiss2.dpc.indb.core.tools.ConfigurationManager;
import cma.cimiss2.dpc.indb.core.tools.LogUtil;
import cma.cimiss2.dpc.indb.surf.FilePolling;
import org.nutz.ioc.Ioc;

import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 质控后地面日照资料_一体化
 * 端口 ：4012
 * Created by xzh on 2017/11/01.
 */
public class dpc_surf_awss {

    static Ioc ioc;

    public static void main(String[] args) throws SQLException, InterruptedException {
        if (args == null || args.length != 2) {
            System.out.println("args is null,args[0]=<port>,args[1]=<properties> ");
            System.exit(1);
        }
        try {
            ConfigurationManager.loadPros("db2.properties", "dpc_global_config.properties", args[1]);
        } catch (Exception e) {
            System.out.println("properties error!" + e);
            System.exit(1);
        }

        System.setProperty("process_log", ConfigurationManager.getString("process_log")
                .replaceAll("\\{name}","awss")
                .replaceAll("\\{port}", args[0]));
        System.setProperty("message_log", ConfigurationManager.getString("message_log")
                .replaceAll("\\{name}", "awss")
                .replaceAll("\\{port}", args[0]));

        LogUtil.info("loggerInfo", "日照开始运行：" + new Date());

        System.out.println(ConfigurationManager.getString("RabbitMQ.host"));
        System.out.println("配置文件：" + args[1]);
        dpc_surf_awss dayLightMainThread = new dpc_surf_awss();

        dayLightMainThread.run();
    }


    public void run() {
        int count = ConfigurationManager.getInteger("MessageThreadCount");
        String cts_code = ConfigurationManager.getString("cts_code");
        String dataSource = ConfigurationManager.getString("dataBaseType");
        ExecutorService executor = Executors.newFixedThreadPool(count);

        if  ("1".equals(ConfigurationManager.getString("fileloop"))) {
            executor.execute(new FilePolling(ConfigurationManager.getString("src"), cts_code + ":"));
        }

        for (int i = 1; i < count; i++) {
            switch (dataSource) {
                case "2":
                    executor.execute(new DayLightOTSSubThread());
                    break;
                case "1":
                    executor.execute(new DayLightMysqlSubThread());
                    break;
                case "0":
                    executor.execute(new DayLightMysqlSubThread());
                    break;
                default:
                    LogUtil.error("loggerInfo", "缓冲库类型选择失败");
                    return;
            }
        }
        executor.shutdown();

    }
}

