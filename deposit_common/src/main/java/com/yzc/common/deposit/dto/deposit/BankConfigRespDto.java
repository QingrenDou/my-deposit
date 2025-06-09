package com.yzc.common.deposit.dto.deposit;

import lombok.Data;

import java.util.Date;

@Data
public class BankConfigRespDto {

    /**
     * 主键id
     */
    private Integer bankConfigId;

    /**
     * 银行类型编码
     */
    private Integer bankTypeCode;

    /**
     * 银行ip
     */
    private String bankIp;

    /**
     * 银行端口
     */
    private String bankPort;

    /**
     * 客户id
     */
    private String consumerId;

    /**
     * 主账户
     */
    private String mainAccount;

    /**
     * 主账户名称
     */
    private String mainAccountName;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 银行编码
     */
    private String bankCode;

    /**
     * 银行类型
     */
    private Integer bankMold;

    /**
     * 使用状态
     */
    private Integer useStatus;

    /**
     * 删除状态
     */
    private Integer delStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 银行http地址
     */
    private String bankHttpUrl;

}
