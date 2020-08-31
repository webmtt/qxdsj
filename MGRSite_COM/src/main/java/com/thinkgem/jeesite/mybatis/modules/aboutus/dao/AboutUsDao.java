/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.aboutus.dao;


import com.thinkgem.jeesite.mybatis.modules.aboutus.entity.AboutUs;
import com.thinkgem.jeesite.mybatis.common.persistence.CrudDao;
import com.thinkgem.jeesite.mybatis.common.persistence.annotation.MyBatisDao;

/**
 * 关于我们信息维护DAO接口
 * @author songyan
 * @version 2020-02-27
 */
@MyBatisDao
public interface AboutUsDao extends CrudDao<AboutUs> {
	
}