package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import lombok.Data;

/**
 * 响应头
 */
@Data
public class CMBCloudCommonRespHeadDto {
    private String funcode;//接口名称
    private String userid;//用户ID
    private String reqid;//请求唯一编号
    private String rspid;//响应ID
    private String resultcode;//错误代码
    private String resultmsg;//错误描述
}
