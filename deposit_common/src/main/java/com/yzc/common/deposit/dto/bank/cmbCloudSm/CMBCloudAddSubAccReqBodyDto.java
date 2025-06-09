package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import lombok.Data;

import java.util.List;

/**
 * 新增虚拟号-完整报文体
 */
@Data
public class CMBCloudAddSubAccReqBodyDto extends CMBCloudCommonReqBodyDto {
    private List<CMBCloudBusModyReqDto> ntbusmody;
    private List<CMBCloudAddSubAccReqDto> ntdmaaddx; //报文体
}
