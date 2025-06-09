package com.yzc.common.deposit.vo;

import lombok.Data;

import java.util.Date;

@Data
public class WFConfigVo {

    /**
     * 工作流类型
     */
    private Integer wfType;

    /**
     * 工作流类型名称
     */
    private String wfTypeName;

    /**
     * 主键id【id为空，则未绑定】
     */
    private Long wfConfigId;

    /**
     * 企业或子公司id
     */
    private String companyId;

    /**
     * 启用状态[1启用，0停用，null 未绑定]
     */
    private Integer startStatus;

    /**
     * 最后更新人id
     */
    private String updateUserId;

    /**
     * 最后更新人名称
     */
    private String updateUserName;

    /**
     * 最后更新时间
     */
    private Date updateTime;
}
