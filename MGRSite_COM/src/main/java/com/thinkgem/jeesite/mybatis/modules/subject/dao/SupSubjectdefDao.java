/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.subject.dao;

import com.thinkgem.jeesite.mybatis.common.persistence.CrudDao;
import com.thinkgem.jeesite.mybatis.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.mybatis.modules.subject.entity.ProductImgDef;
import com.thinkgem.jeesite.mybatis.modules.subject.entity.SupSubjectdef;
import com.thinkgem.jeesite.mybatis.modules.subject.entity.TableInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 专题产品DAO接口
 * @author yangkq
 * @version 2020-02-28
 */
@MyBatisDao
public interface SupSubjectdefDao extends CrudDao<SupSubjectdef> {
    /**
     * 获取单条数据
     * @param id
     * @return
     */
    @Override
    SupSubjectdef get(String id);
    /**
     * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<T>());
     * @param entity
     * @return
     */
    @Override
    List<SupSubjectdef> findList(SupSubjectdef entity);

    /**
     * 创建图片表
     * @param tableName
     */
    void createTable(@Param("tableName") String tableName,@Param("productName")String productName);

    /**
     * 删除图片表
     * @param tableName
     */
    void dropPngTable(@Param("tableName") String tableName);

    /**
     * 查看图片表字段信息
     * @param tablename
     * @return
     */
    List<TableInfo> tableInfoList(@Param("tableName") String tablename,@Param("database_id") String database_id);

    void insertIntoProductImg(@Param("list") List<ProductImgDef> list);

    List<ProductImgDef> getProductImgInfo(@Param("tableName")String tablename);

    Integer checkProcode(@Param("procode")String procode,@Param("kind")String kind);

    void updatepub(SupSubjectdef supSubjectdef);
}