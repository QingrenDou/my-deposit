package com.yzc.common.guar.dto;

import lombok.Data;

/**
 * 企业证件 复制后新的文件id信息
 */
@Data
public class CertFileCopyRespDto {

    //营业执照-复制后新的文件id
    private String businessLicAttIdNew;

    //法人身份证正面-复制后新的文件id
    private String legalIdCarAttIdNew;

    //法人身份证反面-复制后新的文件id
    private String legalIdCarBackAttIdNew;
}
