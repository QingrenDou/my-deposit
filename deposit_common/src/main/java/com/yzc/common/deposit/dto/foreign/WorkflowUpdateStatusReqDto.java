package com.yzc.common.deposit.dto.foreign;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

/**
 * 工作流回调 入参
 */
@Data
public class WorkflowUpdateStatusReqDto {

    /**
     * 工作流类型 使用枚举：WorkFlowType
     */
    @JsonAlias({"WorkFlowType","workFlowType"})
    private Integer workFlowType;

    /**
     * 更新类型：1首次提交  2审核通过 3 审核不通过
     */
    @JsonAlias({"UpdateType","updateType"})
    private Integer updateType;

    /**
     * 业务id
     */
    @JsonAlias({"BusinessId","businessId"})
    private String businessId;

    /**
     * 首次提交时更新GroupID
     */
    @JsonAlias({"GroupId","groupId"})
    private String groupId;

}
