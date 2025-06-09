package com.yzc.deposit.dao.guar;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yzc.common.guar.entity.GuarLog;

/**
 * <p>
 * 日志表 服务类
 * </p>
 *
 * @author DouQingRen
 * @since 2025-04-07
 */
public interface IGuarLogDao extends IService<GuarLog> {

    /**
     * 保存日志
     * @param logCode 日志编码
     * @param logContent 日志内容
     * @return 保存结果
     */
    boolean saveLog(String logCode, String logContent);
}
