/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.aboutus.service;

import java.util.List;

import com.thinkgem.jeesite.mybatis.common.service.CrudService;
import com.thinkgem.jeesite.mybatis.modules.aboutus.dao.AboutUsDao;
import com.thinkgem.jeesite.mybatis.modules.aboutus.entity.AboutUs;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.mybatis.common.persistence.Page;


/**
 * 关于我们信息维护Service
 * @author songyan
 * @version 2020-02-27
 */
@Service
@Transactional(readOnly = true)
public class AboutUsService extends CrudService<AboutUsDao, AboutUs> {

	public AboutUs get(String id) {
		return super.get(id);
	}
	
	public List<AboutUs> findList(AboutUs aboutUs) {
		return super.findList(aboutUs);
	}
	
	public Page<AboutUs> findPage(Page<AboutUs> page, AboutUs aboutUs) {
		return super.findPage(page, aboutUs);
	}
	
	@Transactional(readOnly = false)
	public void save(AboutUs aboutUs) {
		super.save(aboutUs);
	}
	
	@Transactional(readOnly = false)
	public void delete(AboutUs aboutUs) {
		super.delete(aboutUs);
	}
	
}