package com.yzc.deposit.service.outside.impl;

import com.yzc.deposit.service.AbstractBaseService;
import com.yzc.deposit.service.outside.IHelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author liuzhongxiang
 * @version 1.0
 * @title HelloServiceImpl
 * @description
 * @create 2025/3/6 21:00
 */
@Slf4j
@Service
public class HelloServiceImpl extends AbstractBaseService implements IHelloService {
    @Override
    public String hello() {
        return "hello deposit";
    }
}
