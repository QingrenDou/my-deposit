package com.yzc.common.guar.dto.hanhua;

import lombok.Data;

import java.io.Serializable;

/**
 * @description 申请保函-结果
 * @author douqr 2021/6/24 16:09
 */
@Data
public class ApplyGuarRespDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String lgNo; //保函申请编号

    private ApplyGuarRespPayInfoDto payInfo;
}
