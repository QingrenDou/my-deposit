package com.yzc.deposit.controller.deposit.api;

import com.yzc.common.deposit.dto.foreign.WorkflowUpdateStatusReqDto;
import com.yzc.common.deposit.dto.foreign.WorkflowUpdateStatusRespDto;
import com.yzc.deposit.service.deposit.IWorkflowUpdateStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * 工作流回调接口
 */
@Slf4j
@RequestMapping("/api/WFUpdateStatus")
@RestController
public class WorkflowUpdateStatusController {

    @Resource
    private IWorkflowUpdateStatusService workflowUpdateStatusService;


    /**
     * 工作流回调
     * @param reqDto 输入参数
     * @return WorkflowUpdateStatusDto 返回参数
     */
    @PostMapping("/updateStatus")
    public WorkflowUpdateStatusRespDto updateStatus(@RequestBody @NotNull WorkflowUpdateStatusReqDto reqDto) {
        return workflowUpdateStatusService.updateStatus(reqDto);
    }

}
