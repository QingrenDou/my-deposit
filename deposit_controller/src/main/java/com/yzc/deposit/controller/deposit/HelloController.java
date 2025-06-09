package com.yzc.deposit.controller.deposit;

import com.yzc.common.api.Result;
import com.yzc.deposit.service.outside.IHelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author liuzhongxiang
 * @version 1.0
 * @title HelloController
 * @description
 * @create 2025/3/7 9:16
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    @Resource
    private IHelloService helloService;


    @GetMapping("/hello")
    public Result<String> hello() {
        String hello = helloService.hello();
        return Result.success(hello);
    }
}
