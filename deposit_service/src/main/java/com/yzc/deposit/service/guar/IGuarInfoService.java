package com.yzc.deposit.service.guar;

import com.alibaba.fastjson.JSONObject;
import com.yzc.common.api.PageResult;
import com.yzc.common.api.Result;
import com.yzc.common.guar.dto.common.*;
import com.yzc.common.guar.dto.hanhua.HanHuaCloseResultSyncReqDto;
import com.yzc.common.guar.dto.hanhua.HanHuaSyncResultBaseReqDto;
import com.yzc.common.guar.dto.hanhua.HanHuaOpenResultSyncReqDto;
import com.yzc.common.guar.vo.*;

import java.io.IOException;
import java.util.List;

public interface IGuarInfoService {

    /**
     * 担保申请
     * @param reqVo 请求参数
     * @return 处理结果
     */
    Result<GuarApplyRespVo> applyGuar(GuarApplyReqVo reqVo);

    /**
     * 根据id查询电子保函明细
     * @param guarInfoId 电子保函id
     * @return 电子保函明细
     */
    Result<GuarInfoRespVo> getGuarInfo(Long guarInfoId);

    /**
     * 保存或提交电子保函信息
     * @param reqVo 保存参数
     * @return 处理结果
     */
    Result<GuarInfoSaveRespVo> saveGuarInfo(GuarInfoSaveReqVo reqVo);

    /**
     * 保函支付成功回调
     * @param reqVo 回调参数
     * @return 处理结果
     */
    Result paySuccess(GuarPaySuccessReqVo reqVo);

    /**
     * 投标人端：获取保函列表
     * @param reqVo 入参
     * @return 分页列表
     */
    PageResult<GuarInfoRespVo> pageListBidder(GuarBidderPageReqVo reqVo);

    /**
     * 发起退保
     * @param reqVo 入参
     * @return 处理结果
     */
    Result<String> closeGuar(GuarCloseReqVo reqVo);

    /**
     * （瀚华）同步保函退保结果通知
     * @param reqDto 入参
     * @return 处理结果
     */
    Result syncCloseResult(HanHuaSyncResultBaseReqDto<HanHuaCloseResultSyncReqDto> reqDto);

    /**
     * 修改投标截止时间
     * @param reqVo 入参
     * @return 处理结果
     */
    Result updateSignUpEndTime(SignUpEndTimeUpdateReqVo reqVo);

    /**
     * 修改保函终止状态
     * @param reqVo 入参
     * @return 处理结果
     */
    Result updateAbortive(AbortiveStatusUpdateReqVo reqVo);

    /**
     * （瀚华）同步保函开函结果通知
     * @param reqDto 入参
     * @return 处理结果
     */
    Result syncOpenResult(HanHuaSyncResultBaseReqDto<HanHuaOpenResultSyncReqDto> reqDto);

    /**
     * 通用-同步保函开函结果通知
     * @param reqDto 入参
     * @return 处理结果
     */
    Result syncOpenResultCommon(OpenResultSyncReqDto reqDto);

    /**
     * 通用-同步保函退保结果通知
     * @param reqDto 入参
     * @return 处理结果
     */
    Result syncCloseResultCommon(CloseResultSyncReqDto reqDto);

    /**
     * 通用-同步保函补偿结果通知
     * @param reqDto 入参
     * @return 处理结果
     */
    Result syncCompensationResultCommon(CompensationResultSyncReqDto reqDto);

    /**
     * (业务接口)刷新保函
     * @param reqVo 入参
     * @return 处理结果
     */
    Result<GuarApiRespVo> refreshGuar(RefreshGuarReqVo reqVo);

    /**
     * (业务接口)批量退保
     * @param reqVo 入参
     * @return 处理结果
     */
    Result decBatch(ApplyDecReqVo reqVo);


    /**
     * (业务接口)查询保函列表
     * @param reqVo 入参
     * @return 处理结果
     */
    Result<List<GuarApiRespVo>> queryListApi(QueryListApiReqVo reqVo);

    /**
     * (前端接口)保存非优质采保函
     * @param reqVo 入参
     * @return 处理结果
     */
    Result<GuarInfoSaveRespVo> saveNotYzcGuar(NotYzcGuarSaveReqVo reqVo);

    /**
     * (前端接口)获取CA签章url
     * @return 签章url
     */
    Result<String> getESignUrl(GuarESignReqVo reqVo) throws IOException;

    /**
     * CA签章结果保存
     * @param json json对象
     * @return 操作结果
     */
    Boolean saveSignResult(JSONObject json);

    /**
     * (前端接口)获取CA签章结果
     * @param lgNo lgNo
     * @return 签章结果
     */
    GuarSignResultRespVo getSignResult(String lgNo);

    /**
     * (业务接口)单笔解密
     * @param reqVo 入参
     * @return 处理结果
     */
    Result decSingle(ApplyDecReqVo reqVo);
}
