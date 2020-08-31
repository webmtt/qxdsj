/*
 * @(#)FtpUserInfoService.java 2016年11月30日
 *
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.Users.service;

import com.thinkgem.jeesite.modules.Users.dao.ServiceLevelDao;
import com.thinkgem.jeesite.modules.Users.entity.ServiceLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/** 服务级别表 */
@Service
public class ServiceLevelService {
  @Autowired private ServiceLevelDao serviceLevelDao;

  public List<ServiceLevel> getServiceLevel() {
    return serviceLevelDao.getServiceLevel();
  }
}
