/*
 * @(#)FtpUserInfoService.java 2016年11月30日
 *
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.Users.service;

import com.thinkgem.jeesite.modules.Users.dao.FtpUserInfoDAO;
import com.thinkgem.jeesite.modules.Users.entity.FtpUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年11月30日
 */
@Service
public class FtpUserInfoService {
  @Autowired private FtpUserInfoDAO ftpUserInfoDao;

  @Transactional
  public void saveFtpUserInfo(FtpUserInfo ftpUserInfo) {
    ftpUserInfoDao.saveFtpUserInfo(ftpUserInfo);
  }

  public Boolean getUserInfoByName(String userName) {
    return ftpUserInfoDao.getUserInfoByName(userName);
  }
}
