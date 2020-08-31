package com.thinkgem.jeesite.modules.index.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.index.dao.FuncItmesDefDao;
import com.thinkgem.jeesite.modules.index.entity.FuncItmesDef;
import com.thinkgem.jeesite.modules.index.entity.FuncItmes;

@Service
public class FuncItmesDefService extends BaseService {
	
	@Autowired
	private FuncItmesDefDao funcItmesDefDao;
	
	public List<FuncItmesDef> findAll(){
		return this.funcItmesDefDao.findAll();
	}
	
	public List<FuncItmes> findFuncItmesAll(){
		return this.funcItmesDefDao.findFuncItmesAll();
	}
	
	public FuncItmes findFuncItmesByFuncID(int funcItemID){
		return this.funcItmesDefDao.findFuncItmesByFuncID(funcItemID);
	}
	
	public FuncItmesDef findFuncItmesDefByFuncID(int funcItemID){
		return this.funcItmesDefDao.findFuncItmesDefByFuncID(funcItemID);
	}
	
	public List<FuncItmesDef> findFuncItmesDefByParentID(int parentID){
		return this.funcItmesDefDao.findFuncItmesDefByParentID(parentID);
	}
	
	public Integer getMaxFuncItemID(Integer parentID){
		return this.funcItmesDefDao.getMaxFuncItemID(parentID);
	}
	
	public Integer getMaxOrderNo(Integer parentID){
		return this.funcItmesDefDao.getMaxOrderNo(parentID);
	}
	
	public Integer getLayer(Integer funcItemID){
		return this.funcItmesDefDao.getLayer(funcItemID);
	}
	
	@Transactional
	public void updateFuncItmesDef(FuncItmesDef funcItmesDef){
		this.funcItmesDefDao.save(funcItmesDef);
	}
	
	@Transactional
	public void deleteFuncItmesDefByFuncID(int funcItemID){
		this.funcItmesDefDao.deleteFuncItmesDefByFuncID(funcItemID);
	}
	
	@Transactional
	public void saveFuncItmesDef(FuncItmesDef funcItmesDef){
		this.funcItmesDefDao.save(funcItmesDef);
	}
	
	public Page<FuncItmesDef> getByPage(Page<FuncItmesDef> page, String CHNName){
		return this.funcItmesDefDao.getByPage(page, CHNName);
	}
	
	public Page<FuncItmesDef> searchByPage(Page<FuncItmesDef> page, String CHNName){
		return this.funcItmesDefDao.searchByPage(page, CHNName);
	}
	
}
