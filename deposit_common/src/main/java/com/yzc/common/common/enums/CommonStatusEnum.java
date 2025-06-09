package com.yzc.common.common.enums;

import lombok.Getter;

/**
 * 通用状态
 */
@Getter
public enum CommonStatusEnum {
    Yes(1,"是"),
    No(0,"否"),
    ;

    private Integer code;
    private String message;

    CommonStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
