package com.yzc.common.guar.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 订单中心支付成功回调入参
 */
@Data
public class GuarPaySuccessReqVo {

    /**
     * 保函申请编号
     */
    @NotBlank(message = "lgNo不能为空")
    private String lgNo;


}
