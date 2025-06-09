package com.yzc.common.guar.entity;

import java.math.BigDecimal;

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
 * 保函支付详情
 * </p>
 *
 * @author DouQingRen
 * @since 2025-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_guar_pay")
@ApiModel(value = "GuarPay对象", description = "保函支付详情")
public class GuarPay extends Model<GuarPay> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id，自增长")
    @TableId(value = "guar_pay_id", type = IdType.AUTO)
    private Integer guarPayId;

    @ApiModelProperty(value = "保函申请编号")
    private String lgNo;

    @ApiModelProperty(value = "申请单位名称")
    private String enterpriseName;

    @ApiModelProperty(value = "申请企业社会统一信用代码")
    private String creditCode;

    @ApiModelProperty(value = "支付状态00=成功03=失败05=订单关闭")
    private String payStatus;

    @ApiModelProperty(value = "瀚华支付流水主键")
    private String paymentId;

    @ApiModelProperty(value = "第三方流水编号")
    private String paymentNo;

    @ApiModelProperty(value = "订单金额，单位为元，精确到分")
    private BigDecimal orderAmt;

    @ApiModelProperty(value = "支付金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "账户名称")
    private String accountName;

    @ApiModelProperty(value = "账户号")
    private String accountNo;

    @ApiModelProperty(value = "分支机构")
    private String branchName;

    @ApiModelProperty(value = "支付渠道")
    private String payChnl;

    @ApiModelProperty(value = "支付方式")
    private String payWay;

    @ApiModelProperty(value = "是否使用了优惠 0=使用 1=不使用")
    private Integer isCoupon;

    @ApiModelProperty(value = "优惠金额，单位为元，精确到分")
    private BigDecimal reduceAmt;

    @ApiModelProperty(value = "支付成功时间")
    private String acctTime;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "最后更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "删除状态(1删除 0正常)")
    private Integer delStatus;


    @Override
    public Serializable pkVal() {
        return this.guarPayId;
    }

}
