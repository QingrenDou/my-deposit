package com.yzc.common.guar.enums;

import lombok.Getter;

/**
 * 投标人端：电子保函查询状态
 */
@Getter
public enum GuarStatusQueryBidderEnum {

    Auditing(1,"审核中"),
    OpenSuccess(2,"已出函"),
    OpenFail(3,"未通过"),
    Close(4,"已退保"),

    CloseAudit(5,"退保审核中"),
    ;

    private Integer code;
    private String desc;

    GuarStatusQueryBidderEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
