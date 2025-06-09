package com.yzc.common.deposit.dto.deposit;

import lombok.Data;

/**
 * 流程类型
 */
@Data
public class WFTypeRespDto {

    /**
     * 主键id
     */
    private Long wfTypeId;

    /**
     * 流程类型
     */
    private Integer wfType;

    /**
     * 流程类型名称
     */
    private String wfTypeName;
}
