package com.yzc.common.guar.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 申请索赔
 */
@Data
public class ApplyCompensationReqVo {
    /**
     * 保函主键
     */
    @NotNull(message = "保函id不能为空")
    private Long guarInfoId;

    /**
     * 理赔金额
     */
    @NotNull(message = "理赔金额不能为空")
    private BigDecimal compensateMoney; //索赔金额

    /**
     * 联系人姓名
     */
    @NotBlank(message = "联系人姓名不能为空")
    private String compensateContactName; //索赔联系人
    /**
     * 联系人手机号
     */
    @NotBlank(message = "联系人手机号不能为空")
    private String compensateContactPhone; //索赔联系方式

    /**
     * 扣款原因
     */
    @NotBlank(message = "扣款原因不能为空")
    private String compensateReason; //扣款原因

    /**
     * 理赔收款账号
     */
    @NotBlank(message = "理赔收款账号不能为空")
    private String compensateAccNo; //索赔收款账号

    private String compensateAttRelaId; //索赔文件关联id
}
