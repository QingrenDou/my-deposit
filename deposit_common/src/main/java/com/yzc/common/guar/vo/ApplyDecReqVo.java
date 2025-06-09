package com.yzc.common.guar.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 批量解密-入参
 */
@Data
public class ApplyDecReqVo {
    /**
     * 项目id【必填】
     */
    @NotBlank(message = "项目id不能为空")
    private String projectId;

    /**
     * 投标人id【单笔解密必填】
     */
    private String bidderId;

    /**
     * 项目开标时间，格式【yyyy-MM-dd HH:mm:ss】
     * 注意：如果为空，则默认为当前时间
     */
    private String openBidTime;

}
