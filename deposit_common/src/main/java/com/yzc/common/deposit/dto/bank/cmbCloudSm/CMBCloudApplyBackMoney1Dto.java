package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import lombok.Data;

/**
 * 退款申请-第一部分
 */
@Data
public class CMBCloudApplyBackMoney1Dto {
    private String setnbr;//原交易套号
    private String trxnbr;//原交易流水号
    private String trsamt;//交易金额
    private String accnbr;//主账号
    private String dumnbr;//记账子单元编号
    private String eptdat;//退款日期
    private String rpyacc;//原付方账号
    private String rpynam;//原付方名称
    private String intflg;//是否退息
    private String intamt;//利息
    private String nusage;//用途
    private String busnar;//客户摘要
    private String yurref;//业务参考号
    private String bckflg;//部分退款标志
    private String apdflg;//附加标志
    private String apvflg;//是否需要审批
}
