package com.yzc.common.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 文件预览-入参
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilePreviewReqVo {

    /**
     * 文件关联id
     */
    @NotBlank(message = "attRelaId不能为空")
    private String attRelaId;

    /**
     * 文件id :如果关联id包含多个文件，可以指定id进行预览
     */
    private String attId;
}
