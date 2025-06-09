package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import lombok.Data;

import java.util.List;

/**
 * 新增虚拟号-完整报文体
 */
@Data
public class CMBCloudQueryRecordHisReqBodyDto extends CMBCloudCommonReqBodyDto {
    private List<CMBCloudQueryRecordHisReqDto> ntdmthlsy; //报文体
}
