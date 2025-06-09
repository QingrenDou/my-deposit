package com.yzc.common.guar.vo;

import lombok.Data;

/**
 * 保函CA签署结果
 */
@Data
public class GuarSignResultRespVo {

    /**
     * 签名结果[蒋少：2:签署完成 3:失败 4:拒签]
     */
    private Integer signResult;

    /**
     * 《申请协议书》签署附件关联id
     */
    private String agreementAttRelaId;
}
