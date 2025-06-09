package com.yzc.common.guar.enums;

import lombok.Getter;

/**
 * 保函 启用状态枚举(（-1未启用 1启用 2调试)
 */
@Getter
public enum GuarEnableStatusEnum {

    NotEnable(-1, "未启用"),
    Enable(1, "启用"),
    Debug(2, "调试"),
    ;

    private Integer code;
    private String desc;

    GuarEnableStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
