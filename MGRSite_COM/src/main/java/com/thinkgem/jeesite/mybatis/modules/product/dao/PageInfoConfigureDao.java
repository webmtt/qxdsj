/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.product.dao;

import com.thinkgem.jeesite.mybatis.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.mybatis.common.persistence.CrudDao;
import com.thinkgem.jeesite.mybatis.modules.product.entity.PageInfoConfigure;

/**
 * 产品库页面配置DAO接口
 * @author yang.kq
 * @version 2019-11-04
 */
@MyBatisDao
public interface PageInfoConfigureDao extends CrudDao<PageInfoConfigure> {
    /**
     * 获取单条数据
     * @param id
     * @return
     */
    @Override
    public PageInfoConfigure get(String id);
}