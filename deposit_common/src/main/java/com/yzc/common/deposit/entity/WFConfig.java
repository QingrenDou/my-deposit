package com.yzc.common.deposit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author DouQingRen
 * @since 2025-03-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("T_FNTDWFConfig")
@ApiModel(value="WFConfig对象", description="")
public class WFConfig extends Model<WFConfig> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
      @TableId("wfConfigId")
    private Long wfConfigId;

    @ApiModelProperty(value = "企业或子公司id")
    @TableField("companyId")
    private String companyId;

    @ApiModelProperty(value = "工作流类型")
    @TableField("wfType")
    private Integer wfType;

    @ApiModelProperty(value = "启用状态，可用枚举：UseStatusEnum")
    @TableField("startStatus")
    private Integer startStatus;

    @ApiModelProperty(value = "最后更新人id")
    @TableField("updateUserId")
    private String updateUserId;

    @ApiModelProperty(value = "最后更新人名称")
    @TableField("updateUserName")
    private String updateUserName;

    @ApiModelProperty(value = "最后更新时间")
    @TableField("updateTime")
    private Date updateTime;


    @Override
    public Serializable pkVal() {
        return this.wfConfigId;
    }

}
