package com.thinkgem.jeesite.modules.distribute.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.distribute.dao.DdsPushConfInfoDao;
import com.thinkgem.jeesite.modules.distribute.entity.DdsPushConfInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class DdsPushConfInfoService {
	@Autowired
	private DdsPushConfInfoDao ddsPushConfInfoDao;
	@Transactional(readOnly = false)
	public void save(DdsPushConfInfo ddsPushConfInfo){
		ddsPushConfInfoDao.save(ddsPushConfInfo);
	}
	public Page<DdsPushConfInfo> getDdsPushConfInfoPage(Page<DdsPushConfInfo> page, DdsPushConfInfo ddsPushConfInfo){
		return ddsPushConfInfoDao.getDdsPushConfInfoPage(page,ddsPushConfInfo);
	}
	public DdsPushConfInfo getDdsDataDef(String pushId){
		return ddsPushConfInfoDao.get(pushId);
	}
	public List<DdsPushConfInfo> findDdsPushConfInfoListByDataId(String dataId) {
		return ddsPushConfInfoDao.findDdsPushConfInfoListByDataId(dataId);
	}
	@Transactional(readOnly = false)
	public void updateByDataId(String dataId) {
		ddsPushConfInfoDao.updateByDataId(dataId);
	}
	public int  getMaxId() {
		return ddsPushConfInfoDao.getMaxId();
	}
}
	
	

