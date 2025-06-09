package com.yzc.common.guar.dto.xingtai;

import lombok.Data;

/**
 * 解密保函信息
 */
@Data
public class XingTaiDecGuarInfoDto {

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
