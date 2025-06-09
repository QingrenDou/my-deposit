package com.yzc.deposit.dao.deposit;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yzc.common.deposit.entity.Log;

public interface ILogDao  extends IService<Log> {

    /**
     * 保存日志-封装
     * @param logCode 日志编码
     * @param logContent 日志内容
     * @return 保存结果
     */
    boolean saveLog(String logCode, String logContent);
}
