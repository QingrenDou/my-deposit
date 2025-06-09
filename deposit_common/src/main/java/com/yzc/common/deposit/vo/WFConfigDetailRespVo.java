package com.yzc.common.deposit.vo;

import lombok.Data;

import java.util.List;

/**
 * 工作流配置明细
 */
@Data
public class WFConfigDetailRespVo {

    /**
     * 启用状态[1启用，0停用，null 未绑定]
     */
    private Integer startStatus;

    /**
     * 已配置流程集合
     */
    private List<WFInfoVo> configFlowInfoList;

    /**
     * 企业所有流程集合
     */
    private List<WFInfoVo> allFlowInfoList;
}
