package com.yzc.common.deposit.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ProjectSubAccPageRespVo {
    /**
     * 主键id
     */
    private String projectSubAccId;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 项目编号
     */
    private String projectCode;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 业务类型,同枚举
     */
    private Integer businessType;

    /**
     * 开标时间
     */
    private Date openBidTime;

    /**
     * 公司id
     */
    private String companyId;

    /**
     * 公司名称
     */
    private String companyName;
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
     * 负责人id
     */
    private String purchaserId;
    /**
     * 负责人名称
     */
    private String purchaserName;

    /**
     * 是否确认候选人
     */
    private Integer isConfirmCandidate;

    /**
     * 确认候选人时间
     */
    private Date confirmCandidateTime;

    /**
     * 整个子账户(子账户全称)
     */
    private String wholeSubAcc;

    /*****************************自定义字段**********************************/

    /**
     * 来款记录总数
     */
    private Integer inRecordCount;

    /**
     * 已退款记录总数
     */
    private Integer refundRecordCount;

    /**
     * 业务类型名称
     */
    private String businessTypeStr;

    /*****************************自定义方法**********************************/


}
