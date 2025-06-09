package com.yzc.common.deposit.dto.deposit;

import lombok.Data;

import java.util.Date;

/**
 * 申请记录保存请求参数
 */
@Data
public class ApplyRefundRecordSaveReqDto {
    /**
     * 主键id
     */
    private String applyRecordId;

    /**
     * 申请时间
     */
    private Date applyTime;

    /**
     * 申请人
     */
    private String applyUserName;

    /**
     * 业务单号
     */
    private String docNumber;

    /**
     * 流程ID
     */
    private String flowId;

    /**
     * 示例ID
     */
    private String groupId;

    /**
     * 工作流审核状态
     */
    private Integer workFlowAuditStatus;

    /**
     * 申请人ID
     */
    private String applyUserId;

    /**
     * 保证金子账户
     */
    private String inSubAcc;
}
