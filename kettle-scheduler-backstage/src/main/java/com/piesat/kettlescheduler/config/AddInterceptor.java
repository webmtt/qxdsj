package com.piesat.kettlescheduler.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author YuWenjie
 * @ClassName AddInterceptor
 * @Description TODO
 * @date 2020/7/30 11:28
 **/
@Configuration
public class AddInterceptor implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new LoginInterceptor());
    }
}
