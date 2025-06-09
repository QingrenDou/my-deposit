package com.yzc.common.deposit.dto.bank.common;

import lombok.Data;

/**
 * 刷新当日子账户流水-入参
 */
@Data
public class RefreshRecordListHisReqDto {
    /**
     * 子账户
     */
    private String subAcc;

    /**
     * 整体子账户
     */
    private String wholeSubAcc;

    /**
     * 开始时间
     */
    private String begDate;

    /**
     * 结束时间
     */
    private String endDate;
}
