package com.yzc.deposit.controller.deposit.api;

import com.yzc.common.api.Result;
import com.yzc.common.deposit.dto.bank.common.ApplySubAccReqDto;
import com.yzc.common.deposit.dto.bank.common.ApplySubAccRespDto;
import com.yzc.common.deposit.vo.TestInAccReqVo;
import com.yzc.deposit.service.bank.impl.BankCWHbkyServiceImpl;
import com.yzc.deposit.service.deposit.IInAccRecordService;
import com.yzc.deposit.service.deposit.IProjectSubAccService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 测试接口
 */
@RestController
@RequestMapping(value = "api/test")
public class TestApiController {

    @Resource
    private IInAccRecordService inAccRecordService;

    @Resource
    private IProjectSubAccService projectSubAccService;

    @Autowired
    @Qualifier("bankCWHbkyServiceImpl")
    private BankCWHbkyServiceImpl bankCWHbkyServiceImpl;

    /**
     * 【TD-Web-091】保证金模拟入账接口
     * @param reqVo 请求入参
     * @return 操作结构
     */
    @PostMapping(value = "testInAcc")
    public Result<String> testInAcc(@RequestBody @Validated TestInAccReqVo reqVo) {
        return inAccRecordService.testInAcc(reqVo);
    }

    /**
     * 测试申请虚拟号
     * @param reqVo 入参
     * @return 结果
     */
    @PostMapping(value = "testApplySubAcc")
    public Result<ApplySubAccRespDto> testApplySubAcc(@RequestBody ApplySubAccReqDto reqVo){
        return projectSubAccService.testApplySubAcc(reqVo);
    }

    /**
     * 淮北矿业token测试
     * @return token
     */
    @GetMapping(value ="testToken")
    public String testToken(){
        return bankCWHbkyServiceImpl.testToken();
    }
}
