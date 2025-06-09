package com.yzc.deposit.controller.guar.business;

import com.yzc.common.api.Result;
import com.yzc.common.guar.util.GuarUtil;
import com.yzc.common.guar.vo.*;
import com.yzc.deposit.service.guar.IGuarInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * 电子保函-web接口
 */
@RestController
@RequestMapping(value = "guarInfo")
public class GuarInfoController {

    @Resource
    private IGuarInfoService guarInfoService;

    /**
     * 【guar-info-001】（前端接口）保函申请信息查询接口开发
     * @param guarInfoId 电子保函id
     * @return 电子保函信息
     */
    @RequestMapping(value = "getGuarInfo")
    public Result<GuarInfoRespVo> getGuarInfo(@RequestParam("guarInfoId") @NotNull Long guarInfoId) {
        return guarInfoService.getGuarInfo(guarInfoId);
    }

    /**
     * 【guar-info-002】（前端接口）电子保函申请提交接口开发
     * @param reqVo 保函申请信息
     * @return 保函申请id
     */
    @PostMapping(value = "saveGuarInfo")
    public Result<GuarInfoSaveRespVo> saveGuarInfo(@RequestBody @NotNull GuarInfoSaveReqVo reqVo){
        return guarInfoService.saveGuarInfo(reqVo);
    }

    /**
     * 【guar-info-003】（前端接口）根据担保金额计算支付金额
     * @param amount
     * @return
     */
    @GetMapping(value = "getPayMoney")
    public Result<BigDecimal> getPayMoney(@RequestParam("amount") @NotNull BigDecimal amount){
        return Result.success(GuarUtil.getPayMoney(amount));
    }

    /**
     * 【guar-info-004】（前端接口）获取电子签名url
     * @param reqVo 申请信息
     * @return 签名url
     */
    @PostMapping(value = "getESignUrl")
    public Result<String> getESignUrl(@RequestBody @NotNull GuarESignReqVo reqVo) throws IOException {
        return guarInfoService.getESignUrl(reqVo);
    }

    /**
     * 【guar-info-005】（前端接口）获取电子签名结果
     * @param lgNo 保函编号
     * @return 签署结果
     */
    @GetMapping(value = "getSignResult")
    public Result<GuarSignResultRespVo> getSignResult(@RequestParam("lgNo") @NotBlank String lgNo) {
        return Result.success(guarInfoService.getSignResult(lgNo));
    }
}
