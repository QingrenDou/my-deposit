package com.yzc.common.guar.enums;

import lombok.Getter;

/**
 * 索赔状态枚举
 * 1-已发起 2-理赔成功，3-理赔失败
 */
@Getter
public enum GuarCompensateStatusEnum {
    NOT_START(0, "未开始"),
    START(1, "已发起"),
    SUCCESS(2, "理赔成功"),
    FAIL(3, "理赔失败"),
    ;

    private Integer code;
    private String desc;

    GuarCompensateStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
