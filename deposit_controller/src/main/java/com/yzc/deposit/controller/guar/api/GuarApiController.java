package com.yzc.deposit.controller.guar.api;

import com.yzc.common.api.Result;
import com.yzc.common.guar.vo.*;
import com.yzc.deposit.service.guar.IGuarInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 电子保函-业务接口
 */
@RequestMapping(value = "api/guar")
@RestController
public class GuarApiController {

    @Resource
    private IGuarInfoService guarInfoService;

    /**
     * 【guar-api-001】（与业务接口）接收业务保函申请数据
     * @param reqVo 入参
     * @return 返回
     */
    @PostMapping(value = "applyGuar")
    public Result<GuarApplyRespVo> applyGuar(@RequestBody @Validated GuarApplyReqVo reqVo) {
        return guarInfoService.applyGuar(reqVo);
    }

    /**
     * 【guar-api-002】（与业务接口）更新“项目投标截止时间”接口
     * @param reqVo 入参
     * @return 返回
     */
    @PostMapping(value = "updateSignUpEndTime")
    public Result updateSignUpEndTime(@RequestBody @Validated SignUpEndTimeUpdateReqVo reqVo) {
        return guarInfoService.updateSignUpEndTime(reqVo);
    }

    /**
     * 【guar-api-004】（与业务接口）更新“项目异常状态”接口
     * @param reqVo 入参
     * @return 返回
     */
    @PostMapping(value = "updateAbortive")
    public Result updateAbortive(@RequestBody @Validated AbortiveStatusUpdateReqVo reqVo) {
        return guarInfoService.updateAbortive(reqVo);
    }

    /**
     * 【guar-api-005】（与业务接口）刷新保函信息接口
     * @param reqVo 入参
     * @return 返回
     */
    @PostMapping(value = "refreshGuar")
    public Result<GuarApiRespVo> refreshGuar(@RequestBody @Validated RefreshGuarReqVo reqVo) {
        return guarInfoService.refreshGuar(reqVo);
    }

    /**
     * 【guar-api-006】（与业务接口）批量解密保函接口
     * @param reqVo 入参
     * @return 返回
     */
    @PostMapping(value = "decBatch")
    public Result decBatch(@RequestBody @Validated ApplyDecReqVo reqVo) {
        return guarInfoService.decBatch(reqVo);
    }

    /**
     * 【guar-api-008】（与业务接口）单笔解密保函接口
     * @param reqVo 入参
     * @return 返回
     */
    @PostMapping(value = "decSingle")
    public Result decSingle(@RequestBody @Validated ApplyDecReqVo reqVo) {
        return guarInfoService.decSingle(reqVo);
    }

    /**
     * 【guar-api-007】（与业务接口）批量查询保函信息接口
     * @param reqVo 入参
     * @return 返回
     */
    @PostMapping(value = "queryList")
    public Result<List<GuarApiRespVo>> queryList(@RequestBody @Validated QueryListApiReqVo reqVo) {
        return guarInfoService.queryListApi(reqVo);
    }

}
