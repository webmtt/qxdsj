/*
 * @(#)OperLogController.java 2016年3月16日
 *
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.Users.web;

import com.thinkgem.jeesite.common.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年3月16日
 */
@Controller
@RequestMapping(value = "log")
public class OperLogController extends BaseController {

  /** 根据条件获得日志列表 */
  private String logList(
      @RequestParam Map<String, Object> paramMap,
      HttpServletRequest request,
      HttpServletResponse response) {
    return null;
  }
}
