package com.thinkgem.jeesite.mybatis.modules.report.web;

import com.thinkgem.jeesite.mybatis.common.persistence.Page;
import com.thinkgem.jeesite.mybatis.modules.report.entity.ReportLogInfo;
import com.thinkgem.jeesite.mybatis.modules.report.service.ReportLogServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 查询功能日志信息
 * @author yang.kq
 * @version 1.0
 * @date 2020/7/22 13:30
 */
@Controller
@RequestMapping(value = "/report/logList")
public class ReportLogController {
    @Autowired
    private ReportLogServices services;
    @RequestMapping(value = {"/list", ""})
    public String list(
            ReportLogInfo reportLogInfo,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model) {
        String startTime=request.getParameter("start_time");
        String endTime=request.getParameter("end_time");
        String optType=request.getParameter("opt_type");
        Page<ReportLogInfo> page =
                services.findPage(
                        new Page<>(request, response), startTime,endTime,optType);
        model.addAttribute("page", page);
        model.addAttribute("optType", optType);
        return "modules/report/supOptLogList";
    }
}
