package com.yzc.common.guar.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 电子保函-提交申请-入参
 */
@Data
public class GuarInfoSaveReqVo {
    /**
     * 保函申请id
     */
    @NotNull(message = "保函申请id不能为空")
    private Long guarInfoId;

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
//    private String agreementAttRelaId;
}
