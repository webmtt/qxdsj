/*
 * @(#)OrgInfoService.java 2016年3月25日
 *
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.Users.service;

import com.thinkgem.jeesite.modules.Users.dao.OrgInfoDAO;
import com.thinkgem.jeesite.modules.Users.entity.OrgInfo;
import com.thinkgem.jeesite.mybatis.modules.musicuser.dao.MusicInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.thinkgem.jeesite.mybatis.modules.musicuser.entity.MusicInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年3月25日
 */
@Service
public class OrgInfoService {
  @Autowired private OrgInfoDAO oiDao;
  @Autowired private MusicInfoDao midao;

  public List<OrgInfo> getOrgList() {
    return oiDao.getOrgInfo();
  }

  public OrgInfo getOrgListById(String id) {
    OrgInfo orgInfo = null;
    List<OrgInfo> list = oiDao.getOrgListById(id);
    if (list.size() > 0) {
      orgInfo = list.get(0);
    }
    return orgInfo;
  }
  public String getOrgByCodeString(String orgID, int userRankID){
    String orgName = oiDao.getOrgByCodeString(orgID, userRankID, 0);
    return orgName;
  }
  public MusicInfo getMusicByOrgCode(String orgID, int userRankID){
    OrgInfo rootOrg=oiDao.getRootOrgByParentCode(orgID, userRankID, 0);
    MusicInfo mi=midao.getMuiscInfoByOrgId(rootOrg.getId());
    return mi;

  }
  public List<OrgInfo> getOrgListBypId(String pid) {
    return oiDao.getOrgListByPid(pid);
  }

  public List<OrgInfo> getOrgListProivnceBypId(String pid) {
    return oiDao.getOrgListByProvincePid(pid);
  }

  public Map<String, String> getMapList() {
    List<OrgInfo> list = oiDao.getOrgInfo();
    Map<String, String> map = new HashMap<String, String>();
    for (int i = 0; i < list.size(); i++) {
      map.put(list.get(i).getId(), list.get(i).getName());
    }
    return map;
  }
}
