package com.yzc.common.guar.dto.hanhua;

import lombok.Data;

import java.io.Serializable;

/**
 * @description 申请保函-请求
 * @author douqr 2021/6/24 16:09
 */
@Data
public class ApplyHanHuaGuarReqDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String businessType; //业务类型（01 投标保函，02 政采保函，03履约保函）
    private Integer financialId; //开函机构(1:瀚华担保,2:富民银行,3:徽商银行)
    private String enterpriseName; //企业名称
    private String creditCode; //统一社会信用代码
    private String legalName; //法定代表人
    private String certType; //法定代表人证件类型，默认1(1:身份证 2:护照 3:军官证 4:组织机构代码 5:社会信用代码)
    private String legalCertNo; //法定代表人证件号码
    private String lgNo; //保函申请编号
    private String tenderee; //招标人/受益人（加密函传入MD5[32位]加密信息）
    private String bidNo; //标段代码（加密函传入MD5[32位]加密信息）
    private String bidName; //标段名称（加密函传入MD5[32位]加密信息）
    private String guaranteeAmount; //保函申请金额（单位：元，精确到分）
    private String payAmount; //保函支付金额（单位：元，精确到分），非瀚华支付必传
    private String operatorName; //经办人姓名
    private String operatorCertNo; //经办人身份证号码
    private String operatorPhone; //经办人手机号

    public String ensureIsEncryp;//是否加密函：1加密函 0明文函  ---2025-05-29 新增

    private ApplyGuarReqExtInfoDto extInfo; //附加信息
}
