package com.yzc.common.guar.dto.hanhua;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description 瀚华通用请求报文
 * @author douqr 2021/6/24 19:38
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HanHuaRequestDto<T> {
    private String invokeId; //调用链id，不传时由我方生成并返回
    private T data;
}
