package com.yzc.deposit.service.deposit;

import com.yzc.common.api.Result;
import com.yzc.common.deposit.vo.ApplyRefundAndInAccListRespVo;
import com.yzc.common.deposit.vo.ApplyRefundBatchReqVo;
import com.yzc.common.deposit.vo.ApplyRefundRecordRespVo;
import com.yzc.common.deposit.vo.InAccRecordRespVo;

import java.util.List;

public interface IApplyRefundRecordService {

    /**
     * 根据子账号查询项目退款记录
     * @param subAcc 子账号
     * @return 批量退款记录
     */
    List<ApplyRefundRecordRespVo> getListBySubAcc(String subAcc);

    /**
     * 根据申请记录id查询入账记录集合
     * @param applyRecordId 申请记录id
     * @return 入账记录集合
     */
    List<InAccRecordRespVo> getInAccListByApplyRecordId(String applyRecordId);

    /**
     * 根据申请记录id查询退款记录和入账记录集合
     * @param applyRecordId 申请记录id
     * @return 退款记录和入账记录集合
     */
    ApplyRefundAndInAccListRespVo getRefundAndInAccRecordListById(String applyRecordId);

    /**
     * 批量申请退款
     * @param applyRefundBatchReqVo 批量申请退款请求参数
     * @return 批量申请退款结果
     */
    Result<String> applyRefundBatch(ApplyRefundBatchReqVo applyRefundBatchReqVo);
}
