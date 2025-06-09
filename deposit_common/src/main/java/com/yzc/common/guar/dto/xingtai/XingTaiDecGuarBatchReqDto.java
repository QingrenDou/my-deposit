package com.yzc.common.guar.dto.xingtai;

import lombok.Data;

import java.util.List;

/**
 * 按项目批量解密保函
 */
@Data
public class XingTaiDecGuarBatchReqDto {

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
    private List<XingTaiDecGuarInfoDto> guaranteeInfoList;
}
