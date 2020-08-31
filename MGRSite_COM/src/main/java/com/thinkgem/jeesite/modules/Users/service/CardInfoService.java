/*
 * @(#)CardInfoService.java 2016年3月16日
 *
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.Users.service;

import com.thinkgem.jeesite.modules.Users.dao.CardInfoDAO;
import com.thinkgem.jeesite.modules.Users.entity.CardInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年3月16日
 */
@Service
public class CardInfoService {
  @Autowired private CardInfoDAO ciDao;

  public CardInfo getCardById(String id) {
    CardInfo ci = ciDao.getCardById(id);

    return ci;
  }
}
