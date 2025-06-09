package com.yzc.deposit.service.common;

import com.yzc.common.guar.dto.hanhua.*;
import com.yzc.common.guar.entity.GuarInfo;

/**
 * 瀚华保函对接通用方法
 */
public interface IHanhuaGuarService {

    /**
     * 根据保函数据，生成瀚华申请参数
     * @param guarInfo 保函信息
     * @return 瀚华申请参数
     */
    HanHuaRequestDto<ApplyHanHuaGuarReqDto> getHanhuaApplyGuarReqDto(GuarInfo guarInfo);


    /**
     * @desc 开函
     * @param dto
     * @return
     * @author douqr 2021-06-24
     */
    HanHuaReturnDto<ApplyGuarRespDto> open(HanHuaRequestDto<ApplyHanHuaGuarReqDto> dto);

    /**
     * @desc 查询保函
     * @param dto
     * @return
     * @author douqr 2021-06-24
     */
    HanHuaReturnDto<HanHuaQueryGuarRespDto> queryGuarantee(HanHuaRequestDto<HanHuaQueryGuarReqDto> dto);

    /**
     * @desc 解密
     * @param dto
     * @return
     * @author douqr 2021-06-24
     */
    HanHuaReturnDto<DecHanHuaRespDto> pushPreBidInfo(HanHuaRequestDto<DecHanHuaReqDto> dto);
    /**
     * @desc 解密-批量
     * @param dto
     * @return
     * @author douqr 2021-06-26
     */
    BatchDecHanHuaRespDto pushPreBidInfos(BatchDecHanHuaReqDto dto);

    /**
     * @desc 解保(新接口)
     * @param dto
     * @return
     * @author douqr 2021-06-29
     */
    HanHuaReturnDto release(HanHuaRequestDto<ReleaseGuarReqDto> dto);

    /**
     * @desc 批量解保(新接口)
     * @param dto
     * @return
     * @author douqr 2021-06-29
     */
    ReleaseGuarBatchRespDto releaseBatch(ReleaseGuarBatchReqDto dto);

    /**
     * @desc 发起索赔
     * @param dto
     * @return
     * @author douqr 2021-07-06
     */
    HanHuaReturnDto applyCompensation(HanHuaRequestDto<ApplyCompensationHanHuaReqDto> dto);

    /**
     * @desc 退保申请
     * @param dto 入参
     * @return 结果
     * @author douqr 2025-04-10
     */
    HanHuaReturnDto close(HanHuaRequestDto<CloseGuarReqDto> dto);
}
