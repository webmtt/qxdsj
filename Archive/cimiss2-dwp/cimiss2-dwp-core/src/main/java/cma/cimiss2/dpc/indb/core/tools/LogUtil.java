package cma.cimiss2.dpc.indb.core.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: log工具
 * @Aouthor: xzh
 * @create: 2018-06-05 15:56
 */
public class LogUtil {
    /**
     * @param logtype
     * @param content
     * @return
     */
    public static Logger info(String logtype, String content) {
        LoggerFactory.getLogger(logtype).info(content);
        return null;
    }

    /**
     *
     * @param logtype
     * @param content
     * @return
     */
    public static Logger error(String logtype, String content) {
        LoggerFactory.getLogger(logtype).error(content);
        return null;
    }

    /**
     *
     * @param logtype
     * @param content
     * @return
     */
    public static Logger debug(String logtype, String content) {
        LoggerFactory.getLogger(logtype).debug(content);
        return null;
    }
}
