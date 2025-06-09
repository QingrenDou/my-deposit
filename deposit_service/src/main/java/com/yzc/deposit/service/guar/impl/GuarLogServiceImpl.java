package com.yzc.deposit.service.guar.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.yzc.deposit.service.guar.IGuarLogService;
import com.yzc.common.guar.entity.GuarLog;
import com.yzc.deposit.dao.guar.IGuarLogDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class GuarLogServiceImpl implements IGuarLogService {

    @Resource
    private IGuarLogDao guarLogDao;


    @Override
    public boolean save(String logCode, String logContent) {
        return guarLogDao.saveLog(logCode, logContent);
    }
}
