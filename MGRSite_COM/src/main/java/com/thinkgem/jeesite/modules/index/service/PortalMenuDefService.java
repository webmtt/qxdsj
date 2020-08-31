package com.thinkgem.jeesite.modules.index.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.index.dao.PortalMenuDAO;
import com.thinkgem.jeesite.modules.index.dao.PortalMenuDefDao;
import com.thinkgem.jeesite.modules.index.entity.PortalMenu;
import com.thinkgem.jeesite.modules.index.entity.PortalMenuDef;


@Service
public class PortalMenuDefService extends BaseService{
	@Autowired
	private PortalMenuDefDao portalMenuDefDao;
	
	@Autowired 
	private PortalMenuDAO portalMenuDAO;
	
	@Transactional
	public List<PortalMenuDef> findAll(){
		return portalMenuDefDao.findAll();
	}
	@Transactional
	public void savePm(PortalMenu pm) {
		this.portalMenuDAO.saveData(pm);
	}
	@Transactional
	public void savePm2(PortalMenuDef pm) {
		this.portalMenuDefDao.saveData(pm);
	}
	@Transactional
	public Integer getMaxMid(Integer parentid) {
		return portalMenuDefDao.getMaxMid(parentid);
	}
	@Transactional
	public Integer getMaxOrderno(Integer parentid) {
		return portalMenuDefDao.getMaxOrderno(parentid);
	}
	
	@Transactional
	public PortalMenuDef findPortalMenuDefById(Integer menuid) {
		return portalMenuDefDao.findPortalMenuDefById(menuid);
	}
	@Transactional
	public void deletePortalDataById(Integer menuid) {
		this.portalMenuDAO.delById(menuid);
		
	}
	@Transactional
	public void deletePortalDataByPid(Integer menuid) {
		this.portalMenuDAO.delByPid(menuid);
		
	}
	
}
