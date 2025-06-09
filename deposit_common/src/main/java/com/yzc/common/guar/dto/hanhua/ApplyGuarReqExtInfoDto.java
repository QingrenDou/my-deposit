package com.yzc.common.guar.dto.hanhua;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author douqr 2021/6/24 16:38
 * @description //TODO
 */
@Data
public class ApplyGuarReqExtInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String projectName; //项目名称
    private String projectNo; //项目编号

    private List<FileListDto> fileList;
}
