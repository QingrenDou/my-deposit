package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import lombok.Data;

/**
 * 申请子账号-请求参数
 */
@Data
public class CMBCloudAddSubAccReqDto {
    private String accnbr;//账号 String（35）
    private String dmanbr;//记账子单元编号 String（20）
    private String dmanam;//记账子单元名称 Z（82）
    private String ovrctl;//是否可透支 String（1）
    private String bcktyp;//支付失败退回方式 String（1）
    private String clstyp;//关闭条件 String（1）
    private String yurref;//业务参考号 String（30）
    private String lmtflg;//是否收款限额 String（1）
    private String ballmt;//余额上限 M（15）

}
