package com.thinkgem.jeesite.modules.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.user.dao.TargetUnitDao;
import com.thinkgem.jeesite.modules.user.entity.TargetUnit;

@Service
public class TargetUnitService extends BaseService{
	@Autowired
	private TargetUnitDao targetUnitDao;
	
	public List<TargetUnit> findtargetUnitList(){
		return this.targetUnitDao.findAll();
	}
	public TargetUnit findTargetByloginName(String loginName){
		return this.targetUnitDao.findTargetByloginName(loginName);
	}
}
