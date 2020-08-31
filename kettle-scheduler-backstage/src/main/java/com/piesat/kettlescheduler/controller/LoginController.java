package com.piesat.kettlescheduler.controller;

import com.piesat.kettlescheduler.common.Constant;
import com.piesat.kettlescheduler.common.Result;
import com.piesat.kettlescheduler.common.ResultCode;
import com.piesat.kettlescheduler.model.KUser;
import com.piesat.kettlescheduler.service.UserService;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author YuWenjie
 * @ClassName LoginController
 * @Description 登录退出
 * @date 2020/7/30 15:10
 **/

@RestController
@RequestMapping("/login/")
@Api(value = "LoginController|用户登录接口")
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(value ="login.shtml",method= RequestMethod.GET)
    @ApiOperation(value="根据用户名和密码登录", notes="登录成功或失败")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "uAccount", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "uPassword", value = "密码", required = true, dataType = "String")
    })
    public String login(KUser kUser, RedirectAttributes attr, HttpServletRequest request) {
        if (null != kUser && StringUtils.isNotBlank(kUser.getuAccount()) &&
                StringUtils.isNotBlank(kUser.getuPassword())) {
            KUser u = userService.login(kUser);
            if (null != u) {
                request.getSession().setAttribute(Constant.SESSION_ID, u);
                return Result.success(u);
            } else {
                return Result.fail(ResultCode.USER_PASSWORDS_INCORRECT);
            }
        }
        return Result.fail(ResultCode.USER_PASSWORDS_NOT_NULL);
    }

    @RequestMapping(value = "logout.shtml",method= RequestMethod.GET)
    @ApiOperation(value="退出登录", notes="退出登录成功或失败")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(Constant.SESSION_ID);
        return Result.fail(ResultCode.LOGOUT);
    }
}
