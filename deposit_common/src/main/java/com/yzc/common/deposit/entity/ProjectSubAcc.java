package com.yzc.common.deposit.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author DouQingRen
 * @since 2025-03-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("T_FNTDProjectSubAcc")
@ApiModel(value = "TFntdprojectsubacc对象", description = "")
public class ProjectSubAcc extends Model<ProjectSubAcc> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("ProjectSubAccId")
    private String projectSubAccId;

    /**
     * 项目id
     */
    @TableField("ProjectId")
    private String projectId;

    /**
     * 项目编号
     */
    @TableField("ProjectCode")
    private String projectCode;

    /**
     * 项目名称
     */
    @TableField("ProjectName")
    private String projectName;

    /**
     * 业务类型,同枚举
     */
    @TableField("BusinessType")
    private Integer businessType;

    /**
     * 子账户名称
     */
    @TableField("SubAccName")
    private String subAccName;

    /**
     * 申请状态(待定)
     */
    @TableField("ApplyStatus")
    private Integer applyStatus;

    /**
     * 申请时间
     */
    @TableField("ApplyTime")
    private Date applyTime;

    /**
     * 子账户(不含主账号)
     */
    @TableField("SubAcc")
    private String subAcc;

    /**
     * 开标时间
     */
    @TableField("OpenBidTime")
    private Date openBidTime;

    /**
     * 公司id
     */
    @TableField("CompanyId")
    private String companyId;

    /**
     * 公司名称
     */
    @TableField("CompanyName")
    private String companyName;
    /**
     * 是否确认中标人
     */
    @TableField("IsConfirmBidder")
    private Integer isConfirmBidder;

    /**
     * 确认中标时间
     */
    @TableField("ConfirmBidderTime")
    private Date confirmBidderTime;
    /**
     * 是否终止
     */
    @TableField("IsAbortive")
    private Integer isAbortive;
    /**
     * 备注
     */
    @TableField("Remarks")
    private String remarks;
    /**
     * 负责人id
     */
    @TableField("PurchaserId")
    private String purchaserId;
    /**
     * 负责人名称
     */
    @TableField("PurchaserName")
    private String purchaserName;

    /**
     * 是否确认候选人
     */
    @TableField("IsConfirmCandidate")
    private Integer isConfirmCandidate;

    /**
     * 确认候选人时间
     */
    @TableField("ConfirmCandidateTime")
    private Date confirmCandidateTime;

    /**
     * 是否自动退款
     */
    @TableField("IsAutoBackMoney")
    private Integer isAutoBackMoney;

    /**
     * 请求号
     */
    @TableField("ReqNo")
    private String reqNo;

    /**
     * 整个子账户(子账户全称)
     */
    @TableField("WholeSubAcc")
    private String wholeSubAcc;

    /**
     * 银行类型
     */
    @TableField("BankTypeCode")
    private Integer bankTypeCode;

    /**
     * 是否部分退款
     */
    @TableField("PartialRefundEnable")
    private Integer partialRefundEnable;

    /**
     * 公司id备份
     */
    @TableField("CompanyIdBak")
    private String companyIdBak;

    /**
     * 服务类型
     */
    @TableField("ServiceType")
    private Integer serviceType;

    /**
     * 执行部门id
     */
    @TableField("ExecuteDeptId")
    private String executeDeptId;

    /**
     * 执行部门名称
     */
    @TableField("ExecuteDeptName")
    private String executeDeptName;

    /**
     * 招标人id
     */
    @TableField("TenderId")
    private String tenderId;

    /**
     * 招标人名称
     */
    @TableField("TenderName")
    private String tenderName;

    /**
     * 删除状态
     */
    @TableField("DelStatus")
    private Integer delStatus;

    /**
     * 是否签约合同
     */
    @TableField("IsSignContract")
    private Integer isSignContract;

    /**
     * 项目唯一标识
     */
    @TableField("projectUniqueCode")
    private String projectUniqueCode;

    @Override
    public Serializable pkVal() {
        return this.projectSubAccId;
    }

}
