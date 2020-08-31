package com.nmpiesat.idata.util;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yang.kq
 * @version 1.0
 * @date 2020/5/11 16:01
 */
    @Component
    public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String uri = request.getRequestURI();
        if (null == request.getSession().getAttribute("loginUser")) {
            request.getSession().setAttribute("errorMsg", "请重新登陆");
            System.out.println(request.getContextPath() + "/#/login");
            response.sendRedirect(request.getContextPath() + "/#/login");
            return false;
        } else {
            request.getSession().removeAttribute("errorMsg");
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }
}
