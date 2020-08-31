package com.thinkgem.jeesite.modules.index.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.index.dao.PortalitemCategoryDAO;
import com.thinkgem.jeesite.modules.index.dao.PortalitemCategoryDefDao;
import com.thinkgem.jeesite.modules.index.entity.PortalitemCategory;
import com.thinkgem.jeesite.modules.index.entity.PortalitemCategoryDef;


@Service
public class PortalitemCategoryDefService extends BaseService{
	@Autowired
	private PortalitemCategoryDefDao portalitemCategoryDefDao;
	@Autowired 
	private PortalitemCategoryDAO portalitemCategoryDAO;
	
	@Transactional
	public List<PortalitemCategoryDef> findAll(){
		return this.portalitemCategoryDefDao.findAll();
	}

	@Transactional
	public PortalitemCategoryDef findPortalitemCategoryDefById(int id) {
		return portalitemCategoryDefDao.findPortalitemCategoryDefById(id);
		
	}

	@Transactional
	public Integer getPortalitemCategoryDefById(Integer pid) {
		return portalitemCategoryDefDao.getPortalitemCategoryDefId(pid);
		
	}
	@Transactional
	public Integer getPortalitemCategoryDefLayerByPid(Integer parentid) {
		return portalitemCategoryDefDao.getPortalitemCategoryDefLayerByPid(parentid);
	}
	
	
	//修改保存
	@Transactional
	public void save(Integer funcitemid,String chnname, String shortchnname, String chndescription, int orderno, String linkurl,
			int invalid) {
		this.portalitemCategoryDefDao.save(funcitemid,chnname,shortchnname,chndescription,orderno,linkurl,invalid);
		
	}
	
//	@Transactional
//	public void save(Integer funcitemid, String chnname, String shortchnname, Integer layer, Integer showtype,
//			String linkurl, Integer orderno, Integer invalid, String chndescription, Integer isopen) {
//		this.portalitemCategoryDefDao.save(funcitemid,chnname,shortchnname,layer,showtype,linkurl,orderno,invalid,chndescription,isopen);
//		
//	}

	
//	@Transactional
//	public void save(String chnname, String shortchnname, Integer layer, Integer showtype,
//			String linkurl, Integer orderno, Integer invalid, String chndescription, Integer isopen) {
//		this.portalitemCategoryDefDao.save(chnname,shortchnname,layer,showtype,linkurl,orderno,invalid,chndescription,isopen);
//		
//	}
	@Transactional
	public void save(PortalitemCategoryDef portalitemCategoryDef) {
		this.portalitemCategoryDefDao.save(portalitemCategoryDef);
		
	}
	
	
	@Transactional
	public void savePc(PortalitemCategory pc) {
		this.portalitemCategoryDAO.saveData(pc);
		
	}

	@Transactional
	public void delPortalitemCategoryDefById(Integer funcitemid) {
		this.portalitemCategoryDefDao.delById(funcitemid);
		
	}
	@Transactional
	public void delPortalitemCategoryDefByPid(Integer funcitemid) {
		this.portalitemCategoryDAO.delByPid(funcitemid);
		
	}
	@Transactional
	public List<PortalitemCategory> findByPid(Integer funcitemid) {
		return portalitemCategoryDAO.findByPid(funcitemid);
		
	}

}
