package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * NTOPRDMR API Request DTO - Part 2
 */
@Data
public class CMBCloudNTOPRDMRReqNtoprdmrx2Dto {
    @JsonProperty("rpyadr")
    private String rpyadr; // 原收款方地址 Z(100), Y

    @JsonProperty("rpybkn")
    private String rpybkn; // 原收款方开户行名称 Z(62)

    @JsonProperty("rpybbn")
    private String rpybbn; // 原收款方开户行行号 String(20)
}
