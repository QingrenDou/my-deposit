package com.yzc.common.deposit.dto.deposit;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

@Data
public class ProjectSubAccRespDto {
    /**
     * 主键id
     */
    private String projectSubAccId;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 项目编号
     */
    private String projectCode;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 业务类型,同枚举
     */
    private Integer businessType;

    /**
     * 子账户名称
     */
    private String subAccName;

    /**
     * 申请状态(待定)
     */
    private Integer applyStatus;

    /**
     * 申请时间
     */
    private Date applyTime;

    /**
     * 子账户(不含主账号)
     */
    private String subAcc;

    /**
     * 开标时间
     */
    private Date openBidTime;

    /**
     * 公司id
     */
    private String companyId;

    /**
     * 公司名称
     */
    private String companyName;
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
     * 备注
     */
    private String remarks;
    /**
     * 负责人id
     */
    private String purchaserId;
    /**
     * 负责人名称
     */
    private String purchaserName;

    /**
     * 是否确认候选人
     */
    private Integer isConfirmCandidate;

    /**
     * 确认候选人时间
     */
    private Date confirmCandidateTime;

    /**
     * 是否自动退款
     */
    private Integer isAutoBackMoney;

    /**
     * 请求号
     */
    private String reqNo;

    /**
     * 整个子账户(子账户全称)
     */
    private String wholeSubAcc;

    /**
     * 银行类型
     */
    private Integer bankTypeCode;

    /**
     * 是否部分退款
     */
    private Integer partialRefundEnable;

    /**
     * 公司id备份
     */
    private String companyIdBak;

    /**
     * 服务类型
     */
    private Integer serviceType;

    /**
     * 执行部门id
     */
    private String executeDeptId;

    /**
     * 执行部门名称
     */
    private String executeDeptName;

    /**
     * 招标人id
     */
    private String tenderId;

    /**
     * 招标人名称
     */
    private String tenderName;

    /**
     * 删除状态
     */
    private Integer delStatus;

    /**
     * 是否签约合同
     */
    private Integer isSignContract;

    /**
     * 项目唯一标识
     */
    private String projectUniqueCode;
}
