package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import lombok.Data;

/**
 * 查询明细，申请虚拟号 主要报文内容
 */
@Data
public class CMBCloudQueryInfo1RespDto {
    private String bbknbr;//分行号
    private String inbacc;//活期结算账户
    private String dyanbr;//记账子单元编号
    private String dyanam;//记账子单元名称
    private String eftdat;//生效日期
    private String enddat;//终止日期
    private String ovrctl;//是否允许透支
    private String lmtflg;//额度标志
    private String ballmt;//余额上限额度
}
