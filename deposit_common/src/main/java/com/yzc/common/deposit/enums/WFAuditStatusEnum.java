package com.yzc.common.deposit.enums;

import lombok.Getter;

@Getter
public enum WFAuditStatusEnum {
    Draft(0,"草稿、未提交"),
    Auditing(1,"审核中"),
    AuditPass(2,"审核通过"),
    AuditNoPass(-1,"审核不通过");


    private final Integer code;
    private final String text;

    WFAuditStatusEnum(Integer code, String text) {
        this.code = code;
        this.text = text;
    }
}
