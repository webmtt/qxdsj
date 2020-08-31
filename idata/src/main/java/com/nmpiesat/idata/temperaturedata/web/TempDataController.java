package com.nmpiesat.idata.temperaturedata.web;


import com.nmpiesat.idata.result.RestResult;
import com.nmpiesat.idata.temperaturedata.service.TempDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/temperaturedata")
@Api(value = "TempDataController", description = "台站气温表与台站累计气温表查询接口")
public class TempDataController {
    @Autowired
    private TempDataService tempDataService;

    @ApiOperation(value="台站气温表查询", notes="台站气温表查询")
    @RequestMapping("/selectStaTemp")
    public RestResult selectStaTemp(){
        RestResult restResult = new RestResult();
        List<HashMap> aa = tempDataService.selectStaTemp();
        if (aa != null){
            restResult.setCode("200");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("查询数据成功");
            restResult.setData(aa);
        }
        return restResult;
    }

    @ApiOperation(value="台站累计气温表查询", notes="台站累计气温表查询")
    @RequestMapping("/selectStaMulTemp")
    public RestResult selectStaMulTemp(){
        RestResult restResult = new RestResult();
        List<HashMap> bb = tempDataService.selectStaMulTemp();
        if (bb != null){
            restResult.setCode("200");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("查询数据成功");
            restResult.setData(bb);
        }
        return restResult;
    }

}
