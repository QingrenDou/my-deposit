package com.yzc.common.deposit.dto.bank.common;

import lombok.Data;

@Data
public class GetSubAccByReqNoReqDto {

    /**
     * 请求号
     */
    private String reqNo;

    /**
     * 银行类型编码
     */
    private String bankTypeCode;
}
