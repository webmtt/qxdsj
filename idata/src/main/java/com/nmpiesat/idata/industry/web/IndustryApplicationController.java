package com.nmpiesat.idata.industry.web;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nmpiesat.idata.industry.entity.IndustryApplication;
import com.nmpiesat.idata.industry.service.IndustryApplicationService;
import com.nmpiesat.idata.result.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping(value = "/industry")
@Api(value = "IndustryApplicationController", description = "行业应用服务展示接口")
public class IndustryApplicationController {
    @Autowired
    private IndustryApplicationService industryApplicationService;
    @Value("${basepath}")
    private String basepath;

    @ApiOperation(value="查询行业应用服务", notes="查询行业应用服务接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页数", required = true ,dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "页面大小", required = true ,dataType = "Integer")
    })

    @RequestMapping("/findList")
    public  RestResult findList(@RequestParam(defaultValue = "0") Integer pageNum,
                               @RequestParam(defaultValue = "0") Integer pageSize){
        RestResult restResult = new RestResult();
        PageHelper.startPage(pageNum,pageSize);
        List<IndustryApplication> industryApplication = industryApplicationService.findList();
        for (int i = 0; i< industryApplication.size();i++){
            IndustryApplication industryApplication1 = new IndustryApplication();
            industryApplication1 = industryApplication.get(i);
            industryApplication1.setImageurl(basepath+industryApplication1.getImageurl());
        }
        PageInfo<IndustryApplication> page = new PageInfo<IndustryApplication>(industryApplication);
        if (page != null){
            restResult.setCode("200");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("查询数据成功");
            restResult.setData(page);
        }
        return restResult;
    }
    @RequestMapping("/findexample")
    public  RestResult findExampleById(String id){
        RestResult restResult = new RestResult();
        IndustryApplication industryApplication = industryApplicationService.findExampleById(id);
        if (industryApplication != null){
            restResult.setCode("200");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("查询数据成功");
            restResult.setData(industryApplication);
        }
        return restResult;
    }
}
