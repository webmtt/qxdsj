package com.nmpiesat.idata.cloud.web;

import com.alibaba.fastjson.JSONObject;
import com.nmpiesat.idata.cloud.service.CloudIndexService;
import com.nmpiesat.idata.result.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

/**
 * @title: kettle-scheduler->CloudIndexController
 * @description: 云上北疆-前端-数据统计
 * @author: YuWenjie
 * @date: 2020-04-28 10:08
 **/

@RestController
@RequestMapping("/cloud/")
public class CloudIndexController {

    private String ip = "http://192.168.1.18:8888";

    @Autowired
    private CloudIndexService cloudIndexService;

    /**
     * 数据目录交换情况（累计值）
     *
     * @return java.lang.String
     * @author YuWenjie
     * @date 2020/5/8 14:56
     **/
    @RequestMapping("directoryExchange.shtml")
    public RestResult countDataDirectoryExchange() {
        RestResult restResult = new RestResult();
        JSONObject data;
        try {
            data = cloudIndexService.countDataDirectoryExchange();
        } catch (Exception e) {
            e.printStackTrace();
            restResult.setCode("201");
            restResult.setCurrentTime(new Timestamp(System.currentTimeMillis()));
            restResult.setMessage("查询数据失败");
            return restResult;
        }
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(System.currentTimeMillis()));
        restResult.setMessage("查询数据成功");
        restResult.setData(data);
        return restResult;
    }

    /**
     * 数据资源交换情况（统计值）
     *
     * @param
     * @return java.lang.String
     * @author YuWenjie
     * @date 2020/5/8 14:57
     **/
    @RequestMapping("resourceExchange.shtml")
    public RestResult countDataResourceExchange() {
        RestResult restResult = new RestResult();
        JSONObject data;
        try {
            data = cloudIndexService.countDataResourceExchange();
        } catch (Exception e) {
            e.printStackTrace();
            restResult.setCode("201");
            restResult.setCurrentTime(new Timestamp(System.currentTimeMillis()));
            restResult.setMessage("查询数据失败");
            return restResult;
        }
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(System.currentTimeMillis()));
        restResult.setMessage("查询数据成功");
        restResult.setData(data);
        return restResult;
    }

    /**
     * 文件资源交换详情
     *
     * @param startDate
     * @param endDate
     * @return com.nmpiesat.idata.result.RestResult
     * @author YuWenjie
     * @date 2020/5/8 14:17
     **/
    @RequestMapping("fileResourceDetails.shtml")
    public RestResult countFileResourceExchangeDetails(@RequestParam(defaultValue = "2020-01-01") String startDate,
                                                       @RequestParam(defaultValue = "2020-01-09") String endDate) {
        RestResult restResult = new RestResult();
        JSONObject data;
        try {
            data = cloudIndexService.countFileResourceExchangeDetails(startDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
            restResult.setCode("201");
            restResult.setCurrentTime(new Timestamp(System.currentTimeMillis()));
            restResult.setMessage("查询数据失败");
            return restResult;
        }
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(System.currentTimeMillis()));
        restResult.setMessage("查询数据成功");
        restResult.setData(data);
        return restResult;
    }

    /**
     * 库表资源交换详情
     *
     * @param startDate
     * @param endDate
     * @return com.nmpiesat.idata.result.RestResult
     * @author YuWenjie
     * @date 2020/5/8 14:59
     **/
    @RequestMapping("tableResourceDetails.shtml")
    public RestResult countTableResourceExchangeDetails(@RequestParam(defaultValue = "2020-01-01") String startDate,
                                                    @RequestParam(defaultValue = "2020-01-09") String endDate) {
        RestResult restResult = new RestResult();
        JSONObject data;
        try {
            data = cloudIndexService.countTableResourceExchangeDetails(startDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
            restResult.setCode("201");
            restResult.setCurrentTime(new Timestamp(System.currentTimeMillis()));
            restResult.setMessage("查询数据失败");
            return restResult;
        }
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(System.currentTimeMillis()));
        restResult.setMessage("查询数据成功");
        restResult.setData(data);
        return restResult;
    }

    /**
     * 接口库资源交换详情
     *
     * @param startDate
     * @param endDate
     * @return com.nmpiesat.idata.result.RestResult
     * @author YuWenjie
     * @date 2020/5/8 14:58
     **/
    @RequestMapping("interfaceResourceDetails.shtml")
    public RestResult countInterfaceResourceExchangeDetails(@RequestParam(defaultValue = "2020-01-01") String startDate,
                                                        @RequestParam(defaultValue = "2020-01-09") String endDate) {
        RestResult restResult = new RestResult();
        JSONObject data;
        try {
            data = cloudIndexService.countInterfaceResourceExchangeDetails(startDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
            restResult.setCode("201");
            restResult.setCurrentTime(new Timestamp(System.currentTimeMillis()));
            restResult.setMessage("查询数据失败");
            return restResult;
        }
        restResult.setCode("200");
        restResult.setCurrentTime(new Timestamp(System.currentTimeMillis()));
        restResult.setMessage("查询数据成功");
        restResult.setData(data);
        return restResult;
    }
}
