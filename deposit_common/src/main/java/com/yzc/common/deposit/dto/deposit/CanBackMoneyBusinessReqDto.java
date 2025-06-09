package com.yzc.common.deposit.dto.deposit;

import lombok.Data;

import java.util.Date;

/**
 * 校验 业务是否满足退款条件
 */
@Data
public class CanBackMoneyBusinessReqDto {

    /***************当前入账属性*****************************/
    /**
     * 是否中标
     */
    private Integer isWinBidder;

    /**
     * 是否候选人
     */
    private Integer isCandidate;

    /***************项目属性*****************************/
    /**
     * 是否确认中标人
     */
    private Integer isConfirmBidder;

    /**
     * 确认中标时间
     */
    private Date confirmBidderTime;
    /**
     * 是否终止
     */
    private Integer isAbortive;

    /**
     * 是否确认候选人
     */
    private Integer isConfirmCandidate;

    /**
     * 确认候选人时间
     */
    private Date confirmCandidateTime;
}
