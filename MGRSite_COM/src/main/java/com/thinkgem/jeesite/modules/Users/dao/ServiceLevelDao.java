/*
 * @(#)FtpUserInfoDAO.java 2016年11月30日
 *
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.Users.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.modules.Users.entity.ServiceLevel;
import org.springframework.stereotype.Repository;

import java.util.List;

/** 服务级别表 */
@Repository
public class ServiceLevelDao extends BaseDao<ServiceLevel> {

  public List<ServiceLevel> getServiceLevel() {
    String sql = "from ServiceLevel order by servicelevel";
    return this.find(sql);
  }
}
