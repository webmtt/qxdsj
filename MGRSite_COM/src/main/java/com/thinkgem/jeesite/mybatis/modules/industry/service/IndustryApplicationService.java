/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.industry.service;

import java.util.Date;
import java.util.List;

import com.thinkgem.jeesite.modules.products.entity.UploadProduct;
import com.thinkgem.jeesite.mybatis.common.service.CrudService;
import com.thinkgem.jeesite.mybatis.common.utils.DateUtils;
import com.thinkgem.jeesite.mybatis.modules.industry.dao.IndustryApplicationDao;
import com.thinkgem.jeesite.mybatis.modules.industry.entity.IndustryApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.mybatis.common.persistence.Page;


/**
 * 行业应用服务Service
 * @author songyan
 * @version 2020-02-26
 */
@Service
@Transactional(readOnly = true)
public class IndustryApplicationService extends CrudService<IndustryApplicationDao, IndustryApplication> {

	@Autowired
	IndustryApplicationDao industryApplicationDao;

	public IndustryApplication get(String id) {
		return super.get(id);
	}
	
	public List<IndustryApplication> findList(IndustryApplication industryApplication) {
		return super.findList(industryApplication);
	}
	
	public Page<IndustryApplication> findPage(Page<IndustryApplication> page, IndustryApplication industryApplication) {
		return super.findPage(page, industryApplication);
	}
	
	@Transactional(readOnly = false)
	public void save(IndustryApplication industryApplication) {
		industryApplication.setCreatTime(DateUtils.formatDateTime(new Date()));
		super.save(industryApplication);
	}
	
	@Transactional(readOnly = false)
	public void delete(IndustryApplication industryApplication) {
		super.delete(industryApplication);
	}

	public List<IndustryApplication> getAllUploadIndustry() {
		return industryApplicationDao.getAllUploadIndustry();
	}
	
}