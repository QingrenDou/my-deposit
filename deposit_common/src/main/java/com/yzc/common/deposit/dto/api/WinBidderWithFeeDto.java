package com.yzc.common.deposit.dto.api;

import lombok.Data;

@Data
public class WinBidderWithFeeDto {

    /**
     * 流水号
     */
    private String bankSeqNo;

    /**
     * 中标金额
     */
    private String winAmount;

}
