package com.yzc.common.deposit.dto.bank.cwHbky;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 查询入账数据-结果
 */
@Data
public class EsbInAccRespDto {
    private BigDecimal bcdzzjejsz1; //借方发生额
    private BigDecimal bcdzzjejsz2; //贷方发生额(对方说来款金额按这个字段来)
    private String besbzgjic; //备注
    private String besdfhm4ef; //对方户名
    private String besdfkhxmc; //对方开户行名称
    private String besfyoz4m; //附言
    private String beshmulaf; //户名
    private String beskhxlxh; //对方开户行联行号
    private String besskzhbzr; //对方账号
    private String besytuybd; //用途
    private String besyxjyrq; //银行交易日期
    private String besyxjysj; //银行交易时间
    private String beszhsydf; //账号
    private String beszyvgoh; //摘要
    private String dataintype; //数据来源
    private String besyxlshpzl; //银行流水号
}
