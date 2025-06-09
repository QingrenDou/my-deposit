package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import lombok.Data;

import java.util.List;

/**
 * 新增虚拟号-完整报文体
 */
@Data
public class CMBCloudQueryRecordTodayReqBodyDto extends CMBCloudCommonReqBodyDto {
    private List<CMBCloudQueryRecordTodayReqDto> ntdmtlsty; //报文体
}
