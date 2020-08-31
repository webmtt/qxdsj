/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.industry.dao;


import com.thinkgem.jeesite.mybatis.common.persistence.CrudDao;
import com.thinkgem.jeesite.mybatis.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.mybatis.modules.industry.entity.IndustryApplication;

import java.util.List;

/**
 * 行业应用服务DAO接口
 * @author songyan
 * @version 2020-02-26
 */
@MyBatisDao
public interface IndustryApplicationDao extends CrudDao<IndustryApplication> {

    List<IndustryApplication> getAllUploadIndustry();
	
}