package com.yzc.common.guar.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 电子保函机构配置表
 * </p>
 *
 * @author DouQingRen
 * @since 2025-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_guar_type_config")
@ApiModel(value = "GuarTypeConfig对象", description = "电子保函机构配置表")
public class GuarTypeConfig extends Model<GuarTypeConfig> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id，自增")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "电子保函类型（1瀚华2兴泰3国控 同枚举）")
    private Integer guarTypeCode;

    @ApiModelProperty(value = "出函机构")
    private String financeOrgName;

    @ApiModelProperty(value = "收款方")
    private String payeeName;

    @ApiModelProperty(value = "接口url")
    private String apiUrl;

    @ApiModelProperty(value = "删除状态（0正常 1已删除）")
    private Integer delStatus;

    @ApiModelProperty(value = "对接金融机构（1瀚华 2兴泰，同枚举）")
    private Integer financeMode;

    @ApiModelProperty(value = "启用状态（-1未启用 1启用 2调试，同枚举）")
    private Integer enableStatus;

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
