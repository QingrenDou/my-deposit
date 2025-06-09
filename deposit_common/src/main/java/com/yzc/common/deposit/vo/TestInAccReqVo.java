package com.yzc.common.deposit.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 测试入账-入参
 */
@Data
public class TestInAccReqVo {
    /**
     * 入账子账号
     */
    @NotBlank(message = "入账子账号不能为空")
    private String inSubAcc;

    /**
     * 来款账号名称
     */
    @NotBlank(message = "来款账号名称不能为空")
    private String fromAccName;

    /**
     * 交易金额
     */
    @NotNull(message = "交易金额不能为空")
    private BigDecimal tradeMoney;

    /**
     * 来款附言
     */
    private String addedMsg;
}
