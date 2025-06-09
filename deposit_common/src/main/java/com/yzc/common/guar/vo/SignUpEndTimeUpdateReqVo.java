package com.yzc.common.guar.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 修改报名截止时间 -入参
 */
@Data
public class SignUpEndTimeUpdateReqVo {

    /**
     * 优质采项目id
     */
    @NotBlank(message = "项目id不能为空")
    private String projectId;

    /**
     * 投标截止时间[yyyy-MM-dd HH:mm:ss]
     */
    @NotBlank(message = "投标截止时间不能为空")
    private String signupEndTime;
}
