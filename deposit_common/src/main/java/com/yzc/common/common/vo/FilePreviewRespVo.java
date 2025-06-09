package com.yzc.common.common.vo;

import lombok.Data;

/**
 * 文件预览返回信息
 */
@Data
public class FilePreviewRespVo {

    /**
     * 下载地址
     */
    private String downLoadUrl;

    /**
     * 预览地址
     */
    private String previewUrl;

    /**
     * 文件id
     */
    private String baseAttachmentId;

}
