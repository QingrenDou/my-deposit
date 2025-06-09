package com.yzc.common.guar.dto.common;

import lombok.Data;

/**
 * 解密电子保函-信息
 */
@Data
public class DecGuarInfoDto {
    /**
     * 保函申请编号
     */
    private String lgNo;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 统一社会信用代码
     */
    private String creditCode;
}
