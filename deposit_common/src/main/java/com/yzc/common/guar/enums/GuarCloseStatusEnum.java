package com.yzc.common.guar.enums;

import lombok.Getter;

/**
 * 退保状态枚举
 * 数据库状态：1退保成功 0未退 -1退保失败 2退保中
 * 注意：瀚华返回的状态略有区别，2退保成功 3退保失败
 */
@Getter
public enum GuarCloseStatusEnum {
    None(0, "初始状态"),
    SUCCESS(1, "退保成功"),
    FAIL(-1, "退保失败"),
    ING(2, "退保中"),
    ;

    private Integer code;
    private String desc;

    GuarCloseStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
