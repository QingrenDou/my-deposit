package com.yzc.deposit.service.deposit.impl;

import cn.hutool.json.JSONUtil;
import com.yzc.common.deposit.dto.foreign.WorkflowUpdateStatusReqDto;
import com.yzc.common.deposit.dto.foreign.WorkflowUpdateStatusRespDto;
import com.yzc.deposit.service.deposit.IWorkflowUpdateStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 工作流处理
 */
@Slf4j
@Service
public class WorkflowUpdateStatusServiceImpl implements IWorkflowUpdateStatusService {

    /**
     * 工作流回调
     *
     * @param reqDto 输入参数
     * @return WorkflowUpdateStatusDto 返回参数
     */
    @Override
    public WorkflowUpdateStatusRespDto updateStatus(WorkflowUpdateStatusReqDto reqDto) {
        log.info("接收工作流回调=====>开始,输入参数:{}", JSONUtil.toJsonStr(reqDto));

        WorkflowUpdateStatusRespDto respDto = WorkflowUpdateStatusRespDto.success();

        //TODO 业务处理


        log.info("接收工作流回调=====>结束,结果:{}", JSONUtil.toJsonStr(respDto));
        return respDto;
    }
}
