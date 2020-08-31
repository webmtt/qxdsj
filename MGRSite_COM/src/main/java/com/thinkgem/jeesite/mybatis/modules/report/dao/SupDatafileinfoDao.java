/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.report.dao;

import com.thinkgem.jeesite.mybatis.common.persistence.CrudDao;
import com.thinkgem.jeesite.mybatis.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.mybatis.modules.report.entity.SupDatafileinfo;

import java.util.List;

/**
 * 报表数据文件管理DAO接口
 * @author yang.kq
 * @version 2019-11-06
 */
@MyBatisDao
public interface SupDatafileinfoDao extends CrudDao<SupDatafileinfo> {
    /**
     * 获取单条数据
     * @param id
     * @return
     */
    @Override
     SupDatafileinfo get(String id);
    /**
     * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<T>());
     * @param entity
     * @return
     */
    @Override
     List<SupDatafileinfo> findList(SupDatafileinfo entity);
    /**
     * 插入数据
     * @param entity
     * @return
     */
    @Override
     int insert(SupDatafileinfo entity);

    /**
     * 更新数据
     * @param entity
     * @return
     */
    @Override
     int update(SupDatafileinfo entity);

    /**
     * 删除数据（一般为逻辑删除，更新del_flag字段为1）
     * @param entity
     * @return
     */
    @Override
     int delete(SupDatafileinfo entity);
}