/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.report.dao;

import com.thinkgem.jeesite.mybatis.common.persistence.CrudDao;
import com.thinkgem.jeesite.mybatis.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.mybatis.modules.report.entity.SupReportsearchconf;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报表查询参数配置DAO接口
 * @author yang.kq
 * @version 2019-11-21
 */
@MyBatisDao
public interface SupReportsearchconfDao extends CrudDao<SupReportsearchconf> {
    /**
     * 获取单条数据
     * @param id
     * @return
     */
    @Override
    SupReportsearchconf get(String id);
    /**
     * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<T>());
     * @param entity
     * @return
     */
    @Override
    List<SupReportsearchconf> findList(SupReportsearchconf entity);

    List<SupReportsearchconf> getListByDataType(String dataType);

    /**
     * 插入数据
     * @param entity
     * @return
     */
    @Override
     int insert(SupReportsearchconf entity);

    /**
     * 更新数据
     * @param entity
     * @return
     */
    @Override
     int update(SupReportsearchconf entity);
    /**
     * 根据id获取参数配置信息
     *
     * @return
     */
     List<SupReportsearchconf> findListById(SupReportsearchconf entity);
}