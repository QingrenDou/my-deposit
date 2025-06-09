package com.yzc.common.deposit.enums;

import lombok.Getter;

@Getter
public enum BankMoldEnum {

    PingAn(1,"平安银行(前置机版)"),
    CMB(2,"招商银行(前置机模式)"),
    CCB(3,"建设银行"),
    HSBank(4,"徽商银行"),
    CMBCloud(5,"招商银行云直连(AES模式)"),
    YaoDu(6,"药都银行"),
    CMBYZC(7,"招商银行（优质采对接）"),
    BCMFront(8,"交通银行（前置机模式）"),
    PingAnOpenApi(9,"平安银行（开放银行版）"),
    SPDBank(10,"浦发银行-开放银行银行版"),
    CW_HBKY(11,"财务系统-淮北矿业"),

    CMBCloudSM(101,"招商银行云直连(SM模式)"),

    ;


    private final Integer code;
    private final String text;

    BankMoldEnum(Integer code, String text) {
        this.code = code;
        this.text = text;
    }
}
