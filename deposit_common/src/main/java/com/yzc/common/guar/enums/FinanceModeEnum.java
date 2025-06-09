package com.yzc.common.guar.enums;

import lombok.Getter;

/**
 * 对接金融结构类型
 */
@Getter
public enum FinanceModeEnum {
    HanHua(1, "瀚华"),
    XingTai(2, "兴泰"),
    ;

    private int code;
    private String desc;

    FinanceModeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
