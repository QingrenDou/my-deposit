package com.yzc.common.deposit.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ApplyRefundAndInAccListRespVo {
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

    /*******************入账数据***********************************/

    /**
     * 当前申请关联的入账数据
     */
    private List<InAccRecordRespVo> inAccRecordList;
}
