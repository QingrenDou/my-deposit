package com.yzc.common.deposit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
@TableName("T_FNTDApplyRefundRecordRelated")
@ApiModel(value="T_FNTDApplyRefundRecordRelated对象", description="")
public class ApplyRefundRecordRelated extends Model<ApplyRefundRecordRelated> {

    private static final long serialVersionUID = 1L;

    @TableId("Id")
    private String id;

    @TableField("ApplyRefundRecordId")
    private String applyRefundRecordId;

    @TableField("InAccRecordId")
    private String inAccRecordId;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
