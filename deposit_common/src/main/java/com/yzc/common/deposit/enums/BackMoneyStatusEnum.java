package com.yzc.common.deposit.enums;

import lombok.Getter;

/**
 * 退款状态：0退款失败 1退款成功 2处理中
 */
@Getter
public enum BackMoneyStatusEnum {

    FAIL(0, "退款失败"),
    SUCCESS(1, "退款成功"),
    PROCESSING(2, "处理中");

    private Integer code;
    private String desc;

    BackMoneyStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
