package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import lombok.Data;

import java.util.List;

/**
 * 关闭虚拟号-完整报文体
 */
@Data
public class CMBCloudCloseSubAccReqBodyDto extends CMBCloudCommonReqBodyDto {
    private List<CMBCloudBusModyReqDto> ntbusmody;
    private List<CMBCloudCloseSubAccMainDto> ntdmadltx1; //报文体1
    private List<CMBCloudCloseSubAccDto> ntdmadltx2; //报文体2
}
