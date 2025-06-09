package com.yzc.common.guar.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 电子保函-申请入参
 */
@Data
public class GuarApplyReqVo {

    /**
     * 优质采业务类型[1依法招标 2询比 3企业招标 4招募 5竞价]
     */
    @NotNull(message = "不能为空")
    private Integer yzcBusiType;

    /**
     * 项目id
     */
    @NotBlank(message = "项目id不能为空")
    private String projectId;

    /**
     * 项目编号
     */
    @NotBlank(message = "项目编号不能为空")
    private String projectNo;

    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空")
    private String projectName;

    /**
     * 招标人id
     */
    @NotBlank(message = "招标人id不能为空")
    private String companyId;

    /**
     * 招标人名称
     */
    private String companyName;

    /**
     * 投标截止时间[yyyy-MM-dd HH:mm:ss]
     */
    @NotBlank(message = "投标截止时间不能为空")
    private String signupEndTime;

    /**
     * 投标企业id
     */
    @NotBlank(message = "投标企业id不能为空")
    private String bidderId;

    /**
     * 投标企业名称
     */
    @NotBlank(message = "投标企业名称不能为空")
    private String bidderName;

    /**
     * 统一社会信用代码(取主体库字段)
     */
    private String creditCode;

    /**
     * 法定代表人(待定)
     */
    private String legalName;

    /**
     * 法定代表人证件号码(取主体库字段)
     */
    private String legalCertNo;

    /**
     * 招标人/受益人
     */
    @NotBlank(message = "招标人/受益人不能为空")
    private String tenderee;

    /**
     * 经办人身份证号码
     */
    private String operatorCertNo;

    /**
     * 经办人手机号
     */
    private String operatorPhone;

    /**
     * 申请人id
     */
    @NotBlank(message = "申请人id不能为空")
    private String applyUserId;

    /**
     * 申请人名称
     */
    @NotBlank(message = "申请人名称不能为空")
    private String applyUserName;

    /**
     * 项目保证金金额
     */
    @NotNull(message = "项目保证金金额不能为空")
    private BigDecimal projectDeposit;

    /**
     * 投标人国别(取主体库字段)
     */
    private String bidderCountry;

    /**
     * 是否加密保函[1加密函，0明文函，默认为：1]
     */
    private Integer isEncrypt;

    /**
     * 开标时间[yyyy-MM-dd HH:mm:ss]，如果为明文保函，开标时间必填
     */
    private String openBidTime;
}
