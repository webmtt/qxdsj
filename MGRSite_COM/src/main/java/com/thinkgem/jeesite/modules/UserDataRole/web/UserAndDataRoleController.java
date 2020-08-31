package com.thinkgem.jeesite.modules.UserDataRole.web;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.UserDataRole.entity.Datarole;
import com.thinkgem.jeesite.modules.UserDataRole.entity.EntityUserDatarole;
import com.thinkgem.jeesite.modules.UserDataRole.service.UserAndDataRoleService;
import com.thinkgem.jeesite.modules.Users.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 用户和角色联合管理-Controller
 *
 * @author RainingTime
 * @version 1.0 2018年7月5日
 */
@Controller
@RequestMapping(value = "userAndDataRole")
public class UserAndDataRoleController extends BaseController {

  @Autowired private UserAndDataRoleService udrService;

  @RequestMapping(value = "/list")
  public String UserAndDataRoleList(
      HttpServletRequest request, HttpServletResponse response, Model model) {
    String roleCodeOrName = (String) request.getParameter("roleCodeOrName");
    String userCodeOrName = (String) request.getParameter("userCodeOrName");
    String pageNo = (String) request.getParameter("pageNo");
    String pageSize = (String) request.getParameter("pageSize");
    if (pageNo == null || "".equals(pageNo) || pageSize == null || "".equals(pageSize)) {
      pageNo = "1";
      pageSize = "20";
    }
    Page<EntityUserDatarole> page =
        udrService.getEntityUserDataroleList(roleCodeOrName, userCodeOrName, pageNo, pageSize);
    model.addAttribute("page", page);
    model.addAttribute("roleCodeOrName", roleCodeOrName);
    model.addAttribute("userCodeOrName", userCodeOrName);
    return "modules/userdatarole/userdataroleList";
  }

  @RequestMapping(value = "/addUserAndDataRole")
  public String AddUserAndDataRole(
      HttpServletRequest request, HttpServletResponse response, Model model) {
    List<UserInfo> userList = udrService.getUserInfoList(null);
    List<Datarole> dataroleList = udrService.getDataroleList();
    model.addAttribute("userList", userList);
    model.addAttribute("dataroleList", dataroleList);
    return "modules/userdatarole/userdataroleForm";
  }

  @RequestMapping(value = "/delete")
  public String delete(HttpServletRequest request, HttpServletResponse response) {
    String id = request.getParameter("id");
    udrService.deleteUserDataroleById(id);
    return "redirect:" + "/userAndDataRole/list";
  }

  @RequestMapping(value = "/save")
  public String save(HttpServletRequest request, HttpServletResponse response, Model model) {
    String userId = request.getParameter("userId");
    String dataroleId = request.getParameter("dataroleId");
    boolean isOK = udrService.save(userId, dataroleId);
    if (isOK) {
      return "redirect:" + "/userAndDataRole/list";
    } else {
      model.addAttribute("message", "该角色用户已被绑定！");
      return "modules/userdatarole/userdataroleForm";
    }
  }

  @RequestMapping(value = "/searchUser")
  public void searchUser(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String userCodeOrName = (String) request.getParameter("userCodeOrName");
    List<UserInfo> userList = udrService.getUserInfoList(userCodeOrName);
    String jsonString = JsonMapper.toJsonString(userList);
    System.out.println(jsonString);
    response.getWriter().print(jsonString);
  }
}
