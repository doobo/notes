package com._5fu8.admin.config;

import com._5fu8.admin.interceptor.ManagerInterceptor;
import com._5fu8.admin.interceptor.MasterInterceptor;
import com._5fu8.admin.interceptor.SystemInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import javax.annotation.Resource;

/**
 * 自定义拦截器配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    ManagerInterceptor managerInterceptor;

    @Resource
    MasterInterceptor masterInterceptor;

    @Resource
    SystemInterceptor systemInterceptor;

    /**
     * @param registry 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(managerInterceptor).addPathPatterns("/static/","/static/**","/manager/","/manager/**");
        registry.addInterceptor(masterInterceptor)
                .addPathPatterns("/kingmaster/","/kingmaster/**")
                .excludePathPatterns("/kingmaster/*.js","/kingmaster/*.css");
        registry.addInterceptor(systemInterceptor).addPathPatterns("/system/","/system/**");
    }

}
