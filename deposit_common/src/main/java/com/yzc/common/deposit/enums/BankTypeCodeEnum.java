package com.yzc.common.deposit.enums;

import lombok.Getter;

/**
 * 银行类型唯一标识
 */
@Getter
public enum BankTypeCodeEnum {
    YZCPingAn(1, "平安银行-优质采"),
    CMBBankND(2, "招商银行-宁电物流"),
    CMBBankSD(3, "招商银行-山东优质采"),
    CMBBankSDGC(4, "招商银行-山东工程咨询"),
    CCBTlys(5, "建设银行-铜陵有色-鑫铜监理"),
    HSBankBzcj(6, "亳州城建-徽商银行"),
    CMBBankNM(7, "招商银行（云直连）-内蒙"),
    CCBTlys_JC(8, "建设银行-铜陵有色-金诚招标"),
    YaoduBzcj(9, "亳州城建-药都银行"),
    CMBYZC(10, "招商银行-优质采"),
    CMBBankNM_BaoTou(15, "招商银行（云直连）-内蒙古招标有限责任公司包头分公司"),
    CMBBankNM_BaYanZhuoEr(16, "招商银行（云直连）-内蒙古招标有限责任公司巴彦淖尔市分公司"),
    BCM_NMDK(20, "交通银行-内蒙地矿"),
    SPDBank_HJT(25, "浦发银行-合交投"),
    PingAnBengBu(26, "平安银行-蚌埠城投"),
    CW_HBKY(27, "财务系统-淮北矿业"),

    CMBBank_NMJTSJY(28, "内蒙交通设计院-招商云直连"),
    ;

    private final Integer code;
    private final String text;

    BankTypeCodeEnum(Integer code, String text) {
        this.code = code;
        this.text = text;
    }

}
