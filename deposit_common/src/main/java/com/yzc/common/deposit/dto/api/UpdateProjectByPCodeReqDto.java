package com.yzc.common.deposit.dto.api;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 修改项目状态-入参
 */
@Data
public class UpdateProjectByPCodeReqDto {
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
     * 更新类型，见枚举：UpdateProjectTypePCodeEnum
     */
    @NotNull(message = "更新类型不能为空")
    private Integer updateType;

    /**
     * 【UpdateType==1 时，必填】开标时间【yyyy-MM-dd HH:mm:ss】
     */
    private String openBidTimeStr;

    /**
     * 【UpdateType==5 时，必填】异常状态【0：正常，1：异常】 --项目异常时，会还原候选人和中标人数据
     */
    private Integer isAbortive;

    /**
     * 【UpdateType==4\6 时，选填】确定时间【格式：yyyy-MM-dd HH:mm:ss】,如果为空会默认为当前时间
     */
    private String confirmTimeStr;

    /**
     * 【UpdateType==4\6 时，必填】候选人或中标人信息（注意：会先还原项目原来数据）
     */
    private List<WinBidderWithFeeDto> bidderWithFeeList;

}
