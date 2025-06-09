package com.yzc.common.guar.dto.hanhua;

import lombok.Data;

/**
 * 退保结果通知请求入参
 */
@Data
public class HanHuaCloseResultSyncReqDto {
    private String guaranteeNumber;//保函编号
    private String status;//处理结果（2-退保成功，3-退保失败）
    private String auditOpinion;//时给出未通过原因，其他时为空）
    private String lgNo;//投保申请编号
}
