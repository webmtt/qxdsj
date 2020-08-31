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
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.index.dao.PortalDataCategeoryDefDao;
import com.thinkgem.jeesite.modules.index.entity.PortalDataCategoryDef;

/**
 * 描述：
 * 
 * @author pw
 * @version 1.0 2015-9-15
 */
@Component
@Transactional(readOnly = false)
public class PortalDataCategoryDefService extends BaseService {
	@Autowired
	private PortalDataCategeoryDefDao portalDataCategeoryDefDao;
	public List<PortalDataCategoryDef> getPortalDataCategeoryDefList(int layer,String stringValue, String url,String ctxRoot) {
		List<PortalDataCategoryDef> hostsubjectlist = portalDataCategeoryDefDao.getPortalDataCategeoryDefList(layer);
		for (PortalDataCategoryDef rid : hostsubjectlist) {
			if (rid.getImageUrl() == null) {
				rid.setImageUrl("");
			} else {
				rid.setImageUrl(rid.getImageUrl().trim());
			}
			if(rid.getLinkurl()==null){
				rid.setLinkurl("");
			}else{
				rid.setLinkurl(rid.getLinkurl().trim());
			}
			if ((!rid.getImageUrl().startsWith("http"))&& (!rid.getImageUrl().equals(""))) {
				rid.setImageUrl(stringValue + rid.getImageUrl());
			}
			if(rid.getLinkurl().indexOf("<ctx>")!=-1){
				rid.setLinkurl(rid.getLinkurl().replace("<ctx>", ctxRoot));
			}
		}
		return hostsubjectlist;
	}
	public List<PortalDataCategoryDef> getPortalDataListById(int id){
		List<PortalDataCategoryDef> hostsubjectlist=portalDataCategeoryDefDao.getPortalDataListById(id);
		return hostsubjectlist;
	}
	public PortalDataCategoryDef getPortalDataById(int id){
		PortalDataCategoryDef portalDataCategoryDef=portalDataCategeoryDefDao.getPortalDataById(id);
		return portalDataCategoryDef;
	}
	public Page<PortalDataCategoryDef> findCateGoryByPage(String id, Page<PortalDataCategoryDef> page) {
		return portalDataCategeoryDefDao.findCateGoryByPage(id,page);
	}
	public List<PortalDataCategoryDef> findAll(){
		return portalDataCategeoryDefDao.findAll();
	}
	public List<PortalDataCategoryDef> findDataTwo(){
		return portalDataCategeoryDefDao.findDataTwo();
	}
	@Transactional
	public void savePortalData(PortalDataCategoryDef portalDataCategoryDef){
		 portalDataCategeoryDefDao.save(portalDataCategoryDef);
	}
	@Transactional
	public void deletePortalDataById(String id){
		portalDataCategeoryDefDao.deletePortalDataById(id);
	}
	@Transactional
	public void deletePortalDataByPid(String pid){
		portalDataCategeoryDefDao.deletePortalDataByPid(pid);
	}
}
