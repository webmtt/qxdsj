package com.piesat.kettlescheduler.mapper;

import com.piesat.kettlescheduler.model.*;
import org.beetl.sql.core.annotatoin.SqlStatement;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;


public interface KTransDao extends BaseMapper<KTrans> {
    @SqlStatement(params = "kTrans,start,size")
    List<KTrans> pageQuery(KTrans kTrans, Integer start, Integer size);

    @SqlStatement(params = "kTrans")
    List<KTrans> queryByCondition(KTrans kTrans);

    @SqlStatement(params = "kTrans")
    Long allCount(KTrans kTrans);

    @SqlStatement(params = "categoryId")
    List<KTrans> getByID(int categoryId);
}