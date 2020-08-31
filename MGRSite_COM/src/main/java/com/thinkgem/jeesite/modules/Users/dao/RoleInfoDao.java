package com.thinkgem.jeesite.modules.Users.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.Users.entity.RoleInfo;
import com.thinkgem.jeesite.modules.Users.entity.UserRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleInfoDao extends BaseDao<RoleInfo> {
  public List<RoleInfo> getRoleInfoByRoleId(String id) {
    String sql = "from RoleInfo where id=:p1 and del_flag='0'";
    return this.find(sql, new Parameter(id));
  }

  public List<UserRole> getUserRoleInfoByUserId(String id) {
    String sql = "from UserRole where userId=:p1";
    return this.find(sql, new Parameter(id));
  }
}
