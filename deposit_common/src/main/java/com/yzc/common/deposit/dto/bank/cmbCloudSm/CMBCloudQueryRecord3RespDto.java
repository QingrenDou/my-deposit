package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import lombok.Data;

/**
 * 查询当日、历史，接口响应报文之“续传状态”模块
 */
@Data
public class CMBCloudQueryRecord3RespDto {
    private String accnbr;//账号
    private String dmanbr;//记账子单元编号
    private String begdat;//起始日期
    private String enddat;//结束日期
    private String ctnkey;//续传字段
}
