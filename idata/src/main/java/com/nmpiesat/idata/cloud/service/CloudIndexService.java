package com.nmpiesat.idata.cloud.service;

import com.alibaba.fastjson.JSONObject;
import com.nmpiesat.idata.util.HttpClientUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @title: kettle-scheduler->CloudIndexService
 * @description: 云上北疆-前端-数据统计
 * @author: YuWenjie
 * @date: 2020-04-29 11:32
 **/

@Service
public class CloudIndexService {

    @Value("${cloudPath}")
    private String cloudPath;
//    private String ip = "http://192.168.1.18:8888";

    public JSONObject countDataDirectoryExchange() throws Exception {
        String url = cloudPath + "cloud/directoryExchange.shtml";
        String charset = "utf-8";
        String data = HttpClientUtil.doGet(url, charset);
        return JSONObject.parseObject(data);
    }

    public JSONObject countDataResourceExchange() throws Exception {
        String url = cloudPath + "cloud/resourceExchange.shtml";
        String charset = "utf-8";
        String data = HttpClientUtil.doGet(url, charset);
        return JSONObject.parseObject(data);
    }

    public JSONObject countFileResourceExchangeDetails(String startDate, String endDate) throws Exception {
        String url = cloudPath + "cloud/fileResourceDetails.shtml";
        String charset = "utf-8";
        HashMap<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        String data = HttpClientUtil.doPost(url,params,charset, true);
        return JSONObject.parseObject(data);
    }

    public JSONObject countTableResourceExchangeDetails(String startDate, String endDate) throws Exception {
        String url = cloudPath + "cloud/tableResourceDetails.shtml";
        String charset = "utf-8";
        HashMap<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        String data = HttpClientUtil.doPost(url,params,charset, true);
        return JSONObject.parseObject(data);
    }

    public JSONObject countInterfaceResourceExchangeDetails(String startDate, String endDate) throws Exception {
        String url = cloudPath + "cloud/interfaceResourceDetails.shtml";
        String charset = "utf-8";
        HashMap<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        String data = HttpClientUtil.doPost(url,params,charset, true);
        return JSONObject.parseObject(data);
    }

}
