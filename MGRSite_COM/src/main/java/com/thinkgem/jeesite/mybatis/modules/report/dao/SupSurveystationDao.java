/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.report.dao;


import com.thinkgem.jeesite.mybatis.common.persistence.CrudDao;
import com.thinkgem.jeesite.mybatis.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.mybatis.modules.report.entity.SupSurveystation;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

/**
 * 报表查询参数配置DAO接口
 * @author yang.kq
 * @version 2019-11-06
 */
@MyBatisDao
public interface SupSurveystationDao extends CrudDao<SupSurveystation> {
    /**
     * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<T>());
     * @param entity
     * @return
     */
    @Override
    List<SupSurveystation> findList(SupSurveystation entity);
    /**
     * 插入数据
     * @param entity
     * @return
     */
    @Override
     int insert(SupSurveystation entity);

    /**
     * 更新数据
     * @param entity
     * @return
     */
    @Override
     int update(SupSurveystation entity);

    /**
     * 删除数据（一般为逻辑删除，更新del_flag字段为1）
     * @param entity
     * @return
     */
    @Override
     int delete(SupSurveystation entity);

     void insertInfoBatch(List<SupSurveystation> list);

    List<SupSurveystation>  findListById(long stationNum);
}