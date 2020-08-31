package com.thinkgem.jeesite.modules.user.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.user.entity.TargetUnit;
@Repository
public class TargetUnitDao extends BaseDao<TargetUnit>{
	public List<TargetUnit> findAll(){
		return this.find("from TargetUnit where invalid='0' order by orderNo");
	}
	public TargetUnit findTargetByloginName(String loginName){
		List<TargetUnit> list=this.find("from TargetUnit where invalid='0' and loginName=:p1 order by orderNo",new Parameter(loginName));
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
}
