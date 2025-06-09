package com.yzc.common.guar.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SaveLogReqVo {
    @ApiModelProperty(value = "日志编码")
    private String logCode;

    @ApiModelProperty(value = "日志内容")
    private String logContent;
}
