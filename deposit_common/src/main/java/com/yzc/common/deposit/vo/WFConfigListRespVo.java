package com.yzc.common.deposit.vo;

import lombok.Data;

import java.util.List;

/**
 * 获取流程配置列表-响应
 */
@Data
public class WFConfigListRespVo {

    /**
     * 流程配置列表
     */
    private List<WFConfigVo> configList;
}
