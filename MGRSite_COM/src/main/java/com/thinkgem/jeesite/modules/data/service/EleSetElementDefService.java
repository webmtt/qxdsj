package com.thinkgem.jeesite.modules.data.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.data.dao.EleSetElementDefDao;
import com.thinkgem.jeesite.modules.data.entity.EleSetElementDef;


@Service
public class EleSetElementDefService extends BaseService{
	@Autowired
	private EleSetElementDefDao eleSetElementDefDao;
	
	
	public EleSetElementDef getEleSetElementDefById(Integer id) {
		return this.eleSetElementDefDao.get(id);
	}
	public List<EleSetElementDef> getListByelesetcode(String elesetcode) {
		return this.eleSetElementDefDao.getListByelesetcode(elesetcode);
	}
	public List<EleSetElementDef> getListByelesetcode(String elesetcode,String elegroupcode) {
		return this.eleSetElementDefDao.getListByelesetcode(elesetcode,elegroupcode);
	}
	public List<EleSetElementDef> getListByelesetcode2(String elesetcode) {
		return this.eleSetElementDefDao.getListByelesetcode2(elesetcode);
	}
	@Transactional
	public void deleteElementDef(Integer id,Integer invalid) {
		 this.eleSetElementDefDao.deleteElementDef(id,invalid);
	}
	@Transactional
	public void saveEleSetElementDef(EleSetElementDef eleSetElementDef) {
		 this.eleSetElementDefDao.save(eleSetElementDef);
		 
	}
	public Integer getMaxId() {
		return this.eleSetElementDefDao.getMaxId();
	}
}
