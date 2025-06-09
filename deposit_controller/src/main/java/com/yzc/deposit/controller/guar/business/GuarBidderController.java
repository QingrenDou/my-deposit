package com.yzc.deposit.controller.guar.business;


import com.yzc.common.api.PageResult;
import com.yzc.common.api.Result;
import com.yzc.common.guar.vo.*;
import com.yzc.deposit.service.common.IGuarCommonService;
import com.yzc.deposit.service.guar.IGuarInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 投标人端-电子保函管理
 */
@RestController
@RequestMapping(value = "guarBidder")
public class GuarBidderController {

    @Resource
    private IGuarInfoService guarInfoService;
    @Resource
    private IGuarCommonService guarCommonService;

    /**
     * 【guar-bidder-001】（前端接口）投标人端“我的保函”分页列表接口开发
     * @param reqVo 查询入参
     * @return 分页列表
     */
    @PostMapping(value = "pageList")
    public Result<PageResult<GuarInfoRespVo>> pageList(@RequestBody @Validated GuarBidderPageReqVo reqVo) {
        return Result.success(guarInfoService.pageListBidder(reqVo));
    }

    /**
     * 【guar-bidder-002】（前端接口）投标人端“我的保函”》“退保”提交功能开发
     * @param reqVo 入参
     * @return 退保结果
     */
    @PostMapping(value ="closeGuar")
    public Result<String> closeGuar(@RequestBody @Validated GuarCloseReqVo reqVo) {
        return guarInfoService.closeGuar(reqVo);
    }

    /**
     * 【guar-bidder-003】（前端接口）投标人端“我的保函”》“非优质采项目”提交功能开发
     * @param reqVo 入参
     * @return 暂存结果
     */
    @PostMapping(value = "saveNotYzcGuar")
    public Result<GuarInfoSaveRespVo> saveNotYzcGuar(@RequestBody @Validated NotYzcGuarSaveReqVo reqVo) {
        return guarInfoService.saveNotYzcGuar(reqVo);
    }

    /**
     * 【guar-bidder-004】（前端接口）投标人端随机匹配保函
     * @return 随机保函结果
     */
    @GetMapping(value = "getRandomGuarOrg")
    public Result<RandomGuarOrgRespVo> getRandomGuarOrg() {
        return guarCommonService.getRandomGuarOrg();
    }

}
