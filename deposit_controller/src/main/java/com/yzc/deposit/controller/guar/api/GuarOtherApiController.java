package com.yzc.deposit.controller.guar.api;

import com.alibaba.fastjson.JSONObject;
import com.yzc.common.api.Result;
import com.yzc.common.guar.dto.common.*;
import com.yzc.common.guar.dto.hanhua.HanHuaCloseResultSyncReqDto;
import com.yzc.common.guar.dto.hanhua.HanHuaSyncResultBaseReqDto;
import com.yzc.common.guar.dto.hanhua.HanHuaOpenResultSyncReqDto;
import com.yzc.common.guar.vo.GuarPaySuccessReqVo;
import com.yzc.deposit.service.guar.IGuarInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 电子保函-与网关、订单中心等其他服务接口
 */
@RestController
@RequestMapping(value = "api/guarOther")
public class GuarOtherApiController {

    @Resource
    private IGuarInfoService guarInfoService;

    /**
     * (订单中心)支付成功回调
     * @param reqVo 入参
     * @return 结果
     */
    @PostMapping(value = "paySuccess")
    public Result paySuccess(@RequestBody @Validated GuarPaySuccessReqVo reqVo) {
        return guarInfoService.paySuccess(reqVo);
    }

    /**
     * (网关)瀚华专用-电子保函退保结果通知
     * @param reqDto 入参
     * @return 结果
     */
    @PostMapping(value = "syncCloseResult")
    public Result syncCloseResult(@RequestBody @Validated HanHuaSyncResultBaseReqDto<HanHuaCloseResultSyncReqDto> reqDto) {
        return guarInfoService.syncCloseResult(reqDto);
    }

    /**
     * (网关)瀚华专用-电子保函开函结果通知
     * @param reqDto 入参
     * @return 结果
     */
    @PostMapping(value = "syncOpenResult")
    public Result syncOpenResult(@RequestBody @Validated HanHuaSyncResultBaseReqDto<HanHuaOpenResultSyncReqDto> reqDto) {
        return guarInfoService.syncOpenResult(reqDto);
    }

    /*******************************************电子保函-通用回调接口 start **************************************************************************/

    /**
     * (网关)通用-电子保函退保结果通知
     * @param reqDto 入参
     * @return 结果
     */
    @PostMapping(value = "syncOpenResultCommon")
    public Result syncOpenResultCommon(@RequestBody @Validated OpenResultSyncReqDto reqDto) {
        return guarInfoService.syncOpenResultCommon(reqDto);
    }

    /**
     * (网关)通用-电子保函退保结果通知
     * @param reqDto 入参
     * @return 结果
     */
    @PostMapping(value = "syncCloseResultCommon")
    public Result syncCloseResultCommon(@RequestBody @Validated CloseResultSyncReqDto reqDto) {
        return guarInfoService.syncCloseResultCommon(reqDto);
    }

    /**
     * (网关)通用-电子保函索赔结果通知
     * @param reqDto 入参
     * @return 结果
     */
    @PostMapping(value = "syncCompensationResultCommon")
    public Result syncCompensationResultCommon(@RequestBody @Validated CompensationResultSyncReqDto reqDto) {
        return guarInfoService.syncCompensationResultCommon(reqDto);
    }



    /*******************************************电子保函-通用回调接口 end **************************************************************************/

    /**
     * 保存电子保函电子签名结果
     * @param json 入参
     * @return 结果
     */
    @PostMapping(value = "saveESignResult")
    public Result<Boolean> saveESignResult(@RequestBody JSONObject json) {
        return Result.success(guarInfoService.saveSignResult(json));
    }
}
