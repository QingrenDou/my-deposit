package com.yzc.common.deposit.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * 批量退款 申请明细
 */
@Data
public class ApplyRecordDetailReqVo {

    /**
     * 主键id
     */
    @NotBlank(message = "入账记录id不能为空")
    private String inAccRecordId;

    /**
     * 扣款金额
     */
    private BigDecimal deductMoney;

    /**
     * 中标服务费金额(淮矿版)
     */
    private BigDecimal feeMoney;

    /**
     * 转履约金额
     */
    private BigDecimal performanceMoney;

    /**
     * 利息金额
     */
    private BigDecimal interestMoney;

    /**
     * 申请原因
     */
    private String applyDesc;
}
