package com.yzc.deposit.service.deposit;

/**
 * 保证金通用配置项
 */
public interface ICommonConfigService {

    /**
     * 根据key获取配置
     * @param key 关键字
     * @return 配置值
     */
    String getCommonConfigByKey(String key);
}
