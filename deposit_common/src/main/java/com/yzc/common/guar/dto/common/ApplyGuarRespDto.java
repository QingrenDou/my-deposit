package com.yzc.common.guar.dto.common;

import lombok.Data;

/**
 * 申请保函-出参
 */
@Data
public class ApplyGuarRespDto {

    /**
     * 出函机构
     */
    private String financeOrgName;

    /**
     * 收款方
     */
    private String payeeName;

    /**
     * 保函类型-该字段略有尴尬，根据guarTypeCode判断
     */
    private Integer financialId;
}
