package com.yzc.common.guar.dto.common;

import lombok.Data;

import java.util.List;

/**
 * 发起索赔-入参
 */
@Data
public class ApplyCompensationReqDto {

    /**
     * 【必填项】电子保函类型（1瀚华2兴泰3国控 同枚举）
     */
    private Integer guarTypeCode;

    //保函申请编号
    private String lgNo;

    //联系人姓名
    private String contactName;

    //联系人电话
    private String contactPhone;

    //招标人/受益人
    private String tenderee;

    //理赔原因
    private String reason;

    //理赔收款账号
    private String compensateAccNo;

    //保函编号-瀚华需要
    private String guaranteeNumber;

    //业务类型-瀚华需要（01 投标保函，02 政采保函，03 履约保函）
    private String businessType;

    //文件列表
    private List<GuarFileListDto> fileList;

}
