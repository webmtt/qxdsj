package com.thinkgem.jeesite.modules.data.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.data.dao.SearchInterfaceDefDao;
import com.thinkgem.jeesite.modules.data.entity.SearchCondItems;
import com.thinkgem.jeesite.modules.data.entity.SearchInterfaceDef;


@Service
public class SearchInterfaceDefService extends BaseService{
	@Autowired
	private SearchInterfaceDefDao searchInterfaceDefDao; 
	public List<SearchInterfaceDef> getSearchInterfaceDefList(String interfacename) {
		return this.searchInterfaceDefDao.getSearchInterfaceDefList(interfacename);
	}
	public List<SearchInterfaceDef> getSearchInterfaceDefListByCode(String interfacesetcode) {
		return this.searchInterfaceDefDao.getSearchInterfaceDefListByCode(interfacesetcode);
	}
	public SearchInterfaceDef getSearchInterface(Integer id) {
		return this.searchInterfaceDefDao.get(id);
	}
	public Integer getMaxId() {
		return this.searchInterfaceDefDao.getMaxId();
	}
	@Transactional
	public void saveSearchInterface(SearchInterfaceDef searchInterfaceDef) {
		this.searchInterfaceDefDao.saveData(searchInterfaceDef);
	}
	@Transactional
	public void delSearchInterface(Integer id) {
		this.searchInterfaceDefDao.delSearchInterface(id);
		
	}
	public List<Object[]> getTypename(){
		return this.searchInterfaceDefDao.getTypename();
	}
	
}
