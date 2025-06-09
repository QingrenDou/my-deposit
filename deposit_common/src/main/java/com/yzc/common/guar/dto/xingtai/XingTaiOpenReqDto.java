package com.yzc.common.guar.dto.xingtai;

import com.yzc.common.guar.dto.common.GuarFileListDto;
import lombok.Data;

import java.util.List;

/**
 * 兴泰-开函入参
 */
@Data
public class XingTaiOpenReqDto {

    /**
     * 业务类型（01 投标保函，02 政采保函，03 履约保函）
     * 统一枚举：YzcGuarBusinessTypeEnum
     */
    private String businessType;

    /**
     * 开函机构(4.兴泰 5. 国控),优质采保函枚举：YzcGuarFinancialTypeEnum
     */
    private int financialId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 统一社会信用代码
     */
    private String creditCode;

    /**
     * 法定代表人
     */
    private String legalName;

    /**
     * 法定代表人证件类型，默认1(1:身份证 2:护照 3:军官证 4:组织机构代码 5:社会信用代码)
     */
    private String certType;

    /**
     * 法定代表人证件号码
     */
    private String legalCertNo;

    /**
     * 保函申请编号
     */
    private String lgNo;

    /**
     * 招标人/受益人（加密函传入MD5[32位]加密信息）
     */
    private String tenderee;
    /**
     * 招标人/受益人统一社会信用代码（加密函传入MD5[32位]加密信息）
     */
    private String tendereeCode;

    /**
     * 招标人地址（加密函传入MD5[32位]加密信息）
     */
    private String tendereeAddress;

    /**
     * 标段代码（加密函传入MD5[32位]加密信息）
     */
    private String bidNo;

    /**
     * 标段名称（加密函传入MD5[32位]加密信息）
     */
    private String bidName;

    /**
     * 保函申请金额（单位：元，精确到分）
     */
    private String guaranteeAmount;

    /**
     * 保函支付金额（单位：元，精确到分），非瀚华支付必传
     */
    private String payAmount;

    /**
     * 经办人姓名
     */
    private String operatorName;

    /**
     * 经办人身份证号码
     */
    private String operatorCertNo;

    /**
     * 经办人手机号
     */
    private String operatorPhone;

    /**
     * 预开标时间
     */
    private String openBidTime;

    //文件list
    private List<GuarFileListDto> fileList;
}
