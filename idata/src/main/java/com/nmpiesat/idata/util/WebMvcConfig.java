package com.nmpiesat.idata.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

    /**
     * 静态资源的处理
     * @param registry
     */
    @Override
    public void addResourceHandlers (ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/static/**").addResourceLocations("classpath:/resources/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
    // addPathPatterns 用于添加拦截规则
    // excludePathPatterns 用户排除拦截
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] excludePath={
                "/userInfo/login",
                "/interfacedata/newInterface",
                "/interfacedata/getApiData",
                "/static/**"
        };

//        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/interfacedata/**").excludePathPatterns(excludePath);
//        registry.addInterceptor(new LoginInterceptor()).excludePathPatterns(excludePath);
    }

}
