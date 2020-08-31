/*
 * @(#)UserInfoService.java 2016年3月16日
 *
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.Users.service;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.CacheMapUtil;
import com.thinkgem.jeesite.common.utils.ConPostUtils;
import com.thinkgem.jeesite.common.utils.Mail;
import com.thinkgem.jeesite.modules.Users.dao.OrgInfoDAO;
import com.thinkgem.jeesite.modules.Users.dao.UserInfoDAO;
import com.thinkgem.jeesite.modules.Users.entity.UserInfo;
import com.thinkgem.jeesite.modules.data.dao.DataSourceDefDao;
import com.thinkgem.jeesite.modules.data.entity.DataSourceDef;
import com.thinkgem.jeesite.modules.sys.dao.ComparasDao;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年3月16日
 */
@Service
public class UserInfoService {
  @Autowired private UserInfoDAO uiDao;
  @Autowired private OrgInfoDAO oiDao;
  @Autowired private ComparasDao cDao;
  @Autowired private OrgInfoService orgInfoService;
  @Autowired private DataSourceDefDao dataSourceDefDao;
  @Autowired
  private Environment environment;
  public Page<UserInfo> getUserList(
      String isActive,
      String userType,
      String pageNo,
      String pageSize,
      String name,
      String orderBy,
      String regSource) {
    Page<UserInfo> page=null;
    try {
      int No = Integer.parseInt(pageNo);
      int Size = Integer.parseInt(pageSize);
      List<UserInfo> info = new ArrayList<UserInfo>();
      page = uiDao.getUserList(isActive, userType, No, Size, name, orderBy, regSource);
      if (page.getList() != null && page.getList().size() > 0) {
        info = page.getList();
        for (UserInfo u : info) {
          if (u.getOrgID() != null && !"".equals(u.getOrgID())) {
            u.setOrgID(oiDao.getOrgByCode(u.getOrgID()));
          }
        }
      }
      page.setList(info);
    }catch (Exception e){
      e.printStackTrace();
    }
    return page;
  }

  public UserInfo getUserById(String id) {

    UserInfo ui = uiDao.getUserById(id);
    return ui;
  }

  public UserInfo getUserByIdId(String id) {
    UserInfo ui = uiDao.getUserById(id);
    String orgName = "";
    if (ui != null) {
      orgName = oiDao.getOrgByCodeId(ui.getOrgID(), ui.getUserRankID(), 0);
      //			ui.setOrgID(oiDao.getOrgByCode(ui.getOrgID()));
      ui.setOrgID(orgName);
    }
    return ui;
  }

  public UserInfo getUserById2(String id) {
    UserInfo ui = uiDao.getUserById(id);
    return ui;
  }
  //
  //  public UserInfo getUserByIdForRole(String id) {
  //    UserInfo ui = uiDao.getUserByUserName(id);
  //    return ui;
  //  }
  //
  public List<Map> getUserListByTime(String stime, String etime) {
    List<UserInfo> list = uiDao.getUserListByTime(stime, etime);
    // Map<String,String> orgmap=orgInfoService.getMapList();
    Map<String, String> orgmap = new HashMap<String, String>();
    orgmap = (Map<String, String>) CacheMapUtil.getCache("orgmap", null);
    if (orgmap == null) {
      orgmap = orgInfoService.getMapList();
      CacheMapUtil.putCache("orgmap", orgmap);
    }
    List<Map> alllist = new ArrayList<Map>();
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    for (int i = 0; i < list.size(); i++) {
      Map map = new HashMap();
      map.put("userName", list.get(i).getUserName());
      map.put("chName", list.get(i).getChName());
      map.put("password", list.get(i).getPassword());
      map.put("orgID", list.get(i).getOrgID());
      map.put("orgName", orgmap.get(list.get(i).getOrgID()));
      map.put("delFlag", list.get(i).getDelFlag());
      if (list.get(i).getUpdated() != null) {
        map.put("updated", sdf2.format(list.get(i).getUpdated()));
      }
      alllist.add(map);
    }
    return alllist;
  }

  public boolean sendEmailForUser(UserInfo userInfo,String content) {
    int isActive = userInfo.getIsActive();
    StringBuffer sb = new StringBuffer();
     String emailHost=Global.getConfig("email.host");
    String emailIp=Global.getConfig("email.ip");
    String password=Global.getConfig("email.password");
    String port=Global.getConfig("email.port");
    String url=Global.getConfig("email.url");
    // 1:审核通过，0:待审核 2未通过
    if (isActive == 2) {
      sb.append(userInfo.getChName() + ":<br/>");
      sb.append("<p>您申请国家气象业务内网未通过审核。原因如下：<br/>");
      sb.append("<p style='color:red;'>"+content+"</p>");
    } else if (isActive == 1) {
      sb.append(userInfo.getChName() + ":<br/>");
      sb.append("<p>您好,您的账户：" + userInfo.getUserName() + "，已通过审核！<br/>" +
              "请点击链接：<url>"+url+"</url>");
    }
     return Mail.sendMail(userInfo.getEmailName(), "审核结果", sb.toString(),emailHost,emailIp,password,Integer.parseInt(port));

//    DataSourceDef aSourceDef = dataSourceDefDao.findDataSourceDefByDsaccesscode("MAIL-REST-TXT");
//    String url1 = aSourceDef.getAccessurl();
//    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
//    formparams.add(new BasicNameValuePair("emailId", "11"));
//    formparams.add(new BasicNameValuePair("recipients", userInfo.getUserName()));
//    formparams.add(new BasicNameValuePair("subject", "审核结果"));
//    formparams.add(new BasicNameValuePair("html", sb.toString()));
//    String result = ConPostUtils.readContentFromHttpGet(url1, formparams);
//    if (result.indexOf("\"returnMsg\":\"success\"") > 0) {
//      return true;
//    } else {
//      return false;
//    }
  }

  @Transactional
  public void saveUser(UserInfo userInfo) {
    uiDao.updateUser(userInfo);
  }

  @Transactional
  public void delUserById(String id) {
    uiDao.delUserById(id);
  }

  public UserInfo getUser(String id) {
    return uiDao.getUserById(id);
  }

  @Transactional
  public void saveUserInfo(UserInfo userInfo) {
    uiDao.save(userInfo);
  }
  //
  //  public UserInfo getUserInfoByName(String userName) {
  //    return uiDao.getUserInfoByName(userName);
  //  }
  //
  //  public UserInfo getUserInfoByEmail(String userName) {
  //    return uiDao.getUserInfoByEmail(userName);
  //  }
  //
  public Boolean getUserInfoEmail(String userName) {
    UserInfo user = uiDao.getUserInfoByEmail(userName);
    if (user == null) {
      return true;
    } else {
      return false;
    }
  }
  //
  //  public Boolean getUserInfoPhone(String userName, String id) {
  //    UserInfo user = uiDao.getUserInfoByPhone(userName);
  //    if (user == null) {
  //      return true;
  //    } else {
  //      if (user.getiD().equals(id)) {
  //        return true;
  //      } else {
  //        return false;
  //      }
  //    }
  //  }

  /**
   * 检查手机号是已否存在
   *
   * @param mobile
   * @return
   */
  public Boolean getUserInfoByMobile(String mobile, String id) {
    UserInfo user = uiDao.getUserInfoByMobile(mobile);
    System.out.println(user);
    if (user == null) {
      return false;
    } else {
      if (id != null && user.getiD().equals(id)) {
        return false;
      } else {
        return true;
      }
    }
  }

    public List<UserInfo> getUserByDataRoleId(String dataroleid) {
      return uiDao.getUserByDataRoleId(dataroleid);
    }

    public List<UserInfo> getUsers() {
      return uiDao.getUsers();
    }

  public void updateUrl(String username,String url) {
    uiDao.updateUrl(username,url);
  }

    public UserInfo selectUrl(String replace) {
      return uiDao.selectUrl(replace);
    }

  public void deleteUserProduct(String id) {
    uiDao.deleteUserProduct(id);
  }


  /*public void updateIsChecked(String username) {
    uiDao.updateIsChecked(username);
  }

  public UserInfo getName(String username) {
    return uiDao.getName(username);
  }*/
}
