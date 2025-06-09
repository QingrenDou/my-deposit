package com.yzc.common.guar.dto.hanhua;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @description 保函查询-请求
 * @author douqr 2021/6/24 17:11
 */
@Data
@Builder
public class HanHuaQueryGuarReqDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String lgNo; //保函申请编号
}
