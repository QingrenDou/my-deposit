package com.yzc.deposit.controller.guar.business;

import com.yzc.common.api.PageResult;
import com.yzc.common.api.Result;
import com.yzc.common.guar.vo.ApplyCompensationReqVo;
import com.yzc.common.guar.vo.GuarInfoPurRespVo;
import com.yzc.common.guar.vo.GuarPurPageReqVo;
import com.yzc.deposit.service.guar.IGuarPurService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 采购人端-保函管理
 */
@RestController
@RequestMapping(value = "guarPur")
public class GuarPurController {

    @Resource
    private IGuarPurService guarPurService;

    /**
     * 【guar-pur-001】采购人端-保函管理-保函列表
     */
    @PostMapping(value = "pageListPur")
    public Result<PageResult<GuarInfoPurRespVo>> pageListPur(@RequestBody @Validated GuarPurPageReqVo reqVo){
        return Result.success(guarPurService.pageListPur(reqVo));
    }

    /**
     * 【guar-pur-02】采购人端-保函管理-申请索赔
     */
    @PostMapping(value = "applyCompensation")
    public Result<String> applyCompensation(@RequestBody @Validated ApplyCompensationReqVo reqVo){
        return guarPurService.applyCompensation(reqVo);
    }

}
