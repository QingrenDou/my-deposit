package com.yzc.deposit.service.common;

import com.yzc.common.model.infoRemind.PhoneMsgDto;

/**
 * 消息发送封装
 */
public interface IMsgService {

    /**
     * 发送短信
     * @param phoneMsgDto 入参
     * @return 结果
     */
    boolean sendPhoneMsg(PhoneMsgDto phoneMsgDto);
}
