package com.yzc.common.guar.dto.xingtai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 退保申请
 * @description 退保-入参
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class XingTaiCloseGuarReqDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String reason;//退保原因说明

    /**
     * 保函集合
     */
    private List<XingTaiCloseGuarInfoDto> guaranteeInfoList;

}
