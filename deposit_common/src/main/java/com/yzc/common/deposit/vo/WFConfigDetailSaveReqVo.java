package com.yzc.common.deposit.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 流程配置提交请求参数
 */
@Data
public class WFConfigDetailSaveReqVo {

    /**
     * 企业或子公司id
     */
    @NotBlank(message = "企业或子公司id不能为空")
    private String companyId;

    /**
     * 工作流类型
     */
    @NotNull(message = "工作流类型不能为空")
    private Integer wfType;

    /**
     * 启用状态[1启用，0停用，null 未绑定]
     */
    @NotNull(message = "启用状态不能为空")
    private Integer startStatus;

    /**
     * 已选流程集合
     */
    @NotNull(message = "工作流不能为空")
    private List<WFInfoVo> chooseFlowInfoList;
}
