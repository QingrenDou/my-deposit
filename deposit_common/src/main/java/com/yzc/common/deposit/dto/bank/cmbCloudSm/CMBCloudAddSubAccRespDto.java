package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import lombok.Data;

/**
 * 申请虚拟号-响应报文
 */
@Data
public class CMBCloudAddSubAccRespDto {
    private String dmanbr;//记账子单元编号 String(20)
    private String rtnsts;//业务处理结果 String(1)
    private String errcod;//错误码 String(7)
    private String errtxt;//错误信息 Z(192)
}
