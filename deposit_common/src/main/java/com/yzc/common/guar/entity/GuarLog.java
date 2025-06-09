package com.yzc.common.guar.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 日志表
 * </p>
 *
 * @author DouQingRen
 * @since 2025-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_guar_log")
@ApiModel(value = "GuarLog对象", description = "日志表")
public class GuarLog extends Model<GuarLog> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键，自增")
    @TableId(value = "log_id", type = IdType.AUTO)
    private Long logId;

    @ApiModelProperty(value = "日志编码")
    private String logCode;

    @ApiModelProperty(value = "日志内容")
    private String logContent;

    @ApiModelProperty(value = "记录时间")
    private Date logTime;


    @Override
    public Serializable pkVal() {
        return this.logId;
    }

}
