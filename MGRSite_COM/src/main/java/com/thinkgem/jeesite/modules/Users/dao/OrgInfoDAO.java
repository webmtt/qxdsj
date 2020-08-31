/*
 * @(#)OrgInfoDAO.java 2016年3月25日
 *
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.Users.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.Users.entity.OrgInfo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年3月25日
 */
@Repository
public class OrgInfoDAO extends BaseDao<OrgInfo> {
  List<String> names = new ArrayList<String>();

  public List<OrgInfo> getOrgInfo() {
    String sql = "from OrgInfo";
    return this.find(sql);
  }

  public String getOrgByCode(String orgID) {
    String sql = "from OrgInfo where id=:p1";
    List<OrgInfo> list = this.find(sql, new Parameter(orgID));
    OrgInfo oi;
    if (list != null && list.size() > 0) {
      oi = list.get(0);
      if (oi != null) {
        return oi.getName();
      }
    }
    return "";
  }

  public List<OrgInfo> getOrgListByPid(String pid) {
    String sql = "from OrgInfo where parentId=:p1";
    if ("001".equals(pid)) {
      sql += " and  (code  like '1%' or code like '0%')";
    }
    return this.find(sql, new Parameter(pid));
  }

  public List<OrgInfo> getOrgListById(String id) {
    String sql = "from OrgInfo where id=:p1";
    return this.find(sql, new Parameter(id));
  }

  public List<OrgInfo> getOrgListByProvincePid(String pid) {
    String sql = "from OrgInfo where parentId=:p1 and code not like '1%' and code not like '0%'";
    return this.find(sql, new Parameter(pid));
  }

  public String getOrgByCodeString(String orgID, int userRankID, int i) {
    List<OrgInfo> list = getOrgList(orgID);
    if (list.size() > 0) {
      if (i == 0) {
        names.clear();
      }
      names.add(list.get(0).getName());
      i++;
      getOrgByCodeString(list.get(0).getParentId(), userRankID, i);
    } else {
      return listToString(userRankID, names);
    }
    return listToString(userRankID, names);
  }
public OrgInfo getRootOrgByParentCode(String orgID, int userRankID, int i){
  List<OrgInfo> list = getOrgList(orgID);
  OrgInfo rootOrg=null;
  if (list.size() > 0) {
    OrgInfo oi=list.get(0);
    if("0".equals(oi.getParentId())){
      return oi;
    }else{
      i++;
      rootOrg=getRootOrgByParentCode(oi.getParentId(), userRankID, i);
    }
  }
    return rootOrg;

}
  public String getOrgByCodeId(String orgID, int userRankID, int i) {
    List<OrgInfo> list = getOrgList(orgID);
    if (list.size() > 0) {
      if (i == 0) {
        names.clear();
      }
      names.add(list.get(0).getId());
      i++;
      getOrgByCodeId(list.get(0).getParentId(), userRankID, i);
    } else {
      return listToString(userRankID, names);
    }
    return listToString(userRankID, names);
  }

  public List<OrgInfo> getOrgList(String id) {
    String sql = "from OrgInfo where id=:p1";
    return this.find(sql, new Parameter(id));
  }

  private String listToString(int userRankId, List<String> names) {
    String name = "";
    int size = names.size();
    if (userRankId == 1) {
      size -= 1;
    }
    for (int i = size - 1; i >= 0; i--) {
      name += "-" + names.get(i);
    }
    if (!"".equals(name)) {
      name = name.substring(1);
    }
    return name;
  }
}
