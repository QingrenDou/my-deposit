package com.yzc.deposit.service.common.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.yzc.common.api.service.IRemindService;
import com.yzc.common.model.infoRemind.PhoneMsgDto;
import com.yzc.deposit.dao.deposit.ILogDao;
import com.yzc.deposit.service.common.IMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 消息发送类 实现
 */
@Slf4j
@Service
public class MsgServiceImpl implements IMsgService {

    /**
     * 短信发送服务
     */
    @Resource
    private IRemindService remindService;

    @Resource
    private ILogDao logDao;

    /**
     * 发送短信(单个)
     *
     * @param phoneMsgDto 入参
     * @return 结果
     */
    @Override
    public boolean sendPhoneMsg(PhoneMsgDto phoneMsgDto) {
        String logStr = "[sendPhoneMsg]===>";

        if(ObjectUtil.isNull(phoneMsgDto)){
            log.error("发送短信入参不能为空");
            return false;
        }
        //发送短信
        boolean rst = remindService.sendMail(phoneMsgDto);
        log.info("{}发送短信入参={},结果={}", logStr, JSONUtil.toJsonStr(phoneMsgDto),rst);

        //写入日志
        logDao.saveLog("sendPhoneMsg", String.format("发送短信,模板id=%s,发送结果=%s,内容=%s",phoneMsgDto.getAliYunTemplateCode(), rst, phoneMsgDto.getMsgContent()));
        return rst;
    }
}
