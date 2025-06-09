package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * NTOPRDMR API Request Body DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CMBCloudNTOPRDMRReqBodyDto extends CMBCloudCommonReqBodyDto {
    @JsonProperty("ntbusmody")
    private List<CMBCloudBusModyReqDto> ntbusmody;

    @JsonProperty("ntoprdmrx1")
    private List<CMBCloudNTOPRDMRReqNtoprdmrx1Dto> ntoprdmrx1;

    @JsonProperty("ntoprdmrx2")
    private List<CMBCloudNTOPRDMRReqNtoprdmrx2Dto> ntoprdmrx2;

    @JsonProperty("ntoprdmrx3")
    private List<CMBCloudNTOPRDMRReqNtoprdmrx3Dto> ntoprdmrx3;
}
