package com.yzc.common.guar.enums;

import lombok.Getter;

/**
 * 开函状态（0-未申请 1-审核中 2-通过，3-未通过 ，4-取消，6-已解保，7-已拒绝）
 */
@Getter
public enum GuarOpenStatusEnum {
    None(0, "未申请"),
    Auditing(1, "审核中"),
    Success(2, "通过"),
    Fail(3, "未通过"),
    Cancel(4, "取消"),
    Remove(5, "已解保"),
    Refuse(7, "已拒绝"),
    ;

    private Integer code;
    private String desc;

    GuarOpenStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
