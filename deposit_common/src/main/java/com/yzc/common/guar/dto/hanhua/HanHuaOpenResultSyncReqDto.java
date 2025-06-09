package com.yzc.common.guar.dto.hanhua;

import lombok.Data;

/**
 * 瀚华开函状态通知-入参
 */
@Data
public class HanHuaOpenResultSyncReqDto {
    private String lgNo; //投标申请编号
    private String status; //开函状态（2-通过，3-未通过）
    private String auditOpinion; //（status=3时给出未通过原因，其他时为空）
    private String guaranteeFileUrl; //保函文件url（status=2时才返回）
    private String guaranteeNumber; //保函编号（用于在瀚华官网查询保函真实性）（status=2时才返回）
    private String guaranteeCode; //保函代码（用于在瀚华官网查询保函真实性）（status=2时才返回）
    private String creditCode; //统一社会信用码（status=2时才返回）
    private String enterpriseName; //投保企业名称（status=2时才返回）
    private String createTime; //出函时间（yyyy-MM-dd）（status=2时才返回）
    private String guaranteeAmount; //担保金额（status=2时才返回）
    private String letterExpireStartTime; //电子保函有效期起始时间（yyyy-MM-dd）（status=2时才返回）
    private String letterExpireEndTime; //电子保函有效期截止时间（yyyy-MM-dd）（status=2时才返回）
    private String rate; //费率(小数，如：0.01) （status=2时才返回）
    private String payAmount; //付款金额（status=2时才返回）（单位：元，精确到分）
    private String transactor; //保函办理人
    private String transactorNumber; //保函办理人联系电话
    private String ensureType; //保函类型（bank：银行保函,company：担保保函，insurance：保险保函）（status=2时才返回）
    private String ensureIsEncryp; //是否加密函（1=加密函 0=明文函）（status=2时才返回）
    private String orgName; //金融机构名称
    private String bidNo; //标段代码
    private String bidName; //标段名称
}
