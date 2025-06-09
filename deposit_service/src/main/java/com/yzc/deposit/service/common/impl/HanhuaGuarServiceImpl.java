package com.yzc.deposit.service.common.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yzc.common.guar.dto.hanhua.*;
import com.yzc.common.guar.entity.GuarInfo;
import com.yzc.common.util.HttpUtils;
import com.yzc.deposit.service.common.IHanhuaGuarService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 与瀚华对接 实现
 * 注释：与网关接口基本都是从旧版代码复制而来
 */
@Slf4j
@Service
public class HanhuaGuarServiceImpl implements IHanhuaGuarService {

    @Value("${yzc.outGetWayUrl}")
    private String outGetwayServiceUrl;

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 根据保函数据，生成瀚华申请参数
     *
     * @param guarInfo 保函信息
     * @return 结果 瀚华申请参数
     */
    @Override
    public HanHuaRequestDto<ApplyHanHuaGuarReqDto> getHanhuaApplyGuarReqDto(GuarInfo guarInfo) {
        if(guarInfo == null){
            return null;
        }

        ApplyHanHuaGuarReqDto applyHanHuaGuarReqDto = new ApplyHanHuaGuarReqDto();
        applyHanHuaGuarReqDto.setBusinessType(guarInfo.getHanhuaBusiType());//业务类型（01 投标保函，02 政采保函，03履约保函）
        applyHanHuaGuarReqDto.setFinancialId(guarInfo.getFinancialId());//开函机构(1:瀚华担保,2:富民银行,3:徽商银行)

        applyHanHuaGuarReqDto.setEnterpriseName(guarInfo.getBidderName());//企业名称
        applyHanHuaGuarReqDto.setCreditCode(guarInfo.getCreditCode());//统一社会信用代码
        applyHanHuaGuarReqDto.setLegalName(guarInfo.getLegalName());//法定代表人
        applyHanHuaGuarReqDto.setLegalCertNo(guarInfo.getLegalCertNo());//法定代表人证件号码
        applyHanHuaGuarReqDto.setLgNo(guarInfo.getLgNo());//保函申请编号
        applyHanHuaGuarReqDto.setTenderee(guarInfo.getTenderee());//招标人/受益人（加密函传入MD5[32位]加密信息）
        applyHanHuaGuarReqDto.setBidNo(guarInfo.getProjectId());//标段代码（加密函传入MD5[32位]加密信息）[注释：标段编码使用平台标段id，防止重复]
        applyHanHuaGuarReqDto.setBidName(guarInfo.getProjectName());//标段名称（加密函传入MD5[32位]加密信息）
        applyHanHuaGuarReqDto.setGuaranteeAmount(guarInfo.getGuaranteeAmount().toString());//保函申请金额（单位：元，精确到分）
        applyHanHuaGuarReqDto.setOperatorName(guarInfo.getOperatorName());//经办人姓名
        applyHanHuaGuarReqDto.setOperatorCertNo(guarInfo.getOperatorCertNo());//经办人身份证号码
        applyHanHuaGuarReqDto.setOperatorPhone(guarInfo.getOperatorPhone());//经办人手机号

        //其他
        List<FileListDto> fileList = new ArrayList<>();
        fileList.add(FileListDto.builder().fileCode("A001").fileGuid(guarInfo.getBusiLicenseAttRelaId()).build());
        fileList.add(FileListDto.builder().fileCode("A008").fileGuid(guarInfo.getIdcardFrontAttRelaId()).build());
        fileList.add(FileListDto.builder().fileCode("A009").fileGuid(guarInfo.getIdcardBackAttRelaId()).build());

        ApplyGuarReqExtInfoDto applyGuarReqExtInfoDto = new ApplyGuarReqExtInfoDto();
        applyGuarReqExtInfoDto.setFileList(fileList);

        applyHanHuaGuarReqDto.setExtInfo(applyGuarReqExtInfoDto);

        HanHuaRequestDto<ApplyHanHuaGuarReqDto> requestDto = new HanHuaRequestDto<>();
        requestDto.setData(applyHanHuaGuarReqDto);

        return requestDto;
    }

    /**
     * @desc 开函
     * @param dto 入参
     * @return 结果
     * @author douqr 2021-06-24
     */
    @Override
    public HanHuaReturnDto<ApplyGuarRespDto> open(HanHuaRequestDto<ApplyHanHuaGuarReqDto> dto) {
        String logStr = "[hanhua_open]瀚华开函申请===>";
        if (dto == null) {
            return null;
        }
        String url = outGetwayServiceUrl + "api/hanhua/open";
        try {
            HanHuaReturnDto rst = HttpUtils.postUrlObjByModel(url, dto, HanHuaReturnDto.class,null,applicationName);
            log.info("{}[info],url={},入参={},结果={}" ,logStr, url,JSON.toJSONString(dto),JSON.toJSONString(rst));
            //单独解析一下子类
            if(StringUtils.equals(rst.getResultCode(),"0000") && rst.getData() != null){
                rst.setData(JSON.parseObject(JSON.toJSONString(rst.getData()),ApplyGuarRespDto.class));
            }else{
                rst.setData(null);
            }
            return rst;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("{}[exp],url={},入参={},结果={}" ,logStr, url,JSON.toJSONString(dto),e.getMessage());
        }
        return null;
    }

    /**
     * @desc 查询保函
     * @param dto 入参
     * @return 结果
     * @author douqr 2021-06-24
     */
    @Override
    public HanHuaReturnDto<HanHuaQueryGuarRespDto> queryGuarantee(HanHuaRequestDto<HanHuaQueryGuarReqDto> dto) {
        String logStr = "[hanhua_queryGuarantee]瀚华保函查询===>";
        if (dto == null) {
            return null;
        }
        String url = outGetwayServiceUrl + "api/hanhua/queryGuarantee";
        try {
            HanHuaReturnDto<HanHuaQueryGuarRespDto> rst = HttpUtils.postUrlObjByModel(url, dto, HanHuaReturnDto.class,null,applicationName);
            log.info("{}[info],url={},入参={},结果={}" ,logStr, url,JSON.toJSONString(dto),JSON.toJSONString(rst));
            //单独解析一下子类
            if(StringUtils.equals(rst.getResultCode(),"0000") && rst.getData() != null){
                rst.setData(JSON.parseObject(JSON.toJSONString(rst.getData()), HanHuaQueryGuarRespDto.class));
            }else{
                rst.setData(null);
            }
            return rst;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("{}[exp],url={},入参={},结果={}" ,logStr, url,JSON.toJSONString(dto),e.getMessage());
        }
        return null;
    }

    /**
     * @desc 解密
     * @param dto 入参
     * @return 结果
     * @author douqr 2021-06-24
     */
    @Override
    public HanHuaReturnDto<DecHanHuaRespDto> pushPreBidInfo(HanHuaRequestDto<DecHanHuaReqDto> dto) {
        String logStr = "[hanhua_pushPreBidInfo]瀚华保函解密===>";
        if (dto == null) {
            return null;
        }
        String url = outGetwayServiceUrl + "api/hanhua/pushPreBidInfo";
        try {
            HanHuaReturnDto<DecHanHuaRespDto> rst = HttpUtils.postUrlObjByModel(url, dto, HanHuaReturnDto.class,null,applicationName);
            log.info("{}[info],url={},入参={},结果={}" ,logStr, url,JSON.toJSONString(dto),JSON.toJSONString(rst));

            //单独解析一下子类
            if(StringUtils.equals(rst.getResultCode(),"0000") && rst.getData() != null){
                rst.setData(JSON.parseObject(JSON.toJSONString(rst.getData()), DecHanHuaRespDto.class));
            }else{
                rst.setData(null);
            }
            return rst;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("{}[exp],url={},入参={},结果={}" ,logStr, url,JSON.toJSONString(dto),e.getMessage());
        }
        return null;
    }

    /**
     * @desc 解密-按项目批量
     * @param dto 入参
     * @return 结果
     * @author douqr 2021-06-25
     */
    @Override
    public BatchDecHanHuaRespDto pushPreBidInfos(BatchDecHanHuaReqDto dto) {
        if (dto == null) {
            return null;
        }

        String logStr = "[hanhua_pushPreBidInfos]瀚华批量解密===>";
        String url = outGetwayServiceUrl + "api/hanhua/pushPreBidInfos";
        try {

            String rstStr = HttpUtils.postUrlObjByModel(url, dto, String.class,null,applicationName);
            log.info("{}[info],url={},入参={},结果={}" ,logStr, url,JSON.toJSONString(dto),rstStr);

            JSONObject jsonObject = JSONObject.parseObject(rstStr);

            BatchDecHanHuaRespDto rst = new BatchDecHanHuaRespDto();
            rst.setResultCode(jsonObject.getString("resultCode"));
            rst.setResultMessage(jsonObject.getString("resultMessage"));

            JSONArray jsonArray = jsonObject.getJSONArray("results");
            if(jsonArray != null && jsonArray.size() > 0) {
                List<HanHuaReturnDto> results = jsonArray.toJavaList(HanHuaReturnDto.class);

                results.forEach(item->{
                    //单独解析一下子类
                    if(StringUtils.equals(item.getResultCode(),"0000") && item.getData() != null){
                        item.setData(JSON.parseObject(JSON.toJSONString(item.getData()), DecHanHuaRespDto.class));
                    }else{
                        item.setData(null);
                    }
                });
                rst.setResults(results);
            }
            return rst;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("{}[exp],url={},入参={},结果={}" ,logStr, url,JSON.toJSONString(dto),e.getMessage());
        }
        return null;
    }

    /**
     * @desc （接口停用--业务已删除解保逻辑）解保(新接口)
     * @param dto 入参
     * @return 结果
     * @author douqr 2021-06-29
     */
    @Override
    public HanHuaReturnDto release(HanHuaRequestDto<ReleaseGuarReqDto> dto) {
        String logStr = "[hanhua_release]瀚华解保===>";
        if (dto == null) {
            return null;
        }
        String url = outGetwayServiceUrl + "api/hanhua/release";
        try {
            HanHuaReturnDto rst = HttpUtils.postUrlObjByModel(url, dto, HanHuaReturnDto.class,null,applicationName);
            log.info("{}[info],url={},入参={},结果={}" ,logStr, url,JSON.toJSONString(dto),JSON.toJSONString(rst));
            return rst;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("{}[exp],url={},入参={},结果={}" ,logStr, url,JSON.toJSONString(dto),e.getMessage());
        }
        return null;
    }

    /**
     * @desc （接口停用-业务已删除解保逻辑）批量解保(新接口)
     * @param dto 入参
     * @return 结果
     * @author douqr 2021-06-29
     */
    @Override
    public ReleaseGuarBatchRespDto releaseBatch(ReleaseGuarBatchReqDto dto) {
        String logStr = "[hanhua_releaseBatch]瀚华批量解保===>";
        if (dto == null) {
            return null;
        }
        String url = outGetwayServiceUrl + "api/hanhua/bailOuts";
        try {
            String rstStr = HttpUtils.postUrlObjByModel(url, dto, String.class,null,applicationName);
            log.info("{}[info],url={},入参={},结果={}" ,logStr, url,JSON.toJSONString(dto),rstStr);
            JSONObject jsonObject = JSONObject.parseObject(rstStr);

            ReleaseGuarBatchRespDto rst = new ReleaseGuarBatchRespDto();
            rst.setResultCode(jsonObject.getString("resultCode"));
            rst.setResultMessage(jsonObject.getString("resultMessage"));

            JSONArray jsonArray = jsonObject.getJSONArray("results");
            if(jsonArray != null && jsonArray.size() > 0) {
                List<ReleaseGuarRespDto> results = jsonArray.toJavaList(ReleaseGuarRespDto.class);
                rst.setResults(results);
            }
            return rst;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("{}[exp],url={},入参={},结果={}" ,logStr, url,JSON.toJSONString(dto),e.getMessage());
        }
        return null;
    }

    /**
     * @desc 发起索赔
     * @param dto 入参
     * @return 结果
     * @author douqr 2021-07-06
     */
    @Override
    public HanHuaReturnDto applyCompensation(HanHuaRequestDto<ApplyCompensationHanHuaReqDto> dto) {
        String logStr = "[hanhua_applyCompensation]瀚华发起索赔===>";
        if (dto == null) {
            return null;
        }
        String url = outGetwayServiceUrl + "api/hanhua/applyCompensation";
        try {
            HanHuaReturnDto returnDto = HttpUtils.postUrlObjByModel(url, dto, HanHuaReturnDto.class,null,applicationName);
            log.info("{}[info],url={},入参={},结果={}" ,logStr, url,JSON.toJSONString(dto),JSON.toJSONString(returnDto));
            return returnDto;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("{}[exp],url={},入参={},结果={}" ,logStr, url,JSON.toJSONString(dto),e.getMessage());
        }
        return null;
    }


    /**
     * @desc 退保申请
     * @param dto 入参
     * @return 结果
     * @author douqr 2025-04-10
     */
    @Override
    public HanHuaReturnDto close(HanHuaRequestDto<CloseGuarReqDto> dto) {
        String logStr = "[hanhua_close]瀚华退保申请===>";
        if (dto == null) {
            return null;
        }
        String url = outGetwayServiceUrl + "api/hanhua/close";
        try {
            HanHuaReturnDto rst = HttpUtils.postUrlObjByModel(url, dto, HanHuaReturnDto.class,null,applicationName);
            log.info("{}[info],url={},入参={},结果={}" ,logStr, url,JSON.toJSONString(dto),JSON.toJSONString(rst));
            return rst;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("{}[exp],url={},入参={},结果={}" ,logStr, url,JSON.toJSONString(dto),e.getMessage());
        }
        return null;
    }
}
