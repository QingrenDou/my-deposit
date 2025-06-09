package com.yzc.deposit.service.deposit;

import com.yzc.common.deposit.dto.foreign.WorkflowUpdateStatusReqDto;
import com.yzc.common.deposit.dto.foreign.WorkflowUpdateStatusRespDto;

public interface IWorkflowUpdateStatusService {
    /**
     * 工作流回调
     * @param reqDto 输入参数
     * @return WorkflowUpdateStatusDto 返回参数
     */
    WorkflowUpdateStatusRespDto updateStatus(WorkflowUpdateStatusReqDto reqDto);
}
