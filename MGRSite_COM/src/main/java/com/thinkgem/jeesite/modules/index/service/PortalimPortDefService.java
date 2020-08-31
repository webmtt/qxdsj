/*
 * @(#)GroundDataService.java 2015-9-15
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.index.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.index.dao.PortalimPortDefDao;
import com.thinkgem.jeesite.modules.index.entity.PortalimPortDef;

@Service
public class PortalimPortDefService extends BaseService {
	@Autowired
	private PortalimPortDefDao portalimPortDefDao;

	//无查询条件
	public Page<PortalimPortDef> findPortalimPortDefPage(Page<PortalimPortDef> page) {
		return portalimPortDefDao.findPortalimPortDefPage(page);
	}
	public List<PortalimPortDef> findAll() {
		return portalimPortDefDao.findPortalimPortDef();
	}
	public PortalimPortDef getPortalimPortDef(Integer id) {
		return portalimPortDefDao.get(id);
	}
	
	//有查询条件
	public Page<PortalimPortDef> getByPage(Page<PortalimPortDef> page, String chnname) {
		return portalimPortDefDao.getByPage(page,chnname);
	}
	public List<PortalimPortDef> getByChnname(String chnname) {
		return portalimPortDefDao.getByChnname(chnname);
	}
	
	public PortalimPortDef getMaxId() {
		return portalimPortDefDao.getMaxId();
	}
	
	@Transactional
	public void saveppd(PortalimPortDef portalimPortDef) {
		this.portalimPortDefDao.saveData(portalimPortDef);
		
	}
	@Transactional
	public void delppd(Integer recommenditemid) {
		this.portalimPortDefDao.delppd(recommenditemid);
	}
	@Transactional
	public PortalimPortDef findPortalimPortDefById(Integer recommenditemid) {
		return portalimPortDefDao.findPortalimPortDefById(recommenditemid);
	}
	@Transactional
	public void saveUpdate(PortalimPortDef portalimPortDef) {
		this.portalimPortDefDao.saveData(portalimPortDef);
		
	}
	@Transactional
	public List<PortalimPortDef> getReverseData(Integer id1, Integer id2) {
		return portalimPortDefDao.getReverseData(id1,id2);
	}
	
}
