package com.thinkgem.jeesite.mybatis.modules.report.service;

import com.thinkgem.jeesite.mybatis.common.persistence.Page;
import com.thinkgem.jeesite.mybatis.modules.report.dao.ReportLogDao;
import com.thinkgem.jeesite.mybatis.modules.report.entity.ReportLogInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 查询功能日志
 * @author yang.kq
 * @version 1.0
 * @date 2020/7/22 13:28
 */
@Service
public class ReportLogServices {
    @Autowired
    private ReportLogDao dao;

    public Page<ReportLogInfo> findPage(Page<ReportLogInfo> page,  String startTime, String endTime, String optType) {
        long count=dao.getCount(startTime,endTime,optType);
        List<ReportLogInfo> list=dao.findList((page.getPageNo()-1)*page.getPageSize(),page.getPageSize(),startTime,endTime,optType);
        Page page1=new Page(page.getPageNo(),page.getPageSize(),count,list);
        Page<ReportLogInfo> page2=page1.setList(list);
        return page2;
    }
    public void insertLog(ReportLogInfo reportLogInfo) {
        dao.insertLog(reportLogInfo);
    }
    public void insertLogBatch(List<ReportLogInfo> list) {
        dao.insertLogBatch(list);
    }

}
