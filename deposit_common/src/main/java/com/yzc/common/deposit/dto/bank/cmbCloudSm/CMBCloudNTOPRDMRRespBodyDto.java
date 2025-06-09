package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * NTOPRDMR API Response Body DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CMBCloudNTOPRDMRRespBodyDto extends CMBCloudBaseRespDto {
    @JsonProperty("ntoprrtnz")
    private List<CMBCloudNTOPRDMRRespNtoprrtnzDto> ntoprrtnz;
}
