package com.yzc.common.deposit.enums;

import lombok.Getter;

@Getter
public enum TDBackOperTypeEnum {

    BackMoney(1,"退款"),
    DeductMoney(2,"扣款"),
    Performance(3,"转履约"),
    UseFee(4,"平台使用费扣款"),
    PerformanceUnderLine(5,"转线下履约"),
    ManualLock(6,"手动锁定"),
    GuarPerformance(9,"履约金收入");

    private final Integer code;
    private final String text;

    TDBackOperTypeEnum(Integer code, String text) {
        this.code = code;
        this.text = text;
    }
}
