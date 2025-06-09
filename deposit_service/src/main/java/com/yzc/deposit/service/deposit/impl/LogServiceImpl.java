package com.yzc.deposit.service.deposit.impl;

import com.yzc.deposit.dao.deposit.ILogDao;
import com.yzc.deposit.service.AbstractBaseService;
import com.yzc.deposit.service.deposit.ILogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class LogServiceImpl extends AbstractBaseService implements ILogService{

    @Resource
    private ILogDao logDao;

    /**
     * 保存日志-封装
     * @param logCode 日志编码
     * @param logContent 日志内容
     * @return 保存结果
     */
    public boolean saveLog(String logCode, String logContent){
        return logDao.saveLog(logCode, logContent);
    }
}
