/*
 * @(#)OperLogDAO.java 2016年3月16日
 *
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.Users.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.modules.Users.entity.OperLog;
import org.springframework.stereotype.Repository;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年3月16日
 */
@Repository
public class OperLogDAO extends BaseDao<OperLog> {
  @Override
  public void save(OperLog entity) {
    super.save(entity);
  }
}
