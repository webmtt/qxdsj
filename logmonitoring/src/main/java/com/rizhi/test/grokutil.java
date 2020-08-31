package com.rizhi.test;

import io.krakens.grok.api.Grok;
import io.krakens.grok.api.GrokCompiler;
import io.krakens.grok.api.Match;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class grokutil {

    private static InputStream inputStream = null;
    public  void grok(String log , String GROK_PATTERN_PATH ) {
        try {
            //创建实例
            inputStream = new FileInputStream(new File(GROK_PATTERN_PATH));
            GrokCompiler grokCompiler = GrokCompiler.newInstance();
            grokCompiler.register(inputStream);
            //设置匹配式
            Grok grok1 = grokCompiler.compile("%{Exception:exception}");
            String log1 = log;
            Match gm1 = grok1.match(log1);
            //执行匹配
            Map<String, Object> capture1 = gm1.capture();
            if (capture1.size()!= 0){
                System.out.println(log1);//如果匹配到不为空，就打印匹配到的那行日志
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("读取pattern文件失败");
                }
            }
        }
    }
}
