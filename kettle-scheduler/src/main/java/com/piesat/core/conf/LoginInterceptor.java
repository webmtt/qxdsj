package com.piesat.core.conf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.piesat.common.toolkit.Constant;

public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 云上北疆-前端-数据统计 接口
     **/
    String staUri = "/cloud/";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Object attribute = request.getSession().getAttribute(Constant.SESSION_ID);
        String uri = request.getRequestURI();

        // 云上北疆-前端-数据统计 不拦截
        if (uri.contains(staUri)) {
            return true;
        }

        //登陆请求不能被拦截
        if (!uri.contains("view/loginUI.shtml") && !uri.contains("index/login.shtml")) {
            //判断session中是否有值？
            if (attribute == null) {
                response.sendRedirect(request.getContextPath() + "/view/loginUI.shtml");
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