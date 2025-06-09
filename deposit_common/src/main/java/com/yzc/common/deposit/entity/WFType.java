package com.yzc.common.deposit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
@TableName("T_FNTDWFType")
@ApiModel(value="WFType对象", description="")
public class WFType extends Model<WFType> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
      @TableId("wfTypeId")
    private Long wfTypeId;

    @ApiModelProperty(value = "工作流类型")
    @TableField("wfType")
    private Integer wfType;

    @ApiModelProperty(value = "业务流程名称")
    @TableField("wfTypeName")
    private String wfTypeName;


    @Override
    public Serializable pkVal() {
        return this.wfTypeId;
    }

}
