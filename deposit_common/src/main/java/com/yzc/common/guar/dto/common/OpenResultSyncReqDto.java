package com.yzc.common.guar.dto.common;

import lombok.Data;

/**
 * 统一开函结果通知-入参
 */
@Data
public class OpenResultSyncReqDto {

    //投标申请编号
    private String lgNo;

    //开函状态（2-通过，3-未通过）
    private String status;

    //（status=3时给出未通过原因，其他时为空）
    private String auditOpinion;

    //保函文件url（status=2时才返回）
    private String guaranteeFileUrl;

    //保函编号（用于在瀚华官网查询保函真实性）（status=2时才返回）
    private String guaranteeNumber;

    //保函代码（用于在瀚华官网查询保函真实性）（status=2时才返回）
    private String guaranteeCode;

    //出函时间（yyyy-MM-dd）（status=2时才返回）
    private String createTime;

    //电子保函有效期起始时间（yyyy-MM-dd）（status=2时才返回）
    private String letterExpireStartTime;

    //电子保函有效期截止时间（yyyy-MM-dd）（status=2时才返回）
    private String letterExpireEndTime;

    //费率(小数，如：0.01) （status=2时才返回）
    private String rate;

}
