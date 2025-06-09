package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import lombok.Data;

import java.util.List;

/**
 * 新增虚拟号-完整报文体
 */
@Data
public class CMBCloudQueryInfoReqBodyDto extends CMBCloudCommonReqBodyDto {
    private List<CMBCloudQueryInfoReqDto> ntduminfx1; //报文体
}
