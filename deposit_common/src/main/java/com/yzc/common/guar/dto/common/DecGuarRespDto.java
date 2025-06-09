package com.yzc.common.guar.dto.common;

import lombok.Data;

/**
 * 批量解密-
 */
@Data
public class DecGuarRespDto {
    private String lgNo;//保函申请编号

    //状态：成功0000,失败-1
    public String resultCode;

    //状态：成功SUCCESS,失败原因
    public String resultMessage;
}
