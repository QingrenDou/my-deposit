package com.yzc.deposit.service.deposit.impl;

import com.yzc.deposit.dao.deposit.ICommonConfigDao;
import com.yzc.deposit.service.deposit.ICommonConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class CommonConfigServiceImpl implements ICommonConfigService {

    @Resource
    private ICommonConfigDao commonConfigDao;

    /**
     * 根据key获取配置
     * @param key 关键字
     * @return 配置值
     */
    @Override
    public String getCommonConfigByKey(String key) {
        return commonConfigDao.getCommonConfigByKey(key);
    }
}
