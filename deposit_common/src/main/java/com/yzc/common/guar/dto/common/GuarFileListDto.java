package com.yzc.common.guar.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 保函对外文件接口
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GuarFileListDto {

    //文件编码
    private String fileCode;

    //文件后缀
    private String fileSuffix;

    //文件下载完整url(fileUrl,fileGuid二者必传其一)
    private String fileUrl;

    //文件标识，用于下载文件的唯一标识(fileUrl,fileGuid二者必传其一)
    private String fileGuid;

    //文件名称(带后缀)
    private String fileName;
}
