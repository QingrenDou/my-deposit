package com.yzc.common.deposit.enums;

import lombok.Getter;

/**
 * 项目查询权限类型枚举
 */
@Getter
public enum QueryPermissionTypeEnum {

    Company(1,"公司权限"),
    Dept(2,"部门权限"),
    ChildCompany(3,"子公司权限"),
    User(4,"个人权限"),
    ;

    private Integer code;

    private String text;

    QueryPermissionTypeEnum(Integer code, String text) {
        this.code = code;
        this.text = text;
    }
}
