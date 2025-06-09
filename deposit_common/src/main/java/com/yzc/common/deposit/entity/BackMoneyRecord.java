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
@TableName("T_FNTDBackMoneyRecord")
@ApiModel(value="T_Fntdbackmoneyrecord对象", description="")
public class BackMoneyRecord extends Model<BackMoneyRecord> {

    private static final long serialVersionUID = 1L;

      @TableId("BackMoneyRecordId")
    private String backMoneyRecordId;

    @TableField("SubAcc")
    private String subAcc;

    @TableField("InBankSeqNo")
    private String inBankSeqNo;

    @TableField("ApplyType")
    private String applyType;

    @TableField("ApplyMoney")
    private BigDecimal applyMoney;

    @TableField("ApplyAddedMsg")
    private String applyAddedMsg;

    @TableField("ApplyDesc")
    private String applyDesc;

    @TableField("ApplySeqNo")
    private String applySeqNo;

    @TableField("ApplyFrom")
    private Integer applyFrom;

    @TableField("ApplyTime")
    private Date applyTime;

    @TableField("ApplyUserId")
    private String applyUserId;

    @TableField("ApplyUserName")
    private String applyUserName;

    @TableField("Accrual")
    private String accrual;

    @TableField("ServiceFee")
    private String serviceFee;

    @TableField("BackRst")
    private String backRst;

    @TableField("BackRstExplain")
    private String backRstExplain;

    @TableField("BackRstDesc")
    private String backRstDesc;

    @TableField("ReqNo")
    private String reqNo;

    @TableField("BankTypeCode")
    private Integer bankTypeCode;

    @TableField("DelStatus")
    private Integer delStatus;

    @Override
    public Serializable pkVal() {
        return this.backMoneyRecordId;
    }

}
