package com.piesat.kettlescheduler.mapper;

import com.piesat.kettlescheduler.model.*;
import org.beetl.sql.core.annotatoin.SqlStatement;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;


public interface KTransMonitorDao extends BaseMapper<KTransMonitor> {
    @SqlStatement(params = "kTransMonitor,start,size,categoryId")
    List<KTransMonitor> pageQuery(KTransMonitor kTransMonitor, Integer start, Integer size, Integer categoryId);

    @SqlStatement(params = "kTransMonitor,categoryId")
    Long allCount(KTransMonitor kTransMonitor, Integer categoryId);
}