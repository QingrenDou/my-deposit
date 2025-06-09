package com.yzc.common.deposit.dto.bank.common;

import lombok.Data;

/**
 * 刷新退款状态-入参
 */
@Data
public class RefreshBackMoneyStatusReqDto {
    private String reqNo; // 流程实例号
}
