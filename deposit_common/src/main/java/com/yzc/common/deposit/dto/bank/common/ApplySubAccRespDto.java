package com.yzc.common.deposit.dto.bank.common;

import lombok.Data;

/**
 * 申请虚拟子账号-结果
 */
@Data
public class ApplySubAccRespDto {

    /**
     * 子账户
     */
    private String subAcc;

    /**
     * 整体子账户
     */
    private String wholeSubAcc;

    /**
     * 流程实例号
     */
    private String reqNo;
}
