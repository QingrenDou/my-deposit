package com.yzc.deposit.service.bank;

import com.yzc.common.api.Result;
import com.yzc.common.guar.dto.common.*;
import com.yzc.common.guar.entity.GuarTypeConfig;

import java.util.List;

public interface IGuarAdapterService {


    /**
     * 判断是否是当前担保机构
     * @param financeMode 保函机构类型
     * @return true:是当前担保机构
     */
    boolean isCurrentGuar(Integer financeMode);

    /**
     * 申请开函
     * @param reqDto 入参
     * @param guarTypeConfig 保函机构配置信息
     * @return 结果
     */
    Result<ApplyGuarRespDto> open(ApplyGuarReqDto reqDto, GuarTypeConfig guarTypeConfig);

    /**
     * 申请退保
     * @param reqDto 入参
     * @param guarTypeConfig 保函机构配置信息
     * @return 结果
     */
    Result close(ApplyCloseReqDto reqDto, GuarTypeConfig guarTypeConfig);

    /**
     * 批量解密
     * @param reqDto 入参
     * @param guarTypeConfig 保函机构配置信息
     * @return 结果
     */
    Result<List<DecGuarRespDto>> decBatch(DecGuarBatchReqDto reqDto, GuarTypeConfig guarTypeConfig);

    /**
     * 申请索赔
     * @param reqDto 入参
     * @param guarTypeConfig 保函机构配置信息
     * @return 结果
     */
    Result applyCompensation(ApplyCompensationReqDto reqDto, GuarTypeConfig guarTypeConfig);

    /**
     * 查询保函
     * @param reqDto 入参
     * @param guarTypeConfig 保函机构配置信息
     * @return 结果
     */
    Result<QueryGuarRespDto> query(QueryGuarReqDto reqDto, GuarTypeConfig guarTypeConfig);

    /**
     * 单个解密
     * @param reqDto 入参
     * @return 结果
     */
    Result<DecGuarRespDto> decSingle(DecGuarSingleReqDto reqDto, GuarTypeConfig guarTypeConfig);
}
