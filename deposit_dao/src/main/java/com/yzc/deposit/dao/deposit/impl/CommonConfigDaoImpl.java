package com.yzc.deposit.dao.deposit.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzc.common.deposit.entity.CommonConfig;
import com.yzc.common.deposit.enums.UseStatusEnum;
import com.yzc.deposit.dao.deposit.ICommonConfigDao;
import com.yzc.deposit.repository.mapper.deposit.CommonConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class CommonConfigDaoImpl extends ServiceImpl<CommonConfigMapper, CommonConfig> implements ICommonConfigDao {

    @Resource
    private CommonConfigMapper commonConfigMapper;

    /**
     * 根据配置编码获取配置值
     * @param key 配置编码
     * @return 配置值
     */
    public String getCommonConfigByKey(String key){
        LambdaQueryWrapper<CommonConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CommonConfig::getConfigKey, key);
        queryWrapper.eq(CommonConfig::getUseStatus, UseStatusEnum.Normal.getCode());
        List<CommonConfig> configList = commonConfigMapper.selectList(queryWrapper);
        if(CollectionUtil.isEmpty(configList)){
            return null;
        }
        return ObjectUtil.isNotNull(configList.get(0)) ? configList.get(0).getConfigValue() : null;
    }

    /**
     * 根据key修改value
     *
     * @param key   配置项
     * @param value 配置值
     * @return 是否成功
     */
    @Override
    public boolean saveOrUpdateCommonConfigByKey(String key, String value) {
        //获取key值对应的配置项是否存在
        LambdaQueryWrapper<CommonConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CommonConfig::getConfigKey, key);
        queryWrapper.eq(CommonConfig::getUseStatus, UseStatusEnum.Normal.getCode());
        List<CommonConfig> configList = commonConfigMapper.selectList(queryWrapper);

        //如果不存在则新增
        if(CollectionUtil.isEmpty(configList)){
            CommonConfig commonConfig = new CommonConfig();
            commonConfig.setConfigKey(key);
            commonConfig.setConfigValue(value);
            commonConfig.setUseStatus(UseStatusEnum.Normal.getCode());
            return commonConfigMapper.insert(commonConfig) > 0;
        }
        else{
            CommonConfig commonConfig = configList.get(0);
            commonConfig.setConfigValue(value);
            return commonConfigMapper.updateById(commonConfig) > 0;
        }
    }

}
