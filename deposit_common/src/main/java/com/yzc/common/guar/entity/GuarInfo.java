package com.yzc.common.guar.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 保函申请信息
 * </p>
 *
 * @author DouQingRen
 * @since 2025-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_guar_info")
@ApiModel(value = "GuarInfo对象", description = "保函申请信息")
public class GuarInfo extends Model<GuarInfo> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id（雪花算法）")
    @TableId(value = "guar_info_id")
    private Long guarInfoId;

    @ApiModelProperty(value = "优质采业务类型[1依法招标]")
    private Integer yzcBusiType;

    @ApiModelProperty(value = "项目id")
    private String projectId;

    @ApiModelProperty(value = "项目编号")
    private String projectNo;

    @ApiModelProperty(value = "项目所属企业id")
    private String companyId;

    @ApiModelProperty(value = "项目所属企业名称")
    private String companyName;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "投标截止时间")
    private Date signupEndTime;

    @ApiModelProperty(value = "投标企业id")
    private String bidderId;

    @ApiModelProperty(value = "投标企业名称")
    private String bidderName;

    @ApiModelProperty(value = "瀚华业务类型（01 投标保函，02 政采保函，03履约保函）")
    private String hanhuaBusiType;

    @ApiModelProperty(value = "开函机构(1:瀚华担保,2:富民银行,3:徽商银行)")
    private Integer financialId;

    @ApiModelProperty(value = "统一社会信用代码")
    private String creditCode;

    @ApiModelProperty(value = "法定代表人")
    private String legalName;

    @ApiModelProperty(value = "法定代表人证件号码")
    private String legalCertNo;

    @ApiModelProperty(value = "保函申请编号")
    private String lgNo;

    @ApiModelProperty(value = "招标人/受益人")
    private String tenderee;

    @ApiModelProperty(value = "保函申请金额（单位：元，精确到分）")
    private BigDecimal guaranteeAmount;

    @ApiModelProperty(value = "经办人姓名")
    private String operatorName;

    @ApiModelProperty(value = "经办人身份证号码")
    private String operatorCertNo;

    @ApiModelProperty(value = "经办人手机号")
    private String operatorPhone;

    @ApiModelProperty(value = "营业执照附件关联id")
    private String busiLicenseAttRelaId;

    @ApiModelProperty(value = "身份证正面附件关联id")
    private String idcardFrontAttRelaId;

    @ApiModelProperty(value = "身份证反面附件关联id")
    private String idcardBackAttRelaId;

    @ApiModelProperty(value = "申请状态（0暂存 1提交）")
    private Integer applyStatus;

    @ApiModelProperty(value = "申请提交人id")
    private String applyUserId;

    @ApiModelProperty(value = "申请提交人名称")
    private String applyUserName;

    @ApiModelProperty(value = "申请时间")
    private Date applyTime;

    @ApiModelProperty(value = "支付链接")
    private String payUrl;

    @ApiModelProperty(value = "支付金额")
    private BigDecimal payAmount;

    @ApiModelProperty(value = "支付人账户名称")
    private String payAccountName;

    @ApiModelProperty(value = "支付人账户号")
    private String payAccountNo;

    @ApiModelProperty(value = "支付状态00=成功03=失败05=订单关闭")
    private String payStatus;

    @ApiModelProperty(value = "实际支付成功时间")
    private Date payTime;

    @ApiModelProperty(value = "支付更新时间")
    private Date payUpdateTime;

    @ApiModelProperty(value = "开函状态（0-未申请 1-审核中 2-通过，3-未通过 ，4-取消，6-已解保，7-已拒绝）")
    private Integer openStatus;

    @ApiModelProperty(value = "审核意见（open_status=3时给出未通过原因，其他时为空）")
    private String auditOpinion;

    @ApiModelProperty(value = "保函文件url")
    private String guaranteeFileUrl;

    @ApiModelProperty(value = "保函编号（用于在瀚华官网查询保函真实性）")
    private String guaranteeNumber;

    @ApiModelProperty(value = "保函代码（用于在瀚华官网查询保函真实性）")
    private String guaranteeCode;

    @ApiModelProperty(value = "出函时间（yyyy-MM-dd）")
    private String openTime;

    @ApiModelProperty(value = "出函接收时间")
    private Date openUpdateTime;

    @ApiModelProperty(value = "电子保函有效期起始时间（yyyy-MM-dd）")
    private String letterExpireStartTime;

    @ApiModelProperty(value = "电子保函有效期截止时间（yyyy-MM-dd）")
    private String letterExpireEndTime;

    @ApiModelProperty(value = "费率(小数，如：0.01)")
    private BigDecimal rate;

    @ApiModelProperty(value = "保函类型（bank：银行保函,company：担保保函，insurance：保险保函）（status=2时才返回）")
    private String ensureType;

    @ApiModelProperty(value = "金融机构名称（orgName）")
    private String financeOrgName;

    @ApiModelProperty(value = "解密状态[1已解密 0未解密]")
    private Integer decStatus;

    @ApiModelProperty(value = "解密申请时间")
    private Date decTime;

    @ApiModelProperty(value = "是否中标（1是,2否）(isWinBidding)")
    private Integer winStatus;

    @ApiModelProperty(value = "解保状态")
    private Integer releaseStatus;

    @ApiModelProperty(value = "解保时间")
    private Date releaseTime;

    @ApiModelProperty(value = "异常原因（1-撤标，2-流标，3-延标，4-放弃申请，9-其他）")
    private Integer closeReason;

    @ApiModelProperty(value = "退保状态[1退保成功 0未退 -1退保失败 2退保中 同枚举：GuarCloseStatusEnum]")
    private Integer closeStatus;

    @ApiModelProperty(value = "退保结果描述(瀚华返回)")
    private String closeResult;

    @ApiModelProperty(value = "退保发起时间")
    private Date closeTime;

    @ApiModelProperty(value = "发票查询状态[1已查询成功]")
    private Integer invoiceStatus;

    @ApiModelProperty(value = "发票查询时间")
    private Date invoiceTime;

    @ApiModelProperty(value = "发票文件id")
    private String invoiceDocId;

    @ApiModelProperty(value = "发票文件下载url")
    private String invoiceDocUrl;

    @ApiModelProperty(value = "索赔状态[1已发起索赔 2-理赔成功，3-理赔失败 同枚举]")
    private Integer compensateStatus;

    @ApiModelProperty(value = "索赔金额")
    private BigDecimal compensateMoney;

    @ApiModelProperty(value = "索赔联系人")
    private String compensateContactName;

    @ApiModelProperty(value = "索赔联系方式")
    private String compensateContactPhone;

    @ApiModelProperty(value = "索赔原因")
    private String compensateReason;

    @ApiModelProperty(value = "索赔收款账号")
    private String compensateAccNo;

    @ApiModelProperty(value = "索赔发起人id")
    private String compensateUserId;

    @ApiModelProperty(value = "索赔发起人名称")
    private String compensateUserName;

    @ApiModelProperty(value = "索赔附件关联id")
    private String compensateAttRelaId;

    @ApiModelProperty(value = "索赔发起时间")
    private Date compensateTime;

    @ApiModelProperty(value = "理赔受理人")
    private String compensateRecName;
    @ApiModelProperty(value = "理赔受理人联系方式")
    private String compensateRecPhone;
    @ApiModelProperty(value = "理赔受理意见")
    private String compensateRecRemark;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人id")
    private String createUserId;

    @ApiModelProperty(value = "创建人名称")
    private String createUserName;

    @ApiModelProperty(value = "最后更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "删除状态(1删除 0正常)")
    private Integer delStatus;

    @ApiModelProperty(value = "项目保证金金额")
    private BigDecimal projectDeposit;

    @ApiModelProperty(value = "优质采订单号")
    private String orderNo;

    @ApiModelProperty(value = "支付来源（1原瀚华支付 2优质采支付）")
    private String payFrom;

    /**
     * 退保人id
     */
    private String closeUserId;

    /**
     * 退保人名称
     */
    private String closeUserName;

    @ApiModelProperty(value = "退保原因")
    private String closeDesc;

    @ApiModelProperty(value = "投标人国别(其中中国、CHN、156或空值 都按国内算)")
    private String bidderCountry;

    /**
     * 项目异常状态[1已终止,0或null正常]
     */
    private Integer abortiveStatus;

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

    @Override
    public Serializable pkVal() {
        return this.guarInfoId;
    }

}
