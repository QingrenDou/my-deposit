package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import lombok.Data;

/**
 * 完整响应结果
 */
@Data
public class CMBCloudCommonResponseDto<T> {
    private CMBCloudCommonRespHeadDto head;
    private T body;
}
