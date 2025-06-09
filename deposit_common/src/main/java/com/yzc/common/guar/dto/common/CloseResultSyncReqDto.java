package com.yzc.common.guar.dto.common;

import lombok.Data;

/**
 * 统一-退保结果通知请求入参
 */
@Data
public class CloseResultSyncReqDto {
    private String lgNo;//投保申请编号
    private String status;//处理结果（2-退保成功，3-退保失败）
    private String auditOpinion;//时给出未通过原因，其他时为空）
}
