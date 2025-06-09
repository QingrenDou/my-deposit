package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import lombok.Data;

import java.util.List;

/**
 * 新增虚拟号-完整报文体
 */
@Data
public class CMBCloudApplyBackMoneyReqBodyDto extends CMBCloudCommonReqBodyDto {
    private List<CMBCloudBusModyReqDto> ntbusmody;
    private List<CMBCloudApplyBackMoney1Dto> ntoprdmrx1; //报文体1
    private List<CMBCloudApplyBackMoney2Dto> ntoprdmrx2; //报文体1
    private List<CMBCloudApplyBackMoney3Dto> ntoprdmrx3; //报文体1
}
