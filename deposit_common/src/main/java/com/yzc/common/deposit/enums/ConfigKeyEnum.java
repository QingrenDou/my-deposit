package com.yzc.common.deposit.enums;

import lombok.Getter;

/**
 * 通用配置key关键字
 * 注意：数据库使用的字符串，而非code
 */
@Getter
public enum ConfigKeyEnum {
    T_NoRecBackRemindMsg(1, "业主-不接收操作退款提醒短信"),
    B_NoSendBackMsg(2, "投标人-不发送已退款提醒短信"),
    TBT_InSubAcc(3, "投标通-担保子账户"),
    WFChildCompanyEnable(4, "是否启用子公司工作流"),
    ;

    private final Integer code;
    private final String text;

    ConfigKeyEnum(Integer code, String text) {
        this.code = code;
        this.text = text;
    }
}
