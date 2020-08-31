package com.piesat.kettlescheduler.kettle.environment;


import com.piesat.kettlescheduler.kettle.environment.KettleInit;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;


/**
 * @author YuWenjie
 * @ClassName ApplicationContextInitializer
 * @Description TODO
 * @date 2020/7/30 14:23
 **/
@Order(1)
@Slf4j
public class StartInitializer implements ApplicationContextInitializer {

    @SneakyThrows
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        /*// 打印容器里面有多少个bean
        System.out.println("bean count=====" + applicationContext.getBeanDefinitionCount());

        // 打印人所有 beanName
        System.out.println(applicationContext.getBeanDefinitionCount() + "个Bean的名字如下：");
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanDefinitionNames) {
            System.out.println(beanName);
        }*/

        //初始化环境***
        log.info("初始化环境 ------ kettle ------ 开始");
        KettleInit.init();
//		KettleInit.environmentInit();
        org.pentaho.di.core.KettleEnvironment.init(false);
        log.info("初始化环境 ------ kettle ------ 结束");

    }
}