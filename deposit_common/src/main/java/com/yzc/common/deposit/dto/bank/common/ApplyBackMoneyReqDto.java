package com.yzc.common.deposit.dto.bank.common;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 申请退款入参
 */
@Data
public class ApplyBackMoneyReqDto {
    /**
     * 银行类型【枚举：BankTypeCode】
     * 注：【可为空】默认为优质采平安银行
     */
    private Integer bankTypeCode;

    /**
     * 银行流水号[原交易流水号]
     */
    private String bankSeqNo;

    /**
     * 交易套号[原交易套号]
     */
    private String setNumber;

    /**
     * 原交易金额
     */
    private BigDecimal tradeMoney;

    /**
     * 转入子账号【取值：项目表子账号字段，注意：非WholeSubAcc字段】
     */
    private String inSubAcc;

    /**
     * 是否全部退款【通用枚举】
     */
    private Integer isAllBack;

    /**
     * 申请金额【全额退款时,为原金额】
     */
    private BigDecimal applyMoney;

    /**
     * 交易日期[格式：yyyyMMdd]
     */
    private String tradeDay;

    /**
     * 交易时间[格式：HHmmss]
     */
    private String tradeTime;

    /**
     * 来款银行号
     */
    private String fromBankCode;

    /**
     * 来款方开户行名
     */
    private String fromBankName;

    /**
     * 来款方开户行地址
     */
    private String fromBankAddress;

    /**
     * 来款方帐号
     */
    private String fromAcc;

    /**
     * 来款方账户名
     */
    private String fromAccName;

    /**
     * 退款附言
     */
    private String applyAddedMsg;

    /**
     * 交易备注
     */
    private String applyDesc;

    /**
     * 子账户名
     */
    private String subAccName;

    /**
     * 虚拟号的流程实例号/中心授权码等
     */
    private String projectReqNo;

    private Integer isRefundInterest; //是否退息【通用枚举】。默认为 否

    private BigDecimal interestAmount; // 利息

    private String applySeqNo; //申请流水号，默认为空，用途：当重新发起申请时，该字段需要与前一次保持一致
}
