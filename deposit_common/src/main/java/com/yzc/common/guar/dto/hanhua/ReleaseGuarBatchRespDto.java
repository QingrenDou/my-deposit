package com.yzc.common.guar.dto.hanhua;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @description 保函解保-结果
 * @author douqr 2021/6/24 17:51
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseGuarBatchRespDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String invokeId; //调用链id
    private String resultCode; //状态：成功0000,失败-1
    private String resultMessage; //状态：成功SUCCESS,失败原因

    private List<ReleaseGuarRespDto> results;

}
