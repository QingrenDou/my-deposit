package com.yzc.deposit.service.common;

import com.yzc.common.api.Result;
import com.yzc.common.common.vo.FilePreviewReqVo;
import com.yzc.common.common.vo.FilePreviewRespVo;

public interface IFileService {

    /**
     * 获取 文件预览信息
     * @param reqVo 请求参数
     * @return 响应
     */
    Result<FilePreviewRespVo> previewFile(FilePreviewReqVo reqVo);
}
