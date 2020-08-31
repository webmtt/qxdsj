package com.thinkgem.jeesite.modules.distribute.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.distribute.dao.BpsJobPlanDao;
import com.thinkgem.jeesite.modules.distribute.entity.BpsJobPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class BpsJobPlanService extends BaseService {
	@Autowired
	private BpsJobPlanDao bpsJobPlanDao;
	
	@Transactional(readOnly = false)
	public void saveJobPlan(BpsJobPlan bpsJobPlan){
		bpsJobPlanDao.save(bpsJobPlan);
	}
	@Transactional(readOnly = false)
	public void delete(String jobName){
		bpsJobPlanDao.delete(jobName);
	}
	public Page<BpsJobPlan> getBpsJobPlanPage(Page<BpsJobPlan> page, String jobName){
		return bpsJobPlanDao.getBpsJobPlanPage(page,jobName);
	}
	public BpsJobPlan getBpsJobPlan(String jobName){
		return bpsJobPlanDao.get(jobName);
	}
	public List<BpsJobPlan> getBpsJobPlanList(String jobName){
		return bpsJobPlanDao.findDdsDataDef(jobName);
	}
}
	
	

