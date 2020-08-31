package com.piesat.kettlescheduler.controller;

import com.piesat.kettlescheduler.common.Constant;
import com.piesat.kettlescheduler.common.BootTablePage;
import com.piesat.kettlescheduler.model.KUser;
import com.piesat.kettlescheduler.service.TransMonitorService;
import com.piesat.kettlescheduler.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/trans/monitor/")
public class TransMonitorController {

    @Autowired
    private TransMonitorService transMonitorService;

    @RequestMapping("getList.shtml")
    public String getList(Integer offset, Integer limit,Integer monitorStatus, Integer categoryId, String transName, HttpServletRequest request) {
        KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        BootTablePage list = transMonitorService.getList(offset, limit, monitorStatus,categoryId, transName, kUser.getuId());
        return JsonUtils.objectToJson(list);
    }

    @RequestMapping("getAllMonitorTrans.shtml")
    public String getAllMonitorJob(HttpServletRequest request) {
        KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        return JsonUtils.objectToJson(transMonitorService.getAllMonitorTrans(kUser.getuId()));
    }

    @RequestMapping("getAllSuccess.shtml")
    public String getAllSuccess(HttpServletRequest request) {
        KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        return JsonUtils.objectToJson(transMonitorService.getAllSuccess(kUser.getuId()));
    }

    @RequestMapping("getAllFail.shtml")
    public String getAllFail(HttpServletRequest request) {
        KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        return JsonUtils.objectToJson(transMonitorService.getAllFail(kUser.getuId()));
    }
}
