package com.yzc.common.deposit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 银行秘钥表
 * </p>
 *
 * @author DouQingRen
 * @since 2025-06-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("T_FNTDBankKey")
@ApiModel(value="BankKey对象", description="银行秘钥表")
public class BankKey extends Model<BankKey> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
      private Integer id;

    @ApiModelProperty(value = "银行类型")
    @TableField("bankTypeCode")
    private Integer bankTypeCode;

    @ApiModelProperty(value = "优质采私钥")
    @TableField("yzcPrivateKey")
    private String yzcPrivateKey;

    @ApiModelProperty(value = "优质采公钥")
    @TableField("yzcPublicKey")
    private String yzcPublicKey;

    @ApiModelProperty(value = "银行公钥")
    @TableField("bankPublicKey")
    private String bankPublicKey;

    @ApiModelProperty(value = "秘钥")
    private String cipher;

    @ApiModelProperty(value = "创建时间")
    @TableField("CreateTime")
    private Date CreateTime;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
