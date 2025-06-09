package com.yzc.common.deposit.dto.deposit;

import lombok.Data;

/**
 * 入账数据统计
 */
@Data
public class InAccRecordCountRespDto {
    /**
     * 来款记录总数
     */
    private Integer inRecordCount;

    /**
     * 已发起退款记录总数
     */
    private Integer refundRecordCount;
}
