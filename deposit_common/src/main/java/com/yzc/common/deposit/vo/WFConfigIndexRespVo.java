package com.yzc.common.deposit.vo;

import lombok.Data;

import java.util.List;

/**
 * 工作流配置首页参数
 */
@Data
public class WFConfigIndexRespVo {

    /**
     * 是否使用子公司工作流[1启用]
     */
    private Integer wfChildCompanyEnable;

    /**
     * 子公司列表
     */
    private List<WFChildCompanyVo> childCompanyList;
}
