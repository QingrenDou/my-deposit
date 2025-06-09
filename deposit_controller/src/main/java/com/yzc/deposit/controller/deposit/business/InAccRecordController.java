package com.yzc.deposit.controller.deposit.business;

import com.yzc.common.api.Result;
import com.yzc.common.deposit.vo.InAccRecordRespVo;
import com.yzc.deposit.service.deposit.IInAccRecordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 入账数据查询
 */
@RestController
@RequestMapping(value = "inAccRecord")
public class InAccRecordController {

    @Resource
    private IInAccRecordService inAccRecordService;

    /**
     * 【TD-Web-002】根据子账号查询入账记录
     * @param subAcc 子账号
     * @return 入账记录集合
     */
    @GetMapping(value = "getApplyListBySubAcc")
    public Result<List<InAccRecordRespVo>> getApplyListBySubAcc(@RequestParam(value = "subAcc", required = true, defaultValue = "")String subAcc) {
        List<InAccRecordRespVo> list = inAccRecordService.getApplyListBySubAcc(subAcc);
        return Result.success(list);
    }

    /**
     * 【TD-Web-004】根据入账记录ID查询入账记录
     * @param id 入账记录ID
     * @return 入账记录
     */
    @GetMapping(value = "getById")
    public Result<InAccRecordRespVo> getById(@RequestParam(value = "id", required = true, defaultValue = "")String id) {
        InAccRecordRespVo respVo = inAccRecordService.getById(id);
        return Result.success(respVo);
    }

    /**
     * 【TD-Web-007】已选中保证金入账明细集合
     * @param inAccRecordIdList 入账记录ID集合
     * @return 入账记录集合
     */
    @PostMapping(value = "getListByIds")
    public Result<List<InAccRecordRespVo>> getListByIds(@RequestBody @NotNull List<String> inAccRecordIdList) {
        return Result.success(inAccRecordService.getListByIds(inAccRecordIdList));
    }
}
