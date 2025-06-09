package com.yzc.common.guar.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 退保申请入参
 */
@Data
public class GuarCloseReqVo {
    /**
     * 主键id
     */
    @NotNull(message = "guarInfoId不能为空")
    private Long guarInfoId;

    /**
     * 退保原因
     */
    private String closeDesc;
}
