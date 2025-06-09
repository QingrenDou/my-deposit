package com.yzc.deposit.dao.guar.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzc.common.guar.entity.GuarLog;
import com.yzc.deposit.dao.guar.IGuarLogDao;
import com.yzc.deposit.repository.mapper.guar.GuarLogMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 日志表 服务实现类
 * </p>
 *
 * @author DouQingRen
 * @since 2025-04-07
 */
@Service
public class GuarLogDaoImpl extends ServiceImpl<GuarLogMapper, GuarLog> implements IGuarLogDao {

    /**
     * 保存日志
     *
     * @param logCode    日志编码
     * @param logContent 日志内容
     * @return 保存结果
     */
    @Override
    public boolean saveLog(String logCode, String logContent) {
        GuarLog guarLog = new GuarLog();
        guarLog.setLogId(IdWorker.getId());
        guarLog.setLogCode(logCode);
        guarLog.setLogContent(logContent);
        guarLog.setLogTime(new Date());
        return super.save(guarLog);
    }
}
