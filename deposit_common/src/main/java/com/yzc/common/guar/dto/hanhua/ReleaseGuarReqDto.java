package com.yzc.common.guar.dto.hanhua;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description 保函解保-新接口
 * @author douqr 2021/6/29 13:43
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseGuarReqDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String businessType; //业务类型（01 投标保函，02 政采保函，03 履约保函）
    private String bidNo; //标段编号
    private String lgNo; //保函申请编号
}
