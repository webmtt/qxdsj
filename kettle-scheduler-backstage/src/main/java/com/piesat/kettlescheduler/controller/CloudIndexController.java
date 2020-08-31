package com.piesat.kettlescheduler.controller;

import com.piesat.kettlescheduler.model.vo.DataDirectoryExchangeVO;
import com.piesat.kettlescheduler.model.vo.DataResourceExchangeVO;
import com.piesat.kettlescheduler.model.vo.ResourceDetailsVO;
import com.piesat.kettlescheduler.service.CloudIndexService;
import com.piesat.kettlescheduler.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @title: kettle-scheduler->CloudIndexController
 * @description: 云上北疆-前端-数据统计
 * @author: YuWenjie
 * @date: 2020-04-28 10:08
 **/

@RestController
@RequestMapping("/cloud/")
public class CloudIndexController {

    @Autowired
    private CloudIndexService cloudIndexService;

    /**
     * 数据目录交换情况（累计值）
     *
     * @param request
     * @return java.lang.String
     * @author YuWenjie
     * @date 2020/4/29 11:48
     **/
    @RequestMapping("directoryExchange.shtml")
    public String countDataDirectoryExchange(HttpServletRequest request) {
//        KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        DataDirectoryExchangeVO dataDirectoryExchangeVO = cloudIndexService.countDataDirectoryExchange();
        return JsonUtils.objectToJson(dataDirectoryExchangeVO);
    }

    /**
     * 数据资源交换情况（统计值）
     *
     * @param request
     * @return java.lang.String
     * @author YuWenjie
     * @date 2020/5/6 11:25
     **/
    @RequestMapping("resourceExchange.shtml")
    public String countDataResourceExchange(HttpServletRequest request) {
        DataResourceExchangeVO dataResourceExchangeVO = cloudIndexService.countDataResourceExchange();
        return JsonUtils.objectToJson(dataResourceExchangeVO);
    }

    /**
     * 文件资源交换详情
     *
     * @param request
     * @return java.lang.String
     * @author YuWenjie
     * @date 2020/5/7 09:26
     **/
    @RequestMapping("fileResourceDetails.shtml")
    public String countFileResourceExchangeDetails(HttpServletRequest request, String startDate, String endDate) {
        ResourceDetailsVO fileResourceDetailsVO = cloudIndexService.countFileResourceExchangeDetails(startDate, endDate);
        return JsonUtils.objectToJson(fileResourceDetailsVO);
    }

    /**
     * 库表资源交换详情
     *
     * @param request
     * @return java.lang.String
     * @author YuWenjie
     * @date 2020/5/7 9:40
     **/
    @RequestMapping("tableResourceDetails.shtml")
    public String countTableResourceExchangeDetails(HttpServletRequest request, String startDate, String endDate) {
        ResourceDetailsVO tableResourceDetailsVO = cloudIndexService.countTableResourceExchangeDetails(startDate, endDate);
        return JsonUtils.objectToJson(tableResourceDetailsVO);
    }

    /**
     * 接口库资源交换详情
     *
     * @param request
     * @return java.lang.String
     * @author YuWenjie
     * @date 2020/5/7 9:40
     **/
    @RequestMapping("interfaceResourceDetails.shtml")
    public String countInterfaceResourceExchangeDetails(HttpServletRequest request, String startDate, String endDate) {
        ResourceDetailsVO interfaceResourceDetailsVO = cloudIndexService.countInterfaceResourceExchangeDetails(startDate, endDate);
        return JsonUtils.objectToJson(interfaceResourceDetailsVO);
    }
}
