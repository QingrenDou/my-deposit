package com.yzc.common.guar.enums;

import lombok.Getter;

/**
 * 保函申请状态
 */
@Getter
public enum GuarApplyStatusEnum {

    Temp(0, "暂存"),
    Apply(1, "申请"),

    ;

    private Integer code;
    private String desc;

    GuarApplyStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}

