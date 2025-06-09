package com.yzc.common.deposit.dto.api;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 修改中标服务费信息
 */
@Data
public class UpdateWinBidServiceFeeReqDto {

    /**
     * 保证金账号
     */
    @NotBlank(message = "保证金账号不能为空")
    private String subAcc;

    /**
     * 保证金流水号
     */
    @NotBlank(message = "保证金流水号不能为空")
    private String bankSeqNo;

    /**
     * 中标服务费金额
     */
    private String feeMoneyStr;
}
