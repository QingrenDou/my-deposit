package com.yzc.deposit.controller.deposit.business;

import com.yzc.common.api.Result;
import com.yzc.common.deposit.vo.ApplyRefundAndInAccListRespVo;
import com.yzc.common.deposit.vo.ApplyRefundBatchReqVo;
import com.yzc.common.deposit.vo.ApplyRefundRecordRespVo;
import com.yzc.deposit.service.deposit.IApplyRefundRecordService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 项目批量退款
 */
@RestController
@RequestMapping("applyRefundRecord")
public class ApplyRefundRecordController {

    @Resource
    private IApplyRefundRecordService applyRefundRecordService;

    /**
     * 【TD-Web-005】保证金已申请退款列表接口
     *
     * @param subAcc 保证金子账户
     * @return 申请记录列表
     */
    @GetMapping("getListBySubAcc")
    public Result<List<ApplyRefundRecordRespVo>> getListBySubAcc(@RequestParam(value = "subAcc", required = true) String subAcc) {
        return Result.success(applyRefundRecordService.getListBySubAcc(subAcc));
    }

    /**
     * 【TD-Web-006】保证金已申请退款详情接口
     *
     * @param applyRecordId 申请记录id
     * @return 明细
     */
    @GetMapping(value = "getRefundAndInAccListById")
    public Result<ApplyRefundAndInAccListRespVo> getRefundAndInAccListById(@RequestParam(value = "applyRecordId", required = true) String applyRecordId) {
        return Result.success(applyRefundRecordService.getRefundAndInAccRecordListById(applyRecordId));
    }

    /**
     * 【TD-Web-003】批量发起退款接口
     *
     * @param applyRefundBatchReqVo 请求参数
     * @return 操作结果
     */
    @PostMapping(value = "applyRefundBatch")
    public Result<String> applyRefundBatch(@RequestBody @Validated ApplyRefundBatchReqVo applyRefundBatchReqVo){
        return applyRefundRecordService.applyRefundBatch(applyRefundBatchReqVo);
    }
}
