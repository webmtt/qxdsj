/*
 * @(#)FtpUserInfoDAO.java 2016年11月30日
 *
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.Users.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao_ftp;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.Users.entity.FtpUserInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年11月30日
 */
@Repository
public class FtpUserInfoDAO extends BaseDao_ftp<FtpUserInfo> {

  public void saveFtpUserInfo(FtpUserInfo ftpUserInfo) {
    String sql = "update FtpUserInfo set Password=:p1 where upper(User)=upper(:p2)";
    this.update(sql, new Parameter(ftpUserInfo.getPassword(), ftpUserInfo.getUser()));
  }

  public Boolean getUserInfoByName(String userName) {
    List<FtpUserInfo> list = this.find("from FtpUserInfo where User=:p1", new Parameter(userName));
    if (list != null && list.size() > 0) {
      return false;
    }
    return true;
  }
}
