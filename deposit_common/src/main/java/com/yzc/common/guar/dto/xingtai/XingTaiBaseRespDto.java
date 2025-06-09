package com.yzc.common.guar.dto.xingtai;

import lombok.Data;

/**
 * 兴泰 通用入参
 */
@Data
public class XingTaiBaseRespDto<T> {

    //调用链id
    public String invokeId;

    //状态：成功0000,失败-1
    public String resultCode;

    //状态：成功SUCCESS,失败原因
    public String resultMessage;

    //返回数据
    public T data;

    public XingTaiBaseRespDto() {
    }

    public XingTaiBaseRespDto(String invokeId, String resultCode, String resultMessage, T data) {
        this.invokeId = invokeId;
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        this.data = data;
    }

    public static <T> XingTaiBaseRespDto<T> success(String invokeId) {
        return new XingTaiBaseRespDto<T>(invokeId, "0000", "SUCCESS", null);
    }

    public static <T> XingTaiBaseRespDto<T> success(String invokeId, T data) {
        return new XingTaiBaseRespDto<T>(invokeId, "0000", "SUCCESS", data);
    }

    public static <T> XingTaiBaseRespDto<T> fail(String invokeId, String resultCode, String resultMessage) {
        return new XingTaiBaseRespDto<T>(invokeId, resultCode, resultMessage, null);
    }
}
