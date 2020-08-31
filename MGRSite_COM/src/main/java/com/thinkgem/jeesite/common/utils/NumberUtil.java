package com.thinkgem.jeesite.common.utils;

import java.util.regex.Pattern;

/**
 * 数字处理工具类
 *
 * @author yang.kq
 * @version 1.0
 * @date 2019/12/25 15:24
 */
public class NumberUtil {
  /**
   * 判断是否是数字
   *
   * @param str
   * @return
   */
  public static boolean isNumeric(String str) {
    Pattern pattern = Pattern.compile("[0-9]*");
    return pattern.matcher(str).matches();
  }
}
