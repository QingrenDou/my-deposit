package com.yzc.common.deposit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@TableName("T_FNTDInAccRecord")
@ApiModel(value="TFntdinaccrecord对象", description="")
public class InAccRecord extends Model<InAccRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
      @TableId("InAccRecordId")
    private String inAccRecordId;

      /**
     * 银行流水号
     */
    @TableField("BankSeqNo")
    private String bankSeqNo;

    /**
     * 来款银行编码
     */
    @TableField("FromBankCode")
    private String fromBankCode;

    /**
     * 来款银行名称
     */
    @TableField("FromBankName")
    private String fromBankName;

    /**
     * 来款账号
     */
    @TableField("FromAcc")
    private String fromAcc;

    /**
     * 来款账号名称
     */
    @TableField("FromAccName")
    private String fromAccName;

    /**
     * 入账主账号
     */
    @TableField("InMainAcc")
    private String inMainAcc;

    /**
     * 入账子账号
     */
    @TableField("InSubAcc")
    private String inSubAcc;

    /**
     * 入账子账号名称
     */
    @TableField("InSubAccName")
    private String inSubAccName;

    /**
     * 交易金额
     */
    @TableField("TradeMoney")
    private BigDecimal tradeMoney;

    /**
     * 交易日期
     */
    @TableField("TradeDay")
    private String tradeDay;

    /**
     * 交易时间
     */
    @TableField("TradeTime")
    private String tradeTime;

    /**
     * 附加信息
     */
    @TableField("AddedMsg")
    private String addedMsg;

    /**
     * 交易摘要
     */
    @TableField("TradeSummary")
    private String tradeSummary;

    /**
     * 创建时间
     */
    @TableField("CreateTime")
    private Date createTime;

    /**
     * 是否中标
     */
    @TableField("IsWinBidder")
    private Integer isWinBidder;

    /**
     * 退票操作类型
     */
    @TableField("BackOperType")
    private Integer backOperType;

    /**
     * 退款操作申请时间
     */
    @TableField("ApplyTime")
    private Date applyTime;

    /**
     * 申请人id
     */
    @TableField("ApplyUserId")
    private String applyUserId;

    /**
     * 申请人姓名
     */
    @TableField("ApplyUserName")
    private String applyUserName;

    /**
     * 申请人联系方式
     */
    @TableField("ApplyContactWay")
    private String applyContactWay;

    /**
     * 申请原因
     */
    @TableField("ApplyDesc")
    private String applyDesc;

    /**
     * 申请附件关联id
     */
    @TableField("ApplyAttRelaId")
    private String applyAttRelaId;

    /**
     * 审核状态
     */
    @TableField("AuditStatus")
    private Integer auditStatus;

    /**
     * 审核描述
     */
    @TableField("AuditDesc")
    private String auditDesc;

    /**
     * 审核时间
     */
    @TableField("AuditTime")
    private Date auditTime;

    /**
     * 审核人id
     */
    @TableField("AuditUserId")
    private String auditUserId;

    /**
     * 审核人姓名
     */
    @TableField("AuditUserName")
    private String auditUserName;

    /**
     * 受理时间
     */
    @TableField("AcceptTime")
    private Date acceptTime;

    /**
     * 受理人id
     */
    @TableField("AcceptUserId")
    private String acceptUserId;

    /**
     * 受理人姓名
     */
    @TableField("AcceptUserName")
    private String acceptUserName;

    /**
     * 转履约金额
     */
    @TableField("PerformanceMoney")
    private BigDecimal performanceMoney;
    /**
     * 转履约时间
     */
    @TableField("PerformanceTime")
    private Date performanceTime;

    /**
     * 转履约人id
     */
    @TableField("PerformanceUserId")
    private String performanceUserId;

    /**
     * 转履约人姓名
     */
    @TableField("PerformanceUserName")
    private String performanceUserName;

    /**
     * 余额(应退金额)
     */
    @TableField("BalanceMoney")
    private BigDecimal balanceMoney;

    /**
     * 扣款金额
     */
    @TableField("DeductMoney")
    private BigDecimal deductMoney;

    /**
     * 投标人id
     */
    @TableField("BidderId")
    private String bidderId;

    /**
     * 投标人姓名
     */
    @TableField("BidderName")
    private String bidderName;

    /**
     * 投标负责人id
     */
    @TableField("SignUpUserId")
    private String signUpUserId;

    /**
     * 投标负责人名称
     */
    @TableField("SignUpUserName")
    private String signUpUserName;

    /**
     * 投标负责人联系方式
     */
    @TableField("SignUpUserPhone")
    private String signUpUserPhone;

    /**
     * 是否候选人
     */
    @TableField("IsCandidate")
    private Integer isCandidate;

    /**
     * 扣款附件关联id
     */
    @TableField("DeductAttRelaId")
    private String deductAttRelaId;

    /**
     * 扣款附件保存文件名
     */
    @TableField("DeductSaveFileName")
    private String deductSaveFileName;

    /**
     * 扣款附件源文件名
     */
    @TableField("DeductSourceFileName")
    private String deductSourceFileName;

    /**
     * 服务费金额
     */
    @TableField("FeeMoney")
    private BigDecimal feeMoney;

    /**
     * 利息金额
     */
    @TableField("InterestMoney")
    private BigDecimal interestMoney;

    /**
     * 序列号
     */
    @TableField("SetNumber")
    private String setNumber;

    /**
     * 来款银行地址
     */
    @TableField("FromBankAddress")
    private String fromBankAddress;

    /**
     * 流程id
     */
    @TableField("FlowId")
    private String flowId;

    /**
     * 实例id
     */
    @TableField("GroupId")
    private String groupId;

    /**
     * 工作流审核状态
     */
    @TableField("WorkFlowAuditStatus")
    private Integer workFlowAuditStatus;

    /**
     * 中标金额
     */
    @TableField("WinAmount")
    private String winAmount;

    /**
     * 中标服务费金额
     */
    @TableField("FeeMoneyStr")
    private String feeMoneyStr;

    /**
     * 来款来源（同枚举）
     */
    @TableField("FromSource")
    private Integer fromSource;

    /**
     * 备注
     */
    @TableField("Remarks")
    private String remarks;

    /**
     * 是否需要扣款
     */
    @TableField("IsNeedDeduct")
    private Integer isNeedDeduct;

    /**
     * 保证金用途
     */
    @TableField("UseWay")
    private Integer useWay;

    /**
     * 冻结状态
     */
    @TableField("FrozenStatus")
    private Integer frozenStatus;

    /**
     * 冻结时间
     */
    @TableField("FrozenTime")
    private Date frozenTime;

    /**
     * 解冻时间
     */
    @TableField("ThawTime")
    private Date thawTime;

    /**
     *  咨询服务费金额
     */
    @TableField("ServiceFeeMoney")
    private BigDecimal serviceFeeMoney;

    /**
     * 会员级别
     */
    @TableField("MemberLevel")
    private String memberLevel;

    @TableField("MemberInvaidTime")
    private Date memberInvaidTime;
    @TableField("MemberSyncFrom")
    private Integer memberSyncFrom;
    @TableField("MemberSyncTime")
    private Date memberSyncTime;

    /**
     * 批量审核锁定状态
     */
    @TableField("AuditLockStatus")
    private Integer auditLockStatus;
    @TableField("AuditLockTime")
    private Date auditLockTime;

    @TableField("OutMoneyType")
    private Integer outMoneyType;

    /**
     * 人工解冻时间
     */
    @TableField("ManualUnlockTime")
    private Date manualUnlockTime;

    @TableField("DelStatus")
    private Integer delStatus;

    @Override
    public Serializable pkVal() {
        return this.inAccRecordId;
    }

}
