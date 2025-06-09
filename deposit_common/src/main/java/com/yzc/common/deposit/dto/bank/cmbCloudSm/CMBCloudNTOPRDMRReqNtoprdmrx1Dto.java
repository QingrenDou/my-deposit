package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * NTOPRDMR API Request DTO - Part 1
 */
@Data
public class CMBCloudNTOPRDMRReqNtoprdmrx1Dto {
    @JsonProperty("setnbr")
    private String setnbr; // 原交易套号 String(15)

    @JsonProperty("trxnbr")
    private String trxnbr; // 原交易流水号 String(15), Y

    @JsonProperty("trsamt")
    private BigDecimal trsamt; // 交易金额 M, Y

    @JsonProperty("accnbr")
    private String accnbr; // 主账号 String(35), Y

    @JsonProperty("dumnbr")
    private String dumnbr; // 记账子单元编号 String(20), Y

    @JsonProperty("eptdat")
    private String eptdat; // 退款日期 D, Y

    @JsonProperty("rpyacc")
    private String rpyacc; // 原付方账号 String(35), Y

    @JsonProperty("rpynam")
    private String rpynam; // 原付方名称 Z(200), Y

    @JsonProperty("intflg")
    private String intflg; // 是否退息 String(1), Y

    @JsonProperty("intamt")
    private BigDecimal intamt; // 利息 M, Y

    @JsonProperty("nusage")
    private String nusage; // 用途 Z(62)

    @JsonProperty("busnar")
    private String busnar; // 客户摘要 Z(200)

    @JsonProperty("yurref")
    private String yurref; // 业务参考号 String(30), Y

    @JsonProperty("bckflg")
    private String bckflg; // 部分退款标志 String(1)

    @JsonProperty("apdflg")
    private String apdflg; // 附加标志 String(1)

    @JsonProperty("apvflg")
    private String apvflg; // 是否需要审批 String(1)
}
