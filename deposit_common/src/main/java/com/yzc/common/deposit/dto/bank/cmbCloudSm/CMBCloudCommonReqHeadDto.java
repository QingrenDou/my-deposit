package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import lombok.Data;

/**
 * 招商银行云直连SM方式 请求头
 */
@Data
public class CMBCloudCommonReqHeadDto {
    private String funcode;//接口名称 String(20) Y
    private String userid;//用户ID String(10) Y
    private String reqid;//请求ID String(51) N
}
