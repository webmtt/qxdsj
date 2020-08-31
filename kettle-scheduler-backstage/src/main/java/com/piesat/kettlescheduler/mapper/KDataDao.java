package com.piesat.kettlescheduler.mapper;

import com.piesat.kettlescheduler.model.KData;
import org.beetl.sql.core.annotatoin.SqlStatement;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

public interface KDataDao extends BaseMapper<KData> {

    @SqlStatement(params = "kData,start,size")
    List<KData> pageQuery(KData kData, Integer start, Integer size);

    @SqlStatement(params = "kData")
    Long allCount(KData kData);
}
