package com.yzc.common.guar.dto.common;

import lombok.Data;

/**
 * 统一索赔结果同步
 */
@Data
public class CompensationResultSyncReqDto {
    private String lgNo;//投保申请编号
    private String status;//处理结果（2-理赔成功，3-理赔失败）
    private String receiverName;//受理人
    private String receiverPhone;//受理人联系方式
    private String remark;//处理意见
}
