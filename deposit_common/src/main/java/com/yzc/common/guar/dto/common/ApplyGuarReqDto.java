package com.yzc.common.guar.dto.common;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 电子保函申请-通用入参
 */
@Data
public class ApplyGuarReqDto {

    /**
     * 【必填项】电子保函类型（1瀚华2兴泰3国控 同枚举）
     */
    private Integer guarTypeCode;

    /**
     * 业务类型（01 投标保函，02 政采保函，03 履约保函）
     * 统一枚举：YzcGuarBusinessTypeEnum
     */
    private String businessType;

    /**
     * 是否加密保函
     */
    private int isEncrypt;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 项目编号
     */
    private String projectNo;

    /**
     * 项目所属企业id
     */
    private String companyId;

    /**
     * 项目所属企业名称
     */
    private String companyName;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 投标截止时间
     */
    private Date signupEndTime;

    /**
     * 投标企业id
     */
    private String bidderId;

    /**
     * 投标企业名称
     */
    private String bidderName;

    /**
     * 瀚华业务类型（01 投标保函，02 政采保函，03履约保函）
     */
    private String hanhuaBusiType;

    /**
     * 开函机构(1:瀚华担保,2:富民银行,3:徽商银行)
     */
    private Integer financialId;

    /**
     * 统一社会信用代码
     */
    private String creditCode;

    /**
     * 法定代表人
     */
    private String legalName;

    /**
     * 法定代表人证件号码
     */
    private String legalCertNo;

    /**
     * 保函申请编号
     */
    private String lgNo;

    /**
     * 招标人/受益人
     */
    private String tenderee;

    /**
     * 保函申请金额（单位：元，精确到分）
     */
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
     * 申请协议书附件关联id
     * 【兴泰】必填
     */
    private String agreementAttRelaId;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 开标时间
     */
    private Date openBidTime;

}
