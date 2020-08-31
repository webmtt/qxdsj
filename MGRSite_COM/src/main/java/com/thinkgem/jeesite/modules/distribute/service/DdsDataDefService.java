package com.thinkgem.jeesite.modules.distribute.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.distribute.dao.DdsDataDefDao;
import com.thinkgem.jeesite.modules.distribute.entity.DdsDataDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class DdsDataDefService {
	@Autowired
	private DdsDataDefDao ddsDataDefDao;
	@Transactional(readOnly = false)
	public void save(DdsDataDef ddsDataDef){
		ddsDataDefDao.save(ddsDataDef);
	}
	public List<DdsDataDef> getDdsDataDefList(){
		return ddsDataDefDao.findDdsDataDefList();
	}
	public List<DdsDataDef> getDdsDataDefList(String dataId){
		return ddsDataDefDao.findDdsDataDefList(dataId);
	}
	public Page<DdsDataDef> getDdsDataDefPage(Page<DdsDataDef> page, DdsDataDef ddsDataDef, String dataId, String hostName){
		return ddsDataDefDao.getDdsDataDefPage(page,ddsDataDef,dataId,hostName);
	}
	public DdsDataDef getDdsDataDef(int Id){
		return ddsDataDefDao.get(Id);
	}
	@Transactional(readOnly = false)
	public void delete(int Id) {
		ddsDataDefDao.delete(Id);
		
	}
	public  int findMaxId(){
		return ddsDataDefDao.findMaxId();
	}
	
	public int updateDdsDataDef(DdsDataDef ddsDataDef){
		return ddsDataDefDao.updateDdsDataDef(ddsDataDef);
	}
	
	public List<String> findHostName(){
		return ddsDataDefDao.findHostName();
	}
}
	
	

