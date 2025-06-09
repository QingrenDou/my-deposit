package com.yzc.common.guar.dto.common;

import lombok.Data;

/**
 * (单笔)申请退保请求参数
 */
@Data
public class ApplyCloseReqDto {
    /**
     * 【必填项】电子保函类型（1瀚华2兴泰3国控 同枚举）
     */
    private Integer guarTypeCode;

    //项目信息
    private String bidNo;//标段编号，开标前密文，开标后明文
    private String bidName;//标段名称，开标前密文，开标后明文
    private String guaranteeNumber;//保函编号(只有开函成功)
    private String operatorPhone;//经办人手机号

    //关键信息
    private String lgNo;//保函申请编号
    private String memo;//退保原因说明
}
