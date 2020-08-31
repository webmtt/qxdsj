package com.thinkgem.jeesite.modules.data.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.data.dao.SearchSetDefDao;
import com.thinkgem.jeesite.modules.data.entity.SearchSetDef;


@Service
public class SearchSetDefService extends BaseService{
	@Autowired
	private SearchSetDefDao searchSetDefDao;
	public Page<SearchSetDef> getByPage(Page<SearchSetDef> page, String searchsetcode) {
		return searchSetDefDao.getByPage(page,searchsetcode);
	}
	public Page<SearchSetDef> getByPage(Page<SearchSetDef> page) {
		return searchSetDefDao.getByPage(page);
	}
	public Integer getMaxId() {
		return searchSetDefDao.getMaxId();
	}
	@Transactional
	public void saveSearchSetDef(SearchSetDef searchSetDef) {
		this.searchSetDefDao.save(searchSetDef);
	}
	public SearchSetDef getsearchSetDefById(Integer id) {
		return searchSetDefDao.getsearchSetDefById(id);
	}
	@Transactional
	public void delSearchSetDefById(Integer id) {
		this.searchSetDefDao.delSearchSetDefById(id);
	}
	public List<SearchSetDef> getsearchSetDefListById(String  searchsetcode) {
		return this.searchSetDefDao.getsearchSetDefListById(searchsetcode);
	}
	public List<SearchSetDef> getsearchSetDefListBy(String  searchsetcode,String searchgroupcode) {
		return this.searchSetDefDao.getsearchSetDefListBy(searchsetcode, searchgroupcode);
	}
}
