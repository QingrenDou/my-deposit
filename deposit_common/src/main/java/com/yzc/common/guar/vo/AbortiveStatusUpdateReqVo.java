package com.yzc.common.guar.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 修改项目异常状态请求参数
 */
@Data
public class AbortiveStatusUpdateReqVo {
    /**
     * 优质采项目id
     */
    @NotBlank(message = "项目id不能为空")
    private String projectId;

    /**
     * 项目异常状态[1已终止,0正常],如果为空 则默认为1终止
     */
    private Integer abortiveStatus;
}
