package com.piesat.kettlescheduler.mapper;

import com.piesat.kettlescheduler.model.KmKettleLog;
import org.beetl.sql.core.annotatoin.SqlStatement;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

public interface KmKettleLogDao extends BaseMapper<KmKettleLog> {

    @SqlStatement(params = "categoryId,startDate,endDate")
    Long countByDateAndCategoryId(Integer categoryId, String startDate, String endDate);

    @SqlStatement(params = "categoryId,startDate,endDate")
    List<KmKettleLog> countByDateAndCategoryIdGroupByDate(Integer categoryId, String startDate, String endDate);
}