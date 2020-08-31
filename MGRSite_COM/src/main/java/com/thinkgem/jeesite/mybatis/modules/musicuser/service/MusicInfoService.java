/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.musicuser.service;

import com.thinkgem.jeesite.modules.Users.dao.OrgInfoDAO;
import com.thinkgem.jeesite.mybatis.common.persistence.Page;
import com.thinkgem.jeesite.mybatis.common.service.CrudService;
import com.thinkgem.jeesite.mybatis.modules.musicuser.dao.MusicInfoDao;
import com.thinkgem.jeesite.mybatis.modules.musicuser.entity.MusicInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * music用户维护Service
 *
 * @author yang.kq
 * @version 2020-01-20
 */
@Service
@Transactional(readOnly = true)
public class MusicInfoService extends CrudService<MusicInfoDao, MusicInfo> {
  @Autowired private OrgInfoDAO oiDao;
  @Autowired private MusicInfoDao midao;

  @Override
  public MusicInfo get(String id) {
    return super.get(id);
  }

  @Override
  public List<MusicInfo> findList(MusicInfo musicInfo) {
    return super.findList(musicInfo);
  }

  public boolean checkUserName(String name) {
    MusicInfo mi = midao.getUserByName(name);
    if (mi != null) {
      return true;
    }
    return false;
  }

  @Override
  public Page<MusicInfo> findPage(Page<MusicInfo> page, MusicInfo musicInfo) {
    Page<MusicInfo> list = super.findPage(page, musicInfo);
    for (MusicInfo mi : list.getList()) {
      String name = oiDao.getOrgByCode(mi.getOrgid());
      mi.setOrgid(name);
    }
    return list;
  }

  @Override
  @Transactional(readOnly = false)
  public void save(MusicInfo musicInfo) {
    super.save(musicInfo);
  }

  @Override
  @Transactional(readOnly = false)
  public void delete(MusicInfo musicInfo) {
    super.delete(musicInfo);
  }
}
