package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import lombok.Data;

/**
 * 查询历史入账数据
 */
@Data
public class CMBCloudQueryRecordHisReqDto {
    private String accnbr;//账号
    private String dmanbr;//记账子单元编号
    private String begdat;//起始日期:只能查询近13 个月的历史交易
    private String enddat;//结束日期:小于当天，大于或等于起始日期，与起始日期相距100天内
    private String ctnkey;//续传字段
}
