package com.yzc.common.guar.dto.common;

import lombok.Data;

/**
 * 刷新保函信息-请求参数
 */
@Data
public class QueryGuarReqDto {
    /**
     * 【必填项】电子保函类型（1瀚华2兴泰3国控 同枚举）
     */
    private Integer guarTypeCode;

    private String lgNo; //保函申请编号
}
