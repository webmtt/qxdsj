package com.thinkgem.jeesite.modules.data.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.data.dao.SearchCondCfgDao;
import com.thinkgem.jeesite.modules.data.entity.SearchCondCfg;


@Service
public class SearchCondCfgService extends BaseService{
	@Autowired
	private SearchCondCfgDao searchCondCfgDao;
	public Page<SearchCondCfg> getByPage(Page<SearchCondCfg> page, String searchconfigcode) {
		return searchCondCfgDao.getByPage(page,searchconfigcode);
	}
	public Page<SearchCondCfg> getByPage(Page<SearchCondCfg> page) {
		return searchCondCfgDao.getByPage(page);
	}
	@Transactional
	public void saveSearchCondCfg(SearchCondCfg searchCondCfg) {
		this.searchCondCfgDao.save(searchCondCfg);
	}
	public SearchCondCfg getSearchCondCfgByScc(String searchconfigcode) {
		return searchCondCfgDao.getSearchCondCfgByScc(searchconfigcode);
	}
	public List<SearchCondCfg>  getSearchCondCfgList() {
		return searchCondCfgDao.getSearchCondCfgList();
	}
	@Transactional
	public void delSearchCondCfgByScc(String searchconfigcode) {
		this.searchCondCfgDao.delSearchCondCfgByScc(searchconfigcode);
	}
	public List<SearchCondCfg> getSearchCondCfgListByScc(String searchconfigcode) {
		return this.searchCondCfgDao.getSearchCondCfgListByScc(searchconfigcode);
	}
	
}
