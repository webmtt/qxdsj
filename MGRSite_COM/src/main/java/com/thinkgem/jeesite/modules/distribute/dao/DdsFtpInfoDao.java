package com.thinkgem.jeesite.modules.distribute.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.distribute.entity.DdsFtpInfo;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class DdsFtpInfoDao extends BaseDao<DdsFtpInfo> {

	public List<DdsFtpInfo> findDdsFtpInfoList() {
		String hql = "from DdsFtpInfo where invalid=0";
		return this.find(hql);
		
	}
	public List<DdsFtpInfo> findDdsFtpInfoList(String ftpId) {
		String hql = "from DdsFtpInfo where ftpId=:p1 and  invalid=0";
		return this.find(hql,new Parameter(ftpId));
		
	}
	public Page<DdsFtpInfo> getDdsFtpInfoPage(Page<DdsFtpInfo> page, DdsFtpInfo ddsFtpInfo) {
		String hql = "from DdsFtpInfo where invalid=0";
		return this.find(page,hql);
	}
	public void delete(String ftpId) {
		String hql="update DdsFtpInfo set invalid=1 where ftpId=:p1";
		this.update(hql,new Parameter(ftpId));
		
	}
	public List<DdsFtpInfo> findDdsFtpList(String dataId) {
		String hql = "from DdsFtpInfo where ftpId in(select ftpId from DdsPushConfInfo where dataId=:p1 and invalid=0) and  invalid=0";
		return this.find(hql,new Parameter(dataId));
		
	}
}
