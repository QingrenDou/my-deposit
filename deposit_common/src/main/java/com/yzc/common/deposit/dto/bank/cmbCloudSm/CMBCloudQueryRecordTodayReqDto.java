package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import lombok.Data;

/**
 * 查询当日入账记录-请求参数
 */
@Data
public class CMBCloudQueryRecordTodayReqDto {
    private String accnbr;//账号
    private String dmanbr;//记账子单元编号
    private String ctnkey;//续传字段
}
