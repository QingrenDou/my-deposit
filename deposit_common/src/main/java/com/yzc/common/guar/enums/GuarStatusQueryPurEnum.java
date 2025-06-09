package com.yzc.common.guar.enums;

import lombok.Getter;

/**
 * 采购人端 保函查询枚举
 */
@Getter
public enum GuarStatusQueryPurEnum {
    Normal(1, "已出函"),
    Close(2, "已退保"),
    Complaining(3, "索赔中"),
    Complained(4, "已索赔")
    ;

    private Integer code;
    private String desc;
    GuarStatusQueryPurEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
