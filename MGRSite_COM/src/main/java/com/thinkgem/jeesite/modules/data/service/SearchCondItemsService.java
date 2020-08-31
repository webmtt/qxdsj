package com.thinkgem.jeesite.modules.data.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.data.dao.SearchCondItemsDao;
import com.thinkgem.jeesite.modules.data.entity.SearchCondItems;


@Service
public class SearchCondItemsService extends BaseService{
	@Autowired
	private SearchCondItemsDao searchCondItemsDao; 
	
	public SearchCondItems getSearchCondItems(Integer id) {
		return this.searchCondItemsDao.get(id);
		
	}
	public List<SearchCondItems> getSearchCondList(String itemtype) {
		return searchCondItemsDao.getSearchCondList(itemtype);
	}
	@Transactional
	public void saveSearchCondItems(SearchCondItems searchCondItems) {
		this.searchCondItemsDao.saveData(searchCondItems);
	}
	@Transactional
	public void deleteCondItems(Integer id,Integer invalid) {
		this.searchCondItemsDao.deleteCondItems(id,invalid);
	}
	public Integer getMaxId() {
		return searchCondItemsDao.getMaxId();
	}
}
