package com.yzc.deposit.service.bank.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.yzc.common.api.Result;
import com.yzc.common.common.enums.CommonStatusEnum;
import com.yzc.common.guar.dto.common.*;
import com.yzc.common.guar.dto.common.ApplyGuarRespDto;
import com.yzc.common.guar.dto.hanhua.*;
import com.yzc.common.guar.dto.xingtai.XingTaiBaseRespDto;
import com.yzc.common.guar.dto.xingtai.XingTaiCommonGuarRespDto;
import com.yzc.common.guar.entity.GuarTypeConfig;
import com.yzc.common.guar.enums.FinanceModeEnum;
import com.yzc.common.util.HttpUtils;
import com.yzc.deposit.service.bank.IGuarAdapterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 瀚华电子保函对接
 */
@Slf4j
@Service
public class GuarHanHuaServiceImpl implements IGuarAdapterService {

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 判断是否是当前担保机构对接方
     *
     * @param financeMode 保函对接方编码
     * @return true:是当前对接方
     */
    @Override
    public boolean isCurrentGuar(Integer financeMode) {
        return ObjectUtil.equal(financeMode, FinanceModeEnum.HanHua.getCode());
    }

    /**
     * 申请开函
     *
     * @param reqDto  入参
     * @param guarTypeConfig
     * @return 结果
     */
    @Override
    public Result<ApplyGuarRespDto> open(ApplyGuarReqDto reqDto, GuarTypeConfig guarTypeConfig) {

        //组装保函机构入参
        ApplyHanHuaGuarReqDto applyGuarReqDto = new ApplyHanHuaGuarReqDto();
        applyGuarReqDto.setBusinessType(reqDto.getHanhuaBusiType());//业务类型（01 投标保函，02 政采保函，03履约保函）
        applyGuarReqDto.setFinancialId(1);//开函机构(1:瀚华担保,2:富民银行,3:徽商银行)

        applyGuarReqDto.setEnterpriseName(reqDto.getBidderName());//企业名称
        applyGuarReqDto.setCreditCode(reqDto.getCreditCode());//统一社会信用代码
        applyGuarReqDto.setLegalName(reqDto.getLegalName());//法定代表人
        applyGuarReqDto.setLegalCertNo(reqDto.getLegalCertNo());//法定代表人证件号码
        applyGuarReqDto.setLgNo(reqDto.getLgNo());//保函申请编号
        applyGuarReqDto.setTenderee(reqDto.getTenderee());//招标人/受益人（加密函传入MD5[32位]加密信息）
//        applyGuarReqDto.setBidNo(reqDto.getProjectId());//标段代码（加密函传入MD5[32位]加密信息）[注释：标段编码使用平台标段id，防止重复]
        //包含方说支持相同编号的项目，所以这里改为项目编号
        applyGuarReqDto.setBidNo(reqDto.getProjectNo());//标段代码（加密函传入MD5[32位]加密信息）[注释：标段编码使用平台标段id，防止重复]

        applyGuarReqDto.setBidName(reqDto.getProjectName());//标段名称（加密函传入MD5[32位]加密信息）
        applyGuarReqDto.setGuaranteeAmount(reqDto.getGuaranteeAmount().toString());//保函申请金额（单位：元，精确到分）
        applyGuarReqDto.setOperatorName(reqDto.getOperatorName());//经办人姓名
        applyGuarReqDto.setOperatorCertNo(reqDto.getOperatorCertNo());//经办人身份证号码
        applyGuarReqDto.setOperatorPhone(reqDto.getOperatorPhone());//经办人手机号
//        applyGuarReqDto.setPayAmount(reqDto.getPayAmount().toString()); //待收金额（单位：元，精确到分）
        //是否加密函 -- 2025-05-29
        applyGuarReqDto.setEnsureIsEncryp(ObjectUtil.equal(reqDto.getIsEncrypt(), CommonStatusEnum.Yes.getCode()) ? "1" : "0");

        //其他
        List<FileListDto> fileList = new ArrayList<>();
        fileList.add(FileListDto.builder().fileCode("A001").fileGuid(reqDto.getBusiLicenseAttRelaId()).build());
        fileList.add(FileListDto.builder().fileCode("A008").fileGuid(reqDto.getIdcardFrontAttRelaId()).build());
        fileList.add(FileListDto.builder().fileCode("A009").fileGuid(reqDto.getIdcardBackAttRelaId()).build());

        ApplyGuarReqExtInfoDto applyGuarReqExtInfoDto = new ApplyGuarReqExtInfoDto();
        applyGuarReqExtInfoDto.setFileList(fileList);

        applyGuarReqDto.setExtInfo(applyGuarReqExtInfoDto);

        HanHuaRequestDto<ApplyHanHuaGuarReqDto> requestDto = new HanHuaRequestDto<>();
        requestDto.setData(applyGuarReqDto);

        String logStr = "[hanhua_open]瀚华开函申请===>";
        String url = guarTypeConfig.getApiUrl() + "api/hanhua/open";
        try {
            HanHuaReturnDto rst = HttpUtils.postUrlObjByModel(url, requestDto, HanHuaReturnDto.class,null,applicationName);
            log.info("{}[info],url={},入参={},结果={}" ,logStr, url, JSON.toJSONString(requestDto),JSON.toJSONString(rst));
            //单独解析一下子类
            if(StringUtils.equals(rst.getResultCode(),"0000")){
                //定义结果集
                ApplyGuarRespDto respDto = new ApplyGuarRespDto();
                respDto.setFinanceOrgName(guarTypeConfig.getFinanceOrgName());
                respDto.setPayeeName(guarTypeConfig.getPayeeName());
                respDto.setFinancialId(1); //瀚华

                return Result.success(respDto);
            }
            return Result.error(ObjectUtil.isNull(rst) ? "保函申请失败[瀚华]": rst.getResultMessage());
        } catch (Exception e) {
            log.info("{}[exp],url={},入参={},结果={}" ,logStr, url,JSON.toJSONString(requestDto),e.toString());
            return Result.error("操作异常:" + e.getMessage());
        }

    }

    /**
     * 申请退保
     *
     * @param reqDto         入参
     * @param guarTypeConfig 保函机构配置信息
     * @return 结果
     */
    @Override
    public Result close(ApplyCloseReqDto reqDto, GuarTypeConfig guarTypeConfig) {

        // 发起瀚华退保
        HanHuaRequestDto<CloseGuarReqDto> hanhuaReqDto = new HanHuaRequestDto<>();
        CloseGuarReqDto closeGuarReqDto = CloseGuarReqDto.builder()
                .bidNo(reqDto.getBidNo())
                .bidName(reqDto.getBidName())
                .lgNo(reqDto.getLgNo())
                .guaranteeNumber(reqDto.getGuaranteeNumber())
                .memo(reqDto.getMemo())  //原因
                .operatorPhone(reqDto.getOperatorPhone())
                .build();
        hanhuaReqDto.setData(closeGuarReqDto);

        String logStr = "[hanhua_close]瀚华退保申请===>";

        String url = guarTypeConfig.getApiUrl() + "api/hanhua/close";
        try {
            HanHuaReturnDto rst = HttpUtils.postUrlObjByModel(url, hanhuaReqDto, HanHuaReturnDto.class,null,applicationName);
            log.info("{}[info],url={},入参={},结果={}" ,logStr, url,JSON.toJSONString(hanhuaReqDto),JSON.toJSONString(rst));

            if(StringUtils.equals(rst.getResultCode(),"0000")){
                return Result.success();
            }

            return Result.error("申请失败");
        } catch (Exception e) {
            log.info("{}[exp],url={},入参={},结果={}" ,logStr, url,JSON.toJSONString(hanhuaReqDto),e.toString());
            return Result.error("操作异常:" + e.getMessage());
        }

    }

    /**
     * 批量解密
     *
     * @param reqDto         入参
     * @param guarTypeConfig 保函机构配置信息
     * @return 结果
     */
    @Override
    public Result<List<DecGuarRespDto>> decBatch(DecGuarBatchReqDto reqDto, GuarTypeConfig guarTypeConfig) {

        //组装保函机构入参
        List<HanHuaRequestDto<DecHanHuaReqDto>> bidderGuarList = reqDto.getGuaranteeInfoList().stream().map(guar -> {
            DecHanHuaReqDto decHanHuaReqDto = DecHanHuaReqDto.builder()
                    .bidNo(reqDto.getBidNo())
                    .bidName(reqDto.getBidName())
                    .lgNo(guar.getLgNo())
                    .creditCode(guar.getCreditCode())
                    .enterpriseName(guar.getEnterpriseName())
                    .tenderee(reqDto.getTenderee())
                    .build();
            return new HanHuaRequestDto<>(null, decHanHuaReqDto);
        }).collect(Collectors.toList());

        BatchDecHanHuaReqDto hanhuaReqDto = BatchDecHanHuaReqDto.builder()
                .bidders(bidderGuarList)
                .build();

        String logStr = "[hanhua_decBatch]瀚华批量解密===>";
        String url = guarTypeConfig.getApiUrl() + "api/hanhua/pushPreBidInfos";
        try {

            String rstStr = HttpUtils.postUrlObjByModel(url, hanhuaReqDto, String.class,null,applicationName);
            log.info("{}[info],url={},入参={},结果={}" ,logStr, url,JSON.toJSONString(hanhuaReqDto),rstStr);

            JSONObject jsonObject = JSONObject.parseObject(rstStr);

            JSONArray jsonArray = jsonObject.getJSONArray("results");

            //循环解析
            if(jsonArray != null && jsonArray.size() > 0) {
                //结果集
                List<DecGuarRespDto> respList = new ArrayList<>();

                //解析瀚华结果
                List<HanHuaReturnDto> results = jsonArray.toJavaList(HanHuaReturnDto.class);

                //循环解析
                results.forEach(item->{
                    DecGuarRespDto decGuarRespDto = new DecGuarRespDto();

                    //单独解析一下子类
                    if(StringUtils.equals(item.getResultCode(),"0000") && item.getData() != null){
                        DecHanHuaRespDto hanhuaRespDto = JSON.parseObject(JSON.toJSONString(item.getData()), DecHanHuaRespDto.class);
                        decGuarRespDto.setLgNo(hanhuaRespDto.getLgNo());
                        decGuarRespDto.setResultCode(StringUtils.equals(hanhuaRespDto.getStatus(),"2") ? "0000" : "-1");
                        decGuarRespDto.setResultMessage(hanhuaRespDto.getAuditOpinion());

                        respList.add(decGuarRespDto);
                    }
                });

                return Result.success(respList);
            }

            return Result.error("申请失败");
        } catch (Exception e) {
            log.info("{}[exp],url={},入参={},结果={}" ,logStr, url,JSON.toJSONString(hanhuaReqDto),e.getMessage());
            return Result.error("操作异常:" + e.getMessage());
        }
    }

    /**
     * 申请索赔
     *
     * @param reqDto         入参
     * @param guarTypeConfig 保函机构配置信息
     * @return 结果
     */
    @Override
    public Result applyCompensation(ApplyCompensationReqDto reqDto, GuarTypeConfig guarTypeConfig) {
        //组装保函机构入参
        ApplyCompensationHanHuaReqDto hanhuaApplyReqDto = BeanUtil.copyProperties(reqDto, ApplyCompensationHanHuaReqDto.class);

        HanHuaRequestDto<ApplyCompensationHanHuaReqDto> hanhuaReqDto = new HanHuaRequestDto<>();
        hanhuaReqDto.setData(hanhuaApplyReqDto);

        String logStr = "[hanhua_applyCompensation]瀚华索赔申请===>";

        String url = guarTypeConfig.getApiUrl() + "api/hanhua/applyCompensation";
        try {
            HanHuaReturnDto rst = HttpUtils.postUrlObjByModel(url, hanhuaReqDto, HanHuaReturnDto.class,null,applicationName);
            log.info("{}[info],url={},入参={},结果={}" ,logStr, url,JSON.toJSONString(hanhuaReqDto),JSON.toJSONString(rst));

            if(StringUtils.equals(rst.getResultCode(),"0000")){
                return Result.success();
            }

            return Result.error("申请失败");
        } catch (Exception e) {
            log.info("{}[exp],url={},入参={},结果={}" ,logStr, url,JSON.toJSONString(hanhuaReqDto),e.toString());
            return Result.error("操作异常:" + e.getMessage());
        }
    }

    /**
     * 查询保函
     *
     * @param reqDto         入参
     * @param guarTypeConfig 保函机构配置信息
     * @return 结果
     */
    @Override
    public Result<QueryGuarRespDto> query(QueryGuarReqDto reqDto, GuarTypeConfig guarTypeConfig) {
        //组装保函机构入参
        HanHuaQueryGuarReqDto queryGuarReqDto = BeanUtil.copyProperties(reqDto, HanHuaQueryGuarReqDto.class);

        HanHuaRequestDto<HanHuaQueryGuarReqDto> hanhuaReqDto = new HanHuaRequestDto<>();
        hanhuaReqDto.setData(queryGuarReqDto);

        String logStr = "[hanhua_query]瀚华保函查询===>";

        String url = guarTypeConfig.getApiUrl() + "api/hanhua/queryGuarantee";
        try {
            TypeReference<HanHuaReturnDto<HanHuaQueryGuarRespDto>> type = new TypeReference<>() {};

            HanHuaReturnDto<HanHuaQueryGuarRespDto> rst = HttpUtils.postUrlObjByModel(url, hanhuaReqDto, type,null,applicationName);
            log.info("{}[info],url={},入参={},结果={}" ,logStr, url,JSON.toJSONString(hanhuaReqDto),JSON.toJSONString(rst));

            if(StringUtils.equals(rst.getResultCode(),"0000") && ObjectUtil.isNotNull(rst.getData())){
                QueryGuarRespDto respDto = BeanUtil.copyProperties(rst.getData(), QueryGuarRespDto.class);
                return Result.success(respDto);
            }

            return Result.error("申请失败");
        } catch (Exception e) {
            log.info("{}[exp],url={},入参={},结果={}" ,logStr, url,JSON.toJSONString(hanhuaReqDto),e.toString());
            return Result.error("操作异常:" + e.getMessage());
        }
    }

    /**
     * 单个解密
     *
     * @param reqDto 入参
     * @return 结果
     */
    @Override
    public Result<DecGuarRespDto> decSingle(DecGuarSingleReqDto reqDto, GuarTypeConfig guarTypeConfig) {
        //组装保函机构入参
        BeforeBidInfoDTO guarReqDto = BeanUtil.copyProperties(reqDto, BeforeBidInfoDTO.class);

        //特殊字段处理
        if(StringUtils.isNotBlank(reqDto.getOpenBidTime()) && reqDto.getOpenBidTime().length() > 10){
            //截取日期格式字符串
            guarReqDto.setBidReleaseTime(reqDto.getOpenBidTime().substring(0,10));
        }
        guarReqDto.setTendereeAdress(reqDto.getTendereeAddress()); //注意：地址字段有差异，虽然目前都是空值，但是这里还是先列出来

        HanHuaRequestDto<BeforeBidInfoDTO> hanhuaReqDto = new HanHuaRequestDto<>();
        hanhuaReqDto.setData(guarReqDto);

        String logStr = "[hanhua_decSingle]瀚华单笔解密===>";

        String url = guarTypeConfig.getApiUrl() + "api/hanhua/pushPreBidInfo";
        try {

            HanHuaReturnDto rst = HttpUtils.postUrlObjByModel(url, hanhuaReqDto, HanHuaReturnDto.class,null,applicationName);
            log.info("{}[info],url={},入参={},结果ResultCode={}" ,logStr, url,JSON.toJSONString(hanhuaReqDto),ObjectUtil.isNull(rst) ? null : rst.getResultCode());

            if(StringUtils.equals(rst.getResultCode(),"0000") && ObjectUtil.isNotNull(rst.getData())){
                QueryGuarRespDto respDto = BeanUtil.copyProperties(rst.getData(), QueryGuarRespDto.class);
                return Result.success(respDto);
            }

            return Result.error("申请失败");
        } catch (Exception e) {
            log.info("{}[exp],url={},入参={},结果={}" ,logStr, url,JSON.toJSONString(hanhuaReqDto),e.toString());
            return Result.error("操作异常:" + e.getMessage());
        }
    }
}
