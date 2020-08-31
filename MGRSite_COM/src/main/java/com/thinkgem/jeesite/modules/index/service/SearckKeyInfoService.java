package com.thinkgem.jeesite.modules.index.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.index.dao.SearchKeyInfoDao;
import com.thinkgem.jeesite.modules.index.entity.SearchKeyInfo;


/**
 * 描述：
 *
 * @author yb
 * @version 1.0 
 */
@Component
public class SearckKeyInfoService extends BaseService{
	@Autowired
	private SearchKeyInfoDao searchKeyInfoDao;
	public SearchKeyInfo getSearchKeyInfo(Integer id){
		return this.searchKeyInfoDao.get(id);
	}
	public Page<SearchKeyInfo> getSearckKeyInfoList(int no, int size,String orderBy,String searchKeyWord){
		return searchKeyInfoDao.getSearckKeyInfoList(no, size, orderBy,searchKeyWord);
	}
}
