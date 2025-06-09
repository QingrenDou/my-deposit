package com.yzc.common.deposit.dto.api;

import lombok.Data;

@Data
public class BidderInfoDto {

    /**
     * 流水号
     */
    private String bankSeqNo;

    /**
     * 投标人id
     */
    private String bidderId;

    /**
     * 投标人名称
     */
    private String bidderName;

    /**
     * 报名人id
     */
    private String signUpUserId;

    /**
     * 报名人名称
     */
    private String signUpUserName;

    /**
     * 报名人手机号
     */
    private String signUpUserPhone;

}
