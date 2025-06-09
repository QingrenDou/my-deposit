package com.yzc.deposit.service.bank.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.yzc.common.api.Result;
import com.yzc.common.guar.dto.common.*;
import com.yzc.common.guar.dto.xingtai.*;
import com.yzc.common.guar.entity.GuarTypeConfig;
import com.yzc.common.guar.enums.FinanceModeEnum;
import com.yzc.common.guar.enums.GuarTypeCodeEnum;
import com.yzc.common.util.HttpUtils;
import com.yzc.deposit.service.bank.IGuarAdapterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 兴泰电子保函对接
 */
@Slf4j
@Service
public class GuarXingTaiServiceImpl implements IGuarAdapterService {

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
        return ObjectUtil.equal(financeMode, FinanceModeEnum.XingTai.getCode());
    }

    /**
     * 申请开函
     *
     * @param reqDto         入参
     * @param guarTypeConfig
     * @return 结果
     */
    @Override
    public Result<ApplyGuarRespDto> open(ApplyGuarReqDto reqDto, GuarTypeConfig guarTypeConfig) {
        //组装保函机构入参
        XingTaiOpenReqDto xingTaiOpenReqDto = new XingTaiOpenReqDto();

        xingTaiOpenReqDto.setBusinessType(reqDto.getBusinessType());

        //开函机构：4.兴泰 5. 国控
        int financialId = ObjectUtil.equal(guarTypeConfig.getGuarTypeCode(), GuarTypeCodeEnum.XingTai.getCode()) ? 4 : 5;
        xingTaiOpenReqDto.setFinancialId(financialId);

        xingTaiOpenReqDto.setEnterpriseName(reqDto.getBidderName());
        xingTaiOpenReqDto.setCreditCode(reqDto.getCreditCode());
        xingTaiOpenReqDto.setLegalName(reqDto.getLegalName());
        xingTaiOpenReqDto.setLegalCertNo(reqDto.getLegalCertNo());
        xingTaiOpenReqDto.setLgNo(reqDto.getLgNo());
        xingTaiOpenReqDto.setTenderee(reqDto.getTenderee());
//        xingTaiOpenReqDto.setBidNo(reqDto.getProjectId()); //项目编号使用项目id
        //包含方说支持相同编号的项目，所以这里改为项目编号
        xingTaiOpenReqDto.setBidNo(reqDto.getProjectNo()); //项目编号使用项目id
        xingTaiOpenReqDto.setBidName(reqDto.getProjectName());//标段名称（加密函传入MD5[32位]加密信息）
        xingTaiOpenReqDto.setGuaranteeAmount(reqDto.getGuaranteeAmount().toString());//保函申请金额（单位：元，精确到分）
        xingTaiOpenReqDto.setOperatorName(reqDto.getOperatorName());//经办人姓名
        xingTaiOpenReqDto.setOperatorCertNo(reqDto.getOperatorCertNo());//经办人身份证号码
        xingTaiOpenReqDto.setOperatorPhone(reqDto.getOperatorPhone());//经办人手机号

        //由于兴泰的开标时间必要参数，所以这里如果开标时间为空，则使用报名截止时间来代替
        if (ObjectUtil.isNull(reqDto.getOpenBidTime())) {
            reqDto.setOpenBidTime(reqDto.getSignupEndTime());
        }
        xingTaiOpenReqDto.setOpenBidTime(DateUtil.format(reqDto.getOpenBidTime(), "yyyy-MM-dd HH:mm:ss")); //开标时间

        xingTaiOpenReqDto.setPayAmount(reqDto.getPayAmount().toString()); //待收金额（单位：元，精确到分）

        //其他
        List<GuarFileListDto> fileList = new ArrayList<>();
        fileList.add(GuarFileListDto.builder().fileCode("A001").fileGuid(reqDto.getBusiLicenseAttRelaId()).build());
        fileList.add(GuarFileListDto.builder().fileCode("A008").fileGuid(reqDto.getIdcardFrontAttRelaId()).build());
        fileList.add(GuarFileListDto.builder().fileCode("A009").fileGuid(reqDto.getIdcardBackAttRelaId()).build());
        fileList.add(GuarFileListDto.builder().fileCode("HH05").fileGuid(reqDto.getAgreementAttRelaId()).build());
        xingTaiOpenReqDto.setFileList(fileList);

        String logStr = "[xingtai_open]兴泰开函申请===>";

        String url = guarTypeConfig.getApiUrl() + "api/xingtai/open";
        try {
            //调用网关保函接口
            XingTaiBaseRespDto rst = HttpUtils.postUrlObjByModel(url, xingTaiOpenReqDto, XingTaiBaseRespDto.class, null, applicationName);
            log.info("{}[info],url={},入参={},结果={}", logStr, url, JSON.toJSONString(xingTaiOpenReqDto), JSON.toJSONString(rst));


            //单独解析一下子类
            if (StringUtils.equals(rst.getResultCode(), "0000")) {
                //定义结果集
                ApplyGuarRespDto respDto = new ApplyGuarRespDto();
                respDto.setFinanceOrgName(guarTypeConfig.getFinanceOrgName());
                respDto.setPayeeName(guarTypeConfig.getPayeeName());
                respDto.setFinancialId(financialId);

                return Result.success(respDto);
            }

            return Result.error(ObjectUtil.isNull(rst) ? "保函申请失败[兴泰]" : rst.getResultMessage());
        } catch (Exception e) {
            log.info("{}[exp],url={},入参={},结果={}", logStr, url, JSON.toJSONString(xingTaiOpenReqDto), e.toString());
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

        //组装保函机构入参
        XingTaiCloseGuarReqDto closeReqDto = new XingTaiCloseGuarReqDto();
        closeReqDto.setReason(reqDto.getMemo());

        XingTaiCloseGuarInfoDto closeGuarInfoDto = new XingTaiCloseGuarInfoDto();
        closeGuarInfoDto.setLgNo(reqDto.getLgNo());
        closeReqDto.setGuaranteeInfoList(List.of(closeGuarInfoDto));

        String logStr = "[xingtai_close]兴泰开函申请===>";

        String url = guarTypeConfig.getApiUrl() + "api/xingtai/close";
        try {
            //调用网关保函接口
            XingTaiBaseRespDto rst = HttpUtils.postUrlObjByModel(url, closeReqDto, XingTaiBaseRespDto.class, null, applicationName);
            log.info("{}[info],url={},入参={},结果={}", logStr, url, JSON.toJSONString(closeReqDto), JSON.toJSONString(rst));

            //解析结果
            if (StringUtils.equals(rst.getResultCode(), "0000")) {
                return Result.success();
            }

            return Result.error(ObjectUtil.isNull(rst) ? "退保申请失败[兴泰]" : rst.getResultMessage());
        } catch (Exception e) {
            log.info("{}[exp],url={},入参={},结果={}", logStr, url, JSON.toJSONString(closeReqDto), e.toString());
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
        //组装保函机构入参,注：由于兴泰接口是按优质采的标准规范，所以申请参数基本一致，这里可以使用copyProperties
        XingTaiDecGuarBatchReqDto decGuarBatchReqDto = BeanUtil.copyProperties(reqDto, XingTaiDecGuarBatchReqDto.class);

        String logStr = "[xingtai_decBatch]兴泰解密申请===>";

        String url = guarTypeConfig.getApiUrl() + "api/xingtai/decBatch";
        try {
            TypeReference<XingTaiBaseRespDto<List<XingTaiCommonGuarRespDto>>> type = new TypeReference<>() {};
            //调用网关保函接口
            XingTaiBaseRespDto<List<XingTaiCommonGuarRespDto>> rst = HttpUtils.postUrlObjByModel(url, decGuarBatchReqDto, type,null,  applicationName);

            log.info("{}[info],url={},入参={},结果={}", logStr, url, JSON.toJSONString(decGuarBatchReqDto), JSON.toJSONString(rst));

            //解析结果
            if (StringUtils.equals(rst.getResultCode(), "0000")) {
                //由于兴泰接口是按优质采的标准规范，所以直接copy属性
                List<DecGuarRespDto> respDtoList = BeanUtil.copyToList(rst.getData(), DecGuarRespDto.class);

                return Result.success(respDtoList);
            }

            return Result.error(ObjectUtil.isNull(rst) ? "解密申请失败[兴泰]" : rst.getResultMessage());
        } catch (Exception e) {
            log.info("{}[exp],url={},入参={},结果={}", logStr, url, JSON.toJSONString(decGuarBatchReqDto), e.toString());
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
        XingTaiApplyCompensationDto guarReqDto = BeanUtil.copyProperties(reqDto, XingTaiApplyCompensationDto.class);

        String logStr = "[xingtai_applyCompensation]兴泰索赔申请===>";

        String url = guarTypeConfig.getApiUrl() + "api/xingtai/applyCompensation";
        try {
            //调用网关保函接口
            XingTaiBaseRespDto rst = HttpUtils.postUrlObjByModel(url, guarReqDto, XingTaiBaseRespDto.class, null, applicationName);
            log.info("{}[info],url={},入参={},结果={}", logStr, url, JSON.toJSONString(guarReqDto), JSON.toJSONString(rst));

            //解析结果
            if (StringUtils.equals(rst.getResultCode(), "0000")) {
                return Result.success();
            }

            return Result.error(ObjectUtil.isNull(rst) ? "索赔申请失败[兴泰]" : rst.getResultMessage());
        } catch (Exception e) {
            log.info("{}[exp],url={},入参={},结果={}", logStr, url, JSON.toJSONString(guarReqDto), e.toString());
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
        //兴泰保函，暂无该接口
        return Result.success();
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
        XingTaiDecGuarSingleReqDto guarReqDto = BeanUtil.copyProperties(reqDto, XingTaiDecGuarSingleReqDto.class);
        String logStr = "[xingtai_decSingle]兴泰单笔解密===>";

        String url = guarTypeConfig.getApiUrl() + "api/xingtai/decSingle";
        try {
            //调用网关保函接口
            XingTaiBaseRespDto rst = HttpUtils.postUrlObjByModel(url, guarReqDto, XingTaiBaseRespDto.class, null, applicationName);
            log.info("{}[info],url={},入参={},结果={}", logStr, url, JSON.toJSONString(guarReqDto), JSON.toJSONString(rst));

            //解析结果
            if (StringUtils.equals(rst.getResultCode(), "0000")) {
                return Result.success();
            }

            return Result.error(ObjectUtil.isNull(rst) ? "单笔解密失败[兴泰]" : rst.getResultMessage());
        } catch (Exception e) {
            log.info("{}[exp],url={},入参={},结果={}", logStr, url, JSON.toJSONString(guarReqDto), e.toString());
            return Result.error("操作异常:" + e.getMessage());
        }
    }
}
