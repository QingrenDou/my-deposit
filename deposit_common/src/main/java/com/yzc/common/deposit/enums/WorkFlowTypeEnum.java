package com.yzc.common.deposit.enums;

import lombok.Getter;

@Getter
public enum WorkFlowTypeEnum {
    SingleBack(9300,"保证金单笔退款"),
    ProjectBack(9304,"保证金按项目退款"),
    ;

    private final Integer code;
    private final String text;

    WorkFlowTypeEnum(Integer code, String text) {
        this.code = code;
        this.text = text;
    }
}
