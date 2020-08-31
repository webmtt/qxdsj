package com.thinkgem.jeesite.modules.data.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.data.dao.SearchCondDefDao;
import com.thinkgem.jeesite.modules.data.entity.SearchCondDef;


@Service
public class SearchCondDefService extends BaseService{
	@Autowired
	private SearchCondDefDao searchCondDefDao; 

	public Page<SearchCondDef> getByPage(Page<SearchCondDef> page, String searchgroupcode) {
		return searchCondDefDao.getByPage(page,searchgroupcode);
	}

	public Page<SearchCondDef> getByPage(Page<SearchCondDef> page) {
		return searchCondDefDao.getByPage(page);
	}

	public Integer getMaxId() {
		return searchCondDefDao.getMaxId();
	}
	@Transactional
	public void saveSearchCondDef(SearchCondDef searchCondDef) {
		this.searchCondDefDao.saveData(searchCondDef);
	}
	public SearchCondDef getSearchCondDefById(Integer id) {
		return searchCondDefDao.getSearchCondDefById(id);
	}
	@Transactional
	public void delSearchCondDefById(Integer id) {
		this.searchCondDefDao.delSearchCondDefById(id);
		
	}
	@Transactional
	public void updateSearchCondDefById(String  searchgroupcode) {
		this.searchCondDefDao.updateSearchCondDefById(searchgroupcode);
		
	}
	public List<SearchCondDef> getSearchListByGroupCode(String GroupCode) {
		return this.searchCondDefDao.getSearchListByGroupCode(GroupCode);
	}
	public List<SearchCondDef> getSearchListGroupCode(String GroupCode) {
		return this.searchCondDefDao.getSearchListGroupCode(GroupCode);
	}
	public List<SearchCondDef> getSearchClistByCode(String searchsetcode) {
		return this.searchCondDefDao.getSearchClistByCode(searchsetcode);
	}
	
}
