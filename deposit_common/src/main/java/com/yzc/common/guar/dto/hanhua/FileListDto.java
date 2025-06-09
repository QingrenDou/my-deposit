package com.yzc.common.guar.dto.hanhua;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author douqr 2021/6/24 16:39
 * @description //TODO
 */
@Data
@Builder
public class FileListDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fileCode; //文件编码，参考“文件编码”章节
    private String fileSuffix; //空
    private String fileUrl; //空
    private String fileGuid; //文件关联id
    private String fileName; //空
}
