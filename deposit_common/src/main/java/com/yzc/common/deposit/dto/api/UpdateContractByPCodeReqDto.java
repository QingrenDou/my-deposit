package com.yzc.common.deposit.dto.api;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 修改合同状态请求参数
 */
@Data
public class UpdateContractByPCodeReqDto {
    /**
     * 项目编号
     */
    @NotBlank(message = "项目编号不能为空")
    private String projectCode;

    /**
     * 业务类型,同枚举
     */
    @NotNull(message = "业务类型不能为空")
    private Integer businessType;

    /**
     * 合同状态(1签订（默认），0取消)
     */
    private Integer contractStatus;
}
