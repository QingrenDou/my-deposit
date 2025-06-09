package com.yzc.common.deposit.enums;

import lombok.Getter;

/**
 * 使用状态枚举[用于配置项、银行配置等]
 */
@Getter
public enum UseStatusEnum {
    Cancel(0,"未启用"),
    Normal(1,"使用中"),
    Test(2,"测试中"),
    ;

    private final Integer code;
    private final String text;

    UseStatusEnum(Integer code, String text) {
        this.code = code;
        this.text = text;
    }
}
