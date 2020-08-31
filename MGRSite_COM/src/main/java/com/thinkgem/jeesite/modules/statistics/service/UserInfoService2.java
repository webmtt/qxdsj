/*
 * @(#)UserInfoService.java 2016年3月16日
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.statistics.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.Mail;
import com.thinkgem.jeesite.modules.Users.dao.OrgInfoDAO;
import com.thinkgem.jeesite.modules.Users.dao.UserInfoDAO;
import com.thinkgem.jeesite.modules.Users.entity.UserInfo;
import com.thinkgem.jeesite.modules.statistics.dao.OrgInfoDao2;
import com.thinkgem.jeesite.modules.statistics.dao.UserInfoDao;
import com.thinkgem.jeesite.modules.sys.dao.ComparasDao;

/**
 * 描述：
 * 
 * @author Administrator
 * @version 1.0 2016年3月16日
 */
@Service
public class UserInfoService2 {
	@Autowired
	private UserInfoDao uiDao;
	@Autowired
	private OrgInfoDao2 oiDao;
	@Autowired
	private ComparasDao cDao;

	public List<Object[]> getSumByTime(String start_time, String end_time, String timeType, Integer page, Integer row,
			String orderType) {
		return uiDao.getSumByTime(start_time, end_time, timeType, page, row, orderType);

	}
	public List<String> statYears(){
	     return this.uiDao.statYears();
		
	}
	public List<Object[]> getSumByTimeOrg(String start_time, String end_time, String timeType, Integer page, Integer row,
			String orderType) {
		return uiDao.getSumByTimeOrg(start_time, end_time, timeType, page, row, orderType);
	}
	public List<Object[]> getSumByTimePro(String start_time, String end_time, String timeType, Integer page, Integer row,
			String orderType) {
		return uiDao.getSumByTimePro(start_time, end_time, timeType, page, row, orderType);
	}
	
}
