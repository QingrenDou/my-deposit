package com.yzc.common.deposit.dto.deposit;

import lombok.Data;

import java.util.Date;

/**
 * 工作流配置明细
 */
@Data
public class WFConfigDetailRespDto {

    /**
     * 工作流配置明细id
     */
    private Long wfConfigDetailId;

    /**
     * 企业或子公司id
     */
    private String companyId;

    /**
     * 工作流类型
     */
    private Integer wfType;

    /**
     * 绑定流程id
     */
    private String flowId;

    /**
     * 流程名称
     */
    private String flowName;

    /**
     * 更新时间
     */
    private Date updateTime;

}
