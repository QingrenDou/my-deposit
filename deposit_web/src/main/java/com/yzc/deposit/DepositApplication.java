package com.yzc.deposit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author liuzhongxiang
 * @version 1.0
 * @title DepositApplication
 * @description
 * @create 2025/3/4 11:18
 */
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.yzc.deposit.repository"})
@SpringBootApplication(scanBasePackages = {"com.youzhicai.*", "com.yzc.deposit.*","com.yzc.common.container", "com.yzc.common.api.service.config"})
@EnableFeignClients(basePackages = "**/rpc")
@EnableScheduling // 启用定时任务支持
public class DepositApplication {

    public static void main(String[] args) {
        SpringApplication.run(DepositApplication.class, args);
    }
}
