package com.yzc.common.guar.dto.hanhua;

import lombok.Data;

import java.io.Serializable;

/**
 * @author douqr 2021/6/24 16:52
 * @description //TODO
 */
@Data
public class ApplyGuarRespPayInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String url;//支付链接
    private String payAmount;//支付金额
}
