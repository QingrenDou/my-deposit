package com.yzc.common.deposit.enums;

import lombok.Getter;

@Getter
public enum TDAuditStatusEnum {
    NotPass(0,"平台审核不通过"),
    Pass(1,"平台审核通过|已转履约"),
    WaitAudit(2,"平台待审核|待划转履约"),
    WaitAccept(4,"平台待受理|待交纳履约金"),
    FirstAuditPass(5,"平台初审通过")
    ;

    private final Integer code;
    private final String text;

    TDAuditStatusEnum(Integer code, String text) {
        this.code = code;
        this.text = text;
    }
}
