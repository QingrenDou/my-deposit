package com.yzc.deposit.service.deposit;

public interface ILogService {

    /**
     * 保存日志
     * @param logCode 日志编码
     * @param logContent 日志内容
     * @return
     */
    boolean saveLog(String logCode,String logContent);

}
