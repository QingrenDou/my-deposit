package com.yzc.common.deposit.enums;

import lombok.Getter;

/**
 * 根据项目编号更新项目信息枚举（仅保留项目基本部分）
 */
@Getter
public enum UpdateProjectTypePCodeEnum {
    OpenBidTime(1,"修改开标时间"),
    IsAbortive(5,"修改异常状态"),
    ConfirmBidder(4,"确定中标人状态"),
    ConfirmCandidate(6,"确定中标候选人"),
    ;
    private final Integer code;
    private final String text;

    UpdateProjectTypePCodeEnum(Integer code, String text) {
        this.code = code;
        this.text = text;
    }
}
