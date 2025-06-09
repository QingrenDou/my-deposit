package com.yzc.deposit.config;

import com.yzc.common.config.BaseSwaggerConfig;
import com.yzc.common.domain.SwaggerProperties;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author liuzhongxiang
 * @version 1.0
 * @title SwaggerConfig
 * @description
 * @create 2024/4/22 13:44
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.yzc.deposit")
                .title("保证金系统")
                .description("保证金系统相关接口文档")
                .contactName("豆庆仁")
                .version("1.0")
                .enableSecurity(false)
                .build();
    }

    /***
     * 解决springboot与swagger2冲突
     */
    @Bean
    public BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return generateBeanPostProcessor();
    }
}
