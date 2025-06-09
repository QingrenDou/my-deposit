package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import lombok.Data;

import java.util.List;

/**
 * 申请虚拟号响应报文体
 */
@Data
public class CMBCloudQueryInfoRespBodyDto {
    private List<CMBCloudQueryInfo1RespDto> ntdumadxz1; //子账户申请结果-主要响应体
    private List<CMBCloudQueryInfo2RespDto> ntduminfz1; //退款状态-主要响应体
}
