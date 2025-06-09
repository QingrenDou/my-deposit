package com.yzc.deposit.dao.deposit;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yzc.common.deposit.dto.deposit.ApplyRefundRecordRespDto;
import com.yzc.common.deposit.dto.deposit.ApplyRefundRecordSaveReqDto;
import com.yzc.common.deposit.entity.ApplyRefundRecord;

import java.util.List;

public interface IApplyRefundRecordDao extends IService<ApplyRefundRecord> {

    /**
     * 根据子账号查询羡慕退款记录
     * @param subAcc 子账号
     * @return 结果集合
     */
    List<ApplyRefundRecordRespDto> getListBySubAcc(String subAcc);

    /**
     * 根据申请记录id查询退款记录
     * @param applyRecordId 申请记录id
     * @return 结果集合
     */
    ApplyRefundRecordRespDto getById(String applyRecordId);

    /**
     * 保存退款记录
     * @param reqDto 请求参数
     * @return 操作结果
     */
    boolean save(ApplyRefundRecordSaveReqDto reqDto);
}
