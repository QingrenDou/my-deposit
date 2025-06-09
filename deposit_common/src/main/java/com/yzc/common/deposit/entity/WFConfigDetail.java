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
@TableName("T_FNTDWFConfigDetail")
@ApiModel(value="WFConfigDetail对象", description="")
public class WFConfigDetail extends Model<WFConfigDetail> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
      @TableId("wfConfigDetailId")
    private Long wfConfigDetailId;

    @ApiModelProperty(value = "企业或子公司id")
    @TableField("companyId")
    private String companyId;

    @ApiModelProperty(value = "工作流类型")
    @TableField("wfType")
    private Integer wfType;

    @ApiModelProperty(value = "绑定流程id")
    @TableField("flowId")
    private String flowId;

    @ApiModelProperty(value = "流程名称")
    @TableField("flowName")
    private String flowName;

    @ApiModelProperty(value = "更新时间")
    @TableField("updateTime")
    private Date updateTime;


    @Override
    public Serializable pkVal() {
        return this.wfConfigDetailId;
    }

}
