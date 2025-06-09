package com.yzc.common.guar.dto.xingtai;

import lombok.Data;

/**
 * 退保-响应结果
 */
@Data
public class XingTaiCommonGuarRespDto {
    private String lgNo;//保函申请编号

    //状态：成功0000,失败-1
    public String resultCode;

    //状态：成功SUCCESS,失败原因
    public String resultMessage;
}
