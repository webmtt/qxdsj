/*
 * @(#)OrgInfoService.java 2016年3月25日
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.statistics.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thinkgem.jeesite.modules.Users.dao.OrgInfoDAO;
import com.thinkgem.jeesite.modules.Users.entity.OrgInfo;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年3月25日
 */
@Service
public class OrgInfoService2 {
	@Autowired
	private OrgInfoDAO oiDao;

	public List<OrgInfo> getOrgList() {
		return oiDao.getOrgInfo();
	}
	public OrgInfo getOrgListById(String id) {
		OrgInfo orgInfo=null;
		List<OrgInfo> list=oiDao.getOrgListById(id);
		if(list.size()>0){
			orgInfo=list.get(0);
		}
		return  orgInfo;
	}
	public List<OrgInfo> getOrgListBypId(String pid) {
		return oiDao.getOrgListByPid(pid);
	}

	public List<OrgInfo> getOrgListProivnceBypId(String pid) {
		return oiDao.getOrgListByProvincePid(pid);
	}
}
