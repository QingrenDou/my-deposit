package com.yzc.common.deposit.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 绑定记录表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("T_FNTDBindRecord")
@ApiModel(value = "BindRecord对象", description = "")
public class BindRecord {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("bindRecordId")
    private Long bindRecordId;

    /**
     * 入账记录id
     */
    @TableField("inAccRecordId")
    private String inAccRecordId;
    /**
     * 项目唯一标识
     */
    @TableField("projectUniqueCode")
    private String projectUniqueCode;
    /**
     * 绑定状态（同枚举）
     */
    @TableField("bindStatus")
    private Integer bindStatus;

    /**
     * 绑定用户ID
     */
    @TableField("bindUserId")
    private String bindUserId;

    /**
     * 绑定用户姓名
     */
    @TableField("bindUserName")
    private String bindUserName;

    /**
     * 绑定时间
     */
    @TableField("bindTime")
    private Date bindTime;
}
