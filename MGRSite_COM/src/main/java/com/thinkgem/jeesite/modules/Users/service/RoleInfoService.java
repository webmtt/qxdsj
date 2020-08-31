package com.thinkgem.jeesite.modules.Users.service;

import com.thinkgem.jeesite.modules.Users.dao.RoleInfoDao;
import com.thinkgem.jeesite.modules.Users.entity.RoleInfo;
import com.thinkgem.jeesite.modules.Users.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleInfoService {
  @Autowired private RoleInfoDao roleInfoDao;

  public RoleInfo getRoleInfoByRoleId(String id) {
    RoleInfo roleInfo = null;
    List<RoleInfo> list = roleInfoDao.getRoleInfoByRoleId(id);
    if (list.size() > 0) {
      roleInfo = list.get(0);
    }
    return roleInfo;
  }

  public UserRole getUserRoleInfoByUserId(String id) {
    UserRole userRole = null;
    List<UserRole> list = roleInfoDao.getUserRoleInfoByUserId(id);
    if (list.size() > 0) {
      userRole = list.get(0);
    }
    return userRole;
  }
}
