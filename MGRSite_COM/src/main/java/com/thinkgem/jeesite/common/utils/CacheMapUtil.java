package com.thinkgem.jeesite.common.utils;

import com.danga.MemCached.MemCachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class CacheMapUtil {
  static final Logger log = LoggerFactory.getLogger(CacheMapUtil.class);
  private static MemCachedClient memcachedClient = MemcachedUtil.getMemcachedClient();
  // 设置放入memcached缓存key的前缀
  private static final String PRO_NAME = "msgsite_";

  public static Object getCache(String key, Object defaultValue) {
    Object obj = memcachedClient.get(PRO_NAME + key);
    return obj == null ? defaultValue : obj;
  }

  public static void putCache(String key, Object value) {
    log.info("放入无过期缓存 key:【" + PRO_NAME + key + "】value:【" + value + "】");
    memcachedClient.set(PRO_NAME + key, value);
  }

  /**
   * 放入缓存key-value 过期时间expireSeconds分钟
   *
   * @param key 关键字
   * @param value 值
   * @param expireSeconds 分钟
   */
  public static void putCache(String key, Object value, int expireSeconds) {
    log.info(
        "放入缓存过期时间：【" + expireSeconds * 60 + "s】 key:【" + PRO_NAME + key + "】value:【" + value + "】");
    memcachedClient.set(PRO_NAME + key, value, new Date(expireSeconds * 1000 * 60));
  }

  public static void removeCache(String key) {
    log.info("删除缓存key:【" + PRO_NAME + key + "】");
    memcachedClient.delete(PRO_NAME + key);
  }

  public static void removeCache2(String name, String key) {
    memcachedClient.delete(name + key);
  }
  /** 清空所有缓存 */
  public static void clearCache() {
    log.info("清空缓存");
    memcachedClient.flushAll();
  }
}
