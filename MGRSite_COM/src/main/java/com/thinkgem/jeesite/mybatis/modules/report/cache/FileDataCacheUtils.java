package com.thinkgem.jeesite.mybatis.modules.report.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yang.kq
 * @version 1.0
 * @date 2019/12/12 10:16
 */
public class FileDataCacheUtils {
  /** 存储站点信息 key:区站号 value：区站名称 */
  public static Map<Long, String> stationCache = new ConcurrentHashMap<Long, String>();
  /** 存储项目信息 key:区站号 value：区站名称 */
  public static Map<String, String> projectCache = new ConcurrentHashMap<String, String>();
}
