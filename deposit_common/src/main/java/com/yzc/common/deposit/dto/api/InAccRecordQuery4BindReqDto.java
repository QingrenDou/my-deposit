package com.yzc.common.deposit.dto.api;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 搜索打款记录-入参
 */
@Data
public class InAccRecordQuery4BindReqDto {

    /**
     * 投标人名称
     */
    @NotBlank(message = "投标人名称不能为空")
    private String bidderName;

    /**
     * 交易日期(yyyy-MM-dd)
     */
    @NotBlank(message = "交易日期不能为空")
    private String tradeDay;

    /**
     * 转入账号
     */
    @NotBlank(message = "转入账号不能为空")
    private String inSubAcc;
}
