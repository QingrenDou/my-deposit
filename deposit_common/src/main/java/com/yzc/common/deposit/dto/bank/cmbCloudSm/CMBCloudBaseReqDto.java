package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import lombok.Data;

/**
 * 完整请求参数
 */
@Data
public class CMBCloudBaseReqDto<T> {
    private CMBCloudCommonRequestDto<T> request;  //请求报文
    private CMBCloudCommonSignatureDto signature; //签名
}
