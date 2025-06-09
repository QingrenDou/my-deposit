package com.yzc.common.guar.enums;

import lombok.Getter;

/**
 * 保函类型枚举
 */
@Getter
public enum GuarTypeCodeEnum {
    HanHua(1,"瀚华"),

    XingTai(2,"兴泰"),

    GuoKong(3,"国控"),
            ;

    private Integer code;
    private String desc;

    GuarTypeCodeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
