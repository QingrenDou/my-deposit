package com.yzc.common.guar.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class GuarInfoRespDto {

    /**
     * 主键id（雪花算法）
     */
    private Long guarInfoId;

    /**
     * 优质采业务类型[1依法招标]
     */
    private Integer yzcBusiType;

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
     * 申请状态（0暂存 1提交）
     */
    private Integer applyStatus;

    /**
     * 申请提交人id
     */
    private String applyUserId;

    /**
     * 申请提交人名称
     */
    private String applyUserName;

    /**
     * 申请时间
     */
    private Date applyTime;

    /**
     * 支付链接
     */
    private String payUrl;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 支付人账户名称
     */
    private String payAccountName;

    /**
     * 支付人账户号
     */
    private String payAccountNo;

    /**
     * 支付状态00=成功03=失败05=订单关闭
     */
    private String payStatus;

    /**
     * 实际支付成功时间
     */
    private Date payTime;

    /**
     * 支付更新时间
     */
    private Date payUpdateTime;

    /**
     * 开函状态（0-未申请 1-审核中 2-通过，3-未通过 ，4-取消，6-已解保，7-已拒绝）
     */
    private Integer openStatus;

    /**
     * 审核意见（open_status=3时给出未通过原因，其他时为空）
     */
    private String auditOpinion;

    /**
     * 保函文件url
     */
    private String guaranteeFileUrl;

    /**
     * 保函编号（用于在瀚华官网查询保函真实性）
     */
    private String guaranteeNumber;

    /**
     * 保函代码（用于在瀚华官网查询保函真实性）
     */
    private String guaranteeCode;

    /**
     * 出函时间（yyyy-MM-dd）
     */
    private String openTime;

    /**
     * 出函接收时间
     */
    private Date openUpdateTime;

    /**
     * 电子保函有效期起始时间（yyyy-MM-dd）
     */
    private String letterExpireStartTime;

    /**
     * 电子保函有效期截止时间（yyyy-MM-dd）
     */
    private String letterExpireEndTime;

    /**
     * 费率(小数，如：0.01)
     */
    private BigDecimal rate;

    /**
     * 保函类型（bank：银行保函,company：担保保函，insurance：保险保函）（status=2时才返回）
     */
    private String ensureType;

    /**
     * 金融机构名称（orgName）
     */
    private String financeOrgName;

    /**
     * 解密状态[1已解密 0未解密]
     */
    private Integer decStatus;

    /**
     * 解密申请时间
     */
    private Date decTime;

    /**
     * 是否中标（1是,2否）(isWinBidding)
     */
    private Integer winStatus;

    /**
     * 解保状态
     */
    private Integer releaseStatus;

    /**
     * 解保时间
     */
    private Date releaseTime;

    /**
     * 异常原因（1-撤标，2-流标，3-延标，4-放弃申请，9-其他）
     */
    private Integer closeReason;

    /**
     * 退保状态[1退保成功 0未退 -1退保失败]
     */
    private Integer closeStatus;

    /**
     * 退保结果描述(瀚华返回)
     */
    private String closeResult;

    /**
     * 退保发起时间
     */
    private Date closeTime;

    /**
     * 发票查询状态[1已查询成功]
     */
    private Integer invoiceStatus;

    /**
     * 发票查询时间
     */
    private Date invoiceTime;

    /**
     * 发票文件id
     */
    private String invoiceDocId;

    /**
     * 发票文件下载url
     */
    private String invoiceDocUrl;

    /**
     * 索赔状态[1已发起索赔 2索赔成功 3索赔失败]
     */
    private Integer compensateStatus;

    /**
     * 索赔金额
     */
    private BigDecimal compensateMoney;

    /**
     * 索赔联系人
     */
    private String compensateContactName;

    /**
     * 索赔联系方式
     */
    private String compensateContactPhone;

    /**
     * 索赔原因
     */
    private String compensateReason;

    /**
     * 索赔收款账号
     */
    private String compensateAccNo;

    /**
     * 索赔发起人id
     */
    private String compensateUserId;

    /**
     * 索赔发起人名称
     */
    private String compensateUserName;

    /**
     * 索赔附件关联id
     */
    private String compensateAttRelaId;

    /**
     * 索赔发起时间
     */
    private Date compensateTime;

    /**
     * 理赔受理人
     */
    private String compensateRecName;
    /**
     * 理赔受理人联系方式
     */
    private String compensateRecPhone;
    /**
     * 理赔受理意见
     */
    private String compensateRecRemark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人id
     */
    private String createUserId;

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 最后更新时间
     */
    private Date updateTime;

    /**
     * 删除状态(1删除 0正常)
     */
    private Integer delStatus;

    /**
     * 优质采订单号
     */
    private String orderNo;

    /**
     * 支付来源（1原瀚华支付 2优质采支付）
     */
    private String payFrom;

    /**
     * 退保原因
     */
    private String closeDesc;

    /**
     * 投标人国别(其中CHN、156或空值 都按国内算)
     */
    private String bidderCountry;

    /**
     * 项目异常状态[1已终止,0或null正常]
     */
    private Integer abortiveStatus;

    /**
     * 退保人id
     */
    private String closeUserId;

    /**
     * 退保人名称
     */
    private String closeUserName;

    /**
     * 项目保证金金额
     */
    private BigDecimal projectDeposit;

    /**
     * 电子保函类型（1瀚华2兴泰3国控 同枚举）
     */
    private Integer guarTypeCode;

    /**
     * 收款方
     */
    private String payeeName;

    /**
     * 申请协议书附件关联id
     * 【兴泰】必填
     */
    private String agreementAttRelaId;

    /**
     * 是否优质采项目
     */
    private Integer isYzc;

    /**
     * 是否加密保函
     */
    private Integer isEncrypt;

    /**
     * 开标时间
     */
    private Date openBidTime;

    /**
     * 签名结果[蒋少：2:签署完成 3:失败 4:拒签]
     */
    private Integer signResult;

}
