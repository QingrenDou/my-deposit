package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import lombok.Data;

/**
 * 查询当日、历史，接口响应报文之“退款数据”模块
 */
@Data
public class CMBCloudQueryRecord2RespDto {
    private String trxnbr;//记账流水号
    private String rpybbn;//收付方开户行行号
    private String rpybkn;//收付方开户行行名
    private String rpyadr;//收付方开户行地址
    private String trxset;//交易套号
    private String rlttrx;//关联交易流水号
    private String rltset;//关联交易套号
    private String trsmod;//记账类型
    private String bussts;//业务类型
    private String rsv100;//保留字
}
