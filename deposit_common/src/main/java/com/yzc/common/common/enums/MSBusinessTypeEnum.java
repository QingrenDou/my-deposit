package com.yzc.common.common.enums;

import lombok.Getter;

@Getter
public enum MSBusinessTypeEnum {
    TendBidding(1,"依法招标"),
    SupplyPur(2,"询价"),
    CustomBidding(3,"自主招标"),
    Recruit(4,"招募"),
    Bidding(5,"竞价"),
    Subscription(6,"消息订阅"),
    CreditMoney(7,"诚信保障金"),
    Performance(11,"履约金收取"),
    Recover(12,"追索金收取"),
    Other(99,"其他、非业务类型"),

    ;
    private final Integer code;
    private final String text;

    MSBusinessTypeEnum(Integer code, String text) {
        this.code = code;
        this.text = text;
    }

    public static String getTextByCode(Integer businessType) {

        for (MSBusinessTypeEnum businessTypeEnum : MSBusinessTypeEnum.values()) {
            if (businessTypeEnum.getCode().equals(businessType)) {
                return businessTypeEnum.getText();
            }
        }
        return "";
    }
}
