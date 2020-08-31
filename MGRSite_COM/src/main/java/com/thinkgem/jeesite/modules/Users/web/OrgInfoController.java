/*
 * @(#)OrgInfoController.java 2016年6月30日
 *
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.Users.web;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.Users.entity.OrgInfo;
import com.thinkgem.jeesite.modules.Users.service.OrgInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年6月30日
 */
@Controller
@RequestMapping("/org")
public class OrgInfoController extends BaseController {
  @Autowired private OrgInfoService orgInfoService;

  @RequestMapping("/orgListByPid")
  public void getOrgInfoList(HttpServletRequest request, HttpServletResponse response) {
    String pid = request.getParameter("pid");
    List<OrgInfo> list = new ArrayList<OrgInfo>();
    if (pid != null && !"".equals(pid)) {
      list = orgInfoService.getOrgListBypId(pid);
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("list", list);
    renderText(JsonMapper.toJsonString(map), response);
  }
}
