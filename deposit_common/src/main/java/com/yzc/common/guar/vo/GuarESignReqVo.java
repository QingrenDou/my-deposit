package com.yzc.common.guar.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 电子保函-申请CA签章-入参
 */
@Data
public class GuarESignReqVo {

    /**
     * 是否优质采项目
     */
    @NotNull(message = "是否优质采标识不能为空")
    private Integer isYzc;

    /**
     * 保函申请id
     */
    @NotNull(message = "保函申请id不能为空")
    private Long guarInfoId;

    /**
     * 保函申请编号
     */
    @NotBlank(message = "保函申请编号不能为空")
    private String lgNo;

    /**
     * 保函机构类型（1瀚华2兴泰3国控 同枚举）
     */
    @NotNull(message = "保函机构类型不能为空")
    private Integer guarTypeCode;

    /**
     * 法人姓名
     */
    private String legalName;

    /**
     * 法人身份证号码
     */
    private String legalCertNo;

    /**
     * 保函申请金额
     */
    @NotNull(message = "保函申请金额不能为空")
    private BigDecimal guaranteeAmount;

    /**
     * 经办人姓名
     */
    private String operatorName;
    /**
     * 经办人身份证号码
     */
    private String operatorCertNo;
    /**
     * 经办人手机号
     */
    private String operatorPhone;

    /**
     * 项目编号【非优质采项目 必填】
     */
    private String projectNo;

    /**
     * 项目名称【非优质采项目 必填】
     */
    private String projectName;

    /**
     * 招标人/受益人【非优质采项目 必填】
     */
    private String tenderee;

    /******************** 申请签章 所需字段 ***************************************************/

    /**
     * 操作完成后的动作类型1.跨域通知 2.直接页面跳转 3.无动作(默认)
     */
    private Integer finishOperateType;

    /**
     * 操作完成后业务网址 1.跨域通知iframe通知地址2.直接页面跳转网址,用于新标签页打开的情况,
     * 签署完成后跳转到具体业务页面3.无动作时为空
     */
    private String finishOperateUrl;

    /**
     * 印章来源默认为0 0/UKey  2/小程序端
     */
    private Integer sealSource;

}
