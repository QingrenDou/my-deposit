package com.yzc.common.deposit.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ApplyRefundBatchReqVo {

    /**
     * 保证金子账户
     */
    @NotBlank(message = "保证金子账户不能为空")
    private String inSubAcc;

    /**
     * 退款退款申请明细
     */
    @NotNull(message = "退款退款申请明细不能为空")
    private List<ApplyRecordDetailReqVo> applyRecordList;
}
