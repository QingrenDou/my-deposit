package com.yzc.common.deposit.dto.bank.cwHbky;

import lombok.Data;

/**
 * ESB 系统返回通用结果
 */
@Data
public class EsbBaseRespDto<T> {
    private String code; //200||ok 成功
    private T data; //数据
    private String msg; //描述
    private Integer total; //总数

    /**
     * 设置失败结果
     * @param msg 错误信息
     * @return 对象
     */
    public static EsbBaseRespDto error(String msg) {
        EsbBaseRespDto esbBaseRespDto = new EsbBaseRespDto();
        esbBaseRespDto.code = "500";
        esbBaseRespDto.msg = msg;
        return esbBaseRespDto;
    }
}
