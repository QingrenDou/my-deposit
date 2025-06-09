package com.yzc.common.deposit.dto.deposit;

import lombok.Data;

import java.util.Date;

/**
 * 流程配置信息
 */
@Data
public class WFConfigRespDto {
    /**
     * 主键id
     */
    private Long wfConfigId;

    /**
     * 企业或子公司id
     */
    private String companyId;

    /**
     * 工作流类型
     */
    private Integer wfType;

    /**
     * 启用状态
     */
    private Integer startStatus;

    /**
     * 更新用户id
     */
    private String updateUserId;

    /**
     * 更新用户名称
     */
    private String updateUserName;


    /**
     * 更新时间
     */
    private Date updateTime;
}
