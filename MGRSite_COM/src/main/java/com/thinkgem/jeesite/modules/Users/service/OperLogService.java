/*
 * @(#)OperLogService.java 2016年3月16日
 *
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.Users.service;

import com.thinkgem.jeesite.modules.Users.dao.OperLogDAO;
import com.thinkgem.jeesite.modules.Users.entity.OperLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年3月16日
 */
@Service
public class OperLogService {
  @Autowired private OperLogDAO olDao;

  @Transactional
  public void saveLog(OperLog ol) {
    olDao.save(ol);
  }
}
