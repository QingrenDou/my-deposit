package com.yzc.common.guar.dto.hanhua;

import lombok.Data;

/**
 * 瀚华通知-基础入参
 */
@Data
public class HanHuaSyncResultBaseReqDto<T> {

    //调用链id，不传时由我方生成并返回
    public String invokeId;

    //数据
    public T data;
}
