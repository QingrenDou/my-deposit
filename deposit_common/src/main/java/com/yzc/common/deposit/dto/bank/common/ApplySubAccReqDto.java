package com.yzc.common.deposit.dto.bank.common;

import lombok.Data;

/**
 * 申请虚拟子账号-入参
 */
@Data
public class ApplySubAccReqDto {

    /**
     * [必填]子账号名称
     */
    private String subAccName;

    /**
     * [必填]银行类型
     */
    private Integer bankTypeCode;

    /**
     * 项目名称[部分银行需要]
     */
    private String projectName;
}
