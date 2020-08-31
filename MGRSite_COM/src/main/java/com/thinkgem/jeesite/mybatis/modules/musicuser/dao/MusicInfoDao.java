/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.musicuser.dao;

import com.thinkgem.jeesite.mybatis.common.persistence.CrudDao;
import com.thinkgem.jeesite.mybatis.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.mybatis.modules.musicuser.entity.MusicInfo;

/**
 * music用户维护DAO接口
 *
 * @author yang.kq
 * @version 2020-01-20
 */
@MyBatisDao
public interface MusicInfoDao extends CrudDao<MusicInfo> {
  MusicInfo getUserByName(String name);
  MusicInfo getMuiscInfoByOrgId(String orgid);
}
