package com.piesat.core.mapper;

import org.beetl.sql.core.annotatoin.SqlStatement;
import org.beetl.sql.core.mapper.BaseMapper;

import com.piesat.core.model.*;

import java.util.List;


public interface KTransRecordDao extends BaseMapper<KTransRecord> {

    @SqlStatement(params = "kTransRecord,start,size")
    List<KTransRecord> pageQuery(KTransRecord kTransRecord, Integer start, Integer size);

    @SqlStatement(params = "kTransRecord")
    Long allCount(KTransRecord kTrans);

    @SqlStatement(params = "kTransId,startTime,endTime")
    List<KTransRecord> getAll(int kTransId, String startTime, String endTime);


}