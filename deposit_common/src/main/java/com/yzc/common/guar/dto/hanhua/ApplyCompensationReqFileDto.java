package com.yzc.common.guar.dto.hanhua;

import lombok.Data;

/**
 * @description 索赔-文件集合
 * @author douqr 2021/7/6 11:11
 */
@Data
public class ApplyCompensationReqFileDto {

    private String fileCode; //文件编码，参考“文件编码”章节 SP01
    private String fileSuffix; //空
    private String fileUrl; //空
    private String fileGuid; //文件关联id
    private String fileName; //空
}
