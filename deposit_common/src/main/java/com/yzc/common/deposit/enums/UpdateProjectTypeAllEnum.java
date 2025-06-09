package com.yzc.common.deposit.enums;

import lombok.Getter;

/**
 * 修改项目状态类型枚举
 */
@Getter
public enum UpdateProjectTypeAllEnum {
    OpenBidTime(1,"修改开标时间"),
    ConfirmBidder(4,"修改成交状态"),
    IsAbortive(5,"修改异常状态"),
    IsConfirmCandidate(6,"修改确定中标候选人状态"),
    AutoBackMoney(7,"修改自动退款状态"),
    ProjectName(8,"修改项目名称"),
    ProjectRestart(9,"修改项目状态为重启"),
    WinBidderList(10,"重新设置项目中标人"),
    WinBidderWithFeeList(11,"修改中标人及服务费"),
    WinCandidateList(12,"重新设置项目候选人"),
    SyncBidderList(13,"设置来款绑定投标人信息"),
    ;

    private final Integer code;
    private final String text;

    UpdateProjectTypeAllEnum(Integer code, String text) {
        this.code = code;
        this.text = text;
    }
}
