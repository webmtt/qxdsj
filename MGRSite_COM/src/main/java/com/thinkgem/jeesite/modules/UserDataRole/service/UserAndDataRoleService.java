package com.thinkgem.jeesite.modules.UserDataRole.service;

import com.alibaba.fastjson.JSONObject;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.UserDataRole.dao.DataroleDao;
import com.thinkgem.jeesite.modules.UserDataRole.dao.UserDataroleDao;
import com.thinkgem.jeesite.modules.UserDataRole.entity.Datarole;
import com.thinkgem.jeesite.modules.UserDataRole.entity.EntityUserDatarole;
import com.thinkgem.jeesite.modules.UserDataRole.entity.UserDatarole;
import com.thinkgem.jeesite.modules.Users.dao.UserInfoDAO;
import com.thinkgem.jeesite.modules.Users.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 用户和角色联合管理-Service
 *
 * @author RainingTime
 * @version 1.0 2018年7月5日
 */
@Service
public class UserAndDataRoleService {

  @Autowired private DataroleDao drDao;
  @Autowired private UserDataroleDao udrDao;
  @Autowired private UserInfoDAO uiDao;

  public Page<EntityUserDatarole> getEntityUserDataroleList(
      String roleCodeOrName, String userCodeOrName, String pageNo, String pageSize) {
    List<EntityUserDatarole> eudrList = new ArrayList<EntityUserDatarole>(); // 构建联合实体类列表eudrList
    int no = Integer.parseInt(pageNo);
    int size = Integer.parseInt(pageSize);
    Long count = udrDao.getcount(roleCodeOrName, userCodeOrName);
    List<Object> list =
        udrDao.getUserDataroleList(roleCodeOrName, userCodeOrName, no, size); // 查出所有的用户角色关系
    for (int i = (no - 1) * size; i < list.size(); i++) {
      EntityUserDatarole userDatarole = new EntityUserDatarole();
      Object[] object = (Object[]) list.get(i);
      String id = (String) object[0];
      String dataroleId = (String) object[1];
      String dataroleName = (String) object[2];
      String userName = (String) object[3];
      String chName = (String) object[4];
      userDatarole.setId(id);
      userDatarole.setDataroleId(dataroleId);
      userDatarole.setDataroleName(dataroleName);
      userDatarole.setUserName(userName);
      userDatarole.setChName(chName);
      eudrList.add(userDatarole);
    }
    return new Page<EntityUserDatarole>(no, size, count, eudrList);
  }

  @Transactional
  public boolean deleteUserDataroleById(String id) {
    try {
      UserDatarole udr = udrDao.getById(id);
      udrDao.deleteUserDataroleById(id);
      String userId = udr.getUserId();
      if (udrDao.findByUserId(userId) == null) {
        uiDao.updateUserIsSpecDataroleById(userId, "0");
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public List<UserInfo> getUserInfoList(String userCodeOrName) {
    return uiDao.getUserList(userCodeOrName);
  }

  public List<Datarole> getDataroleList() {
    return drDao.getDataroleList();
  }

  @Transactional
  public boolean save(String userId, String dataroleId) {
    UserDatarole udr = udrDao.findByDataroleIdAndUserId(userId, dataroleId);
    if (udr == null) {
      udr = new UserDatarole();
      udr.setId(UUID.randomUUID().toString());
      udr.setUserId(userId);
      udr.setDataroleId(dataroleId);
      udrDao.save(udr);
      uiDao.updateUserIsSpecDataroleById(userId, "1");
      return true;
    } else {
      return false;
    }
  }

  /**
   * 批量添加角色
   *
   * @param checkRole
   * @param userid
   */
  public void saveBatch(String checkRole, String userid) {
    List<String> list = new ArrayList<String>();
    list = JSONObject.parseArray(checkRole, String.class);
    udrDao.deleteUserDataroleByUserId(userid);
    UserDatarole udr = null;
    for (int i = 0, length = list.size(); i < length; i++) {
      udr = new UserDatarole();
      udr.setId(UUID.randomUUID().toString());
      udr.setUserId(userid);
      udr.setDataroleId(list.get(i));
      udrDao.insertValue(udr);
    }
    uiDao.updateUserIsSpecDataroleById(userid, "1");
  }
}
