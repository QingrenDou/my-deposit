package com.yzc.deposit.service.guar;

import com.yzc.common.api.PageResult;
import com.yzc.common.api.Result;
import com.yzc.common.guar.vo.ApplyCompensationReqVo;
import com.yzc.common.guar.vo.GuarInfoPurRespVo;
import com.yzc.common.guar.vo.GuarPurPageReqVo;

/**
 * 电子保函-采购人端操作相关方法
 * 注释：由于 GuarInfoService方法太多，不利于阅读，所以按业务操作进行拆分
 */
public interface IGuarPurService {

    /**
     * 采购人端：获取保函列表
     * @param reqVo 入参
     * @return 分页列表
     */
    PageResult<GuarInfoPurRespVo> pageListPur(GuarPurPageReqVo reqVo);

    /**
     * 采购人端：申请索赔
     * @param reqVo 索赔入参
     * @return 索赔结果
     */
    Result applyCompensation(ApplyCompensationReqVo reqVo);
}
