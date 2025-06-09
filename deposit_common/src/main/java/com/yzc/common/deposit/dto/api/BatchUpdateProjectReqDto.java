package com.yzc.common.deposit.dto.api;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 批量更新项目请求参数
 */
@Data
public class BatchUpdateProjectReqDto {

    /**
     * 更新项目请求参数列表
     */
    @NotNull(message = "更新项目请求参数列表不能为空")
    private List<UpdateInfoBySubAccReqDto> updateList;
}
