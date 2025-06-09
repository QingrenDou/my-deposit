package com.yzc.common.deposit.enums;


import lombok.Getter;

/**
 * 绑定状态枚举
 */
@Getter
public enum BindStatusEnum {
    Normal(1,"正常"),
    Unbind(2,"解绑");

    private Integer code;
    private String desc;

    private BindStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
