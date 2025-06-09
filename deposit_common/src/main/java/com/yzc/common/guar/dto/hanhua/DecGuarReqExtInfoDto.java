package com.yzc.common.guar.dto.hanhua;

import java.io.Serializable;
import java.util.List;

/**
 * @author douqr 2021/6/24 17:40
 * @description //TODO
 */
public class DecGuarReqExtInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String projectName; //项目名称
    private String projectNo; //项目编号

    private List<DecGuarReqExtInfoFileDto> fileList;
}
