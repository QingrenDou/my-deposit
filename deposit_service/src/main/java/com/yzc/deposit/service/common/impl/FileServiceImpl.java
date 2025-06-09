package com.yzc.deposit.service.common.impl;

import com.yzc.common.api.Result;
import com.yzc.common.api.service.IFileApiService;
import com.yzc.common.common.vo.FilePreviewReqVo;
import com.yzc.common.common.vo.FilePreviewRespVo;
import com.yzc.common.model.file.BaseAttachmentModel;
import com.yzc.deposit.service.common.IFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
public class FileServiceImpl implements IFileService {

    @Resource
    private IFileApiService fileServerApi;

    @Resource
    private Environment env;

    /**
     * 获取文件预览地址
     * @param reqVo 文件关联id等信息
     * @return 文件预览地址
     */
    @Override
    public Result<FilePreviewRespVo> previewFile(FilePreviewReqVo reqVo) {

        if(StringUtils.isBlank(reqVo.getAttRelaId())){
            return Result.error("文件关联id不能为空");
        }
        //根据关联id 获取文件列表
        List<BaseAttachmentModel> fileList = fileServerApi.getListByRelaId(reqVo.getAttRelaId());
        if (fileList == null || fileList.isEmpty()) {
            return Result.error("无预览文件");
        }
        int index = 0;

        //需要指定文件预览
        if(StringUtils.isNotBlank(reqVo.getAttId())) {
            for (int i = 0; i < fileList.size(); i++) {
                if (fileList.get(i).BaseAttachmentId.equalsIgnoreCase(reqVo.getAttId())) {
                    index = i;
                }
            }
        }

        String projectId = fileList.get(index).TendProjectId;
        String fileName = fileList.get(index).AttFileName;
        String attRelaId = fileList.get(index).AttRelaId;
        String attViewName = fileList.get(index).AttViewName;
        String baseAttachmentId = fileList.get(index).BaseAttachmentId;

        if (StringUtils.isEmpty(projectId)
                || StringUtils.isEmpty(fileName)
                || StringUtils.isEmpty(attRelaId)) {
            return Result.error("文件参数异常，不支持预览");
        } else if (checkFileSufix(fileName)) {
            //fileService: https://file.ukzhicai.com
            String fileServiceUrl = env.getProperty("yzc.fileService");
            String newFilePreviewUrl = env.getProperty("yzc.newFilePreviewUrl");
            String downLoadUrl = fileServiceUrl + "/SingleDown/" + baseAttachmentId;
            String previewUrl = newFilePreviewUrl + "/" + attViewName + "?url=" + URLEncoder.encode(downLoadUrl, Charset.forName("UTF-8"));

            //结果集
            FilePreviewRespVo respVo = new FilePreviewRespVo();
            respVo.setDownLoadUrl(downLoadUrl);
            respVo.setPreviewUrl(previewUrl);
            respVo.setBaseAttachmentId(baseAttachmentId);

            return Result.success("操作成功", respVo);
        } else {
            return Result.error("操作异常");
        }
    }


    /**
     * 验证文件后缀
     *
     * @param fileName
     * @return
     * @author SHANHY
     */
    private boolean checkFileSufix(String fileName) {
        String split = fileName.substring(fileName.lastIndexOf("."));
        split = split.toLowerCase(Locale.ENGLISH);
        return ".pdf".equals(split)
                || ".xls".equals(split)
                || ".xlsx".equals(split)
                || ".doc".equals(split)
                || ".docx".equals(split)
                || ".png".equals(split)
                || ".jpg".equals(split)
                || ".jpeg".equals(split);
    }
}
