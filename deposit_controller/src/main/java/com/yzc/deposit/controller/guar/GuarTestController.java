
package com.yzc.deposit.controller.guar;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import com.yzc.common.api.Result;
import com.yzc.common.api.service.IAuthInfoService;
import com.yzc.common.api.service.IExtAPIService;
import com.yzc.common.api.service.IFileApiService;
import com.yzc.common.api.service.IOssFileService;
import com.yzc.common.common.util.CommonUtil;
import com.yzc.common.domain.LoginUserInfo;
import com.yzc.common.dto.SignatureDto;
import com.yzc.common.exception.BusinessException;
import com.yzc.common.guar.enums.GuarTypeCodeEnum;
import com.yzc.common.guar.vo.GuarInfoRespVo;
import com.yzc.common.model.file.PDFFileModel;
import com.yzc.common.util.FileUtil;
import com.yzc.deposit.service.guar.IGuarInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;


/**
 * 测试类
 */

@Slf4j
@RestController
@RequestMapping(value = "guar/test")
public class GuarTestController {

    @Resource
    private IGuarInfoService guarInfoService;

    @Resource
    private IOssFileService ossFileService;

    @Resource
    private IExtAPIService extAPIService;

    @Resource
    private IAuthInfoService authInfoService;

    @Resource
    private Environment env;

    /**
     * minio文件服务
     */
    @Resource
    private IFileApiService fileServerAPI;

    /**
     * 测试文件模板替换
     * 本地浏览器访问：http://127.0.0.1:9296/guar/test/getApplyDoc?guarInfoId=171737111163045888
     *
     * @param request
     * @param response
     * @throws FileNotFoundException
     */

    @GetMapping("getApplyDoc")
    public void getApplyDoc(HttpServletRequest request, HttpServletResponse response, @NotNull(message = "保函申请id不能为空") Long guarInfoId) throws FileNotFoundException {
        try {
            String filePath = "template/兴泰保函申请书.docx";
            // 设置文件名称
            response.reset();

            Result<GuarInfoRespVo> guarInfoResult = guarInfoService.getGuarInfo(guarInfoId);
            if (ObjectUtil.isNull(guarInfoResult) || !guarInfoResult.isSuccess()
                    || ObjectUtil.isNull(guarInfoResult.getData())) {
                log.error("保函信息获取失败[null]");
                return;
            }

            GuarInfoRespVo guarInfo = guarInfoResult.getData();

            try {
                Map<String, String> map = new HashMap<>();
                map.put("LgNo", guarInfo.getLgNo());
                map.put("FinanceOrgName", guarInfo.getFinanceOrgName());
                map.put("BidderName", guarInfo.getBidderName());
                map.put("ProjectNo", guarInfo.getProjectNo());
                map.put("ProjectName", guarInfo.getProjectName());
                map.put("Tenderee", guarInfo.getTenderee());
                map.put("ProjectDeposit", guarInfo.getProjectDeposit().toString());

                //准备工作，生成docx对象
                // 打开 Word 文档
                XWPFDocument docx = null;
                try {
                    InputStream is = new ClassPathResource(filePath).getStream();
                    docx = new XWPFDocument(is);
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //获取表格
                assert docx != null;
                List<XWPFParagraph> paragraphs = docx.getParagraphs();
                for (XWPFParagraph paragraph : paragraphs) {
                    List<XWPFRun> runs = paragraph.getRuns();
                    for (XWPFRun run : runs) {
                        //遍历该段里的所有文本
                        String str = run.toString();
                        //如果该段文本包含map中的key，则替换为map中的value值。
                        Set<String> keySet = map.keySet();
                        for (String key : keySet) {
                            if (str.trim().equals(key)) {
//                        System.out.println("key" + map.get(key));
                                //替换该文本0位置的数据。
                                run.setText(map.get(key), 0);
                            }
                        }
                    }
                }

                // Convert XWPFDocument to PDF using PDFBox
               /*
               //word转pdf代码 由于jar包有冲突，暂未成功
               PdfOptions options = PdfOptions.create();
                ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
                PdfConverter.getInstance().convert(docx, pdfOut, options);
                String authorFileName = "保函申请书.pdf";
                */

                String authorFileName = "我的申请书.docx";
                response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(authorFileName, "UTF-8") + "\"");
                response.setHeader("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE);

                ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
                docx.write(pdfOut);

                InputStream pdfIn = new ByteArrayInputStream(pdfOut.toByteArray());
                OutputStream out = response.getOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = pdfIn.read(buffer)) > -1) {
                    out.write(buffer, 0, len);
                }
                out.flush();
                out.close();
                pdfIn.close();
                pdfOut.close();

            } catch (Exception e) {
                log.error("saveFrAuthorization error msg :{}", e.getMessage());
            }
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    /**
     * word转pdf
     * 本地访问：http://127.0.0.1:9296/guar/test/wordToPdf
     */
    @GetMapping("wordToPdf")
    public int wordToPdf() {
        try {
            String filePath = "template/兴泰保函申请书.docx";
            //word临时文件
            String wordTempPath = new ClassPathResource(filePath).getAbsolutePath();
            // pdf创建临时 word文件转pdf
            File tempFile = File.createTempFile("tempPdf", ".pdf");

            FileUtil.word2Pdf(wordTempPath, tempFile.getPath());
            log.info("转pdf成功，路径：" + tempFile.getPath());
            return 1;
        } catch (Exception e) {
            log.error("word转pdf失败", e);
            return -1;
        }
    }


    /**
     * 测试CA签章流程：
     * 1.模板生成word
     * 2.word转pdf临时文件
     * 3.将临时pdf文件存入oss文件
     * 4.调用CA签章接口
     * 5.删除临时pdf文件
     * 本地访问地址：http://127.0.0.1:9296/guar/test/testCA?guarInfoId=160878951604048896
     *
     * @return 结果
     */
    @GetMapping("testCA")
    public Result testCA(@NotNull(message = "保函申请id不能为空") Long guarInfoId) throws IOException {
        Result<GuarInfoRespVo> guarInfoResult = guarInfoService.getGuarInfo(guarInfoId);
        if (ObjectUtil.isNull(guarInfoResult) || !guarInfoResult.isSuccess()
                || ObjectUtil.isNull(guarInfoResult.getData())) {
            return Result.error("保函信息获取失败");
        }

        GuarInfoRespVo guarInfo = guarInfoResult.getData();

        //匹配模板
        String filePath = "";
        if (ObjectUtil.equals(guarInfo.getGuarTypeCode(), GuarTypeCodeEnum.HanHua.getCode())) {
            filePath = "template/瀚华保函申请书.docx";
        } else {
            filePath = "template/兴泰保函申请书.docx";
        }

        FileOutputStream tempWordFileOutStream = null;
        File tempWordFile = null;
        File tempPdfFile = null;
        try {
            //1.根据模板转换word
            Map<String, String> map = new HashMap<>();
            map.put("LgNo", guarInfo.getLgNo());
            map.put("FinanceOrgName", guarInfo.getFinanceOrgName());
            map.put("BidderName", guarInfo.getBidderName());
            map.put("ProjectNo", guarInfo.getProjectNo());
            map.put("ProjectName", guarInfo.getProjectName());
            map.put("Tenderee", guarInfo.getTenderee());
            map.put("ProjectDeposit", guarInfo.getGuaranteeAmount().toString()); //项目担保金额

            // 打开 Word 文档
            XWPFDocument docx = null;
            try {
                InputStream is = new ClassPathResource(filePath).getStream();
                docx = new XWPFDocument(is);
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //获取表格
            assert docx != null;
            List<XWPFParagraph> paragraphs = docx.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    //遍历该段里的所有文本
                    String str = run.toString();
                    //如果该段文本包含map中的key，则替换为map中的value值。
                    Set<String> keySet = map.keySet();
                    for (String key : keySet) {
                        if (str.trim().equals(key)) {
                            //替换该文本0位置的数据。
                            run.setText(map.get(key), 0);
                        }
                    }
                }
            }

            String tempFilePath = "E:\\tempFile\\guarTest";
            File directory = new File(tempFilePath);
            //将word文件存储为临时文件，名为：tempWord+时间戳+随机数.docx
            String wordTempPath = "tempWord" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + RandomUtil.randomInt(100, 999);
            // pdf创建临时 word文件转pdf
            String tempPdfFileName = "tempPdf" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + RandomUtil.randomInt(100, 999);

            String envCode = env.getProperty("deposit.envCode");
            if(StringUtils.equals(envCode, "dev")){
                tempWordFile = File.createTempFile(wordTempPath, ".docx", directory);
                tempPdfFile = File.createTempFile(tempPdfFileName, ".pdf", directory);
            }else{
                tempWordFile = File.createTempFile(wordTempPath, ".docx");
                tempPdfFile = File.createTempFile(tempPdfFileName, ".pdf");
            }

            log.info("word临时文件路径：" + tempWordFile.getPath());
            log.info("pdf临时文件路径：" + tempPdfFile.getPath());

            //写入临时word文件
            tempWordFileOutStream = new FileOutputStream(tempWordFile);
            docx.write(tempWordFileOutStream);

            //2.根据word生成pdf
//            FileUtil.word2Pdf(tempWordFile.getPath(), tempPdfFile.getPath()); //测试失败---centos cmd执行报错
            convertFileWord2Pdf(tempWordFile.getPath(), tempPdfFile.getPath()); //测试成功

            //3.将临时pdf文件存入oss文件
            String ossFilePath = "fileupload" + StrUtil.C_SLASH + "guar" + StrUtil.C_SLASH + tempPdfFileName + ".pdf";
            boolean uploadResult = ossFileService.uploadFileToOss(tempPdfFile.getPath(), ossFilePath);

            if (!uploadResult) {
                return Result.error("上传文件至OSS失败");
            }

            //oss下载路径
            String ossDownloadPath = ossFileService.getOssFileUri(ossFilePath);
            log.info("oss下载路径：" + ossDownloadPath);

            LoginUserInfo logInfo = authInfoService.getAuthInfoByUserId("6FD1194B-CCFD-41FF-8E14-28D9385487C6");

            //4.调用CA签章接口
            String businessName = "协议书2025051914332535";
            String businessId = UUID.randomUUID().toString();

            SignatureDto signatureDto = new SignatureDto();
            signatureDto.setFileDownloadUrl(ossDownloadPath); //文件外网下载地址
            signatureDto.setFileName(businessName + ".pdf");
            signatureDto.setFileViewName(businessName + ".pdf");

            signatureDto.setBusinessType("GUAR"); //投标工具、询比、竞价、企业招标、合同业务类型
            signatureDto.setBusinessName("电子保函");

            /**
             * 业务Id：由业务传入，用于记录扫码记录。
             * 例如：项目Id或者合同Id
             */
            signatureDto.setBusinessId(businessId);

            /**
             * 操作完成后的动作类型1.跨域通知 2.直接页面跳转 3.无动作
             */
            signatureDto.setFinishOperateType(2);

            /**
             * 操作完成后业务网址 1.跨域通知iframe通知地址2.直接页面跳转网址,用于新标签页打开的情况,
             * 签署完成后跳转到具体业务页面3.无动作时为空
             */
            signatureDto.setFinishOperateUrl("https://www.baidu.com");

            signatureDto.setButtonStatus("1-1,2-1,3-1,4-1,5-0,6-1,7-1,8-0");
            signatureDto.setBackUrl("http://portal.ukzhicai.com/gateway/deposit/guar/test/saveSignResult");

            /**
             * 印章来源默认为0 0/UKey  2/小程序端
             */
            signatureDto.setSealSource(0);

            /**
             * 证件类型 0个人，1事件，2企业
             */
            signatureDto.setCertType("2");
            signatureDto.setUnitNo(logInfo.getBusinessLicenseNo());
            signatureDto.setUnit(logInfo.getCompanyName());

            signatureDto.setOperatingPlatform("YOUZC");

            /**
             * 业务唯一标识（各业务自己定义，能找到唯一的就行）
             */
            signatureDto.setBusinessRelationCode(businessId);

            String signUrl = extAPIService.getTzSignUrl(signatureDto);

            log.info("签章链接：" + signUrl);
            return Result.success("测试成功", signUrl);

        } catch (Exception e) {
            log.error("saveFrAuthorization error msg :{}", e.getMessage());
            e.printStackTrace();
        } finally {
            //关闭流
            if(tempWordFileOutStream != null) {
                tempWordFileOutStream.close();
                tempWordFileOutStream.flush();
            }
            //5.删除临时pdf文件
            if (tempWordFile != null && tempWordFile.exists()) {
                tempWordFile.delete();
            }

            if (tempPdfFile != null && tempPdfFile.exists()) {
                tempPdfFile.delete();
            }
        }

        return Result.success("测试异常");
    }

    /**
     * 保存签章结果
     *
     * @param json json对象
     * @return 结果
     */
    @GetMapping("saveSignResult")
    public Boolean saveSignResult(@RequestBody JSONObject json) {
        log.info("签章回调接口：", JSONUtil.toJsonStr(json));
        return true;
    }

    /**
     * minio上传测试
     * @return 结果
     * 本地访问地址：http://127.0.0.1:9296/guar/test/minioUpload
     */
    @GetMapping("minioUpload")
    public Result testMinioUpload() {

        try{
            String filePath = "E:\\tempFile\\guarTest\\tempPdf2025051919115275316018400054537409277.pdf";
            String attId = UUID.randomUUID().toString();
            String fileName = "tempPdf2025051919115275316018400054537409277.pdf";
            PDFFileModel pdfFileModel = new PDFFileModel();
            pdfFileModel.setFilePath(fileName);
            pdfFileModel.setBaseAttachmentId(attId);
            pdfFileModel.setTendProjectId(attId);
            pdfFileModel.setRelaId(attId);
            pdfFileModel.setFileViewName(fileName);
            log.info("attId：" + attId);
            log.info("fileName：" + fileName);
            boolean rst = fileServerAPI.addFile(filePath, pdfFileModel);

            /**
             * 检验文件
             *  http://consul-node1-test.ahyzc.com:1000/smallfile/api/FileInfo/get/
             *  post "attId"   //attId
             *
             */

            return Result.success("测试成功", rst);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return Result.error("测试失败");
    }

    /**
     * word转pdf  ---测试成功
     */
    public void convertFileWord2Pdf(String wordFilePath, String pdfFilePath) {
        try{
            if (CommonUtil.getAsposeLicense()) {
                log.error("Aspose.Words 许可证应用失败");
                throw new BusinessException("Aspose.Words 许可证应用失败");
            }

            Document document_Report = new Document(wordFilePath);
            document_Report.save(pdfFilePath, SaveFormat.PDF);
        }
        catch (Exception ex){

        }
    }
}

