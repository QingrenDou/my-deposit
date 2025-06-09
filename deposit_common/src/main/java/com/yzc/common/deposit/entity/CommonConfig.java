package com.yzc.common.deposit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

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
@TableName("T_FNTDCommonConfig")
@ApiModel(value = "T_FNTDCommonConfig对象", description = "")
public class CommonConfig extends Model<CommonConfig> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id，递增
     */
    @TableId(value = "Id", type = IdType.AUTO)
    private Integer id;

    @TableField("ConfigKey")
    private String configKey;

    @TableField("ConfigValue")
    private String configValue;

    @TableField("UseStatus")
    private Integer useStatus;

    @TableField("Remarks")
    private String remarks;

    @TableField("CreateTime")
    private Date createTime;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
