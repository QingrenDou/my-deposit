package com.yzc.common.guar.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 刷新保函请求参数
 */
@Data
public class RefreshGuarReqVo {

    /**
     * 项目id
     */
    @NotBlank(message = "项目id不能为空")
    private String projectId;

    /**
     * 投标企业id
     */
    @NotBlank(message = "投标企业id不能为空")
    private String bidderId;
}
