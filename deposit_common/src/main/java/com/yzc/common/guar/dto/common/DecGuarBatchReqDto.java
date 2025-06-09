package com.yzc.common.guar.dto.common;

import lombok.Data;

import java.util.List;

/**
 * 批量解密-入参
 */
@Data
public class DecGuarBatchReqDto {

    /**
     * 【必填项】电子保函类型（1瀚华2兴泰3国控 同枚举）
     */
    private Integer guarTypeCode;

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
     * 招标人地址（明文信息）
     */
    private String tendereeAddress;


    /**
     * 开标日期(yyyy-MM-dd HH:mm:ss)
     */
    private String openBidTime;

    /**
     * 保函集合
     */
    private List<DecGuarInfoDto> guaranteeInfoList;
}
