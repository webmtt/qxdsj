package com.thinkgem.jeesite.modules.Users.web;

import com.thinkgem.jeesite.common.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "user")
public class UserRoleController extends BaseController {
  //  @Autowired private UserInfoService uiService;
  //  @Autowired private CardInfoService ciService;
  //  @Autowired private OperLogService olService;
  //  @Autowired private UserExamInfoService ueService;
  //  @Autowired private DictService dcService;
  //  @Autowired private OrgInfoService oiService;
  //  @Autowired private RoleInfoService roleInfoService;
  //  /** 根据id获得用户信息，并在模态框中显示 */
  //  @RequestMapping("/getUserRoleById")
  //  public void roleInfo(
  //      HttpServletRequest request,
  //      HttpServletResponse response,
  //      Model model,
  //      RedirectAttributes redirectAttributes) {
  //    String id = request.getParameter("userId");
  //    UserInfo user = uiService.getUserByIdForRole(id);
  //    Map map = new HashMap();
  //    if (user != null) {
  //      map.put("userName", user.getUserName());
  //      map.put("chName", user.getChName());
  //      String orgId = user.getOrgID();
  //      System.out.println("orgId=" + orgId);
  //      UserRole ur = roleInfoService.getUserRoleInfoByUserId(id);
  //      RoleInfo role;
  //      if (ur != null) {
  //        role = roleInfoService.getRoleInfoByRoleId(ur.getRoleID());
  //        if (role != null) {
  //          Map roleMap = new HashMap();
  //          roleMap.put("id", role.getId());
  //          roleMap.put("name", role.getName());
  //          map.put("roleinfo", roleMap);
  //        } else {
  //          map.put("roleinfo", role);
  //        }
  //      } else {
  //        map.put("roleinfo", ur);
  //      }
  //
  //      OrgInfo orgInfo = oiService.getOrgListById(orgId);
  //      if (orgInfo != null) {
  //        Map orgInfoMap = new HashMap();
  //        orgInfoMap.put("code", orgInfo.getCode());
  //        orgInfoMap.put("name", orgInfo.getName());
  //        map.put("orginfo", orgInfoMap);
  //      } else {
  //        map.put("orginfo", orgInfo);
  //      }
  //    } else {
  //      map.put("returnCode", "-1");
  //    }
  //    String json = JsonMapper.toJsonString(map);
  //    renderText(json, response);
  //  }
}
