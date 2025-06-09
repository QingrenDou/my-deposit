package com.yzc.common.deposit.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class InAccRecordRespVo {

    /**************************入账数据**************************************************/
    /**
     * 主键id
     */
    private String inAccRecordId;

    /**
     * 银行流水号
     */
    private String bankSeqNo;

    /**
     * 来款银行编码
     */
    private String fromBankCode;

    /**
     * 来款银行名称
     */
    private String fromBankName;

    /**
     * 来款账号
     */
    private String fromAcc;

    /**
     * 来款账号名称
     */
    private String fromAccName;

    /**
     * 入账主账号
     */
    private String inMainAcc;

    /**
     * 入账子账号
     */
    private String inSubAcc;

    /**
     * 入账子账号名称
     */
    private String inSubAccName;

    /**
     * 交易金额
     */
    private BigDecimal tradeMoney;

    /**
     * 交易日期
     */
    private String tradeDay;

    /**
     * 交易时间
     */
    private String tradeTime;

    /**
     * 附加信息
     */
    private String addedMsg;

    /**
     * 交易摘要
     */
    private String tradeSummary;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否中标
     */
    private Integer isWinBidder;

    /**
     * 退票操作类型
     */
    private Integer backOperType;

    /**
     * 退款操作申请时间
     */
    private Date applyTime;

    /**
     * 申请人id
     */
    private String applyUserId;

    /**
     * 申请人姓名
     */
    private String applyUserName;

    /**
     * 申请人联系方式
     */
    private String applyContactWay;

    /**
     * 申请原因
     */
    private String applyDesc;

    /**
     * 申请附件关联id
     */
    private String applyAttRelaId;

    /**
     * 审核状态
     */
    private Integer auditStatus;

    /**
     * 审核描述
     */
    private String auditDesc;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 审核人id
     */
    private String auditUserId;

    /**
     * 审核人姓名
     */
    private String auditUserName;

    /**
     * 受理时间
     */
    private Date acceptTime;

    /**
     * 受理人id
     */
    private String acceptUserId;

    /**
     * 受理人姓名
     */
    private String acceptUserName;

    /**
     * 转履约金额
     */
    private BigDecimal performanceMoney;
    /**
     * 转履约时间
     */
    private Date performanceTime;

    /**
     * 转履约人id
     */
    private String performanceUserId;

    /**
     * 转履约人姓名
     */
    private String performanceUserName;

    /**
     * 余额(应退金额)
     */
    private BigDecimal balanceMoney;

    /**
     * 扣款金额
     */
    private BigDecimal deductMoney;

    /**
     * 投标人id
     */
    private String bidderId;

    /**
     * 投标人姓名
     */
    private String bidderName;

    /**
     * 投标负责人id
     */
    private String signUpUserId;

    /**
     * 投标负责人名称
     */
    private String signUpUserName;

    /**
     * 投标负责人联系方式
     */
    private String signUpUserPhone;

    /**
     * 是否候选人
     */
    private Integer isCandidate;

    /**
     * 扣款附件关联id
     */
    private String deductAttRelaId;

    /**
     * 扣款附件保存文件名
     */
    private String deductSaveFileName;

    /**
     * 扣款附件源文件名
     */
    private String deductSourceFileName;

    /**
     * 中标服务费金额(淮矿版)
     */
    private BigDecimal feeMoney;

    /**
     * 利息金额
     */
    private BigDecimal interestMoney;


    /**
     * 来款银行地址
     */
    private String fromBankAddress;

    /**
     * 流程id
     */
    private String flowId;

    /**
     * 实例id
     */
    private String groupId;

    /**
     * 工作流审核状态
     */
    private Integer workFlowAuditStatus;

    /**
     * 中标金额
     */
    private String winAmount;

    /**
     * 中标服务费金额(文本-宁电使用)
     */
    private String feeMoneyStr;

    /**
     * 来款来源（同枚举）
     */
    private Integer fromSource;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 是否需要扣款
     */
    private Integer isNeedDeduct;

    /**
     * 保证金用途
     */
    private Integer useWay;

    /**
     * 冻结状态
     */
    private Integer frozenStatus;

    /**
     * 冻结时间
     */
    private Date frozenTime;

    /**
     * 解冻时间
     */
    private Date thawTime;

    /**
     * 咨询服务费金额
     */
    private BigDecimal serviceFeeMoney;

    /**************************项目数据**************************************************/
    /**
     * 是否确认中标人
     */
    private Integer isConfirmBidder;

    /**
     * 确认中标时间
     */
    private Date confirmBidderTime;
    /**
     * 是否终止
     */
    private Integer isAbortive;

    /**
     * 是否确认候选人
     */
    private Integer isConfirmCandidate;

    /**
     * 确认候选人时间
     */
    private Date confirmCandidateTime;


    /*************************自定义属性***********************************/

    /**
     * 是否能退款【参考平台退款逻辑】 1：能，0：不能
     */
    private Integer isCanBackMoney;

    /**
     * 不能退款原因
     */
    private String cannotBackMoneyReason;

    /**
     * 申请状态【-1审核不通过， 0未提交 1已提交 2审核中】
     */
    private Integer applyStatus;

    /**
     * 来款日期显示
     */
    private String tradeDayStr;

    /*************************自定义方法**********************************/

}
