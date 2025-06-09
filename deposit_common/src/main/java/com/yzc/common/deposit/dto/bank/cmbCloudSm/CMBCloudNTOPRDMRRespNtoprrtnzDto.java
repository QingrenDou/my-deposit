package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * NTOPRDMR API Response DTO - Ntoprrtnz Part
 */
@Data
public class CMBCloudNTOPRDMRRespNtoprrtnzDto {
    @JsonProperty("sqrnbr")
    private String sqrnbr; // 流程实例号 String(10)

    @JsonProperty("reqnbr")
    private String reqnbr; // 业务请求号 String(10), Y

    @JsonProperty("reqsts")
    private String reqsts; // 请求状态 String(3), Y

    @JsonProperty("oprsqn")
    private String oprsqn; // 经办序号 String(3)

    @JsonProperty("oprals")
    private String oprals; // 经办别名 Z(32)

    @JsonProperty("rtnflg")
    private String rtnflg; // 处理结果 String(1)

    @JsonProperty("errcod")
    private String errcod; // 错误码 String(7), Y

    @JsonProperty("errtxt")
    private String errtxt; // 错误文本 Z(92)
}
