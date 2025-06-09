package com.yzc.common.guar.dto.hanhua;

import lombok.Getter;
import lombok.Setter;

/**
 * 标前项目信息接口DTO
 */

@Getter
@Setter
public class BeforeBidInfoDTO {

    //业务类型（01 投标保函，02 政采保函，03 履约保函）
    private String businessType;

    //标段编号
    private String bidNo;

    //标段名称
    private String bidName;

    //保函保证金(元)
    private String guaranteeAmount;

    //投标申请编号
    private String lgNo;

    //统一社会信用代码
    private String creditCode;

    //企业名称
    private String enterpriseName;

    //招标人/受益人
    private String tenderee;

    //招标人地址
    private String tendereeAdress;

    //项目发布时间(yyyy-MM-dd)
    private String bidReleaseTime;

}
