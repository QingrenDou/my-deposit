package com.yzc.common.guar.dto.hanhua;

import lombok.Data;

/**
 * @description 瀚华通用返回报文
 * @author douqr 2021/6/24 16:47
 */
@Data
public class HanHuaReturnDto<T> {

    private String invokeId; //调用链id
    private String resultCode; //状态：成功0000,失败-1
    private String resultMessage; //状态：成功SUCCESS,失败原因

    private T data;
}
