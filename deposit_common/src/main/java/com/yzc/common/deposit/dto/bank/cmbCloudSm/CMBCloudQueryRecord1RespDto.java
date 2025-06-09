package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import lombok.Data;

/**
 * 查询当日、历史，接口响应报文之“来款数据”模块
 */
@Data
public class CMBCloudQueryRecord1RespDto {
    private String accnbr;//账号
    private String dmanbr;//记账子单元编号
    private String dmanam;//记账子单元名称
    private String trxnbr;//记账流水号
    private String ccynbr;//币种
    private String trxamt;//交易金额
    private String trxdir;//交易方向
    private String trxtim;//交易时间
    private String rpyacc;//收方/付方账号
    private String rpynam;//收方/付方名称
    private String trxtxt;//交易摘要
    private String narinn;//原内部编号
    private String mthflg;//匹配标志
    private String balflg;//余额有效标志
    private String onlbal;//余额
    private String rvstag;//冲补账标志
    private String autflg;//记账方式
    private String txtcod;//摘要代码
    private String rsv30z;//保留字30
}
