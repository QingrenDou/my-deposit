package com.yzc.common.deposit.dto.api;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 修改项目状态-入参
 */
@Data
public class UpdateInfoBySubAccReqDto {
    /**
     * 保证金子账户
     */
    @NotBlank(message = "保证金子账户不能为空")
    private String subAcc;

    /**
     * 更新类型，见枚举：UpdateProjectTypeAllEnum
     */
    @NotNull(message = "更新类型不能为空")
    private Integer updateType;

    /**
     * 【UpdateType==1 时，必填】开标时间【yyyy-MM-dd HH:mm:ss】
     */
    private String openBidTimeStr;

    /**
     * 【UpdateType==4 时，选填】中标人确定时间【yyyy-MM-dd HH:mm:ss】,如果为空会默认为当前时间
     */
    private String confirmBidderTimeStr;

    /**
     * 【UpdateType==5 时，必填】异常状态【0：正常，1：异常】
     */
    private Integer isAbortive;

    /**
     * 【UpdateType==6 时，选填】确定中标候选人时间【yyyy-MM-dd HH:mm:ss】,如果为空会默认为当前时间
     */
    private String confirmCandidateTimeStr;

    /**
     * 【UpdateType==7 时，必填】是否自动退款【0：否，1：是】
     */
    private Integer isAutoBackMoney;

    /**
     * 【UpdateType==8 时，必填】项目名称
     */
    private String projectName;

    /**
     * 【UpdateType==9 时，必填】重启项目时，需要重新启用的担保流水号集合
     * 【示例：项目发了三次，1次有两笔担保且做了异常处理，2次有两笔新的担保且做了异常处理，3次又有两笔新担保也做了异常处理，假如3次项目触发了重启操作，此时仅推送3次项目的两笔担保流水号】
     * 部署版，用不上
     */
    //private List<String> restartSeqNos;

    /**
     * 【UpdateType==10\11 时，必填】 流水号集合（注意：会先还原项目原来数据）
     */
    private List<String> bankSeqNoList;

    /**
     * 【UpdateType==12 时，必填】中标人以及服务费用（注意：会先还原项目原来数据）
     */
    private List<WinBidderWithFeeDto> bidderWithFeeList;

    /**
     * 【UpdateType==13 时，必填】来款绑定投标人信息
     */
    private List<BidderInfoDto> bidderList;
}
