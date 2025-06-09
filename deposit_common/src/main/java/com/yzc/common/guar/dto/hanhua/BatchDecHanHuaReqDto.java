package com.yzc.common.guar.dto.hanhua;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description 批量申请解密
 * @author douqr 2021/6/26 8:53
 */
@Data
@Builder
public class BatchDecHanHuaReqDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<HanHuaRequestDto<DecHanHuaReqDto>> bidders;

}
