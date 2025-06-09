package com.yzc.deposit.dao.deposit;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yzc.common.deposit.entity.CommonConfig;

public interface ICommonConfigDao extends IService<CommonConfig> {

    /**
     * 根据配置编码获取配置值
     * @param key 配置编码
     * @return 配置值
     */
    String getCommonConfigByKey(String key);

    /**
     * 根据key修改value
     * @param key 配置项
     * @param value 配置值
     * @return 是否成功
     */
    boolean saveOrUpdateCommonConfigByKey(String key, String value);
}
