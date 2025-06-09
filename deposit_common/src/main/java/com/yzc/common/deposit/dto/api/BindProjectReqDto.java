package com.yzc.common.deposit.dto.api;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 项目绑定接口-入参
 */
@Data
public class BindProjectReqDto {

    /**
     * 银行流水号
     */
    @NotBlank(message = "银行流水号不能为空")
    private String bankSeqNo;

    /**
     * 绑定用户ID
     */
    private String bindUserId;

    /**
     * 绑定用户姓名
     */
    private String bindUserName;

    /**
     * 绑定投标人id
     */
    private String bindBidderId;

    /**
     * 绑定投标人名称
     */
    private String bindBidderName;

    /****************项目信息**********************************************/

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 项目编号[招标项目编号/标段编号]
     */
    @NotBlank(message = "项目编号不能为空")
    private String projectCode;

    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空")
    private String projectName;

    /**
     * 业务类型,同枚举
     */
    @NotBlank(message = "业务类型不能为空")
    private Integer businessType;

    /**
     * 开标时间(yyyy-MM-dd HH:mm:ss)
     */
    private String openBidTimeStr;

    /**
     * 公司id
     */
    @NotBlank(message = "公司id不能为空")
    private String companyId;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 负责人id
     */
    @NotBlank(message = "负责人id不能为空")
    private String purchaserId;
    /**
     * 负责人名称
     */
    private String purchaserName;

    /**
     * 保证金账号
     */
    @NotBlank(message = "保证金账号不能为空")
    private String inSubAcc;
}
