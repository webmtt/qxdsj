package com.piesat.kettlescheduler.config;

import com.piesat.kettlescheduler.common.Constant;
import com.piesat.kettlescheduler.common.Result;
import com.piesat.kettlescheduler.common.ResultCode;
import com.piesat.kettlescheduler.utils.JsonUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 云上北疆-前端-数据统计 接口
     **/
    String staUri = "/cloud/";

    String swagger = "swagger";

    String swagger1 = "/v2/api-docs";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Object attribute = request.getSession().getAttribute(Constant.SESSION_ID);
        String uri = request.getRequestURI();

        // 云上北疆-前端-数据统计 不拦截
        if (uri.contains(staUri)) {
            return true;
        }
        if (uri.contains(swagger)) {
            return true;
        }
        if (uri.contains(swagger1)) {
            return true;
        }


        //登陆请求不能被拦截
        if (!uri.contains("login/login.shtml") && !uri.contains("login/logout.shtml")) {
            //判断session中是否有值？
            if (attribute == null) {
                try {
                    response.setHeader("Access-Control-Allow-Origin", "*");
                    response.setHeader("Access-Control-Allow-Methods", "*");
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(403);

//                    response.getWriter().write(Result.fail("900001","未登录！"));
                    response.getWriter().write(Result.fail(ResultCode.NOT_LOGIN));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}