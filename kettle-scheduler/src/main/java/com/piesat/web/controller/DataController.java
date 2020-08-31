package com.piesat.web.controller;

import com.piesat.common.toolkit.Constant;
import com.piesat.core.dto.BootTablePage;
import com.piesat.core.dto.ResultDto;
import com.piesat.core.model.KData;
import com.piesat.core.model.KUser;
import com.piesat.web.service.DataService;
import com.piesat.web.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 * @author YuWenjie
 * @ClassName DataController
 * @Description 数据分类
 * @date 2020/7/15 14:47
 **/
@RestController
@RequestMapping("/data/")
public class DataController {
    @Autowired
    private DataService dataService;

    /**
     * 获取资料大类
     *
     * @param request
     * @return java.lang.String
     * @author YuWenjie
     * @date 2020/7/15 18:01
     **/
    @RequestMapping("getDataBigClassList.shtml")
    public String getDataBigClassList(HttpServletRequest request) {
        List<String> dataBigClassList = dataService.getDataBigClassList();
        return JsonUtils.objectToJson(dataBigClassList);
    }

    /**
     * 获取资料小类
     *
     * @param request
     * @param dataBigClass
     * @return java.lang.String
     * @author YuWenjie
     * @date 2020/7/16 15:09
     **/
    @RequestMapping("getDataSmallClassList.shtml")
    public String getDataSmallClassList(HttpServletRequest request, String dataBigClass) {
        List<String> dataSmallClassList = dataService.getDataSmallClassList(dataBigClass);
        return JsonUtils.objectToJson(dataSmallClassList);
    }

    @RequestMapping("getList.shtml")
    public String getList(Integer offset, Integer limit, HttpServletRequest request, KData kData) {
        KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        BootTablePage list = dataService.getList(offset, limit, kUser.getuId(), kData);
        return JsonUtils.objectToJson(list);
    }

    @RequestMapping("getData.shtml")
    public String getQuartz(Integer dataId) {
        return ResultDto.success(dataService.getQuartz(dataId));
    }

    @RequestMapping("insert.shtml")
    public String insert(KData kData, HttpServletRequest request) {
        KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        dataService.insert(kData, kUser.getuId());
        return ResultDto.success();
    }

    @RequestMapping("delete.shtml")
    public String delete(Integer dataId) {
        dataService.delete(dataId);
        return ResultDto.success();
    }

    @RequestMapping("update.shtml")
    public String update(KData kData, HttpServletRequest request) {
        KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
        try {
            dataService.update(kData, kUser.getuId());
            return ResultDto.success();
        } catch (Exception e) {
            return ResultDto.success(e.toString());
        }
    }

    /**
     * 校验名称
     *
     * @param dataId
     * @param dataName
     * @param response
     * @return void
     * @author YuWenjie
     * @date 2020/7/27 14:51
     **/
    @RequestMapping("IsDataExist.shtml")
    public void IsCategoryExist(Integer dataId, String dataName, HttpServletResponse response) {
        try {
            if (dataService.IsCategoryExist(dataId, dataName)) {
                response.getWriter().write("false");
            } else {
                response.getWriter().write("true");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
