package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * NTOPRDMR API Request DTO - Part 3
 */
@Data
public class CMBCloudNTOPRDMRReqNtoprdmrx3Dto {
    @JsonProperty("intprt")
    private String intprt; // 利息收取方式 String(1)

    @JsonProperty("dmrprt")
    private String dmrprt; // 补贴息收取方式 String(1)
}
