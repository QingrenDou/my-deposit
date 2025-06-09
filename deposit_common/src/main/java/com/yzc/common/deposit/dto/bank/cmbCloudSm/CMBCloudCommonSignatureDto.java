package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import lombok.Data;

/**
 * 请求、响应 签名信息
 */
@Data
public class CMBCloudCommonSignatureDto {
    private String sigdat;
    private String sigtim;
}
