package com.yzc.deposit.config;

import com.yzc.common.interceptor.LoginUserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 登录验证以及登录信息初始化
 * 注：如果接口参数中不包含用户信息(即：token中不包含user关键字)，则不做验证，直接放行，用于自测
 * @author liuzhongxiang
 * @version 1.0
 * @title CustomWebMvcConfig
 * @description
 * @create 2023/10/27 11:44
 */
@Configuration
public class CustomWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //加载登录适配器
        registry.addInterceptor(new LoginUserInterceptor())
                .addPathPatterns("/**");
    }
}
