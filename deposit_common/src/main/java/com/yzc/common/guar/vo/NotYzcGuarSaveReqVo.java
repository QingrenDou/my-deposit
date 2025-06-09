package com.yzc.common.guar.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 非优质采项目保函保存参数
 */
@Data
public class NotYzcGuarSaveReqVo {

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
     * 投标截止时间
     */
    @NotNull(message = "投标截止时间不能为空")
    private Date signupEndTime;

    /**
     * 法定代表人名称
     */
    private String legalName;

    /**
     * 法定代表人证件号码
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
     * 经办人姓名
     */
    @NotBlank(message = "经办人姓名不能为空")
    private String operatorName;

    /**
     * 保函申请金额(项目金额同)
     */
    @NotNull(message = "保函申请金额不能为空")
    private BigDecimal guaranteeAmount;

    /**
     * 营业执照附件关联id
     */
    private String busiLicenseAttRelaId;
    /**
     * 身份证正面附件关联id
     */
    private String idcardFrontAttRelaId;
    /**
     * 身份证反面附件关联id
     */
    private String idcardBackAttRelaId;

    /**
     * 申请状态（0暂存 1提交）
     */
    @NotNull(message = "状态不能为空")
    private Integer applyStatus;

    /**
     * 保函机构类型（1瀚华2兴泰3国控 同枚举）
     */
    @NotNull(message = "保函机构类型不能为空")
    private Integer guarTypeCode;

    /**
     * 申请协议书附件关联id
     * 【兴泰】必填
     * 注：CA签章后会自动更新申请书附件id，所以前端无需处理
     */
    //private String agreementAttRelaId;
}
