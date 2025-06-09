package com.yzc.common.deposit.dto.bank.cwHbky;

import lombok.Data;

/**
 * 查询入账数据 -入参
 */
@Data
public class EsbInAccReqDto {
    private String zhbh; //账号,格式：xxxxx
    private String sDate; //开始日期,格式：xxxx-xx-xx
    private String eDate; //结束日期,格式：xxxx-xx-xx
}
