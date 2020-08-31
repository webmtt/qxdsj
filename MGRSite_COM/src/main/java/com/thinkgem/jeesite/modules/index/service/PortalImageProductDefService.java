/*
 * @(#)GroundDataService.java 2015-9-15
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.index.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.index.dao.PortalDataCategeoryDefDao;
import com.thinkgem.jeesite.modules.index.dao.PortalImageProductDefDao;
import com.thinkgem.jeesite.modules.index.dao.PortalImageRullDao;
import com.thinkgem.jeesite.modules.index.entity.PortalImageProductDef;
import com.thinkgem.jeesite.modules.index.entity.PortalImageRull;
import com.thinkgem.jeesite.modules.index.entity.PortalImageRullModel;

/**
 * 描述：
 * 
 * @author pw
 * @version 1.0 2015-9-15
 */
@Component
@Transactional(readOnly = false)
public class PortalImageProductDefService extends BaseService {
	@Autowired
	private PortalImageProductDefDao portalImageProductDefDao;
	@Autowired
	private PortalImageRullDao portalImageRullDao;
	
	
	public PortalImageRull getPortalImageRull(String id){
		return portalImageRullDao.get(id);
	}
	public List<PortalImageProductDef> getPortalDataListById(int id){
		List<PortalImageProductDef> hostsubjectlist=portalImageProductDefDao.getPortalImageListById(id);
		return hostsubjectlist;
	}
	public PortalImageProductDef getPortalImageById(int id){
		PortalImageProductDef portalDataCategoryDef=portalImageProductDefDao.getPortalImageById(id);
		return portalDataCategoryDef;
	}
	public List<PortalImageRull> getPortalImageRullList(){
		return portalImageRullDao.getPortalImageRullList();
	}
	public List<PortalImageProductDef> findAll(){
		return portalImageProductDefDao.findAll();
	}
	public List<PortalImageRullModel> getAllType(){
		return portalImageRullDao.getAllType();
	}
	public List<Object[]> getProductList(String type){
		return portalImageRullDao.getProductList(type);
	}
	public List<PortalImageRull> getPortalImageRullListByType(String type){
		return portalImageRullDao.getPortalImageRullListByType(type);
	}
	@Transactional
	public void updateAll() {
		 portalImageRullDao.updateAll();
	}
	@Transactional
	public void updatePortalImageRull(String type) {
		portalImageRullDao.updatePortalImageRull(type);
	}
	@Transactional
	public void savePortalImage(PortalImageProductDef portalImageProductDef){
		 portalImageProductDefDao.save(portalImageProductDef);
	}
	@Transactional
	public void savePortalImageRull(PortalImageRull portalImageRull){
		portalImageRullDao.save(portalImageRull);
	}
	@Transactional
	public void deletePortalImageById(String id){
		portalImageProductDefDao.deletePortalImageById(id);
	}
}
