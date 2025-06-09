package com.yzc.common.deposit.dto.bank.common;

import lombok.Data;

/**
 * 关闭子账号-入参
 */
@Data
public class CloseSubAccReqDto {
    /**
     * 子账户
     */
    private String subAcc;

    /**
     * 流程实例号
     */
    private String reqNo;
}
