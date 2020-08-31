/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.mybatis.common.persistence.Page;
import com.thinkgem.jeesite.mybatis.common.service.CrudService;
import com.thinkgem.jeesite.mybatis.modules.product.dao.PageInfoConfigureDao;
import com.thinkgem.jeesite.mybatis.modules.product.entity.PageInfoConfigure;

/**
 * 产品库页面配置Service
 * @author yang.kq
 * @version 2019-11-04
 */
@Service
@Transactional(readOnly = true)
public class PageInfoConfigureService extends CrudService<PageInfoConfigureDao, PageInfoConfigure> {
	@Autowired
	private PageInfoConfigureDao dao;
	@Override
	public PageInfoConfigure get(String id) {
		return dao.get(id);
	}
	
	@Override
    public List<PageInfoConfigure> findList(PageInfoConfigure pageInfoConfigure) {
		return dao.findList(pageInfoConfigure);
	}
	
	@Override
	public Page<PageInfoConfigure> findPage(Page<PageInfoConfigure> page, PageInfoConfigure pageInfoConfigure) {
		pageInfoConfigure.setPage(page);
		page.setList(dao.findList(pageInfoConfigure));
		return page;
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(PageInfoConfigure pageInfoConfigure) {
		if (pageInfoConfigure.getIsNewRecord()){
			pageInfoConfigure.preInsert();
			dao.insert(pageInfoConfigure);
		}else{
			pageInfoConfigure.preUpdate();
			dao.update(pageInfoConfigure);
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(PageInfoConfigure pageInfoConfigure) {
		dao.delete(pageInfoConfigure);
	}
	
}