package com.yzc.common.deposit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
@TableName("T_FNTDApplyRefundRecord")
@ApiModel(value="T_FNTDApplyRefundRecord对象", description="")
public class ApplyRefundRecord extends Model<ApplyRefundRecord> {

    private static final long serialVersionUID = 1L;

      @TableId("ApplyRecordID")
    private String applyRecordId;

      /**
     * 申请时间
     */
    @TableField("ApplyTime")
    private Date applyTime;

    /**
     * 申请人
     */
    @TableField("ApplyUserName")
    private String applyUserName;

    /**
     * 业务单号
     */
    @TableField("DocNumber")
    private String docNumber;

    /**
     * 流程ID
     */
    @TableField("FlowId")
    private String flowId;

    /**
     * 示例ID
     */
    @TableField("GroupId")
    private String groupId;

    /**
     * 工作流审核状态
     */
    @TableField("WorkFlowAuditStatus")
    private Integer workFlowAuditStatus;

    /**
     * 申请人ID
     */
    @TableField("ApplyUserId")
    private String applyUserId;

    /**
     * 保证金子账户
     */
    @TableField("InSubAcc")
    private String inSubAcc;

    @Override
    public Serializable pkVal() {
        return this.applyRecordId;
    }

}
