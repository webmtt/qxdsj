package com.thinkgem.jeesite.mybatis.modules.report.dao;

import com.thinkgem.jeesite.mybatis.common.persistence.CrudDao;
import com.thinkgem.jeesite.mybatis.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.mybatis.modules.report.entity.ReportLogInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 查询功能日志
 * @author yang.kq
 * @version 1.0
 * @date 2020/7/22 13:28
 */
@MyBatisDao
public interface ReportLogDao  extends CrudDao<ReportLogInfo> {

    void insertLogBatch(List<ReportLogInfo> list);

    void insertLog(ReportLogInfo reportLogInfo);

    List<ReportLogInfo> findList(@Param("pagestart") int pagestart, @Param("pagesize") int pageSize, @Param("startTime") String startTime,  @Param("endTime")String endTime, @Param("optType")String optType);

    long getCount(@Param("startTime") String startTime,  @Param("endTime")String endTime, @Param("optType")String optType);
}
