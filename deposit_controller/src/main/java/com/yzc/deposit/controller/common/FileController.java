package com.yzc.deposit.controller.common;

import com.yzc.common.api.Result;
import com.yzc.common.common.vo.FilePreviewReqVo;
import com.yzc.common.common.vo.FilePreviewRespVo;
import com.yzc.deposit.service.common.IFileService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 文件服务
 */
@RestController
@RequestMapping( "file")
public class FileController {

    @Resource
    private IFileService fileService;

    /**
     * 获取文件预览地址
     * @param reqVo 文件关联id等信息
     * @return 文件预览地址
     */
    @PostMapping(value = "/previewFile")
    public Result<FilePreviewRespVo> previewFile(@RequestBody @Validated FilePreviewReqVo reqVo) {
        return  fileService.previewFile(reqVo);
    }
}
