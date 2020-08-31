package com.rizhi.test;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Component
@Order(value=2)
public class RzRead implements CommandLineRunner {

    private long lastTimeFileSize = 0;
    @Value("${rzfilepath}")
    private String rzfilepath;
    @Value("${patternPath}")
    private String patternPath;

    public void realtimeShowLog(File logFile) throws Exception {
        //指定文件可读可写
        final RandomAccessFile randomFile = new RandomAccessFile(logFile, "rw");
//        lastTimeFileSize = randomFile.length();//从日志的最后一行开始监控，否则从第一行开始
        grokutil grokutil = new grokutil();
        //启动一个线程每1秒钟读取新增的日志信息
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        exec.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                try {
                    //获得变化部分的
                    randomFile.seek(lastTimeFileSize);
                    String tmp = "";
                    while ((tmp = randomFile.readLine()) != null) {
                        grokutil.grok(tmp,patternPath);
                    }
                    lastTimeFileSize = randomFile.length();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, 1, TimeUnit.SECONDS);

    }

    @Override
    public void run(String... args) throws Exception {

        realtimeShowLog(new File(rzfilepath));//读入日志文件
    }
}

