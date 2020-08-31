/*
 * @(#)UserInfoController.java 2016年3月16日
 *
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.Users.web;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.CookieUtils;
import com.thinkgem.jeesite.common.utils.MD5Util;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.UserDataRole.entity.Datarole;
import com.thinkgem.jeesite.modules.UserDataRole.service.DataRoleService;
import com.thinkgem.jeesite.modules.UserDataRole.service.UserAndDataRoleService;
import com.thinkgem.jeesite.modules.Users.entity.*;
import com.thinkgem.jeesite.modules.Users.service.*;
import com.thinkgem.jeesite.modules.products.entity.Products;
import com.thinkgem.jeesite.modules.products.service.ProductsService;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.DictService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.mybatis.modules.musicuser.entity.MusicInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年3月16日
 */
@Controller
@RequestMapping(value = "user")
public class UserInfoController extends BaseController {
  @Autowired private UserInfoService uiService;
  @Autowired private CardInfoService ciService;
  @Autowired private OperLogService olService;
  @Autowired private UserExamInfoService ueService;
  @Autowired private DictService dcService;
  @Autowired private DataRoleService dataRoleService;
  @Autowired private OrgInfoService oiService;
  //  @Autowired private FtpUserInfoService ftpUserInfoService;
  //  @Autowired private ComparasService comparasService;
  @Autowired private ServiceLevelService serviceLevelService;
  @Autowired private UserAndDataRoleService udrService;
  @Autowired private ProductsService productsService;

  /** 根据条件获取用户列表 */
  @RequestMapping("/userList")
  public String userList(
      @RequestParam Map<String, Object> paramMap,
      HttpServletRequest request,
      HttpServletResponse response,
      Model model) {
    // 根据查询的条件有激活状态，用户类型，工作单位，用户级别
    String isActive = (String) paramMap.get("IsActive");
    String userType = (String) paramMap.get("userType");
    String searchName = (String) paramMap.get("searchName");
    String userRankID = request.getParameter("userRankID");
    String orderBy = request.getParameter("orderBy");
    if(orderBy==null||"".equals(orderBy)){
      orderBy="updated DESC";
    }
    String pageNo = (String) paramMap.get("pageNo");
    String pageSize = (String) paramMap.get("pageSize");
//    String orderByNullsLast =
//        orderBy != null ? (orderBy.contains("DESC") ? orderBy + " NULLS LAST" : orderBy) : null;
    if (pageNo == null || "".equals(pageNo) || pageSize == null || "".equals(pageSize)) {
      pageNo = "1";
      pageSize = "20";
    }
//    if (searchName != null && !"".equals(searchName)) {
//      try {
//        searchName = new String(searchName.getBytes("ISO-8859-1"), "utf-8");
//      } catch (UnsupportedEncodingException e) {
//        e.printStackTrace();
//      }
//    }
    //		Page<UserInfo> ulist=uiService.getUserList(isActive, userType, pageNo, pageSize);
    Page<UserInfo> ulist =
        uiService.getUserList(
            isActive, userType, pageNo, pageSize, searchName, orderBy, userRankID);
    if (isActive == null || "".equals(isActive)) {
      isActive = "5";
    }
    User currentUser = UserUtils.getUser();
    setCookie(response, isActive, userType, searchName, userRankID, orderBy); // 列表页面筛选条件存入Cookie
    // System.out.println("排序："+ orderBy);
    model.addAttribute("IsActive", isActive);
    model.addAttribute("userType", userType);
    model.addAttribute("searchName", searchName);
    model.addAttribute("userRankID", userRankID);
    model.addAttribute("pageNo", pageNo);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("page", ulist);
    model.addAttribute("orderBy", orderBy);
    String t="";
    List<Role> roleList=currentUser.getRoleList();
    for(int i=0;i<roleList.size();i++){
      Role role=roleList.get(i);
      if("系统管理员".equals(role.getName())){
        t="系统管理员";
      }
    }
    model.addAttribute("sysuser", t);
    return "modules/Users/UserList";
  }

  /** 重置密码 */
  @RequestMapping("/resetUserPassword")
  public String resetUserPassword(
          HttpServletRequest request,
          HttpServletResponse response, RedirectAttributes redirectAttribute) {
    String id = request.getParameter("id");
    UserInfo user = uiService.getUserById(id);
    user.setPassword("e10adc3949ba59abbe56e057f20f883e");
    uiService.saveUser(user);
    addMessage(redirectAttribute, "用户"+user.getUserName()+"已重置密码为123456");
    String params = getSearchParams(request);
    return "redirect:" + "/user/userList?" + params;
  }
  /** 根据id获得用户信息，并在模态框中显示 */
  @RequestMapping("/getUserById")
  public String userInfo(
      HttpServletRequest request,
      HttpServletResponse response,
      Model model,
      RedirectAttributes redirectAttributes) {
    String id = request.getParameter("id");
    UserInfo user = uiService.getUserById(id);
      List<UserExamInfo> list = ueService.getExamInfo(user.getUserName());
      if (list.size() > 0) {
        model.addAttribute("reason", list.get(0).getRemarks());
      }
    List<Datarole> listRole = dataRoleService.getdataroleByUserId(id);
      OrgInfo orgInfo=oiService.getOrgListById(user.getOrgID());
    user.setOrgID(orgInfo.getName());
    String datarole = "";
    if(null!=listRole) {
      if (listRole.size() > 0) {
        for (int i = 0, size = listRole.size(); i < size; i++) {
          if (i == size - 1) {
            datarole += listRole.get(i).getDataroleName();
          } else {
            datarole += listRole.get(i).getDataroleName() + ",";
          }
        }
      }
    }

    String position = "";
    String jobtitle = "";
    String agerange = "";
    String phonemodel = "";
    if (user != null) {
      try {
        position = dcService.findDictByTypeAndValue(user.getPosition(), "user_position").getLabel();
        jobtitle = dcService.findDictByTypeAndValue(user.getJobTitle(), "user_jobtitle").getLabel();
        agerange =
            dcService.findDictByTypeAndValue(user.getAgeRange() + "", "user_agerange").getLabel();
        phonemodel =
            dcService
                .findDictByTypeAndValue(user.getPhoneModel() + "", "user_phonemodel")
                .getLabel();
      } catch (Exception e) {
        // 老用户未填写，异常捕获处理
      }
      model.addAttribute("position", position);
      model.addAttribute("jobtitle", jobtitle);
      model.addAttribute("agerange", agerange);
      model.addAttribute("phonemodel", phonemodel);
      model.addAttribute("datarole", datarole);
      model.addAttribute("UserInfo", user);
      return "modules/Users/UserForm";
    } else {
      addMessage(redirectAttributes, "该用户信息无效，请重新选择");
      String params = getSearchParams(request);
      return "redirect:" + "/user/userList?" + params;
    }
  }
  /**
   * 返回用于审核的用户信息
   *
   * @param request
   * @param response
   * @param model
   * @param redirectAttributes
   * @return
   */
  @RequestMapping("/getUser")
  public String getUserInfo(
      HttpServletRequest request,
      HttpServletResponse response,
      Model model,
      RedirectAttributes redirectAttributes) {
    String id = request.getParameter("id");
    UserInfo user = uiService.getUserById(id);
    MusicInfo musicInfo=oiService.getMusicByOrgCode(user.getOrgID(), user.getUserRankID());
    String orgName ="";
    if (user != null) {
      orgName =oiService.getOrgByCodeString(user.getOrgID(), user.getUserRankID());
      //			ui.setOrgID(oiDao.getOrgByCode(ui.getOrgID()));
      user.setOrgID(orgName);
    }
    //    CardInfo ci = ciService.getCardById(id);
    String position = "";
    String jobtitle = "";
    String agerange = "";
    String phonemodel = "";
    List<Datarole> listRole = null;
    if (user != null) {
      try {
//        position = dcService.findDictByTypeAndValue(user.getPosition(), "user_position").getLabel();
//        jobtitle = dcService.findDictByTypeAndValue(user.getJobTitle(), "user_jobtitle").getLabel();
//        agerange =
//            dcService.findDictByTypeAndValue(user.getAgeRange() + "", "user_agerange").getLabel();
//        phonemodel =
//            dcService
//                .findDictByTypeAndValue(user.getPhoneModel() + "", "user_phonemodel")
//                .getLabel();
        listRole = dataRoleService.getdataroleByUserId(id);
      } catch (Exception e) {
        // 老用户未填写，异常捕获处理
      }
      model.addAttribute("position", position);
      model.addAttribute("jobtitle", jobtitle);
      model.addAttribute("agerange", agerange);
      model.addAttribute("phonemodel", phonemodel);
      model.addAttribute("UserInfo", user);
      model.addAttribute("listRole", listRole);
      model.addAttribute("musicInfo", musicInfo);
      //      model.addAttribute("CardInfo", ci);
      return "modules/Users/UserActive";
    } else {
      addMessage(redirectAttributes, "该用户信息无效，请重新选择");
      String params = getSearchParams(request);
      return "redirect:" + "/user/userList?" + params;
    }
  }
  /** 获取证件照图片 */
  @RequestMapping("/getCard")
  public void getCardImg(HttpServletRequest request, HttpServletResponse response) {
    String id = request.getParameter("id");
    CardInfo ci = ciService.getCardById(id);
    if (ci != null) {
      Blob blob = null;
      //      if ("1".equals(ci.getValidateType())) {
      blob = ci.getEmpCard();
      //      }
      //      else {
      //        blob = ci.getProtocalImg();
      //      }
      OutputStream os;
      InputStream is;
      byte[] b = new byte[1024];
      try {
        os = response.getOutputStream();
        is = blob.getBinaryStream();
        int i = 0;
        while ((i = is.read(b)) != -1) {
          os.write(b, 0, i);
        }
        os.close();
        is.close();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else {
      renderText("对不起，该用户没有上传证件照", response);
    }
  }
  /** 根据id删除用户 */
  @RequestMapping("/delUserById")
  public String delUserById(
      HttpServletRequest request,
      HttpServletResponse response,
      RedirectAttributes redirectAttributes) {
    String id = request.getParameter("id");
    UserInfo userInfo = uiService.getUserById2(id);
    uiService.delUserById(id);
    Calendar c = Calendar.getInstance();
    OperLog ol = new OperLog();
    ol.setId(UUID.randomUUID().toString());
    String user = UserUtils.getUser().getLoginIp();
    ol.setOperUser(user);
    ol.setCreated(c.getTime());
    ol.setCreatedBy(user);
    ol.setOperedUser(UserUtils.getUser().getLoginName());
    ol.setOperType("delUser");
    ol.setOperedUser(userInfo.getChName());
    olService.saveLog(ol);
    addMessage(redirectAttributes, "删除成功");
    String params = getSearchParams(request);
    return "redirect:" + "/user/userList?" + params;
  }

  /** 根据id取消用户授权URL */
  @RequestMapping("/deleteUserProduct")
  public String deleteUserProduct(
          HttpServletRequest request,
          RedirectAttributes redirectAttributes) {
    String id = request.getParameter("id");
    uiService.deleteUserProduct(id);
    UserInfo userInfo = uiService.getUserById2(id);
    String str = userInfo.getChName()+"("+userInfo.getUserName()+")";
    List<Products> list = productsService.findAllList();
    for (int i = 0; i < list.size(); i++) {
      String username = list.get(i).getUsername();
      String user = "";
      if (username!=null&&username!=""){
        String[] strings = username.split(",");
        if(strings.length==1&&str.equals(strings[0])){
          productsService.updateUserName("",list.get(i).getId());
        }else if(strings.length>1){
          for (int j = 0; j <strings.length ; j++) {
            if (!strings[j].equals(str)){
              user+=strings[j]+",";
            }
          }
          user=user.substring(0,user.length()-1);
          productsService.updateUserName(user,list.get(i).getId());
        }
      }
    }
    addMessage(redirectAttributes, "取消授权成功");
    String params = getSearchParams(request);
    return "redirect:" + "/user/userList?" + params;
  }
  /** 根据用户激活状态发送邮件 */
  @RequestMapping("/activeUser")
  public String saveUser(
      HttpServletRequest request,
      HttpServletResponse response,
      RedirectAttributes redirectAttributes) {
    // 保存成功重定向至列表界面
    String id = request.getParameter("id");
    String content = request.getParameter("content");
//    try {
//      content = new String(content.getBytes("ISO-8859-1"), "UTF-8");
//    } catch (UnsupportedEncodingException e) {
//      e.printStackTrace();
//    }
    String isActive = request.getParameter("isActive");
    String name = request.getParameter("name");
    String email = request.getParameter("email");
    String org = request.getParameter("org");
    String photo = request.getParameter("photo");
    String others = request.getParameter("others");
    String protocal = request.getParameter("protocal");
    String iscreate=request.getParameter("iscreate");
    String selectDataRole=request.getParameter("selectDataRole");
    UserInfo userInfo = uiService.getUser(id);
    if((!"".equals(selectDataRole))&&(!"null".equals(selectDataRole))&&null!=selectDataRole){//关联数据角色
      udrService.saveBatch(selectDataRole, userInfo.getiD());
    }
    userInfo.setIscreate(Integer.parseInt(iscreate));
    userInfo.setIsActive(Integer.parseInt(isActive));
    boolean flag=true;
//    boolean flag= uiService.sendEmailForUser(userInfo,content);
    Calendar c = Calendar.getInstance();
    String updatedBy = UserUtils.getUser().getLoginIp();
    if (flag) {
      userInfo.setUpdated(c.getTime());
      userInfo.setUpdatedBy(updatedBy);
      uiService.saveUser(userInfo);
      if ("2".equals(isActive)) {
        List<UserExamInfo> resultList = ueService.getExamInfo(userInfo.getUserName());
        if (resultList != null && resultList.size() > 0) {
          ueService.deleteExamInfo(userInfo.getUserName());
        }
        ueService.saveUserExam(
            userInfo.getUserName(), name, email, org, photo, protocal, "1", content);
      }
//      addMessage(redirectAttributes, "保存审核结果成功，发送邮件成功");
      addMessage(redirectAttributes, "保存审核结果成功");
    } else {
      userInfo.setIsActive(3);
//      addMessage(redirectAttributes, "发送邮件失败，未成功保存审核结果");
      addMessage(redirectAttributes, "保存审核结果失败");
    }
    OperLog ol = new OperLog();
    ol.setId(UUID.randomUUID().toString());
    String user = UserUtils.getUser().getLoginName();
    ol.setOperUser(user);
    ol.setCreated(c.getTime());
    ol.setCreatedBy(updatedBy);
    ol.setOperedUser(UserUtils.getUser().getLoginName());
    ol.setOperType("userExam");
    ol.setOperedUser(userInfo.getChName());
    olService.saveLog(ol);
    String params = getSearchParams(request);
    return "redirect:" + "/user/userList?" + params;
  }
  /**
   * 跳转至用户添加/修改界面
   *
   * @param userInfo
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping("/addUserPage")
  public String getAddPage(
      UserInfo userInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
    String id = request.getParameter("id");
    if (id != null && !"".equals(id)) {
      // userInfo=uiService.getUserById(id);
      userInfo = uiService.getUserById2(id);
      UserInfo userInfo2 = uiService.getUserByIdId(id);
      model.addAttribute("unitIds", userInfo2.getOrgID());
    } else {
      userInfo.setIsActive(1);
    }
    List<OrgInfo> oneList = oiService.getOrgListBypId("0");
    List<OrgInfo> poneList = oiService.getOrgListProivnceBypId("001");
    List<Dict> positionList = dcService.findListByTpye("user_position");
    List<Dict> JobList = dcService.findListByTpye("user_jobtitle");
    List<Dict> ageList = dcService.findListByTpye("user_agerange");
    List<Dict> phoneList = dcService.findListByTpye("user_phonemodel");
    oneList.addAll(poneList);
    List<ServiceLevel> serviceLevelList = serviceLevelService.getServiceLevel();
    model.addAttribute("oneList", oneList);
    model.addAttribute("poneList", poneList);
    model.addAttribute("userInfo", userInfo);
    model.addAttribute("positionList", positionList);
    model.addAttribute("JobList", JobList);
    model.addAttribute("ageList", ageList);
    model.addAttribute("phoneList", phoneList);
    model.addAttribute("serviceLevelList", serviceLevelList);
    return "modules/Users/userAddPage";
  }
  //  /**
  //   * 跳转至APP用户添加/修改界面
  //   *
  //   * @param userInfo
  //   * @param request
  //   * @param response
  //   * @param model
  //   * @return
  //   */
  //  @RequestMapping("/addUserAppPage")
  //  public String getAddAppPage(
  //      UserInfo userInfo, HttpServletRequest request, HttpServletResponse response, Model model)
  // {
  //    String id = request.getParameter("id");
  //    if (id != null && !"".equals(id)) {
  //      // userInfo=uiService.getUserById(id);
  //      userInfo = uiService.getUserById2(id);
  //      UserInfo userInfo2 = uiService.getUserByIdId(id);
  //      model.addAttribute("unitIds", userInfo2.getOrgID());
  //    }
  //    List<OrgInfo> poneList = oiService.getOrgListProivnceBypId("001");
  //    List<Dict> positionList = dcService.findListByTpye("user_position");
  //    List<Dict> JobList = dcService.findListByTpye("user_jobtitle");
  //    List<Dict> ageList = dcService.findListByTpye("user_agerange");
  //    List<Dict> phoneList = dcService.findListByTpye("user_phonemodel");
  //    //		oneList.addAll(poneList);
  //    List<ServiceLevel> serviceLevelList = serviceLevelService.getServiceLevel();
  //    model.addAttribute("poneList", poneList);
  //    model.addAttribute("positionList", positionList);
  //    model.addAttribute("JobList", JobList);
  //    model.addAttribute("ageList", ageList);
  //    model.addAttribute("phoneList", phoneList);
  //    model.addAttribute("userInfo", userInfo);
  //    model.addAttribute("serviceLevelList", serviceLevelList);
  //    return "modules/Users/userAddAppPage";
  //  }
  /**
   * 保存用户信息
   *
   * @param userInfo
   * @param newPassword
   * @param request
   * @param response
   * @param redirectAttribute
   * @return
   */
  @RequestMapping("/addUser")
  public String addUserInfo(
      UserInfo userInfo,
      String newPassword,
      HttpServletRequest request,
      HttpServletResponse response,
      RedirectAttributes redirectAttribute) {
    // System.out.println(userInfo);
    try {
      /*if(userInfo.getUserName() == null || "".equals(userInfo.getUserName())){
      	userInfo.setUserName(userInfo.getUserName2());
      }*/
      User user = UserUtils.getUser();
      FtpUserInfo ftpUserInfo = new FtpUserInfo();
      //		String ftpBaseUrl=comparasService.getComparasByKey("FtpBaseUrl").toString();
      Boolean flag = true;
      if (userInfo.getiD() == null || "".equals(userInfo.getiD())) {
        userInfo.setiD(UUID.randomUUID().toString());
        userInfo.setPassword(MD5Util.encode(newPassword));
        userInfo.setUrl("");
      } else {
        if (StringUtils.isNotBlank(newPassword)) {
          userInfo.setPassword(MD5Util.encode(newPassword));

          ftpUserInfo.setPassword(MD5Util.encode(newPassword));
          //            ftpUserInfo.setUser(userInfo.getUserName2());
          //					ftpUserInfo.setDir(ftpBaseUrl);
          //					ftpUserInfo.setUid(1000);
          //					ftpUserInfo.setGid(100);
          //          } /*else{
          ftpUserInfo.setPassword(MD5Util.encode(newPassword));
          //          ftpUserInfo.setUser(userInfo.getUserName2());
          //          ftpUserInfo.setDir(ftpBaseUrl);
          ftpUserInfo.setUid(1000);
          ftpUserInfo.setGid(100);
          //            }*/
        }
      }
      //      userInfo.setRegSource(0);
      userInfo.setCreated(new Date());
      userInfo.setCreatedBy(user.getLoginName());
      userInfo.setUpdated(new Date());
      userInfo.setUpdatedBy(user.getLoginName());
      //      userInfo.setUserRankID(2);
      userInfo.setDelFlag("0");
      //      userInfo.setUserType("1");

      if (userInfo.getAgeRange() == null) {
        userInfo.setAgeRange(0);
      }
      if (userInfo.getPhoneModel() == null) {
        userInfo.setPhoneModel(0);
      }
      // 默认国家级用户权限
      userInfo.setUserRankID(2);
      //邮箱默认为空
      userInfo.setEmailName("");
      //用户类型默认展示子系统
      userInfo.setUserType("1");
      String checkRole = request.getParameter("selectRole");
      udrService.saveBatch(checkRole, userInfo.getiD());
      uiService.saveUserInfo(userInfo);
      //      if (!flag) {
      //        ftpUserInfoService.saveFtpUserInfo(ftpUserInfo);
      //      }
      addMessage(redirectAttribute, "保存成功");
    } catch (Exception e) {
      addMessage(redirectAttribute, "保存失败，请重新添加");
      e.printStackTrace();
    }
    String params = getSearchParams(request);
    return "redirect:" + "/user/userList?" + params;
  }

  //  @RequestMapping("/addAppUser")
  //  public String addAppUserInfo(
  //      UserInfo userInfo,
  //      String newPassword,
  //      HttpServletRequest request,
  //      HttpServletResponse response,
  //      RedirectAttributes redirectAttribute) {
  //    // System.out.println(userInfo);
  //    String user_position = request.getParameter("user_position");
  //    String user_jobtitle = request.getParameter("user_jobtitle");
  //    String user_agerange = request.getParameter("user_agerange");
  //    String user_phonemodel = request.getParameter("user_phonemodel");
  //    String userName = request.getParameter("mobile");
  //    Boolean flag = true;
  //    flag = uiService.getUserInfoPhone(userName, userInfo.getiD());
  //    if (userInfo.getiD() == null || "".equals(userInfo.getiD())) {
  //      String id = UUID.randomUUID().toString();
  //      userInfo.setiD(id);
  //    }
  //    if (flag) {
  //      userInfo.setPosition(user_position);
  //      userInfo.setJobTitle(user_jobtitle);
  //      userInfo.setUserName(userName);
  //      userInfo.setPassword(EncryptUtil.entryptPassword("123"));
  //      userInfo.setAgeRange(Integer.valueOf(user_agerange));
  //
  //      // userInfo.setUserType(1);
  //
  //      userInfo.setPhoneModel(Integer.valueOf(user_phonemodel));
  //      userInfo.setCreated(new Date());
  //      userInfo.setUpdated(new Date());
  //      userInfo.setUserRankID(1);
  //      userInfo.setIsActive(1);
  //      userInfo.setDelFlag("0");
  //      userInfo.setUserType("1");
  //      userInfo.setRegSource(1);
  //      try {
  //        uiService.saveUserInfo(userInfo);
  //        addMessage(redirectAttribute, "保存成功");
  //      } catch (Exception e) {
  //        e.printStackTrace();
  //        addMessage(redirectAttribute, "保存失败");
  //      }
  //    } else {
  //      addMessage(redirectAttribute, "此手机用户已存在，请修改");
  //    }
  //    String params = getSearchParams(request);
  //    return "redirect:" + "/user/userList?" + params;
  //  }
  /**
   * 检查登录名或邮箱是否存在
   *
   * @param request
   * @param response
   */
  @RequestMapping("/checkUserName")
  public void checkUserNmae(HttpServletRequest request, HttpServletResponse response) {
    String userName = request.getParameter("userName");
    Boolean flag = true;
      flag = uiService.getUserInfoEmail(userName);

    if (flag) {
      renderText("1", response);
    } else {
      renderText("0", response);
    }
  }
  //  /**
  //   * 检查电话是否存在
  //   *
  //   * @param request
  //   * @param response
  //   */
  //  @RequestMapping("/checkUserTell")
  //  public void checkUserTell(
  //      String userName, String id, HttpServletRequest request, HttpServletResponse response) {
  //    Boolean flag = true;
  //    flag = uiService.getUserInfoPhone(userName, id);
  //    if (flag) {
  //      renderText("0", response);
  //    } else {
  //      renderText("1", response);
  //    }
  //  }

  /**
   * 检查手机号是已否存在（忽略当前用户自身）
   *
   * @param request
   * @param response
   */
  @RequestMapping("/checkMobile")
  public void checkMobile(
      String mobile, String id, HttpServletRequest request, HttpServletResponse response) {
    Boolean flag = true;
    flag = uiService.getUserInfoByMobile(mobile, id);
    if (flag) {
      renderText("0", response); // 手机号已存在，注册（修改）时不可用
    } else {
      renderText("1", response); // 手机号未存在，注册（修改）时可用
    }
  }

  /**
   * 列表页面筛选条件存入Cookie
   *
   * @author zhaojianye
   * @time 2018.12.10 cookie不需要生命周期（会话级别，浏览器关闭时自动清除cookie）
   */
  public void setCookie(
      HttpServletResponse response,
      String isActive,
      String userType,
      String searchName,
      String userRankID,
      String orderBy) {
    CookieUtils.setCookie(response, "IsActive", isActive == null ? "5" : isActive, false, 0);
    CookieUtils.setCookie(response, "userType", userType == null ? "" : userType, false, 0);
    CookieUtils.setCookie(response, "searchName", searchName == null ? "" : searchName, false, 0);
    CookieUtils.setCookie(response, "userRankID", userRankID == null ? "" : userRankID, false, 0);
    CookieUtils.setCookie(response, "orderBy", orderBy == null ? "" : orderBy, false, 0);
  }

  /**
   * 从Cookie取列表页面筛选条件
   *
   * @author zhaojianye
   * @time 2018.12.10
   */
  public String getSearchParams(HttpServletRequest request) {
    String params = "";
    String isActive = CookieUtils.getCookie(request, "IsActive");
    String userType = CookieUtils.getCookie(request, "userType");
    String searchName = CookieUtils.getCookie(request, "searchName");
    String userRankID = CookieUtils.getCookie(request, "userRankID");
    String orderBy = CookieUtils.getCookie(request, "orderBy");
    if (isActive != null && !"".equals(isActive)) {
      params += "IsActive=" + isActive + "&";
    }
    if (userType != null && !"".equals(userType)) {
      params += "userType=" + userType + "&";
    }
    if (searchName != null && !"".equals(searchName)) {
      params += "searchName=" + searchName + "&";
    }
    if (userRankID != null && !"".equals(userRankID)) {
      params += "userRankID=" + userRankID + "&";
    }
    if (orderBy != null && !"".equals(orderBy)) {
      params += "orderBy=" + orderBy + "&";
    }
    return params;
  }
}
