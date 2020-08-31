package com.thinkgem.jeesite.modules.distribute.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.distribute.dao.DdsSearchCondInfoDao;
import com.thinkgem.jeesite.modules.distribute.entity.DdsSearchCondInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class DdsSearchCondInfoService {
	@Autowired
	private DdsSearchCondInfoDao ddsSearchCondInfoDao;
	@Transactional(readOnly = false)
	public void save(DdsSearchCondInfo ddsSearchCondInfo){
		ddsSearchCondInfoDao.save(ddsSearchCondInfo);
	}
	public Page<DdsSearchCondInfo> getDdsSearchCondInfoPage(Page<DdsSearchCondInfo> page, DdsSearchCondInfo ddsSearchCondInfo){
		return ddsSearchCondInfoDao.getDdsSearchCondInfoPage(page,ddsSearchCondInfo);
	}
	public DdsSearchCondInfo getDdsSearchCondInfo(Integer id){
		return ddsSearchCondInfoDao.get(id);
	}
	public List<DdsSearchCondInfo> findDdsSearchCondInfoList(String dataId) {
		return ddsSearchCondInfoDao.findDdsSearchCondInfoList(dataId);
	}
	@Transactional(readOnly = false)
	public void delete(int  id){
		ddsSearchCondInfoDao.delete(id);
	}
	public int  getMaxId(){
		return ddsSearchCondInfoDao.getMaxId();
	}
}
	
	

