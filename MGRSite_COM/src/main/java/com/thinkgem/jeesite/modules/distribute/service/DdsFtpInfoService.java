package com.thinkgem.jeesite.modules.distribute.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.distribute.dao.DdsFtpInfoDao;
import com.thinkgem.jeesite.modules.distribute.entity.DdsFtpInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class DdsFtpInfoService {
	@Autowired
	private DdsFtpInfoDao ddsFtpInfoDao;
	@Transactional(readOnly = false)
	public void save(DdsFtpInfo ddsFtpInfo){
		ddsFtpInfoDao.save(ddsFtpInfo);
	}
	
	public Page<DdsFtpInfo> getDdsFtpInfoPage(Page<DdsFtpInfo> page, DdsFtpInfo ddsFtpInfo){
		return ddsFtpInfoDao.getDdsFtpInfoPage(page,ddsFtpInfo);
	}
	public DdsFtpInfo getDdsFtpInfo(String ftpId){
		return ddsFtpInfoDao.get(ftpId);
	}
	public List<DdsFtpInfo> findDdsFtpInfoList(){
		return ddsFtpInfoDao.findDdsFtpInfoList();
	}
	public List<DdsFtpInfo> findDdsFtpInfoList(String ftpId){
		return ddsFtpInfoDao.findDdsFtpInfoList(ftpId);
	}
	public List<DdsFtpInfo> findDdsFtpList(String dataId){
		return ddsFtpInfoDao.findDdsFtpList(dataId);
	}
	@Transactional(readOnly = false)
	public void delete(String ftpId){
		ddsFtpInfoDao.delete(ftpId);
	}
}
	
	

