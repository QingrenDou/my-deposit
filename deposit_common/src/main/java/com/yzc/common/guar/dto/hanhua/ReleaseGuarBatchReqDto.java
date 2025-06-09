package com.yzc.common.guar.dto.hanhua;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @description 批量保函解保-请求
 * @author douqr 2021/6/24 17:50
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseGuarBatchReqDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<HanHuaRequestDto<ReleaseGuarReqDto>> bidders;
}
