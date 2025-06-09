package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import lombok.Data;

/**
 * 整个发送报文
 * @param <T> 报文体
 */
@Data
public class CMBCloudCommonRequestDto<T> {

    private T body;  //发送报文体

    private CMBCloudCommonReqHeadDto head; //报文头
}
