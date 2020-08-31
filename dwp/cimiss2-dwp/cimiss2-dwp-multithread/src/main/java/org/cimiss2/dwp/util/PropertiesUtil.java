package org.cimiss2.dwp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 *@program: backup
 *@描述 读取配置文件类
 *@author  zzj
 *@创建时间  2019/4/8 14:25
 **/
public class PropertiesUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private static PropertiesUtil pu;

    public static PropertiesUtil getInstance() {

        if (pu == null) {
            pu = new PropertiesUtil();
        }
        return pu;
    }

    /**
     * @param path "resources/env.properties"
     * @return
     */
    public Properties getProperties(String path) {
        Properties p = null;

        try {
            p = new Properties();
           /* InputStream in = this.getClass().getClassLoader()
                    .getResourceAsStream(path);*/
           InputStream in=new FileInputStream(path);

            p.load(in);
        } catch (Exception e) {
            logger.error(e.getMessage(), ExceptionUtil.getException(e));
            throw new RuntimeException(e);
        }
        return p;
    }
}
