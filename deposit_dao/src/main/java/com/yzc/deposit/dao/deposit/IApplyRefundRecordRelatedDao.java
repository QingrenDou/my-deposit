package com.yzc.deposit.dao.deposit;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yzc.common.deposit.dto.deposit.ApplyRefundRecordRelatedDto;
import com.yzc.common.deposit.entity.ApplyRefundRecordRelated;

import java.util.List;

public interface IApplyRefundRecordRelatedDao extends IService<ApplyRefundRecordRelated> {

    /**
     * 根据申请记录id查询退款记录关联子表集合
     * @param applyRecordId 申请记录id
     * @return 退款记录关联子表集合
     */
    List<ApplyRefundRecordRelatedDto> getListByApplyRecordId(String applyRecordId);

    /**
     * 批量保存
     * @param relatedList 关联子表集合
     * @return 操作结果
     */
    boolean batchSave(List<ApplyRefundRecordRelatedDto> relatedList);
}
