package com.yzc.common.guar.dto.common;

import lombok.Data;

/**
 * 单个解密
 */
@Data
public class DecGuarSingleReqDto {

    /**
     * 【必填项】电子保函类型（1瀚华2兴泰3国控 同枚举）
     */
    private Integer guarTypeCode;


    /**
     * 保函申请编号
     */
    private String lgNo;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 统一社会信用代码
     */
    private String creditCode;

    /**
     * 标段代码（明文信息）
     */
    private String bidNo;

    /**
     * 标段名称（明文信息）
     */
    private String bidName;

    /**
     * 招标人/受益人（明文信息）
     */
    private String tenderee;
    /**
     * 招标人/受益人统一社会信用代码（明文信息）
     */
    private String tendereeCode;

    /**
     * 招标人地址（明文信息） ----暂时都是空的
     */
    private String tendereeAddress;


    /**
     * 【兴泰】开标日期(yyyy-MM-dd HH:mm:ss)
     * 【瀚华】开标时间(yyyy-MM-dd)
     */
    private String openBidTime;

    /**
     * 【瀚华】业务类型（01 投标保函，02 政采保函，03 履约保函）
     */
    public String businessType;

    /**
     * 【瀚华】保函保证金(元)
     */
    public String guaranteeAmount;
}
