package com.yzc.common.deposit.dto.bank.common;

import lombok.Data;

/**
 * 刷新退款状态-结果
 */
@Data
public class RefreshBackMoneyStatusRespDto {
    private Integer backStatus; // 0退款失败 1退款成功 2处理中,同枚举
    private String backRstDesc; // 处理结果描述
}
