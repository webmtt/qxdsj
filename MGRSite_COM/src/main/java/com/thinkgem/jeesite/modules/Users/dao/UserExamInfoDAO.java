/*
 * @(#)UserExamInfoDAO.java 2016年3月22日
 *
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.Users.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.Users.entity.UserExamInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年3月22日
 */
@Repository
public class UserExamInfoDAO extends BaseDao<UserExamInfo> {
  @Override
  public void save(List<UserExamInfo> entityList) {
    super.save(entityList);
  }

  public List<UserExamInfo> getExamInfo(String email) {
    String sql = "from UserExamInfo where email=:p1";
    return this.find(sql, new Parameter(email));
  }

  public void deleteExamInfo(String email) {
    String sql = "delete from UserExamInfo where email=:p1";
    this.update(sql, new Parameter(email));
  }
}
