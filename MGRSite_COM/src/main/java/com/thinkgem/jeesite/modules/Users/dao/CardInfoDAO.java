/*
 * @(#)CardInfoDAO.java 2016年3月16日
 *
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.Users.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.Users.entity.CardInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年3月16日
 */
@Repository
public class CardInfoDAO extends BaseDao<CardInfo> {

  public CardInfo getCardById(String id) {
    String sql = "from CardInfo where iD=:p1";
    //    String sql = "select * from SUP_Card where id='" + id + "'";
    //    List<Object[]> list = this.getSession().createSQLQuery(sql).list();
    List<CardInfo> list = this.find(sql, new Parameter(id));
    if (list != null && list.size() > 0) {
      return list.get(0);
    } else {
      return null;
    }
  }
}
