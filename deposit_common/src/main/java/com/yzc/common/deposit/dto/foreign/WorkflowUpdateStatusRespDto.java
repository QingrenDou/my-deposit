package com.yzc.common.deposit.dto.foreign;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

/**
 * 工作流回调响应结果
 */
@Data
public class WorkflowUpdateStatusRespDto {
    /**
     * 处理结果 【true成功  false失败】
     */
    @JsonAlias({"Result","result"})
    private Boolean result;

    /**
     * 处理结果描述
     */
    @JsonAlias({"ResultDesc","resultDesc"})
    private String resultDesc;

    public WorkflowUpdateStatusRespDto(Boolean result, String resultDesc) {
        this.result = result;
        this.resultDesc = resultDesc;
    }

    public static WorkflowUpdateStatusRespDto success() {
        return new WorkflowUpdateStatusRespDto(true, "处理成功");
    }

    public static WorkflowUpdateStatusRespDto failed(String resultDesc) {
        return new WorkflowUpdateStatusRespDto(false, resultDesc);
    }
}
