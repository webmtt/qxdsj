package com.thinkgem.jeesite.common.utils;

import com.danga.MemCached.MemCachedClient;

/**
 * 单例模式创建MemcachedClient
 *
 * @author hanji
 */
public class MemcachedUtil {

  private static MemCachedClient memcachedClient = null;

  public static MemCachedClient getMemcachedClient() {
    try {
      synchronized (MemcachedUtil.class) {
        if (null == memcachedClient) {
          memcachedClient = SpringContextHolder.getBean("memCachedClient");
        }
      }
    } catch (Exception e) {
    }
    return memcachedClient;
  }
}
