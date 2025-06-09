package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import lombok.Data;

/**
 * 完整响应参数
 */
@Data
public class CMBCloudBaseRespDto<T> {
    private CMBCloudCommonResponseDto<T> response;  //请求报文
    private CMBCloudCommonSignatureDto signature; //签名
}
