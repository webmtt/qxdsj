package com.thinkgem.jeesite.modules.data.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.data.dao.EleSetEleGroupDefDao;
import com.thinkgem.jeesite.modules.data.entity.EleSetEleGroupDef;


@Service
public class EleSetEleGroupDefService extends BaseService{
	@Autowired
	private EleSetEleGroupDefDao eleSetEleGroupDefDao;
	
	public EleSetEleGroupDef getEleSetEleGroupDef(Integer id) {
		return this.eleSetEleGroupDefDao.get(id);
	}
	public List<EleSetEleGroupDef> getListByelesetcode(String elesetcode) {
		return this.eleSetEleGroupDefDao.getListByelesetcode(elesetcode);
	}
	public Integer getMaxId() {
		return this.eleSetEleGroupDefDao.getMaxId();
	}
	public String getMaxElegroupcode(String elesetcode) {
		return this.eleSetEleGroupDefDao.getMaxElegroupcode(elesetcode);
		
	}
	@Transactional
	public void saveEleSetEleGroupDef(EleSetEleGroupDef eleSetEleGroupDef) {
		 this.eleSetEleGroupDefDao.save(eleSetEleGroupDef);
		
	}
	@Transactional
	public void deleteEleGroupCode(Integer id){
		this.eleSetEleGroupDefDao.deleteEleGroupCode(id);
	}

}
