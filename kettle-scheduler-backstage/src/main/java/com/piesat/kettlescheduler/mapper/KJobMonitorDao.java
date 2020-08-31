package com.piesat.kettlescheduler.mapper;

import com.piesat.kettlescheduler.model.*;
import org.beetl.sql.core.annotatoin.SqlStatement;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;


public interface KJobMonitorDao extends BaseMapper<KJobMonitor> {
    @SqlStatement(params = "kJobMonitor,start,size,categoryId")
    List<KJobMonitor> pageQuery(KJobMonitor kJobMonitor, Integer start, Integer size, Integer categoryId);

    @SqlStatement(params = "kJobMonitor,categoryId")
    Long allCount(KJobMonitor kJobMonitor, Integer categoryId);
}