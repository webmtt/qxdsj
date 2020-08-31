package com.nmpiesat.idata.desklog.web;


import com.alibaba.fastjson.JSON;
import com.nmpiesat.idata.desklog.entity.DeskLog;
import com.nmpiesat.idata.desklog.service.DeskLogService;
import com.nmpiesat.idata.result.RestResult;
import com.nmpiesat.idata.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Slf4j
@RestController
@RequestMapping(value = "/desklog")
@Api(value = "DeskLogController", description = "前台日志写入接口")
public class DeskLogController {

    @Autowired
    private DeskLogService deskLogService;

    @ApiOperation(value="写入操作日志", notes="写入操作日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "日志标题", required = true),
            @ApiImplicitParam(name = "createBy", value = "创建者", required = true),
            @ApiImplicitParam(name = "requestUri", value = "请求URI", required = true),
    })
    @RequestMapping(value = {"/insertDeskLog"})
    public RestResult insertDeskLog(@RequestBody DeskLog deskLog, HttpServletRequest request){
        RestResult restResult = new RestResult();
        try{
            deskLog.setId(UUID.randomUUID().toString());
            deskLog.setRemoteAddr(StringUtil.getRemoteAddr(request));
            deskLog.setUserAgent(request.getHeader("user-agent"));
            deskLog.setMethod(request.getMethod());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            deskLog.setCreateDate(dateFormat.format(new Date(System.currentTimeMillis())));
            deskLog.setType("3"); //前台用户操作日志
            deskLogService.insert(deskLog);
            restResult.setCode("200");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("写入日志成功");
        }catch (Exception e){
            e.printStackTrace();
            restResult.setCode("300");
            restResult.setCurrentTime(new Timestamp(new Date().getTime()));
            restResult.setMessage("写入日志失败");

        }
        return restResult;
    }

}
