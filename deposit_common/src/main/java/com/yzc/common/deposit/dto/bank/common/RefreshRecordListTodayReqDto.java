package com.yzc.common.deposit.dto.bank.common;

import lombok.Data;

/**
 * 刷新当日子账户流水-入参
 */
@Data
public class RefreshRecordListTodayReqDto {
    /**
     * 子账户
     */
    private String subAcc;

    /**
     * 整体子账户
     */
    private String wholeSubAcc;
}
