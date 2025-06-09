package com.yzc.common.deposit.dto.api;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * 来看记录搜索-结果
 */
@Data
public class InAccRecordQuery4BindRespDto {

    /**
     * 银行流水号
     */
    private String bankSeqNo;

    /**
     * 来款银行编码
     */
    private String fromBankCode;

    /**
     * 来款银行名称
     */
    private String fromBankName;

    /**
     * 来款账号
     */
    private String fromAcc;

    /**
     * 来款账号名称
     */
    private String fromAccName;

    /**
     * 入账保证金账号
     */
    private String inSubAcc;

    /**
     * 交易金额
     */
    private BigDecimal tradeMoney;

    /**
     * 来款附言
     */
    private String addedMsg;

    /**
     * 交易日期
     */
    private String tradeDay;

    /**
     * 交易时间
     */
    private String tradeTime;

    /**
     * 交易时间(格式化：yyyy-MM-dd HH:mm:ss)
     */
    public String getTradeDayStr(){
        if(StringUtils.isBlank(tradeDay) || StringUtils.isBlank(tradeTime)){
            return "";
        }
        return DateUtil.format(DateUtil.parse(tradeDay+tradeTime, "yyyyMMddHHmmss"), "yyyy-MM-dd HH:mm:ss");
    }
}
