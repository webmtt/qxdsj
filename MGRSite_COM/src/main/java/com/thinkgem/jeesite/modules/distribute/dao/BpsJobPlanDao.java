package com.thinkgem.jeesite.modules.distribute.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.distribute.entity.BpsJobPlan;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class BpsJobPlanDao extends BaseDao<BpsJobPlan> {
	public List<BpsJobPlan> findDdsDataDef(String jobName) {
		String hql = "from BpsJobPlan where jobName=:p1";
		return this.find(hql,new Parameter(jobName));
		
	}

	public Page<BpsJobPlan> getBpsJobPlanPage(Page<BpsJobPlan> page, String jobName) {
		String hql = "from BpsJobPlan where invalid=0 and className='com.ultra.dataDS.main.InsertSearchRecord' ";
		if(jobName!=null&&!"".equals(jobName)){
			hql+=" and jobParameter like :p1 ";
			hql+="order by jobName";
			return this.find(page,hql,new Parameter("%"+jobName+"%"));
		}else{
			hql+="order by jobName";
			return this.find(page,hql);
		}
		
	}

	public void delete(String jobName) {
		String hql="update BpsJobPlan set invalid=1 where jobName=:p1";
		this.update(hql,new Parameter(jobName));
	}
	}
	
