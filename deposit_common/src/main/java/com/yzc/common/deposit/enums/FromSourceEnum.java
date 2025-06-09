package com.yzc.common.deposit.enums;

import lombok.Getter;

@Getter
public enum FromSourceEnum {
    Bank(1, "银行入账"),
    Tbt(2, "投标通-担保"),
    Guar(3, "平台担保"),

    ;

    private final Integer code;
    private final String text;

    FromSourceEnum(Integer code, String text) {
        this.code = code;
        this.text = text;
    }
}
