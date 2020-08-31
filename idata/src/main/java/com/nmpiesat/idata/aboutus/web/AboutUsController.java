package com.nmpiesat.idata.aboutus.web;


import com.nmpiesat.idata.aboutus.entity.AboutUs;
import com.nmpiesat.idata.aboutus.service.AboutUsService;
import com.nmpiesat.idata.result.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/aboutus")
@Api(value = "AboutUsController", description = "关于我们展示接口")
public class AboutUsController {
    @Autowired
    private AboutUsService aboutUsService;

    @ApiOperation(value="关于我们展示", notes="关于我们展示")
    @RequestMapping("/findList")
    public RestResult findList(){
        RestResult restResult = new RestResult();
        List<AboutUs> aboutUs = aboutUsService.findList();
        if (aboutUs != null){
            restResult.setCode("200");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("查询数据成功");
            restResult.setData(aboutUs);
        }
        return restResult;
    }

}
