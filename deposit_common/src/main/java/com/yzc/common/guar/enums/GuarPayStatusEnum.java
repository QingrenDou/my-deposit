package com.yzc.common.guar.enums;

import lombok.Getter;

/**
 * 电子保函 支付状态
 * 00-支付成功,01-支付初始化,02-支付处理中,03-支付失败,04-支付异常,05-支付订单关闭,06-退款中,07-退款成功,08-退款失败
 */
@Getter
public enum GuarPayStatusEnum {
    Success("00", "成功"),
    Init("01", "初始化"),
    Processing("02", "处理中"),
    Fail("03", "失败"),
    Exception("04", "异常"),
    Close("05", "订单关闭"),
    Refunding("06", "退款中"),
    RefundSuccess("07", "退款成功"),
    RefundFail("08", "退款失败"),
    ;

    private String code;
    private String desc;

    GuarPayStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
