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
@TableName("T_FNTDBankConfig")
@ApiModel(value = "TFntdbankconfig对象", description = "")
public class BankConfig extends Model<BankConfig> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("BankConfigId")
    private Integer bankConfigId;

    /**
     * 银行类型编码
     */
    @TableField("BankTypeCode")
    private Integer bankTypeCode;

    /**
     * 银行ip
     */
    @TableField("BankIp")
    private String bankIp;

    /**
     * 银行端口
     */
    @TableField("BankPort")
    private String bankPort;

    /**
     * 客户id
     */
    @TableField("ConsumerId")
    private String consumerId;

    /**
     * 主账户
     */
    @TableField("MainAccount")
    private String mainAccount;

    /**
     * 主账户名称
     */
    @TableField("MainAccountName")
    private String mainAccountName;

    /**
     * 银行名称
     */
    @TableField("BankName")
    private String bankName;

    /**
     * 银行编码
     */
    @TableField("BankCode")
    private String bankCode;

    /**
     * 银行类型
     */
    @TableField("BankMold")
    private Integer bankMold;

    /**
     * 使用状态
     */
    @TableField("UseStatus")
    private Integer useStatus;

    /**
     * 删除状态
     */
    @TableField("DelStatus")
    private Integer delStatus;

    /**
     * 创建时间
     */
    @TableField("CreateTime")
    private Date createTime;

    /**
     * 银行http地址
     */
    @TableField("BankHttpUrl")
    private String bankHttpUrl;


    @Override
    public Serializable pkVal() {
        return this.bankConfigId;
    }

}
