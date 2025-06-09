package com.yzc.deposit.dao.deposit.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzc.common.deposit.entity.Log;
import com.yzc.deposit.dao.deposit.ILogDao;
import com.yzc.deposit.repository.mapper.deposit.LogMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@Service
public class LogDaoImpl  extends ServiceImpl<LogMapper, Log> implements ILogDao {

    @Resource
    private LogMapper logMapper;

    /**
     * 保存日志-封装
     * @param logCode 日志编码
     * @param logContent 日志内容
     * @return 保存结果
     */
    public boolean saveLog(String logCode, String logContent){
        if(StringUtils.isBlank(logCode) || StringUtils.isBlank(logContent)){
            return false;
        }

        if(logContent.length() > 1000){
            logContent = logContent.substring(0, 1000);
        }

        Log log = new Log();
        log.setLogId(UUID.randomUUID().toString());
        log.setLogCode(logCode);
        log.setLogContent(logContent);
        log.setLogTime(new Date());

        return logMapper.insert(log) > 0;
    }
}
