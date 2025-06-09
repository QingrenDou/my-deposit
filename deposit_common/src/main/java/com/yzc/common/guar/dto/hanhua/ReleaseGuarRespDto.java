package com.yzc.common.guar.dto.hanhua;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description //TODO
 * @author douqr 2021/6/29 13:54
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseGuarRespDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String lgNo; //投标申请编号
    private String resultCode; //状态：成功0000,失败-1
    private String resultMessage; //状态：成功SUCCESS,失败原因
}
